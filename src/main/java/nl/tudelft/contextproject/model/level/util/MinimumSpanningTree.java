package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Minimum spanning tree (MST) algorithm class. 
 * Takes a graph and turns it into a minimum spanning tree,
 * thus this class will remove edges.
 */
public class MinimumSpanningTree {
	
	public List<RoomNode> roomNodes;
	public List<CorridorEdge> corridorEdges;
	public HashMap<MSTNode, ArrayList<MSTEdge>> treeNodes;
	
	/**
	 * Constructor.
	 * @param roomNodes
	 * 					RoomNodes of graph
	 * @param corridorEdges
	 * 					CorridorEdges of graph
	 */
	public MinimumSpanningTree(List<RoomNode> roomNodes, List<CorridorEdge> corridorEdges) {
		this.roomNodes = roomNodes;
		this.corridorEdges = corridorEdges;
		this.treeNodes = new HashMap<MSTNode, ArrayList<MSTEdge>>();
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
	 * 
	 */
	protected void createTransformedGraph() {
		MSTNode startNode, endNode;
		MSTEdge currentEdge;
		ArrayList<MSTEdge> currentEdgeList;
		for (CorridorEdge corridorEdge : corridorEdges) {
			//Create RoomNode exit node equivalent, in other words, the start node of the corridor
			startNode = new MSTNode(new ArrayList<MSTEdge>(), 
					MSTNodeType.EXIT_NODE, 
					corridorEdge.start, 
					corridorEdge.start.node.id);

			//Create RoomNode entrance node equivalent, in other words, the end node the corridor
			endNode = new MSTNode(new ArrayList<MSTEdge>(),
					MSTNodeType.ENTRANCE_NODE,
					corridorEdge.end,
					corridorEdge.end.node.id);
			//Create edge MSTEdge
			currentEdge = new MSTEdge(startNode, endNode, corridorEdge.weight, corridorEdge.id);
			
			if (treeNodes.containsKey(startNode)) {
				currentEdgeList = treeNodes.get(startNode);
				//currentEdgeList.add(e)
			} else {
				//start
			//	treeNodes.add(startNode);
			}
			
			//Add existing edges to that exit node
			
			//Create RoomNode entrance node equivalent, in other words, the end node the corridor
		}
	}
	
	
	
	
}
