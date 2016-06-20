package nl.tudelft.contextproject.model.entities.exploding;

import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.audio.AudioManager;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.AbstractEntity;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.moving.VRPlayer;
import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;
import nl.tudelft.contextproject.model.entities.util.Health;

/**
 * An explosion that damages the player when close by.
 */
public class Explosion extends AbstractEntity {

	private float maxRadius;
	private Spatial spatial;
	private AudioNode explodeSound;
	private boolean soundStarted;
	private Game game;

	/**
	 * Create an explosion with a certain maximal radius.
	 * 
	 * @param radius
	 * 		the maximal radius of the explosion.	
	 */
	public Explosion(float radius) {
		this.maxRadius = radius;
		this.game = Main.getInstance().getCurrentGame();
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		
		Node node = new Node("Explosion");
		spatial = node;
		
		Sphere sphere = new Sphere(10, 10, .1f);
		Spatial spatial = new Geometry("BOOM!", sphere);
		Material material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		material.setTexture("ColorMap", Main.getInstance().getAssetManager().loadTexture("Textures/explosion.png"));
		spatial.setMaterial(material);
		
		explodeSound = AudioManager.newPositionalSoundEffect("Sound/Effects/explosion.ogg");
		
		node.attachChild(spatial);
		node.attachChild(explodeSound);
		
		return node;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void update(float tpf) {
		Vector3f scale = spatial.getLocalScale();
		if (scale.x > maxRadius) {
			AudioManager.stop(explodeSound);
			setState(EntityState.DEAD);
			return;
		} else if (!soundStarted) {
			soundStarted = true;
			AudioManager.ensurePlaying(explodeSound);
		}
		
		damageEntities(scale.x / 5f, tpf);

		float concurrentMaxRadius = this.maxRadius * tpf;
		spatial.setLocalScale(scale.x + concurrentMaxRadius);
	}

	/**
	 * Damage all entities in range with a given damage.
	 * 
	 * @param range
	 * 		the range for entities to be in
	 * @param damage
	 * 		the damage each entity will take
	 */
	protected void damageEntities(float range, float damage) {
		VRPlayer player = game.getPlayer();
		if (player.getLocation().distance(this.getLocation()) < range) {
			player.takeDamage(damage);
		}
		for (Entity entity : game.getEntities()) {
			if (!(entity instanceof Health)) continue;
			if (entity.getLocation().distance(this.getLocation()) < range) {
				((Health) entity).takeDamage(damage);
			}
		}
	}

	@Override
	public void move(float x, float y, float z) {
		getSpatial().move(x, y, z);
	}

	@Override
	public EntityType getType() {
		return EntityType.EXPLOSION;
	}
}
