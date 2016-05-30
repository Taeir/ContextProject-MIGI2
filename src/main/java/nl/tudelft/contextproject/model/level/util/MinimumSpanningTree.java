package nl.tudelft.contextproject.model.level.util;

import java.util.List;

/**
 * Minimum spanning tree (MST) algorithm class. 
 * Takes a graph and turns it into a minimum spanning tree,
 * thus this class will remove edges.
 */
public class MinimumSpanningTree {
	
	public List<CorridorEdge> edges;
	
	public List<RoomNode> nodes;
	
	
	/**
	 * Constructor.
	 * @param edges
	 * 					edges of graph
	 * @param nodes
	 * 					nodes of graph
	 */
	public MinimumSpanningTree(List<CorridorEdge> edges, List<RoomNode> nodes) {
		this.edges = edges;
		this.nodes = nodes;
	}
	
	
}
