package nl.tudelft.contextproject.model.level.util;

import nl.tudelft.contextproject.util.Vec2I;

/**
 * Classes that implement this abstract class are a door in a room.
 */
public abstract class DoorLocation {

	public Vec2I location;
	public RoomNode node;

	/**
	 * Update the door location if door has been moved.
	 * 
	 * @param coordinates
	 * 		move coordinates.
	 */
	public void updateDoorLocation(Vec2I coordinates) {
		location.add(coordinates);
	}

	@Override
	public int hashCode() {
		return 31 * (31 + location.hashCode()) + node.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof DoorLocation)) return false;
		
		DoorLocation other = (DoorLocation) obj;
		if (!location.equals(other.location)) return false;
		if (!node.equals(other.node)) return false;
		
		return true;
	}
}
