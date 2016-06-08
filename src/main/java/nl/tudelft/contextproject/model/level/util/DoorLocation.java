package nl.tudelft.contextproject.model.level.util;


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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DoorLocation) {
			DoorLocation other = (DoorLocation) obj;
			if (!location.equals(other.location))
				return false;
			if (!node.equals(other.node))
				return false;
			return true;
		} 
		return false;
	}


}
