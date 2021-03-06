package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.util.Vec2I;

/**
 * Corridor breadth first search utility class.
 */
public final class CorridorBreadthFirstSearch {

	/**
	 * Private constructor to prevent initialization.
	 */
	private CorridorBreadthFirstSearch() {}

	/**
	 * Bread first search through maze tiles to find the end tile.
	 * <p>
	 * Works by keeping a hashMap of visited tiles and their distance saved,
	 * holds all possible neighbors of currently visited tiles in a queue. At
	 * each step the neighbors of the current tile are added to the queue and
	 * their distance is updated.
	 * <p>
	 * After visiting the end node, the algorithm looks for a path back to the 
	 * starting node. This done by checking all neighbor tiles from the current tile
	 * and tacking the the one which reduces the distance to the start node. Each visited
	 * node is pushed on a stack which will then be in the correct order to transfer the
	 * path from start to finish.
	 * 
	 * @param mazeTiles
	 * 		maze tiles to carve in
	 * @param start
	 * 		start location of corridor (should be inside the wall of the room)
	 * @param end
	 * 		end location of corridor (should be inside the wall of the room)
	 * @return
	 * 		stack containing the correct order of tiles to take from start node to end node
	 */
	public static Stack<Vec2I> breadthFirstSearch(MazeTile[][] mazeTiles, Vec2I start, Vec2I end) {
		ArrayDeque<Vec2I> queue = new ArrayDeque<Vec2I>();
		HashMap<Vec2I, Integer> distanceMap = new HashMap<Vec2I, Integer>();

		queue.add(start);
		distanceMap.put(start, 0);

		//Search end
		int newDistance;
		Vec2I currentNode;
		ArrayList<Vec2I> validNeighbours;
		while (!queue.isEmpty()) {
			currentNode = queue.remove();
			newDistance = distanceMap.get(currentNode) + 1;
			if (currentNode.equals(end)) {
				//Found end, stop breadth first search. 
				break;
			}
			validNeighbours = getNeighbors(mazeTiles, currentNode, end);
			for (Vec2I neigborNode : validNeighbours) {
				if (distanceMap.containsKey(neigborNode)) {
					continue;
				} else {
					distanceMap.put(neigborNode, newDistance);
					queue.add(neigborNode);
				}
			}
		}
		
		//Retrace steps back
		Stack<Vec2I> route = new Stack<Vec2I>();
		currentNode = end;
		while (!currentNode.equals(start)) {
			currentNode = getSmallerDistanceNode(distanceMap, currentNode);
			route.push(currentNode);
		}

		return route;
	}

	/**
	 * Check all 4 directions for a vector with shorter distance.
	 * This method will return the first node it can find with a shorter distance in 
	 * the order of North, South, West and finally, East.
	 * 
	 * @param distanceMap
	 * 		distance Map
	 * @param previousNode
	 * 		node that is being back tracked from
	 * @return
	 * 		node that has a shorter distance to start node, than previous node
	 */
	protected static Vec2I getSmallerDistanceNode(HashMap<Vec2I, Integer> distanceMap, Vec2I previousNode) {
		int previousDistance = distanceMap.get(previousNode);
		int x = previousNode.x;
		int y = previousNode.y;
		int defaultValue = Integer.MAX_VALUE;
		//Check North
		Vec2I neighbourNode = new Vec2I(x, y - 1);
		if (distanceMap.getOrDefault(neighbourNode, defaultValue) < previousDistance) {
			return neighbourNode;
		}

		//Check South
		neighbourNode = new Vec2I(x, y + 1);
		
		if (distanceMap.getOrDefault(neighbourNode, defaultValue) < previousDistance) {
			return neighbourNode;
		}

		//Check West
		neighbourNode = new Vec2I(x - 1, y);
		if (distanceMap.getOrDefault(neighbourNode, defaultValue) < previousDistance) {
			return neighbourNode;
		} else {
			//East, no check necessary, since this is the only option left
			return new Vec2I(x + 1, y);
		}
	}

	/**
	 * Get list of neighbor nodes.
	 * Only empty Tiles are neighbors, except if that tile is the end tile.
	 * <p>
	 * PMD error: The method getNeighBors() has an NPath complexity of 256
	 * 		This method is not impossible to split up to reduce complexity, however
	 * 		since it would only complicate readability of the method, or create 4 extra methods
	 * 		that we thought it was not necessary to reduce the complexity.
	 * 
	 * @param mazeTiles
	 * 		map with rooms, but no corridors
	 * @param currentNode
	 * 		node that the algorithm is at
	 * @param endNode
	 * 		the end node in the BFS algorithm
	 * @return
	 * 		list that are neighbors of current node and not already a tile.
	 */
	protected static ArrayList<Vec2I> getNeighbors(MazeTile[][] mazeTiles, Vec2I currentNode, Vec2I endNode) {
		ArrayList<Vec2I> neigboursOfCurrentNode = new ArrayList<Vec2I>();
		int x = currentNode.x;
		int y = currentNode.y;

		//Check North
		Vec2I newVector = new Vec2I(x, y - 1);
		if (y != 0 
				&& checkNullOrEnd(mazeTiles[x][y - 1], endNode, newVector)) {
			neigboursOfCurrentNode.add(newVector);
		}

		//Check South
		newVector = new Vec2I(x, y + 1);
		if (y != mazeTiles.length - 1 
				&& checkNullOrEnd(mazeTiles[x][y + 1], endNode, newVector)) {
			neigboursOfCurrentNode.add(newVector);
		}

		//Check West
		newVector = new Vec2I(x - 1, y);
		if (x != 0 
				&& checkNullOrEnd(mazeTiles[x - 1][y], endNode, newVector)) {
			neigboursOfCurrentNode.add(newVector);
		}

		//Check East
		newVector = new Vec2I(x + 1, y);
		if (x != mazeTiles[0].length - 1 
				&& checkNullOrEnd(mazeTiles[x + 1][y], newVector, endNode)) {
			neigboursOfCurrentNode.add(newVector);
		}

		return neigboursOfCurrentNode;
	}

	/**
	 * Check if maze tile is null, or the end has been reached.
	 * 
	 * @param mazeTile
	 * 		mazeTile to check
	 * @param endNode
	 * 		end location
	 * @param newVector
	 * 		location of new vector
	 * @return
	 * 		true if maze tile is null or the end location is equal to the location of the new vector.
	 */
	private static boolean checkNullOrEnd(MazeTile mazeTile, Vec2I endNode, Vec2I newVector) {
		return mazeTile == null || newVector.equals(endNode);
	}
}
