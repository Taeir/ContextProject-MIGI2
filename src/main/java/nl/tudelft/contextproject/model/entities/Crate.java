package nl.tudelft.contextproject.model.entities;

import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import jmevr.app.VRApplication;
import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;
import nl.tudelft.contextproject.model.entities.util.Health;
import nl.tudelft.contextproject.model.entities.util.Holdable;

/**
 * A crate that can be thrown and damaged.
 */
public class Crate extends AbstractEntity implements PhysicsObject, Health, Holdable {

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
		
		Box box = new Box(.4f, .4f, .4f);
		this.spatial = new Geometry("box", box);
		Material material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors", true);  
		ColorRGBA color = ColorRGBA.Yellow;
		material.setColor("Diffuse", color);
		material.setColor("Specular", color);
		material.setFloat("Shininess", 64f);
		material.setColor("Ambient", color);
		material.setTexture("LightMap", Main.getInstance().getAssetManager().loadTexture("Textures/crate.png"));
		this.spatial.setMaterial(material); 
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
		control.setPhysicsRotation(new Quaternion());
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
		if (data.length != 5) throw new IllegalArgumentException("Invalid data length for loading crate! Expected \"<X> <Y> <Z> Crate <Health>\".");
		
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
		if (this.isPickedUp() && VRApplication.getVRViewManager() != null) {
			Camera camera = VRApplication.getVRViewManager().getCamLeft();
			Vector3f vec = camera.getDirection();
			Vector3f vec2 = Main.getInstance().getCurrentGame().getPlayer().getLocation().add(vec.x * 2f, 1.5f, vec.z * 2f);
			control.setPhysicsLocation(vec2);
			control.setPhysicsRotation(camera.getRotation());
		}
		if (getLocation().y < 0.5f) {
			move(0, 0.5f, 0);
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
		if (isPickedUp) {
			isPickedUp = false;
			Vector3f move;
			if (VRApplication.getVRViewManager() != null) {
				move = VRApplication.getVRViewManager().getCamLeft().getDirection();
			} else {
				move = Main.getInstance().getCamera().getDirection();
			}
			move.y = 1.5f;
			doThrow(move.mult(6));
		}
	}

}
