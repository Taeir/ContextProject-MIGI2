package nl.tudelft.contextproject.model.entities;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.entities.util.AbstractPhysicsEntity;
import nl.tudelft.contextproject.model.entities.util.Direction;
import nl.tudelft.contextproject.model.entities.util.EntityType;

/**
 * Class representing a gate.
 */
public class Gate extends AbstractPhysicsEntity implements PhysicsObject {
	private Boolean open;
	
	/**
	 * Constructor for a gate with default orientation (WEST).
	 */
	public Gate() {
		spatial = Main.getInstance().getAssetManager().loadModel("Models/gate.blend");
		spatial.scale(0.5f, 1, 0.5f);
		spatial.rotate(0, (float) (Math.PI), 0);
		spatial.move(0, .6f, 0);
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

	/**
	 * Sets the rotation of the gate.
	 * 
	 * @param direction
	 * 		the new rotation of the gate
	 */
	private void setRotation(Direction direction) {
		switch (direction) {
			case SOUTH:
				spatial.rotate(0, (float) (.5 * Math.PI), 0);
				break;	
			case EAST:
				spatial.rotate(0, (float) Math.PI, 0);
				break;
			case NORTH:
				spatial.rotate(0, (float) (1.5 * Math.PI), 0);
				break;
			default:	// WEST
				break;	
		}
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