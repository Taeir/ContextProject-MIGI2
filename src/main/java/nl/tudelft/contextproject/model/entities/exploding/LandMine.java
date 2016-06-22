package nl.tudelft.contextproject.model.entities.exploding;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.Crate;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.environment.PlayerTrigger;
import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;

/**
 * Landmine that explodes when the player steps on it.
 */
public class LandMine extends PlayerTrigger {

	public static final float RADIUS = 20f;
	public static final float DAMAGE_MULTIPLIER = 1f;
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

		Box box = new Box(.1f, .01f, .1f);
		this.spatial = new Geometry("plate", box);
		Material material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors", true);    
		material.setColor("Diffuse", ColorRGBA.Green.mult(.4f));
		material.setColor("Specular", ColorRGBA.White);
		material.setFloat("Shininess", 64f);
		material.setColor("Ambient", ColorRGBA.Green.mult(.4f));
		this.spatial.setMaterial(material); 
		this.spatial.move(0, 0.505f, 0);
		return spatial;
	}
	
	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}
	
	@Override
	public void onTrigger() {
		Explosion explosion = new Explosion(RADIUS, DAMAGE_MULTIPLIER);
		Vector3f location = getLocation();
		explosion.move(location.x, location.y, location.z);
		Main.getInstance().getCurrentGame().addEntity(explosion);
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
	
	@Override
	public boolean collidesWithPlayer(float distance) {
		Game game = Main.getInstance().getCurrentGame();
		Vector3f playerLoc = game.getPlayer().getLocation();
		Vector3f ownLoc = getLocation();
		if (ownLoc.distanceSquared(playerLoc) < distance * distance) return true;
		
		float crateDistance = (distance + .3f) * (distance + .3f);
		for (Entity entity : game.getEntities()) {
			if (entity instanceof Crate && ownLoc.distanceSquared(entity.getLocation()) < crateDistance) return true;
		}
		return false;
	}
}
