package nl.tudelft.contextproject;

import com.jme3.scene.Geometry;

/**
 * Interface representing a Drawable object.
 * This object has a geometry that can be updated.
 */
public interface Drawable {	
	/**
	 * Getter for the geometry of this Drawable.
	 * @return The geometry representing this Drawable.
	 */
	public Geometry getGeometry();
	
	/**
	 * Update method called once per frame.
	 * This method should update the exact geometry that is returned by {@link #getGeometry()}.
	 * @param tpf The time between this and the previous frame.
	 */
	public void simpleUpdate(float tpf);
}
