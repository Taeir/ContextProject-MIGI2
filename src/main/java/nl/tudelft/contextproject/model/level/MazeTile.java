package nl.tudelft.contextproject.model.level;

import java.awt.Graphics2D;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Drawable;

/**
 * Class representing a tile in the maze.
 */
public class MazeTile implements Drawable {
	private Geometry geom;
	private Vector2f position;
	private int height;
	public static final int MAX_HEIGHT = 1;
	
	/**
	 * Constructor for a tile in the maze.
	 * @param x The x-coordinate of this tile.
	 * @param y The y-coordinate of this tile.
	 */
	public MazeTile(int x, int y) {
		this.position = new Vector2f(x, y);
		this.height = MAX_HEIGHT;
	}

	/**
	 * Constructor for a tile in the maze.
	 * @param x The x-coordinate of this tile.
	 * @param y The y coordinate of this tile.
	 * @param height The height of this tile.
     */
	public MazeTile(int x, int y, int height) {
		this(x, y);
		this.height = height;
	}
	
	/**
	 * Getter for the geometry of the tile that will be rendered.
	 * This geometry is centered at (0, 0, 0) (for now).
	 * For now this method returns a randomly colored box.
	 * @return The geometry representing this tile.
	 */
	public Geometry getGeometry() {
        if (geom != null) return geom;
        
        Box b = new Box(.5f, .5f, .5f + height); // create cube shape
        this.geom = new Geometry("Box", b);  // create cube geometry from the shape
        Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");  // create a simple material
        mat.setBoolean("UseMaterialColors", true);    
        mat.setColor("Diffuse", ColorRGBA.randomColor());
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 64f);  // [0,128]
		mat.setColor("Ambient", ColorRGBA.randomColor());
        this.geom.setMaterial(mat);                   // set the cube's material
        this.geom.move(position.x, position.y, height);
        return geom;
	}

	@Override
	public void mapDraw(Graphics2D g, int resolution) {
		int x = (int) geom.getLocalTranslation().x * resolution;
		int y = (int) geom.getLocalTranslation().y * resolution;
		g.fillRect(x, y, resolution, resolution);
		
	}

	@Override
	public void setGeometry(Geometry geometry) {
		geom = geometry;
	}
}
