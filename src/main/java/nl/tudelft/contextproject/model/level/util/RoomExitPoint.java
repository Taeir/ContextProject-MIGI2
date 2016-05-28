package nl.tudelft.contextproject.model.level.util;

import nl.tudelft.contextproject.model.level.Room;

/**
 * Exit point of a room.
 * 
 * Used for determining if a door connection point has been used.
 */
public class RoomExitPoint extends DoorLocation {

	/**
	 * Door entrance connection point.
	 * Automatically sets used to false.
	 * @param room
	 * 				room to which the door belongs to
	 * @param location
	 * 				Location of door in room
	 */
	public RoomExitPoint(Room room, Vec2I location) {
		super.location = location;
		super.used = false;
		super.room = room;
	}
}
