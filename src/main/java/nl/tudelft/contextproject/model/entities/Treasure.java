package nl.tudelft.contextproject.model.entities;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;

/**
 * A treasure that can be found by the player, this ends the game.
 */
public class Treasure extends PlayerTrigger {
	
	private Spatial spatial;
	
	/**
	 * Create a treasure.
	 */
	public Treasure() {
		super(1.5f, 0f);
	}
	
	@Override
	public void onTrigger() {
		Main.getInstance().getCurrentGame().endGame(true);
	}
	
	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;

		Box b = new Box(1.5f, .75f, 1f);
		this.spatial = new Geometry("plate", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors", true);    
		mat.setColor("Diffuse", ColorRGBA.Brown);
		mat.setColor("Specular", ColorRGBA.White);
		mat.setFloat("Shininess", 64f);
		mat.setColor("Ambient", ColorRGBA.Brown);
		this.spatial.setMaterial(mat); 
		this.spatial.move(0, 0.5f, 0);
		return spatial;
	}
	
	@Override
	public void setSpatial(Spatial s) {
		this.spatial = s;
	}
}