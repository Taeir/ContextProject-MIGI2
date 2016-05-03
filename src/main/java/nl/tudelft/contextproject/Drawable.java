package nl.tudelft.contextproject;

import java.awt.Graphics2D;

import com.jme3.math.Vector3f;
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
	
	public void setGeometry(Geometry geometry);
	
	/**
	 * Update method called once per frame.
	 * This method should update the exact geometry that is returned by {@link #getGeometry()}.
	 * @param tpf The time between this and the previous frame.
	 */
	public default void simpleUpdate(float tpf){ }
	
	/**
	 * Draw the Drawable on a map.
	 * @param g The Graphics2D to draw on.
	 * @param resolution The resolution of a single tile on the map.
	 */
	public default void mapDraw(Graphics2D g, int resolution) {
		Vector3f trans = getGeometry().getLocalTranslation();
		int x = (int) trans.x * resolution;
		int y = (int) trans.y * resolution;

		g.drawOval(x, y, resolution, resolution);
	}
}
