package nl.tudelft.contextproject.model.entities;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;

/**
 * Class representing a gate.
 */
public class Gate extends AbstractPhysicsEntity implements PhysicsObject {
	private Boolean open;
	
	/**
	 * Constructor for a gate.
	 */
	public Gate() {
		spatial = Main.getInstance().getAssetManager().loadModel("Models/gate.blend");
		spatial.scale(0.5f, 1, 0.5f);
		spatial.rotate(0, (float) (Math.PI), 0);
		spatial.move(0, .6f, 0);
		open = false;
	}

	@Override
	public void update(float tpf) {
		if (open) {
			if (spatial.getLocalTranslation().y < 6) {
				this.move(0, 2 * tpf, 0);
			} else {
				open = false;
			}
		} else {
			if (spatial.getLocalTranslation().y > 1) {
				this.move(0, -0.5f * tpf, 0);
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