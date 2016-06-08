package nl.tudelft.contextproject.model.entities;

import com.jme3.bullet.collision.shapes.CollisionShape;

import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;

/**
 * Class representing a gate.
 */
public class Gate extends Entity implements PhysicsObject {
	private Spatial sp;
	private RigidBodyControl rb;
	private Boolean open;
	
	/**
	 * Constructor for a gate.
	 */
	public Gate() {
		sp = Main.getInstance().getAssetManager().loadModel("Models/door.blend");
		sp.scale(1.2f, 2.2f, 1.2f);
		sp.rotate(0, (float) (Math.PI), 0);
		sp.move(0, .5f, 0);
		open = false;
	}

	@Override
	public Spatial getSpatial() {
		return sp;
	}

	@Override
	public void update(float tpf) {
		if (open) {
			if (sp.getLocalTranslation().y < 6) {
				this.move(0, 2 * tpf, 0);
			} else {
				open = false;
			}
		} else {
			if (sp.getLocalTranslation().y > .5f) {
				this.move(0, -0.5f * tpf, 0);
			}
		}
	}

	@Override
	public void setSpatial(Spatial spatial) {
		sp = spatial;
	}

	@Override
	public PhysicsControl getPhysicsObject() {
		if (rb != null) return rb;

		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sp);
		rb = new RigidBodyControl(sceneShape, 0);
		rb.setPhysicsLocation(sp.getLocalTranslation());
		return rb;
	}

	@Override
	public void move(float x, float y, float z) {
		if (rb == null) getPhysicsObject();

		sp.move(x, y, z);
		rb.setPhysicsLocation(rb.getPhysicsLocation().add(x, y, z));
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
		if (data.length != 4) throw new IllegalArgumentException("Invalid data length for loading gate! Expected \"<X> <Y> <Z> Gate\".");
		Gate gate = new Gate();
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