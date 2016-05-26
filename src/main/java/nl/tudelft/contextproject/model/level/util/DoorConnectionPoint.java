package nl.tudelft.contextproject.model.level.util;

/**
 * Door connection point.
 * 
 * Used for determining if a door connection point has been used.
 */
public class DoorConnectionPoint {
	
	public Vec2I location;
	public Boolean used;
	
	/**
	 * Door connection point.
	 * Automatically sets used to false.
	 * @param location
	 * 				Location of door in room
	 */
	public DoorConnectionPoint(Vec2I location) {
		this.location = location;
		used = false;
	}

}
