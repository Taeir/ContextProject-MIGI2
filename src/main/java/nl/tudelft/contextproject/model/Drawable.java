package nl.tudelft.contextproject.model;

import java.awt.Graphics2D;

import com.jme3.scene.Spatial;

/**
 * Interface representing a Drawable object.
 * This object has a geometry that can be updated.
 */
public interface Drawable {

	/**
	 * Getter for the spatial of this Drawable.
	 *
	 * @return
	 * 		the spatial representing this Drawable
	 */
	Spatial getSpatial();
	
	
	/**
	 * Method used for testing with mocked spatial.
	 * Sets the spatial of this instance to the specified spatial.
	 *
	 * @param spatial
	 * 		the new spatial
	 */
	void setSpatial(Spatial spatial);
	
	/**
	 * Draw the Drawable on a map.
	 *
	 * @param g
	 * 		the Graphics2D to draw on
	 * @param resolution
	 * 		the resolution of a single tile on the map
	 */
	void mapDraw(Graphics2D g, int resolution);
}
