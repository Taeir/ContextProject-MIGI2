package nl.tudelft.contextproject.model.level.util;

import nl.tudelft.contextproject.util.Vec2I;

/**
 * Exit point of a room.
 * 
 * Used for determining if a door connection point has been used.
 */
public class RoomExitPoint extends DoorLocation {

	/**
	 * Door entrance connection point.
	 * Automatically sets used to false.
	 * 
	 * @param node
	 * 				room to which the door belongs to
	 * @param location
	 * 				Location of door in room
	 */
	public RoomExitPoint(RoomNode node, Vec2I location) {
		super.location = location;
		super.node = node;
	}
}
