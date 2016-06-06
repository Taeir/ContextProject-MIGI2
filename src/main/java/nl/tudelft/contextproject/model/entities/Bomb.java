package nl.tudelft.contextproject.model.entities;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;

/**
 * Class representing a bomb.
 */
public class Bomb extends Entity implements PhysicsObject, Holdable {
	private static final float TIMER = 10;
	private Spatial sp;
	private RigidBodyControl rb;
	private boolean active;
	private float timer;
	private boolean pickedup;
	/**
	 * Constructor for a bomb.
	 */
	public Bomb() {
		sp = Main.getInstance().getAssetManager().loadModel("Models/bomb.blend");
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setTexture("LightMap", Main.getInstance().getAssetManager().loadTexture("Textures/bombtexture.png"));
		mat.setColor("Color", ColorRGBA.White);
		sp.setMaterial(mat);
	}

	@Override
	public Spatial getSpatial() {
		return sp;
	}

	@Override
	public void update(float tpf) {
		if (this.isPickedUp()) {
			Quaternion rot = Main.getInstance().getCamera().getRotation();
			Vector3f vec = rot.getRotationColumn(2).mult(2f);
			Vector3f vec2 = Main.getInstance().getCurrentGame().getPlayer().getLocation().add(vec.x, 1.5f, vec.z);
			rb.setPhysicsLocation(vec2);
			rb.setPhysicsRotation(rot);
		}
		if (active) {
			timer -= tpf;
			if (timer < 0) {
				Explosion exp = new Explosion(40f);
				exp.move(this.getLocation());
				Main.getInstance().getCurrentGame().getEntities().add(exp);
				active = false;
				this.setState(EntityState.DEAD);
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
		sp.addControl(rb);
		return rb;
	}

	@Override
	public void move(float x, float y, float z) {
		if (rb == null) getPhysicsObject();
		rb.setPhysicsLocation(rb.getPhysicsLocation().add(x, y, z));
		rb.update(0);
	}

	/**
	 * activates the bomb, it will explode in 5 seconds.
	 */
	public void activate() {
		if (!active) {
			this.active = true;
			this.timer = TIMER;
		}
	}

	/**
	 * @return 
	 * 		true if bomb is active
	 */
	public boolean getActive() {
		return this.active;
	}

	/**
	 * @return 
	 * 		the timer
	 */
	public float getTimer() {
		return timer;
	}
	
	/**
	 * Loads a bomb entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the bomb
	 * @param data
	 * 		the data of the bomb
	 * @return
	 * 		the bomb represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static Bomb loadEntity(Vector3f position, String[] data) {
		if (data.length != 4) throw new IllegalArgumentException("Invalid data length for loading bomb! Expected \"<X> <Y> <Z> Bomb\".");

		Bomb bomb = new Bomb();
		bomb.move(position);

		return bomb;
	}

	@Override
	public EntityType getType() {
		return EntityType.BOMB;
	}

	/**
	 * @return 
	 * 		returns wether the bomb is picked up or not
	 */
	@Override
	public boolean isPickedUp() {
		return pickedup;
	}

	@Override
	public void pickUp() {
		pickedup = true;
		activate();
	}

	@Override
	public void drop() {
		pickedup = false;
	}
}
