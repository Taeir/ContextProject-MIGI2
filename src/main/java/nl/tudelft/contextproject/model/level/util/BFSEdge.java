package nl.tudelft.contextproject.model.level.util;

/**
 * Edge class for breadth first search on the Maze Tile map.
 */
public class BFSEdge {
	
	public Vec2I location1;
	public Vec2I location2;

	/**
	 * Constructor. 
	 * Simple edge from location to location.
	 * @param location1
	 * 		first location
	 * @param location2
	 * 		second location
	 */
	public BFSEdge(Vec2I location1, Vec2I location2) {
		this.location1 = location1;
		this.location2 = location2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location1 == null) ? 0 : location1.hashCode());
		result = prime * result + ((location2 == null) ? 0 : location2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BFSEdge other = (BFSEdge) obj;
		if (location1 == null) {
			if (other.location1 != null)
				return false;
		} else if (!location1.equals(other.location1))
			return false;
		if (location2 == null) {
			if (other.location2 != null)
				return false;
		} else if (!location2.equals(other.location2))
			return false;
		return true;
	}
	
}
