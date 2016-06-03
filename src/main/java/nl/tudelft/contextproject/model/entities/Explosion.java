package nl.tudelft.contextproject.model.entities;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Game;

/**
 * An explosion that damages the player when close by.
 */
public class Explosion extends Entity {

	private float maxRadius;
	private Spatial spatial;
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
		Sphere b = new Sphere(10, 10, .1f);
		spatial = new Geometry("BOOM!", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setTexture("ColorMap", Main.getInstance().getAssetManager().loadTexture("Textures/explosion.png"));
		spatial.setMaterial(mat);
		return spatial;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void update(float tpf) {
		Vector3f scale = spatial.getLocalScale();
		if (scale.x > maxRadius) {
			setState(EntityState.DEAD);
			return;
		}
		damageEntities(scale.x / 5f, tpf);

		float m = maxRadius * tpf;
		spatial.setLocalScale(scale.x + m);
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
		VRPlayer p = game.getPlayer();
		if (p.getLocation().distance(this.getLocation()) < range) {
			p.takeDamage(damage);
		}
		for (Entity e : game.getEntities()) {
			if (!(e instanceof Health)) continue;
			if (e.getLocation().distance(this.getLocation()) < range) {
				((Health) e).takeDamage(damage);
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
