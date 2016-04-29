package nl.tudelft.contextproject.level;

import java.awt.Graphics2D;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Drawable;

/**
 * Class representing a tile in the maze.
 */
public class MazeTile implements Drawable {
	private boolean explored;
	private Geometry geom;
	
	/**
	 * Constructor for a tile in the maze.
	 * @param am the Assetmanager from where materials can be loaded.
	 * @param x The x-coordinate of this tile.
	 * @param y The y-coordinate of this tile.
	 */
	public MazeTile(AssetManager am, int x, int y) {
		this.explored = false;
		
		Box b = new Box(.5f, .5f, .5f); // create cube shape
        this.geom = new Geometry("Box", b);  // create cube geometry from the shape
        Material mat = new Material(am, "Common/MatDefs/Light/Lighting.j3md");  // create a simple material
        mat.setBoolean("UseMaterialColors", true);    
        mat.setColor("Diffuse", ColorRGBA.randomColor());
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 64f);  // [0,128]
        this.geom.setMaterial(mat);                   // set the cube's material
        this.geom.move(x, y, 0);
	}
	
	/**
	 * Getter for the geometry of the tile that will be rendered.
	 * This geometry is centered at (0, 0, 0) (for now).
	 * For now this method returns a randomly colored box.
	 * @return The geometry representing this tile.
	 */
	public Geometry getGeometry() {
        return geom;
	}

	@Override
	public void mapDraw(Graphics2D g, int resolution) {
		int x = (int) geom.getLocalTranslation().x * resolution;
		int y = (int) geom.getLocalTranslation().y * resolution;
		g.fillRect(x, y, resolution, resolution);
		
	}

	/**
	 * Check if this tile is explored.
	 * @return True when explored, else otherwise.
	 */
	public boolean isExplored() {
		return explored;
	}
}
