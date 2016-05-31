package nl.tudelft.contextproject.model.entities;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.entities.control.BunnyAI;

/**
 * An {@link MovingEntity} that has a {@link BunnyAI}.
 */
public class KillerBunny extends MovingEntity implements PhysicsObject {

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
		spatial.move(position);
		getPhysicsObject();

	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		spatial = Main.getInstance().getAssetManager().loadModel("Models/bunnylowpoly.blend");
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

		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(.15f, .15f, 1);
		control = new CharacterControl(capsuleShape, .1f);

		control.setJumpSpeed(5f);
		control.setFallSpeed(15);
		control.setGravity(13);

		control.setPhysicsLocation(spatial.getLocalTranslation());
		return control;
	}

}
