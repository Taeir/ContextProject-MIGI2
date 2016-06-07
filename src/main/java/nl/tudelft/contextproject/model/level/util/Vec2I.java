package nl.tudelft.contextproject.model.level.util;

/**
 * 2d integer vector class.
 * Used for locations of objects of importance on the map.
 * Mainly used in map generation.
 */
public class Vec2I {
	
	/**
	 * The x coordinate of the vector.
	 */
	public int x;
	
	/**
	 * The y coordinate of the vector.
	 */
	public int y;
	
	/**
	 * Constructor.
	 * @param x
	 * 		x - coordinate
	 * @param y
	 * 		y - coordinate
	 */	
	public Vec2I(int x, int y) {
		this.x = x;
		this.y = y;
	}

	
	/**
	 * Get x - coordinate.
	 * 
	 * @return
	 * 		x - coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Set x - coordinate.
	 * 
	 * @param x
	 * 		x - coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Get y - coordinate.
	 * 
	 * @return
	 * 			y - coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Set y - coordinate.
	 * 
	 * @param y
	 * 			y - coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int hashCode() {
		return (x << 16) | y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vec2I other = (Vec2I) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Vec2I [x=" + x + ", y=" + y + "]";
	}

	/**
	 * Add another vector to this vector.
	 * 
	 * @param coordinates
	 * 		vector to add
	 */
	public void add(Vec2I coordinates) {
		x += coordinates.x;
		y += coordinates.y;
	}

	/**
	 * Calculate euclidean distance between this vector and the other vector.
	 * 
	 * @param otherVector
	 * 		the other vector
	 * @return
	 * 		euclidean distance
	 */
	public double distance(Vec2I otherVector) {
		double res = (x - otherVector.x) * (x - otherVector.x) + (y - otherVector.y) * (y - otherVector.y);
		return Math.sqrt(res);
	}
}
