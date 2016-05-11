package nl.tudelft.contextproject.model;

import java.awt.Graphics2D;

import com.jme3.scene.Geometry;

/**
 * Interface representing a Drawable object.
 * This object has a geometry that can be updated.
 */
public interface Drawable extends Collidable {	
	/**
	 * Getter for the geometry of this Drawable.
	 * @return The geometry representing this Drawable.
	 */
	public Geometry getGeometry();
	
	
	/**
	 * Method used for testing with mocked geometry.
	 * Sets the geometry of this instance to the specified geometry.
	 * @param geometry The new geometry.
	 */
	void setGeometry(Geometry geometry);
	
	/**
	 * Draw the Drawable on a map.
	 * @param g The Graphics2D to draw on.
	 * @param resolution The resolution of a single tile on the map.
	 */
	public default void mapDraw(Graphics2D g, int resolution) { }
}
