package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimum spanning tree (MST) algorithm class. 
 * Takes a graph and turns it into a minimum spanning tree,
 * thus this class will remove edges.
 */
public class MinimumSpanningTree {
	
	public List<RoomNode> roomNodes;
	
	public List<MSTNode> treeNodes;
	
	/**
	 * Constructor.
	 * @param roomNodes
	 * 					RoomNodes of graph
	 */
	public MinimumSpanningTree(List<RoomNode> roomNodes) {
		this.roomNodes = roomNodes;
		this.treeNodes = new ArrayList<MSTNode>();
	}
	
	/**
	 * Run Prim's algorithm.
	 * A greedy algorithm to find the minimum spanning tree.
	 * 
	 * First generates a new graph that deals with multiple exits and entrances per room.
	 */
	public void runPimAlgorithm() {
		createTransformedGraph();
	}

	/**
	 * Generate a new graph that deals better with rooms.
	 * Split each room into several nodes.
	 * Each 
	 */
	protected void createTransformedGraph() {
		int idCounter = 0;
		ArrayList<RoomExitPoint> exits;
		ArrayList<RoomEntrancePoint> entrances;
		ArrayList<MSTNode> exitNodes;
		ArrayList<MSTNode> entranceNodes;
		MSTNode middelNode;
		for (RoomNode roomNode : roomNodes) {
			exits = roomNode.exits;
			entrances = roomNode.entrances;
			exitNodes = new ArrayList<MSTNode>();
			entranceNodes = new ArrayList<MSTNode>();
			//Create a node for each exit
			for (RoomExitPoint exit : exits) {
				exitNodes.add(new MSTNode(roomNode, roomNode.getOutgoingEdgesOfExit(exit), idCounter++));
			}
			
			//Create a center node for the room
			middelNode = new MSTNode(roomNode, new ArrayList<CorridorEdge>(), idCounter++);
			for (MSTNode exitNode : exitNodes) {
				middelNode.edges.add(new CorridorEdge());
			}
			
		}
		
	}
	
	protected List<MSTEdge> createMSTEdges(List<CorridorEdge> edges) {
		for (Corridor edge)
	}
	
}
