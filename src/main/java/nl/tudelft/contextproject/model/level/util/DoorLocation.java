package nl.tudelft.contextproject.model.level.util;

import nl.tudelft.contextproject.model.level.Room;

/**
 * Classes that implement this abstract class are a door in a room.
 * 
 * Used for determining if a door connection point has been used.
 */
public abstract class DoorLocation {
	
	public Vec2I location;
	public Boolean used;
	public Room room;
}
