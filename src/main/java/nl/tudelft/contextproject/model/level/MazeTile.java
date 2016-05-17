package nl.tudelft.contextproject.model.level;

import java.awt.Graphics2D;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
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
import nl.tudelft.contextproject.model.PhysicsObject;

/**
 * Class representing a tile in the maze.
 */
public class MazeTile implements Drawable, PhysicsObject {
	private Spatial spatial;
	private Vector2f position;
	private int height;
	private boolean explored;
	private ColorRGBA color;
	private TileType type;

	/**
	 * Constructor for a tile in the maze.
	 * @param x
	 * 			the x-coordinate of this tile
	 * @param y
	 * 			the y coordinate of this tile
	 * @param type
	 * 			the type of this tile
	 */
	public MazeTile(int x, int y, TileType type) {
		this.position = new Vector2f(x, y);
		this.explored = false;
		this.type = type;
		
		switch (type) {
			case FLOOR:
				this.height = 0;
				this.color = ColorRGBA.Green;
				break;
			case WALL:
				this.height = 3;
				this.color = ColorRGBA.Blue;
				break;
			case CORRIDOR:
				this.height = 0;
				this.color = ColorRGBA.Red;
				break;
			default:
				throw new IllegalArgumentException("Invalid TileType: " + type);
		}
	}
	
	/**
	 * Getter for tileType.
	 * @return
	 * 				type of maze tile
	 */
	public TileType getTileType() {
		return type;
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;

		Box b = new Box(.5f, .5f + height, .5f); // create cube shape
		this.spatial = new Geometry("Box", b);  // create cube geometry from the shape
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		mat.setBoolean("UseMaterialColors", true);    
		mat.setColor("Diffuse", color);
		mat.setColor("Specular", ColorRGBA.White);
		mat.setFloat("Shininess", 64f);  // [0,128]
		mat.setColor("Ambient", color);
		this.spatial.setMaterial(mat);                   // set the cube's material
		this.spatial.move(position.x, height, position.y);
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
	public PhysicsControl getPhysicsObject() {
		if (spatial == null) {
			this.getSpatial();
		}

		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(spatial);
		RigidBodyControl rigidBody = new RigidBodyControl(sceneShape, 0);
		rigidBody.setPhysicsLocation(spatial.getLocalTranslation());
		return rigidBody;
	}

	/**
	 * @return
	 * 			return if the tile has been explored
	 */
	public boolean isExplored() {
		return explored;
	}

	/**
	 * @param explored
	 * 			the new value to set
	 * @return
	 * 			the old value
	 */
	public boolean setExplored(boolean explored) {
		boolean returnValue = this.explored;
		this.explored = explored;
		return returnValue;
	}
}
