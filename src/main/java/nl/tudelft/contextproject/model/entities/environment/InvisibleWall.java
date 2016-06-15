package nl.tudelft.contextproject.model.entities.environment;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.entities.util.AbstractPhysicsEntity;
import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;
import nl.tudelft.contextproject.model.entities.util.Health;

/**
 * An invisible wall that can be removed by damaging it.
 */
public class InvisibleWall extends AbstractPhysicsEntity implements PhysicsObject, Health {
	private Material material;
	private float health = .75f;

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;

		Box box = new Box(.5f, 3f, .5f);
		spatial = new Geometry("Invisible", box);
		material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", new ColorRGBA(0, 0, 0, 0));
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		spatial.setMaterial(material);
		spatial.setQueueBucket(Bucket.Transparent);
		spatial.move(0, 3.5f, 0);
		return spatial;	
	}

	@Override
	public void move(float x, float y, float z) {
		getPhysicsObject();
		rigidBody.setPhysicsLocation(getLocation().add(x, y, z));
		spatial.setLocalTranslation(rigidBody.getPhysicsLocation());
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
	}

	@Override
	public float getHealth() {
		return health;
	}

	/**
	 * Loads an {@link InvisibleWall} entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the wall
	 * @param data
	 * 		the data of the wall
	 * @return
	 * 		the wall represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static InvisibleWall loadEntity(Vector3f position, String[] data) {
		if (data.length != 4) throw new IllegalArgumentException("Invalid data length for loading an invisible wall! Expected \"<X> <Y> <Z> InvisibleWall\".");
		
		InvisibleWall wall = new InvisibleWall();
		wall.move(position);
		
		return wall;
	}
	
	@Override
	public EntityType getType() {
		return EntityType.INVISIBLE_WALL;
	}
}
