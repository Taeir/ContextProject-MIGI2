package nl.tudelft.contextproject.model.entities;

import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * A damaged wall that can be destroyed by explosions.
 */
public class DamagedWall extends Entity implements Health, PhysicsObject {

	private Spatial spatial;
	private RigidBodyControl phControl;
	private Material material;
	private float health = 1.5f;

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		
		Box b = new Box(.5f, TileType.WALL.getHeight(), .5f);
		this.spatial = new Geometry("Box", b);
		material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors", true);  
		material.setFloat("Shininess", 64f);
		material.setTexture("LightMap", TileType.WALL.getTexture());
		setMaterialColor();
		this.spatial.setMaterial(material);
		spatial.move(0, TileType.WALL.getHeight() + .5f, 0);
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
		phControl = new RigidBodyControl(1.5f);
		spatial.addControl(phControl);
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
		} else {
			setMaterialColor();
		}
	}

	private void setMaterialColor() {
		float c = health / 4f;
		ColorRGBA color = new ColorRGBA(c, c, c, 1);
		material.setColor("Diffuse", color);
		material.setColor("Specular", color);
		material.setColor("Ambient", color);
	}

	@Override
	public float getHealth() {
		return health;
	}

	/**
	 * Loads an {@link DamagedWall} entity from an array of String data.
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
	public static DamagedWall loadEntity(Vector3f position, String[] data) {
		if (data.length != 4) throw new IllegalArgumentException("Invalid data length for loading a damaged wall! Expected \"<X> <Y> <Z> DamagedWall\".");
		
		DamagedWall wall = new DamagedWall();
		wall.move(position);
		
		return wall;
	}
	
	@Override
	public EntityType getType() {
		return EntityType.DAMAGED_WALL;
	}
}
