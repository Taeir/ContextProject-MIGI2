package nl.tudelft.contextproject.model.level.util;

/**
 * Entrance point of a room.
 * 
 * Used for determining if a door connection point has been used.
 */
public class RoomEntrancePoint extends DoorLocation {
			
	/**
	 * Door entrance connection point.
	 * Automatically sets used to false.
	 * @param location
	 * 				Location of door in room
	 */
	public RoomEntrancePoint(Vec2I location) {
		super.location = location;
		super.used = false;
	}

}
