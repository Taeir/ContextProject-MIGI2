package nl.tudelft.contextproject.model.entities;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

import nl.tudelft.contextproject.Main;

/**
 * An explosion that damages the player when close by.
 */
public class Explosion extends Entity {

	private float maxRadius;
	private Spatial spatial;
	private VRPlayer player;

	/**
	 * Create an explosion with a certain maximal radius.
	 * 
	 * @param radius
	 * 		the maximal radius of the explosion.	
	 */
	public Explosion(float radius) {
		this.maxRadius = radius;
		this.player = Main.getInstance().getCurrentGame().getPlayer();
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
		if (collidesWithPlayer(scale.x / 5f)) {
			player.takeDamage(tpf);
		}

		float m = maxRadius * tpf;
		spatial.setLocalScale(scale.x + m);
	}

	@Override
	public void move(float x, float y, float z) {
		getSpatial().move(x, y, z);
	}

}
