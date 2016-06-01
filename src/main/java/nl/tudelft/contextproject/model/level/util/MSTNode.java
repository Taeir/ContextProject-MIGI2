package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;

/**
 * Simple node used in MST creation.
 */
public class MSTNode {

	public ArrayList<MSTEdge> edges;
	
	public int roomNodeID;
	
	public DoorLocation originalDoor;
	
	public MSTNodeType nodeType;
	
	/**
	 * Constructor.
	 * @param nodeType
	 * 			node type
	 * @param originalDoor
	 * 			door location in RoomNode
	 * @param roomNodeID
	 * 			id of MSTNode
	 */
	public MSTNode(MSTNodeType nodeType, DoorLocation originalDoor, int roomNodeID) {
		this.edges = new ArrayList<MSTEdge>();
		this.nodeType = nodeType;
		this.originalDoor = originalDoor;
		this.roomNodeID = roomNodeID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeType == null) ? 0 : nodeType.hashCode());
		result = prime * result + ((originalDoor == null) ? 0 : originalDoor.hashCode());
		result = prime * result + roomNodeID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MSTNode other = (MSTNode) obj;
		if (nodeType != other.nodeType)
			return false;
		if (originalDoor == null) {
			if (other.originalDoor != null)
				return false;
		} else if (!originalDoor.equals(other.originalDoor))
			return false;
		if (roomNodeID != other.roomNodeID)
			return false;
		return true;
	}

	/**
	 * Add an edge to the MST node outgoing edges list.
	 * @param edge
	 *		edge to add
	 */
	public void addEdge(MSTEdge edge) {
		edges.add(edge);
	}
	
	
}