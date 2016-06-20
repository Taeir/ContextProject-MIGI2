package nl.tudelft.contextproject.model.entities;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.audio.AudioManager;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.entities.util.AbstractPhysicsEntity;
import nl.tudelft.contextproject.model.entities.util.Direction;
import nl.tudelft.contextproject.model.entities.util.EntityType;

/**
 * Class representing a gate.
 */
public class Gate extends AbstractPhysicsEntity implements PhysicsObject {
	private Boolean open;
	private AudioNode openSound;
	private Spatial modelSpatial;
	
	/**
	 * Constructor for a gate with default orientation (WEST).
	 */
	public Gate() {
		open = false;
	}
	
	/**
	 * Constructor for a gate with orientation.
	 * 
	 * @param orientation
	 * 		the orientation of the gate
	 */
	public Gate(Direction orientation) {
		this();
		getPhysicsObject();
		setRotation(orientation);
	}
	
	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		
		Node node = new Node("Gate");
		spatial = node;
		
		modelSpatial = Main.getInstance().getAssetManager().loadModel("Models/gate.blend");
		modelSpatial.scale(0.5f, 1, 0.5f);
		modelSpatial.rotate(0, (float) Math.PI, 0);
		
		openSound = AudioManager.newPositionalSoundEffect("Sound/Effects/gate_open.ogg");
		
		node.attachChild(modelSpatial);
		node.attachChild(openSound);
		
		node.move(0, .6f, 0);
		
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
	 * Sets the rotation of the gate.
	 * 
	 * @param direction
	 * 		the new rotation of the gate
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

	@Override
	public void update(float tpf) {
		if (open) {
			if (spatial.getLocalTranslation().y < 6) {
				this.move(0, 2 * tpf, 0);
				
				AudioManager.ensurePlaying(openSound);
			} else {
				open = false;
				
				AudioManager.stop(openSound);
			}
		} else {
			if (spatial.getLocalTranslation().y > 1) {
				this.move(0, -0.5f * tpf, 0);
				
				AudioManager.ensurePlaying(openSound);
			}
		}
	}

	/**
	 * Loads a gate entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the gate
	 * @param data
	 * 		the data of the gate
	 * @return
	 * 		the gate represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static Gate loadEntity(Vector3f position, String[] data) {
		if (data.length != 5) throw new IllegalArgumentException("Invalid data length for loading gate! Expected \"<X> <Y> <Z> Gate <orientation>\".");
		Gate gate = new Gate(Direction.valueOf(data[4]));
		gate.move(position);
		return gate;
	}

	@Override
	public EntityType getType() {
		return EntityType.GATE;
	}

	/**
	 * Opens the gate.
	 */
	public void openGate() {
		open = true;
	}
	
	/**
	 * @return
	 * 		returns true if the gate is open
	 */
	public boolean getOpen() {
		return open;
	}
}