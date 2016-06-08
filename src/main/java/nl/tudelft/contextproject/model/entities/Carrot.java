package nl.tudelft.contextproject.model.entities;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;

/**
 * A carrot that can be eaten.
 */
public class Carrot extends Entity {

	private float health;
	private Spatial spatial;

	/**
	 * Creates a carrot.
	 */
	public Carrot() {
		this.health = 6;
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		spatial = Main.getInstance().getAssetManager().loadModel("Models/carrot.blend");
		spatial.move(0, 1, 0);
		return spatial;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void update(float tpf) { 
		if (health <= 5) {
			spatial = Main.getInstance().getAssetManager().loadModel("Models/carrot2.blend");
		}
		if (health <= 3) {
			spatial = Main.getInstance().getAssetManager().loadModel("Models/carrot3.blend");
		}
		if (health <= 1) {
			spatial = Main.getInstance().getAssetManager().loadModel("Models/carrot4.blend");
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
