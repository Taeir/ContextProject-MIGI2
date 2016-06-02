package nl.tudelft.contextproject.model.entities;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

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
		Sphere b = new Sphere(10, 10, .02f);
		spatial = new Geometry("Carrot", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Orange);
		spatial.setMaterial(mat);
		spatial.scale(1, 10, 1);
		spatial.rotate((float) Math.toRadians(30), 0, (float) Math.toRadians(15));
		return spatial;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void update(float tpf) { }

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
