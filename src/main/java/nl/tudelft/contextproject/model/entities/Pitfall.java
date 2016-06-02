package nl.tudelft.contextproject.model.entities;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;

/**
 * An entity that drops the player through the level when stepped on.
 */
public class Pitfall extends PlayerTrigger {

	private Spatial sp;
	private final float width;
	
	/**
	 * Constructor for Pitfall.
	 * 
	 * @param width 
	 * 		the width of the pitfall
	 */
	public Pitfall(float width) {
		super(width, 2f);
		
		this.width = width;
	}
	
	/**
	 * @return
	 * 		the width of this Pitfall
	 */
	public float getWidth() {
		return width;
	}
	
	@Override
	public void onTrigger() {
		this.setState(EntityState.DEAD);
		VRPlayer p = Main.getInstance().getCurrentGame().getPlayer();
		p.move(0, -2f, 0);
	}

	@Override
	public Spatial getSpatial() {
		if (sp != null) return sp;
		Box b = new Box(.4f, .01f, .4f);
		sp = new Geometry("plate", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors", true);    
		mat.setColor("Diffuse", ColorRGBA.Green);
		mat.setColor("Specular", ColorRGBA.White);
		mat.setFloat("Shininess", 64f);
		mat.setColor("Ambient", ColorRGBA.Green);
		sp.setMaterial(mat);
		sp.move(0, -.2f, 0);
		return sp;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.sp = spatial;
	}

	@Override
	public void move(float x, float y, float z) {
		getSpatial().move(x, y, z);
	}

	/**
	 * Loads a pitfall entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the pitfall
	 * @param data
	 * 		the data of the pitfall
	 * @return
	 * 		the pitfall represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static Pitfall loadEntity(Vector3f position, String[] data) {
		if (data.length != 5) throw new IllegalArgumentException("Invalid data length for loading pitfall! Expected \"<X> <Y> <Z> Pitfall <Width>\".");

		Pitfall pitfall = new Pitfall(Float.parseFloat(data[4]));
		pitfall.move(position);
		return pitfall;
	}

	@Override
	public EntityType getType() {
		return EntityType.PITFALL;
	}
}