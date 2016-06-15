package nl.tudelft.contextproject.model.entities.exploding;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import jmevr.app.VRApplication;
import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.entities.util.AbstractPhysicsEntity;
import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;
import nl.tudelft.contextproject.model.entities.util.Holdable;

/**
 * Class representing a bomb.
 */
public class Bomb extends AbstractPhysicsEntity implements PhysicsObject, Holdable {
	private static final float TIMER = 10;
	
	private boolean active;
	private float timer;
	private boolean pickedup;
	
	/**
	 * Constructor for a bomb.
	 */
	public Bomb() {
		spatial = Main.getInstance().getAssetManager().loadModel("Models/bomb.blend");
		Material material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		material.setTexture("LightMap", Main.getInstance().getAssetManager().loadTexture("Textures/bombtexture.png"));
		material.setColor("Color", ColorRGBA.White);
		spatial.move(0, 1, 0);
		spatial.setMaterial(material);
	}

	@Override
	public void update(float tpf) {
		if (this.isPickedUp() && VRApplication.getVRViewManager() != null) {
			Camera camera = VRApplication.getVRViewManager().getCamLeft();
			Vector3f vec = camera.getDirection().mult(2f);
			Vector3f vec2 = Main.getInstance().getCurrentGame().getPlayer().getLocation().add(vec.x, 1.5f, vec.z);
			rigidBody.setPhysicsLocation(vec2);
			rigidBody.setPhysicsRotation(camera.getRotation());
		}
		if (active) {
			timer -= tpf;
			if (timer < 0) {
				Explosion explosion = new Explosion(40f);
				explosion.move(this.getLocation());
				Main.getInstance().getCurrentGame().getEntities().add(explosion);
				active = false;
				pickedup = false;
				this.setState(EntityState.DEAD);
			}
		}
	}

	@Override
	public PhysicsControl getPhysicsObject() {
		if (rigidBody != null) return rigidBody;

		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(getSpatial());
		rigidBody = new RigidBodyControl(sceneShape, 0);
		rigidBody.setPhysicsLocation(spatial.getLocalTranslation());
		spatial.addControl(rigidBody);
		return rigidBody;
	}

	@Override
	public void move(float x, float y, float z) {
		if (rigidBody == null) getPhysicsObject();
		rigidBody.setPhysicsLocation(rigidBody.getPhysicsLocation().add(x, y, z));
		rigidBody.update(0);
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
