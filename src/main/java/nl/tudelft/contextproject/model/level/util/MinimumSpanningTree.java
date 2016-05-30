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
	 * @param edges
	 * 					edges of graph
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
	 * Split each room into a node 
	 */
	protected void createTransformedGraph() {
		for (RoomNode roomNode : roomNodes) {
			
		}
		
	}
	
}
