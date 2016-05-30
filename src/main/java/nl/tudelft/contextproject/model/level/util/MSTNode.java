package nl.tudelft.contextproject.model.level.util;

import java.util.List;

/**
 * Simple node used in MST creation.
 */
public class MSTNode {

	public List<CorridorEdge> edges;
	
	public int id;
	
	/**
	 * Constructor.
	 * @param edges
	 * 			edges of MSTNode
	 * @param id
	 * 			id of MSTNode
	 */
	public MSTNode(List<CorridorEdge> edges, int id) {
		this.edges = edges;
		this.id = id;
	}
}
