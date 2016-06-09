package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;

/**
 * Simple node used in MST creation.
 */
public class MSTNode {

	/**
	 * Normal out going edges list.
	 */
	public ArrayList<MSTEdge> outgoingEdges;
	
	/**
	 * List used for searching other nodes. Speeds up algorithm.
	 */
	public ArrayList<MSTEdge> incomingEdges;
	
	/**
	 * RoomNode ID used for reconnecting nodes with the same RoomNode ID.
	 */
	public int roomNodeID;
	
	/**
	 * Used as identifier when in use.
	 */
	public DoorLocation originalDoor;
	
	public MSTNodeType nodeType;
	
	/**
	 * Constructor.
	 * 
	 * @param nodeType
	 * 			node type
	 * @param originalDoor
	 * 			door location in RoomNode
	 * @param roomNodeID
	 * 			id of MSTNode
	 */
	public MSTNode(MSTNodeType nodeType, DoorLocation originalDoor, int roomNodeID) {
		this.outgoingEdges = new ArrayList<MSTEdge>();
		this.incomingEdges = new ArrayList<MSTEdge>();
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
		if (obj == this) return true;
		
		if (!(obj instanceof MSTNode)) return false;
		
		MSTNode other = (MSTNode) obj;
		if (roomNodeID != other.roomNodeID) return false;
		if (nodeType != other.nodeType) return false;
		
		if (originalDoor == null) {
			if (other.originalDoor != null) return false;
		} else if (!originalDoor.equals(other.originalDoor)) return false;
		
		return true;
	}

	/**
	 * Add an edge to the MST node outgoing edges list.
	 * 
	 * @param edge
	 *		edge to add
	 */
	public void addOutGoingEdge(MSTEdge edge) {
		outgoingEdges.add(edge);
	}
	
	/**
	 * Add an edge to the MST node incoming edges list.
	 * 
	 * @param edge
	 *		edge to add
	 */
	public void addInComingEdge(MSTEdge edge) {
		incomingEdges.add(edge);
	}

	@Override
	public String toString() {
		return "MSTNode [edges=" + outgoingEdges.size() + ", roomNodeID=" + roomNodeID
				+ ", nodeType=" + nodeType + "]";
	}
}
