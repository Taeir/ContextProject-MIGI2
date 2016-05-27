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
public class Bomb extends Entity implements PhysicsObject {
	private Spatial sp;
	private RigidBodyControl rb;
	private boolean active;
	private float timer;

	/**
	 * Constructor for a bomb.
	 */
	public Bomb() {
		timer = 0f;
		active = false;
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
	public void update(float tdf) {
		if (active) {
			timer += tdf;
			if (timer > 4) {
				Explosion exp = new Explosion(40f);
				Vector3f vec = this.getSpatial().getLocalTranslation();
				exp.move(vec.x, vec.y, vec.z);
				Main.getInstance().getCurrentGame().getEntities().add(exp);
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
	return rb;
}

@Override
public void move(float x, float y, float z) {
	sp.move(x, y, z);
	//explosion.move(x, y, z);
	if (rb == null) getPhysicsObject();

	rb.setPhysicsLocation(rb.getPhysicsLocation().add(x, y, z));
}

/**
 * activates the bomb, it will explode in 5 seconds.
 */
public void activate() {
	this.active = true;
}

/**
 * Returns true if the bomb is active.
 * @return true if bomb is active.
 */
public boolean getActive() {
	return this.active;
}

/**
 * Returns the timer.
 * @return the timer
 */
public float getTimer() {
	return timer;
}
}
