package nl.tudelft.contextproject.model.entities;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;

/**
 * Landmine that explodes when the player steps on it.
 */
public class LandMine extends PlayerTrigger {

	private Spatial spatial;

	/**
	 * Create a new {@link LandMine}.
	 */
	public LandMine() {
		super(.5f, 0);
	}
	
	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;

		Box b = new Box(.1f, .01f, .1f);
		this.spatial = new Geometry("plate", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors", true);    
		mat.setColor("Diffuse", ColorRGBA.Green.mult(.4f));
		mat.setColor("Specular", ColorRGBA.White);
		mat.setFloat("Shininess", 64f);
		mat.setColor("Ambient", ColorRGBA.Green.mult(.4f));
		this.spatial.setMaterial(mat); 
		this.spatial.move(0, 0.505f, 0);
		return spatial;
	}
	
	@Override
	public void setSpatial(Spatial s) {
		this.spatial = s;
	}
	
	@Override
	public void onTrigger() {
		Explosion e = new Explosion(20f);
		Vector3f m = getLocation();
		e.move(m.x, m.y, m.z);
		Main.getInstance().getCurrentGame().addEntity(e);
		this.setState(EntityState.DEAD);
	}

	/**
	 * Loads a landmine entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the landmine
	 * @param data
	 * 		the data of the landmine
	 * @return
	 * 		the landmine represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static LandMine loadEntity(Vector3f position, String[] data) {
		if (data.length != 4) throw new IllegalArgumentException("Invalid data length for loading landmine! Expected \"<X> <Y> <Z> LandMine\".");

		LandMine mine = new LandMine();
		mine.move(position);
		return mine;
	}

	@Override
	public EntityType getType() {
		return EntityType.LANDMINE;
	}
}
