package nl.tudelft.contextproject.model.entities.moving;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.entities.control.BunnyAI;
import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;
import nl.tudelft.contextproject.model.entities.util.Health;

/**
 * An {@link MovingEntity} that has a {@link BunnyAI}.
 */
public class KillerBunny extends MovingEntity implements PhysicsObject, Health {

	private float health = 1;
	private Spatial spatial;
	private CharacterControl control;

	/**
	 * Constructs a {@link KillerBunny} at a specified location.
	 * 
	 * @param position
	 * 		the start location of the bunny
	 */
	public KillerBunny(Vector3f position) {
		super(new BunnyAI());
		spatial = getSpatial();
		spatial.setLocalTranslation(position.add(0, 1, 0));
		getPhysicsObject();
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		spatial = Main.getInstance().getAssetManager().loadModel("Models/bunnylowpoly.j3o");
		spatial.scale(2);
		return spatial;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void move(float x, float y, float z) {
		control.warp(control.getPhysicsLocation().add(x, y, z));
		getSpatial().setLocalTranslation(control.getPhysicsLocation());
	}

	@Override
	public PhysicsControl getPhysicsObject() {
		if (control != null) return control;
		
		BoxCollisionShape boxShape = new BoxCollisionShape(new Vector3f(0.2f, 0.2f, 0.2f));
		control = new CharacterControl(boxShape, .1f);

		control.setJumpSpeed(5f);
		control.setFallSpeed(15);
		control.setGravity(13);

		control.setPhysicsLocation(spatial.getLocalTranslation());
		return control;
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
		}
	}

	@Override
	public float getHealth() {
		return health;
	}

	@Override
	public EntityType getType() {
		return EntityType.KILLER_BUNNY;
	}
	
	/**
	 * Loads a killerBunny entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the bunny
	 * @param data
	 * 		the data of the bunny
	 * @return
	 * 		the bunny represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static KillerBunny loadEntity(Vector3f position, String[] data) {
		if (data.length != 4) throw new IllegalArgumentException("Invalid data length for loading a killer bunny! Expected \"<X> <Y> <Z> KillerBunny\".");
		
		KillerBunny bunny = new KillerBunny(position);		
		return bunny;
	}
}
