package nl.tudelft.contextproject.model.level;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Drawable;
import nl.tudelft.contextproject.model.PhysicsObject;

/**
 * Class representing a tile in the maze.
 */
public class MazeTile implements Drawable, PhysicsObject {
	private static Material wallMaterial, floorMaterial;
	
	private RigidBodyControl rigidBody;
	private Spatial spatial;
	private Vector2f position;
	private int height;
	private boolean explored;
	private ColorRGBA color;
	private Texture texture;
	private TileType type;

	/**
	 * Constructor for a tile in the maze.
	 *
	 * @param x
	 *		the x-coordinate of this tile
	 * @param y
	 *		the y coordinate of this tile
	 * @param type
	 *		the type of this tile
	 */
	public MazeTile(int x, int y, TileType type) {
		this.position = new Vector2f(x, y);
		this.explored = false;
		this.type = type;
		this.height = type.getHeight();
		this.color = type.getColor();
		this.texture = type.getTexture();
	}

	/**
	 * Getter for tileType.
	 *
	 * @return
	 * 		type of maze tile
	 */
	public TileType getTileType() {
		return type;
	}
	
	/**
	 * Get position of MazeTile.
	 * 
	 * @return
	 * 		position
	 */
	public Vector2f getPosition() {
		return position;
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		
		Material material;
		if (type == TileType.WALL) {
			if (wallMaterial == null) {
				wallMaterial = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
				wallMaterial.setBoolean("UseMaterialColors", true);    
				wallMaterial.setColor("Diffuse", color);
				wallMaterial.setColor("Specular", ColorRGBA.White);
				wallMaterial.setFloat("Shininess", 64f);
				wallMaterial.setColor("Ambient", color);
				wallMaterial.setTexture("DiffuseMap", texture);
				wallMaterial.setTexture("NormalMap", Main.getInstance().getAssetManager().loadTexture("Textures/wallnormalmap.png"));
				wallMaterial.setBoolean("UseMaterialColors", true);
			}
			
			material = wallMaterial;
		} else if (type == TileType.FLOOR) {
			if (floorMaterial == null) {
				floorMaterial = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
				floorMaterial.setBoolean("UseMaterialColors", true);    
				floorMaterial.setColor("Diffuse", color);
				floorMaterial.setColor("Specular", ColorRGBA.White);
				floorMaterial.setFloat("Shininess", 64f);
				floorMaterial.setColor("Ambient", color);
				floorMaterial.setTexture("DiffuseMap", texture);
				floorMaterial.setTexture("NormalMap", Main.getInstance().getAssetManager().loadTexture("Textures/wallnormalmap.png"));
				floorMaterial.setBoolean("UseMaterialColors", true);
			}
			
			material = floorMaterial;
		} else {
			material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
			
			material.setBoolean("UseMaterialColors", true);    
			material.setColor("Diffuse", color);
			material.setColor("Specular", ColorRGBA.White);
			material.setFloat("Shininess", 64f);
			material.setColor("Ambient", color);
			material.setTexture("DiffuseMap", texture);
			material.setTexture("NormalMap", Main.getInstance().getAssetManager().loadTexture("Textures/wallnormalmap.png"));
			material.setBoolean("UseMaterialColors", true);
		}

		Box box = new Box(.5f, .5f + height, .5f);
		this.spatial = new Geometry("Box", box);
		TangentBinormalGenerator.generate(box);
		this.spatial.move(position.x, height, position.y);
		this.spatial.setMaterial(material);
		return spatial;
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
		if (rigidBody != null) {
			return rigidBody;
		}

		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(spatial);
		rigidBody = new RigidBodyControl(sceneShape, 0);
		rigidBody.setPhysicsLocation(spatial.getLocalTranslation());
		return rigidBody;
	}
	
	/**
	 * Replace MazeTile.
	 * This method is used during room creations. As rooms have a MazeTile[][] array before they are placed, this would 
	 * result in the wrong actual location of the object. Thus the MazeTile has to be replaced as soon as it actually placed
	 * in the maze.
	 * 
	 * @param x
	 * 		new x location
	 * @param y 
	 * 		new y location
	 */
	public void replace(int x, int y) {
		position.x = x;
		position.y = y;		
		if (spatial == null) {
			this.getSpatial();
		} else {
			spatial.setLocalTranslation(position.x, height, position.y);
		}
		if (rigidBody == null) {
			this.getPhysicsObject();
		} else {
			rigidBody.setPhysicsLocation(new Vector3f(position.x, height, position.y));
		}
	}

	/**
	 * @return
	 *		return if the tile has been explored
	 */
	public boolean isExplored() {
		return explored;
	}

	/**
	 * @param explored
	 *		the new value to set
	 * @return
	 *		the old value
	 */
	public boolean setExplored(boolean explored) {
		boolean returnValue = this.explored;
		this.explored = explored;
		return returnValue;
	}
} 
