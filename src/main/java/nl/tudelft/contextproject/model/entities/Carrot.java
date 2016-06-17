package nl.tudelft.contextproject.model.entities;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;

/**
 * A carrot that can be eaten.
 */
public class Carrot extends AbstractEntity {

	private float health;
	private Spatial spatial;
	private Spatial carrot, carrot2, carrot3, carrot4;
	private boolean updated;

	/**
	 * Creates a carrot.
	 */
	public Carrot() {
		this.health = 6;
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		
		Node node = new Node("CarrotNode");
		spatial = node;
		
		carrot = Main.getInstance().getAssetManager().loadModel("Models/carrot.blend");
		carrot2 = Main.getInstance().getAssetManager().loadModel("Models/carrot2.blend");
		carrot3 = Main.getInstance().getAssetManager().loadModel("Models/carrot3.blend");
		carrot4 = Main.getInstance().getAssetManager().loadModel("Models/carrot4.blend");
		
		carrot2.setCullHint(CullHint.Always);
		carrot3.setCullHint(CullHint.Always);
		carrot4.setCullHint(CullHint.Always);
		
		node.attachChild(carrot);
		node.attachChild(carrot2);
		node.attachChild(carrot3);
		node.attachChild(carrot4);
		
		node.move(0, 1, 0);
		
		return node;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void update(float tpf) {
		if (!updated) return;
		
		if (health <= 1) {
			carrot.setCullHint(CullHint.Always);
			carrot2.setCullHint(CullHint.Always);
			carrot3.setCullHint(CullHint.Always);
			carrot4.setCullHint(CullHint.Inherit);
		} else if (health <= 3) {
			carrot.setCullHint(CullHint.Always);
			carrot2.setCullHint(CullHint.Always);
			carrot3.setCullHint(CullHint.Inherit);
			carrot4.setCullHint(CullHint.Always);
		} else if (health <= 5) {
			carrot.setCullHint(CullHint.Always);
			carrot2.setCullHint(CullHint.Inherit);
			carrot3.setCullHint(CullHint.Always);
			carrot4.setCullHint(CullHint.Always);
		}
	}

	@Override
	public void move(float x, float y, float z) {
		getSpatial().move(x, y, z);
	}

	/**
	 * Eat a piece of the carrot.
	 *  
	 * @param amount
	 * 		the amount eaten.
	 */
	public void eat(float amount) {
		updated = true;
		health -= amount;
		if (health < 0) {
			setState(EntityState.DEAD);
		}
	}

	/**
	 * Get the amount of carrot.
	 * 
	 * @return
	 * 		how much carrot is left
	 */
	public float getAmount() {
		return health;
	}

	/**
	 * Loads a carrot entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the carrot
	 * @param data
	 * 		the data of the carrot
	 * @return
	 * 		the carrot represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static Carrot loadEntity(Vector3f position, String[] data) {
		if (data.length != 4) throw new IllegalArgumentException("Invalid data length for loading carrot! Expected \"<X> <Y> <Z> Carrot\".");
		
		Carrot carrot = new Carrot();
		carrot.move(position);
		
		return carrot;
	}

	@Override
	public EntityType getType() {
		return EntityType.CARROT;
	}
}
