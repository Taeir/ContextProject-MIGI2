package nl.tudelft.contextproject.model.level.util;

import java.util.List;

/**
 * Simple node used in MST creation.
 */
public class MSTNode {

	public List<MSTEdge> edges;
	
	public int id;
	
	public RoomNode roomNode;
	
	/**
	 * Constructor.
	 * @param roomNode
	 * 			RoomNode the MSTNode belongs to
	 * @param edges
	 * 			edges of MSTNode
	 * @param id
	 * 			id of MSTNode
	 */
	public MSTNode(RoomNode roomNode, List<MSTEdge> edges, int id) {
		this.roomNode = roomNode;
		this.edges = edges;
		this.id = id;
	}
}
