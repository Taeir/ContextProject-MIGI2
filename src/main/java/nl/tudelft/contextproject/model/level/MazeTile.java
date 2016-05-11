package nl.tudelft.contextproject.model.level;

import java.awt.Graphics2D;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Drawable;

/**
 * Class representing a tile in the maze.
 */
public class MazeTile implements Drawable {
	public static final int MAX_HEIGHT = 1;
	
	private Spatial spatial;
	private Vector2f position;
	private int height;
	
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
	
	@Override
	public Spatial getSpatial() {
        if (spatial != null) return spatial;
        
        Box b = new Box(.5f, .5f, .5f + height); // create cube shape
        this.spatial = new Geometry("Box", b);  // create cube geometry from the shape
        Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");  // create a simple material
        mat.setBoolean("UseMaterialColors", true);    
        mat.setColor("Diffuse", ColorRGBA.randomColor());
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 64f);  // [0,128]
		mat.setColor("Ambient", ColorRGBA.randomColor());
        this.spatial.setMaterial(mat);                   // set the cube's material
        this.spatial.move(position.x, position.y, height);
        return spatial;
	}

	@Override
	public void mapDraw(Graphics2D g, int resolution) {
		int x = (int) spatial.getLocalTranslation().x * resolution;
		int y = (int) spatial.getLocalTranslation().y * resolution;
		g.fillRect(x, y, resolution, resolution);
		
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public Object getPhysicsObject() {
		if (spatial == null) {
			this.getSpatial();
		}
		
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(spatial);
		return new RigidBodyControl(sceneShape, 0);
	}
}
