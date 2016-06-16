package nl.tudelft.contextproject.model.entities.environment;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.util.EntityType;

/**
 * Class representing a torch.
 */
public class Torch extends Entity {
	private ParticleEmitter fire;
	private Spatial spatial;
	private boolean torchtype;
	private Spatial torchSpatial;

	/**
	 * Constructor for a torch.
	 * 
	 * @param type
	 * 		type of the torch (true = walltorch, false = ceilinglamp)
	 */
	public Torch(boolean type) {
		torchtype = type;
		fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
		Material material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
		material.setTexture("Texture", Main.getInstance().getAssetManager().loadTexture("Effects/Explosion/flame.png"));
		fire.setMaterial(material);
		fire.setImagesX(2); 
		fire.setImagesY(2);
		fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));
		fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f));
		fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0.7f, 0));
		fire.setGravity(0, 0, 0);
		fire.setLowLife(0.2f);
		fire.setHighLife(0.5f);
		fire.getParticleInfluencer().setVelocityVariation(0.0f);
		spatial = new Node("torch");
		((Node) spatial).attachChild(fire);
		if (type) {
			torchSpatial = Main.getInstance().getAssetManager().loadModel("Models/torch.blend");
			fire.setStartSize(0.15f);
			fire.setEndSize(0.05f);
			fire.move(-0.09f, 0.27f, -0.003f);
		} else {
			torchSpatial = Main.getInstance().getAssetManager().loadModel("Models/ceilinglamp.blend");
			fire.setStartSize(0.1f);
			fire.setEndSize(0.04f);
			fire.move(0, 0.11f, 0);
		}
		((Node) spatial).attachChild(torchSpatial);
		spatial.move(-0.05f, 0, -0.05f);
	}
	
	/**
	 * @return the torchtype
	 */
	public boolean isTorchtype() {
		return torchtype;
	}

	/**
	 * @param torchtype the torchtype to set
	 */
	public void setTorchtype(boolean torchtype) {
		this.torchtype = torchtype;
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
		spatial.rotate(0f, (float) (0.5 * Math.PI), 0f);
		spatial.move(0, 0, -0.42f);
	}

	/**
	 * Rotates the torch and the flame to attach to a east wall.
	 */
	public void rotateEast() {
		if (!torchtype) return;
		spatial.rotate(0f, (float) (Math.PI), 0f);
		spatial.move(-0.42f, 0, 0);
	}

	/**
	 * Rotates the torch and the flame to attach to a north wall.
	 */
	public void rotateNorth() {
		if (!torchtype) return;
		spatial.rotate(0f, (float) (-0.5 * Math.PI), 0f);
		spatial.move(0, 0, 0.52f);
	}

	/**
	 * Rotates the torch and the flame to attach to a west wall.
	 */
	public void rotateWest() {
		if (!torchtype) return;
		spatial.move(0.52f, 0, 0);
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
		if (data.length != 5) throw new IllegalArgumentException("Invalid data length for loading torch! Expected \"<X> <Y> <Z> Torch <Type>\".");
		
		Torch torch = new Torch(Boolean.parseBoolean(data[4]));
		torch.move(position);

		return torch;
	}

	@Override
	public EntityType getType() {
		return EntityType.TORCH;
	}

	@Override
	public Spatial getSpatial() {
		return spatial;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void update(float tpf) { }

	@Override
	public void move(float x, float y, float z) {
		spatial.move(x, y, z);
	}
}
