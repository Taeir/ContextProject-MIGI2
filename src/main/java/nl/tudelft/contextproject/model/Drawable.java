package nl.tudelft.contextproject.model;

import java.awt.Graphics2D;

import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

/**
 * Interface representing a Drawable object.
 * This object has a geometry that can be updated.
 */
public interface Drawable {	
	/**
	 * Getter for the spatial of this Drawable.
	 * @return The spatial representing this Drawable.
	 */
	public Spatial getSpatial();
	
	
	/**
	 * Method used for testing with mocked spatial.
	 * Sets the spatial of this instance to the specified spatial.
	 * @param spatial The new spatial.
	 */
	void setSpatial(Spatial spatial);
	
	/**
	 * Draw the Drawable on a map.
	 * @param g The Graphics2D to draw on.
	 * @param resolution The resolution of a single tile on the map.
	 */
	public default void mapDraw(Graphics2D g, int resolution) { }
}
