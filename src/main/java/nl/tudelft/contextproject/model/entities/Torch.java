package nl.tudelft.contextproject.model.entities;

import com.jme3.bullet.collision.shapes.CollisionShape;

import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;

/**
 * Class representing a key.
 */
public class Torch extends Entity implements PhysicsObject {
	private Spatial sp;
	private RigidBodyControl rb;
	private ParticleEmitter fire;

	/**
	 * Constructor for a key.
	 */
	public Torch() {
		sp = Main.getInstance().getAssetManager().loadModel("Models/torch.blend");
		fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
	    Material mat = new Material(Main.getInstance().getAssetManager(), 
	            "Common/MatDefs/Misc/Particle.j3md");
	    mat.setTexture("Texture", Main.getInstance().getAssetManager().loadTexture(
	            "Effects/Explosion/flame.png"));
	    fire.setMaterial(mat);
	    fire.setImagesX(2); 
	    fire.setImagesY(2);
	    fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));
	    fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f));
	    fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 1, 0));
	    fire.setStartSize(0.15f);
	    fire.setEndSize(0.05f);
	    fire.setGravity(0, 0, 0);
	    fire.setLowLife(0.2f);
	    fire.setHighLife(0.5f);
	    fire.getParticleInfluencer().setVelocityVariation(0.1f);
	    fire.move(-0.09f, 0.27f, -0.003f);
	}

	@Override
	public Spatial getSpatial() {
		return sp;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		sp = spatial;
	}

	@Override
	public void update(float tdf) { }

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
		fire.move(x, y, z);
		if (rb == null) getPhysicsObject();

		rb.setPhysicsLocation(rb.getPhysicsLocation().add(x, y, z));
	}
	/**
	 * @return
	 * 		the flame on the torch
	 */
	public ParticleEmitter getFire() {
		return fire;
	}
	
	/**
	 * Rotates the torch and the flame to attach to a south wall.
	 */
	public void rotateSouth() {
		sp.rotate(0f, (float) (0.5 * Math.PI), 0f);
		sp.move(-0.075f, 0, -0.075f);
		this.move(0, 0, 0.65f);
	}
	
	/**
	 * Rotates the torch and the flame to attach to a east wall.
	 */
	public void rotateEast() {
		sp.rotate(0f, (float) (Math.PI), 0f);
		sp.move(-0.17f, 0, 0);
		this.move(.75f, 0, 0);
	}
	
	/**
	 * Rotates the torch and the flame to attach to a north wall.
	 */
	public void rotateNorth() {
		sp.rotate(0f, (float) (-0.5 * Math.PI), 0f);
		sp.move(-0.095f, 0, 0.08f);
		this.move(0, 0, -.63f);
	}
	
	/**
	 * Rotates the torch and the flame to attach to a west wall.
	 */
	public void rotateWest() {
		this.move(-.55f, 0, 0);
	}
	
}
