package nl.tudelft.contextproject.model.entities;

import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;

/**
 * A crate that can be thrown and damaged.
 */
public class Crate extends Entity implements PhysicsObject, Health, Holdable {

	private Spatial spatial;
	private RigidBodyControl control;
	private float health;
	private boolean isPickedUp;

	/**
	 * Constructor for a crate with specific health.
	 * 
	 * @param health
	 * 		the start health of the crate
	 */
	public Crate(float health) {
		this.health = health;
	}
	
	/**
	 * Constructor for a crate with .5 health.
	 */
	public Crate() {
		this.health = .5f;
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		
		Box b = new Box(.4f, .4f, .4f);
		this.spatial = new Geometry("box", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors", true);  
		ColorRGBA color = ColorRGBA.Yellow;
		mat.setColor("Diffuse", color);
		mat.setColor("Specular", color);
		mat.setFloat("Shininess", 64f);
		mat.setColor("Ambient", color);
		this.spatial.setMaterial(mat); 
		this.spatial.move(0, 0.5f, 0);
		return spatial;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public PhysicsControl getPhysicsObject() {
		if (control != null) return control;
		
		control = new RigidBodyControl(1.5f);
		getSpatial().addControl(control);
		return control;
	}

	@Override
	public void move(float x, float y, float z) {
		if (control == null) getPhysicsObject();
		control.setPhysicsLocation(control.getPhysicsLocation().add(x, y, z));
		control.update(0);
	}

	@Override
	public EntityType getType() {
		return EntityType.CRATE;
	}
	
	/**
	 * Throw the crate in the specified relative direction.
	 * 
	 * @param move
	 * 		the direction to throw in
	 */
	public void doThrow(Vector3f move) {
		if (control == null) getPhysicsObject();
		control.applyImpulse(move, new Vector3f());
	}
	
	/**
	 * Loads a crate entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the player
	 * @param data
	 * 		the data of the player
	 * @return
	 * 		the player represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static Crate loadEntity(Vector3f position, String[] data) {
		if (data.length != 6) throw new IllegalArgumentException("Invalid data length for loading crate! Expected \"<X> <Y> <Z> Crate <Health>\".");
		
		Crate crate = new Crate(Float.parseFloat(data[4]));
		crate.move(position);
		
		return crate;
	}

	@Override
	public void setHealth(float newHealth) {
		health = newHealth;
	}

	@Override
	public void takeDamage(float damage) {
		health -= damage;
		if (health < 0) {
			setState(EntityState.DEAD);
			getPhysicsObject().setEnabled(false);
		}
	}

	@Override
	public float getHealth() {
		return health;
	}
	
	@Override
	public void update(float tpf) {
		if (this.isPickedUp()) {
			Quaternion rot = Main.getInstance().getCamera().getRotation();
			Vector3f vec = rot.getRotationColumn(2).mult(2f);
			Vector3f vec2 = Main.getInstance().getCurrentGame().getPlayer().getLocation().add(vec.x, 1.5f, vec.z);
			control.setPhysicsLocation(vec2);
			control.setPhysicsRotation(rot);
		}
	}

	@Override
	public boolean isPickedUp() {
		return isPickedUp;
	}

	@Override
	public void pickUp() {
		isPickedUp = true;
	}

	@Override
	public void drop() {
		isPickedUp = false;
		Vector3f move = Main.getInstance().getCamera().getRotation().getRotationColumn(2).mult(1.5f);
		move.y = 2;
		doThrow(move.mult(10));
	}

}
