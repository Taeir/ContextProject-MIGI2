package nl.tudelft.contextproject.model.entities.util;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.entities.Entity;

/**
 * Abstract base implementation of an entity with Physics.
 */
public abstract class AbstractPhysicsEntity extends Entity implements PhysicsObject {
	protected Spatial spatial;
	protected RigidBodyControl rigidBody;
	
	@Override
	public Spatial getSpatial() {
		return spatial;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void update(float tdf) { }

	@Override
	public PhysicsControl getPhysicsObject() {
		if (rigidBody != null) return rigidBody;

		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(getSpatial());
		rigidBody = new RigidBodyControl(sceneShape, 0);
		rigidBody.setPhysicsLocation(spatial.getLocalTranslation());
		return rigidBody;
	}

	@Override
	public void move(float x, float y, float z) {
		if (rigidBody == null) getPhysicsObject();
		
		spatial.move(x, y, z);
		rigidBody.setPhysicsLocation(rigidBody.getPhysicsLocation().add(x, y, z));
	}
}
