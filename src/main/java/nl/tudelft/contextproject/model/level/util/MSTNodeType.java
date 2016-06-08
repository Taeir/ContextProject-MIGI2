package nl.tudelft.contextproject.model.level.util;

/**
 * Different types of MSTNodes.
 *	Needed for splitting up roomNodes.
 */
public enum MSTNodeType {
	/**
	 * Entrance door location in original RoomNode.
	 */
	ENTRANCE_NODE,
	
	/**
	 * Exit door location in original RoomNode.
	 */
	EXIT_NODE,
	
	/**
	 * Connection node that connects entrance nodes to exit nodes.
	 */
	CONNECTOR_NODE;

}
