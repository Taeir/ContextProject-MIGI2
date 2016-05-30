package nl.tudelft.contextproject.model.level.util;


/**
 * Classes that implement this abstract class are a door in a room.
 * 
 * Used for determining if a door connection point has been used.
 */
public abstract class DoorLocation {
	
	public Vec2I location;
	public Boolean used;
	public RoomNode node;
	
	/**
	 * Update the door location if door has been moved.
	 * @param coordinates
	 * 					move coordinates.
	 */
	public void updateDoorLocation(Vec2I coordinates) {
		location.add(coordinates);
	}
}
