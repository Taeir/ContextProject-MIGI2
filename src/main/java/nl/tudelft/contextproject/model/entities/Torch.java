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
 * Class representing a torch.
 */
public class Torch extends Entity implements PhysicsObject {
	private Spatial sp;
	private RigidBodyControl rb;
	private ParticleEmitter fire;
	private boolean torchtype;

	/**
	 * Constructor for a torch.
	 * 
	 * @param type
	 * 		type of the torch (true = walltorch, false = ceilinglamp)
	 */
	public Torch(boolean type) {
		torchtype = type;
		fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", Main.getInstance().getAssetManager().loadTexture("Effects/Explosion/flame.png"));
		fire.setMaterial(mat);
		fire.setImagesX(2); 
		fire.setImagesY(2);
		fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));
		fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f));
		fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0.7f, 0));
		fire.setGravity(0, 0, 0);
		fire.setLowLife(0.2f);
		fire.setHighLife(0.5f);
		fire.getParticleInfluencer().setVelocityVariation(0.0f);
		if (type) {
			sp = Main.getInstance().getAssetManager().loadModel("Models/torch.blend");
			fire.setStartSize(0.15f);
			fire.setEndSize(0.05f);
			fire.move(-0.09f, 0.27f, -0.003f);
		} else {
			sp = Main.getInstance().getAssetManager().loadModel("Models/ceilinglamp.blend");
			fire.setStartSize(0.1f);
			fire.setEndSize(0.04f);
			fire.move(0, 0.11f, 0);
			this.move(0, 5.32f, 0);
		}



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
		if (!torchtype) return;
		sp.rotate(0f, (float) (0.5 * Math.PI), 0f);
		sp.move(-0.075f, 0, -0.075f);
		this.move(0, 0, 0.65f);
	}

	/**
	 * Rotates the torch and the flame to attach to a east wall.
	 */
	public void rotateEast() {
		if (!torchtype) return;
		sp.rotate(0f, (float) (Math.PI), 0f);
		sp.move(-0.17f, 0, 0);
		this.move(.75f, 0, 0);
	}

	/**
	 * Rotates the torch and the flame to attach to a north wall.
	 */
	public void rotateNorth() {
		if (!torchtype) return;
		sp.rotate(0f, (float) (-0.5 * Math.PI), 0f);
		sp.move(-0.095f, 0, 0.08f);
		this.move(0, 0, -.63f);
	}

	/**
	 * Rotates the torch and the flame to attach to a west wall.
	 */
	public void rotateWest() {
		if (!torchtype) return;
		this.move(-.55f, 0, 0);
	}
	
	/**
	 * @return
	 * 		the boolean depicting the type of torch
	 */
	public boolean getTorchType() {
		return torchtype;
	}

	/**
	 * Loads a torch entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the torch
	 * @param data
	 * 		the data of the torch
	 * @return
	 * 		the torch represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static Torch loadEntity(Vector3f position, String[] data) {
		if (data.length != 5) throw new IllegalArgumentException("Invalid data length for loading key! Expected \"<X> <Y> <Z> Torch <Type>\".");
		Torch torch = null;
		if (data[4] == "true") {
			torch = new Torch(true);
		} else {
			torch = new Torch(false);
		}
		torch.move(position);

		return torch;
	}

	@Override
	public EntityType getType() {
		return EntityType.TORCH;
	}
}
