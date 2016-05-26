package nl.tudelft.contextproject.model.entities;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;

/**
 * Class representing a bomb.
 */
public class Bunny extends Entity implements PhysicsObject {
	private Spatial sp;
	private RigidBodyControl rb;
	private boolean active;
	private float timer;

	/**
	 * Constructor for a bomb.
	 */
	public Bunny() {
		sp = Main.getInstance().getAssetManager().loadModel("Models/bomb.blend");
	}

	@Override
	public Spatial getSpatial() {
		return sp;
	}

	@Override
	public void update(float tdf) {
		System.out.println("test");
		if (this.collidesWithPlayer(20)) {
			Vector3f playerpos = Main.getInstance().getCurrentGame().getPlayer().getSpatial().getLocalTranslation();
			Vector3f pos = sp.getLocalTranslation();
			Vector3f move = playerpos.add(pos.mult(-1f));
			this.move(move.x * 0.5f * tdf, 0, 0.5f * move.z * tdf);
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
	return rb;
}

@Override
public void move(float x, float y, float z) {
	sp.move(x, y, z);
	//explosion.move(x, y, z);
	if (rb == null) getPhysicsObject();

	rb.setPhysicsLocation(rb.getPhysicsLocation().add(x, y, z));
}
}
