package nl.tudelft.contextproject.model.entities.environment;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Observer;
import nl.tudelft.contextproject.model.entities.AbstractEntity;
import nl.tudelft.contextproject.model.entities.util.EntityType;
import nl.tudelft.contextproject.util.FileUtil;
import nl.tudelft.contextproject.util.ScriptLoader;
import nl.tudelft.contextproject.util.ScriptLoaderException;

/**
 * Entity that triggers an action when the player is close enough.
 */
public class PlayerTrigger extends AbstractEntity {

	private float triggerDist;
	private Observer action;
	private Spatial spatial;
	private float timer;
	private float coolDown;
	
	/**
	 * Constructor for a PlayerTrigger entity.
	 *
	 * @param triggerDist
	 * 		how close the player needs to be to trigger the action
	 * @param coolDown
	 * 		how long (in seconds) the delay is between two triggers
	 * @param action
	 * 		a TickListener that is updated when the action is triggered
	 */
	public PlayerTrigger(float triggerDist, float coolDown, Observer action) {
		this.triggerDist = triggerDist;
		this.action = action;
		this.coolDown = coolDown;
		getSpatial();
	}
	
	/**
	 * Construct a {@link PlayerTrigger} with an empty action.
	 * Only use this constructor when calling super() in a constructor.
	 * 
	 * @param triggerDist
	 * 		the distance to the player to check for
	 * @param coolDown
	 * 		the cooldown between two triggers
	 */
	protected PlayerTrigger(float triggerDist, float coolDown) {
		this.triggerDist = triggerDist;
		this.coolDown = coolDown;
		this.action = new Observer() {			
			@Override
			public void update(float tpf) { }
		};
		getSpatial();
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;

		Box box = new Box(.4f, .01f, .4f);
		this.spatial = new Geometry("plate", box);
		Material material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		material.setBoolean("UseMaterialColors", true);    
		material.setColor("Diffuse", ColorRGBA.Orange);
		material.setColor("Specular", ColorRGBA.White);
		material.setFloat("Shininess", 64f);
		material.setColor("Ambient", ColorRGBA.Orange);
		this.spatial.setMaterial(material); 
		this.spatial.move(0, 0.505f, 0);
		return spatial;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void update(float tpf) {
		if (timer <= 0 && collidesWithPlayer(triggerDist)) {
			this.onTrigger();
			timer = coolDown;
		}
		if (timer > 0) {
			timer -= tpf;
		}
	}

	/**
	 * Method called when the action must be triggered.
	 * This triggers the action.
	 */
	public void onTrigger() {
		action.update(0);
	}

	@Override
	public void move(float x, float y, float z) {
		getSpatial().move(x, y, z);
	}
	
	/**
	 * @return
	 * 		the action of this player trigger
	 */
	public Observer getAction() {
		return action;
	}
	
	/**
	 * Loads a player trigger entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the player trigger
	 * @param data
	 * 		the data of the player trigger
	 * @return
	 * 		the player trigger represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 * @throws ScriptLoaderException
	 * 		if the script of the player trigger that is loaded is not present or has errors
	 */
	public static PlayerTrigger loadEntity(Vector3f position, String[] data) throws ScriptLoaderException {
		if (data.length != 7) {
			throw new IllegalArgumentException("Invalid data length for loading player trigger! "
					+ "Expected \"<X> <Y> <Z> PlayerTrigger <TriggerDistance> <Cooldown> <File>\".");
		}
		
		float triggerDist = Float.parseFloat(data[4]);
		float coolDown = Float.parseFloat(data[5]);

		ScriptLoader loader = new ScriptLoader(FileUtil.getFile(data[3]));
		Observer listener = loader.getInstanceOf(data[6], Observer.class);
		
		PlayerTrigger trigger = new PlayerTrigger(triggerDist, coolDown, listener);
		trigger.move(position);
		return trigger;
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER_TRIGGER;
	}
}
