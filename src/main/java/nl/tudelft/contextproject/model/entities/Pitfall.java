package nl.tudelft.contextproject.model.entities;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.environment.PlayerTrigger;
import nl.tudelft.contextproject.model.entities.moving.VRPlayer;
import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;

/**
 * An entity that drops the player through the level when stepped on.
 */
public class Pitfall extends PlayerTrigger {

	private Spatial spatial;
	private final float width;
	private final float crateSqrWidth;
	
	/**
	 * Constructor for Pitfall.
	 * 
	 * @param width 
	 * 		the width of the pitfall
	 */
	public Pitfall(float width) {
		super(width, 2f);
		
		this.width = width;
		this.crateSqrWidth = (width + .3f) * (width + .3f);
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
		
		Game game = Main.getInstance().getCurrentGame();
		VRPlayer vrPlayer = game.getPlayer();
		Vector3f ownLoc = getLocation();
		if (vrPlayer.getLocation().distanceSquared(ownLoc) > width * width) {
			for (Entity entity : game.getEntities()) {
				if (entity instanceof Crate && entity.getLocation().distanceSquared(ownLoc) < crateSqrWidth) {
					entity.setState(EntityState.DEAD);
					return;
				}
			}
		} else {
			vrPlayer.move(0, -2f, 0);
		}
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		Box box = new Box(.4f, .01f, .4f);
		spatial = new Geometry("plate", box);
		Material material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors", true);    
		material.setColor("Diffuse", ColorRGBA.Green);
		material.setColor("Specular", ColorRGBA.White);
		material.setFloat("Shininess", 64f);
		material.setColor("Ambient", ColorRGBA.Green);
		spatial.setMaterial(material);
		spatial.move(0, -.2f, 0);
		return spatial;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
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
	
	@Override
	public boolean collidesWithPlayer(float distance) {
		Game game = Main.getInstance().getCurrentGame();
		Vector3f ownLoc = getLocation();
		Vector3f playerLoc = game.getPlayer().getLocation();
		if (ownLoc.distanceSquared(playerLoc) < distance * distance) return true;
		
		float sqrDistance = (distance + .3f) * (distance + .3f);
		for (Entity entity : game.getEntities()) {
			if (entity instanceof Crate && entity.getLocation().distanceSquared(ownLoc) < sqrDistance) return true;
		}
		return false;
	}
}
