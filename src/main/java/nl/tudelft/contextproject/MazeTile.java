package nl.tudelft.contextproject;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * Class representing a tile in the maze.
 */
public class MazeTile {
	AssetManager am;
	
	/**
	 * Constructor for a tile in the maze.
	 * @param am the Assetmanager from where materials can be loaded.
	 */
	public MazeTile(AssetManager am) {
		this.am = am;
	}
	
	/**
	 * Getter for the geometry of the tile that will be rendered.
	 * This geometry is centered at (0, 0, 0) (for now).
	 * For now this method returns a randomly colored box.
	 * @return The geometry representing this tile.
	 */
	public Geometry getGeometry() {
		Box b = new Box(.5f, .5f, .5f); // create cube shape
        Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
        Material mat = new Material(am,
          "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.randomColor());   // set color of material to blue
        geom.setMaterial(mat);                   // set the cube's material
        return geom;
	}
}
