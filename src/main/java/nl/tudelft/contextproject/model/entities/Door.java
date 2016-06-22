package nl.tudelft.contextproject.model.entities;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.audio.AudioManager;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.entities.util.AbstractPhysicsEntity;
import nl.tudelft.contextproject.model.entities.util.Direction;
import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;
import nl.tudelft.contextproject.util.ParserUtil;

/**
 * Class representing a door.
 */
public class Door extends AbstractPhysicsEntity implements PhysicsObject {
	private ColorRGBA color;
	private Spatial modelSpatial;
	private AudioNode openSound;
	private Material material;

	/**
	 * Constructor for a door with default orientation (WEST).
	 *
	 * @param color
	 * 		Color of the door's lock
	 */
	public Door(ColorRGBA color) {
		this.color = color;
		getSpatial();
	}
	
	/**
	 * Constructor for a door with a color and rotation.
	 * 
	 * @param color
	 * 		the color of the door
	 * @param direction
	 * 		the direction of the door
	 */
	public Door(ColorRGBA color, Direction direction) {
		this(color);
		this.setRotation(direction);
	}
	
	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		
		Node node = new Node("Door");
		spatial = node;
		
		modelSpatial = Main.getInstance().getAssetManager().loadModel("Models/newDoor.j3o");
		modelSpatial.scale(1f, 1f, 0.75f);
		modelSpatial.rotate(0, (float) Math.PI, 0);

		if (modelSpatial instanceof Node) {
			Node modelNode = (Node) modelSpatial;
			Geometry geometry = (Geometry) ((Node) modelNode.getChild("Cylinder")).getChild(0);
			Geometry geometry2 = (Geometry) ((Node) modelNode.getChild("Cylinder")).getChild(2);
			material = geometry.getMaterial();
			material.setColor("Ambient", this.color);
			geometry.setMaterial(material);
			geometry2.setMaterial(material);
		}
		
		openSound = AudioManager.newPositionalSoundEffect("Sound/Effects/door_open.ogg");
		
		node.attachChild(modelSpatial);
		node.attachChild(openSound);
		
		node.move(0, 0.55f, 0);
		
		return node;
	}
	
	@Override
	public PhysicsControl getPhysicsObject() {
		if (rigidBody != null) return rigidBody;
		if (spatial == null) getSpatial();

		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(modelSpatial);
		rigidBody = new RigidBodyControl(sceneShape, 0);
		rigidBody.setPhysicsLocation(spatial.getLocalTranslation());
		return rigidBody;
	}

	/**
	 * Sets the rotation of the door.
	 * 
	 * @param direction
	 * 		the new rotation of the door
	 */
	private void setRotation(Direction direction) {
		if (spatial == null) getSpatial();
		
		switch (direction) {
			case SOUTH:
				modelSpatial.rotate(0, (float) (.5 * Math.PI), 0);
				break;	
			case EAST:
				modelSpatial.rotate(0, (float) Math.PI, 0);
				break;
			case NORTH:
				modelSpatial.rotate(0, (float) (1.5 * Math.PI), 0);
				break;
			default:	// WEST
				break;
		}
		getPhysicsObject();
		rigidBody.setPhysicsRotation(modelSpatial.getLocalRotation());
	}

	/**
	 * Sets the color of the doors lock.
	 * 
	 * @param color
	 * 		the color of the doors lock
	 */
	public void setColor(ColorRGBA color) {
		this.color = color;
		
		if (material != null) material.setColor("Ambient", color);
	}

	/**
	 * @return
	 * 		the color of the doors lock
	 */
	public ColorRGBA getColor() {
		return color;
	}
	
	/**
	 * Opens this door.
	 */
	public void open() {
		AudioManager.ensurePlaying(openSound);
		setState(EntityState.DEAD);
	}
	
	/**
	 * Loads a door entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the door
	 * @param data
	 * 		the data of the door
	 * @return
	 * 		the door represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static Door loadEntity(Vector3f position, String[] data) {
		if (data.length != 6) throw new IllegalArgumentException("Invalid data length for loading door! Expected \"<X> <Y> <Z> Door <Color> <orientation>\".");
		
		Door door = new Door(ParserUtil.getColor(data[4]), Direction.valueOf(data[5]));
		door.move(position);
		
		return door;
	}

	@Override
	public EntityType getType() {
		return EntityType.DOOR;
	}
}