package nl.tudelft.contextproject.model.entities;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;

/**
 * An invisible wall that can be removed by damaging it.
 */
public class InvisibleWall extends Entity implements PhysicsObject, Health {
	private Spatial spatial;
	private RigidBodyControl phControl;
	private Material material;
	private float health = .75f;

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;

		Box b = new Box(.5f, 2f, .5f);
		spatial = new Geometry("Invisible", b);
		material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", new ColorRGBA(0, 0, 0, 0));
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		spatial.setMaterial(material); 
		spatial.move(0, 2.5f, 0);
		return spatial;	
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void update(float tpf) { }

	@Override
	public PhysicsControl getPhysicsObject() {
		if (phControl != null) return phControl;
		
		if (spatial == null) {
			this.getSpatial();
		}
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(spatial);
		phControl = new RigidBodyControl(sceneShape, 0);
		phControl.setPhysicsLocation(spatial.getLocalTranslation());
		return phControl;
	}

	@Override
	public void move(float x, float y, float z) {
		getPhysicsObject();
		phControl.setPhysicsLocation(getLocation().add(x, y, z));
		spatial.setLocalTranslation(phControl.getPhysicsLocation());
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
		} else if (health < .5) {
			material.setColor("Color", new ColorRGBA(0, 0, 0, .6f));
		}
		System.out.println(health);
	}

	@Override
	public float getHealth() {
		return health;
	}
}
