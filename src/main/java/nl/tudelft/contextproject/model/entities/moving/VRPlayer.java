package nl.tudelft.contextproject.model.entities.moving;

import java.util.HashSet;
import java.util.Set;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.audio.AudioManager;
import nl.tudelft.contextproject.model.Inventory;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.Observer;
import nl.tudelft.contextproject.model.Observable;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.entities.Door;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.Key;
import nl.tudelft.contextproject.model.entities.VoidPlatform;
import nl.tudelft.contextproject.model.entities.control.NoControl;
import nl.tudelft.contextproject.model.entities.control.PlayerControl;
import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;
import nl.tudelft.contextproject.model.entities.util.Health;
import nl.tudelft.contextproject.model.entities.util.Holdable;

/**
 * Class representing the player wearing the VR headset.
 */
public class VRPlayer extends MovingEntity implements PhysicsObject, Observable, Health {

	//Physics interaction constants.
	public static final float JUMP_SPEED = 7f;
	//Terminal velocity of the player
	public static final float FALL_SPEED = 15f;
	//How fast the player accelerates while falling
	public static final float PLAYER_GRAVITY = 18f;


	//Physical collision model.

	//Highest vertical step player can make, think of stairs.
	public static final float PLAYER_STEP_HEIGHT = 0.1f;
	public static final float PLAYER_RADIUS = .5f;
	public static final float PLAYER_HEIGHT = 3f;
	//SHOULD NOT BE CHANGED
	public static final int PLAYER_GRAVITY_AXIS = 1;

	//Constants for exploration.

	public static final float EXPLORATION_INTERVAL = 0.5f;
	public static final int EXPLORATION_RADIUS = 8;
	
	//Health constants
	public static final float PLAYER_MAX_HEALTH = 8f;
	public static final float PLAYER_HEALTH = 8f;
	
	//The range in which the player can interact with entities (e.g. picking up bombs/keys and opening doors)
	public static final float INTERACT_RANGE = 2f;
	
	//The height at which the player is spawned in the map
	public static final float SPAWN_HEIGHT = 2f;

	private Spatial spatial;
	private AudioNode damageSound;
	private CharacterControl playerControl;
	private Inventory inventory = new Inventory();
	private Vector3f resp;
	private float fallingTimer;
	private float explorationTimer;
	private float health = PLAYER_HEALTH;
	private Set<Observer> observers = new HashSet<>();

	/**
	 * Constructor for a default player.
	 * This player is (for now) a sphere.
	 */
	public VRPlayer() { 
		super(new PlayerControl());
	}
	
	/**
	 * Private constructor used by {@link #loadEntity(Vector3f, String[])}, to be able to load
	 * dummy players.
	 * 
	 * @param location
	 * 		the location of this player
	 */
	private VRPlayer(Vector3f location) {
		super(new NoControl());
		
		getSpatial().move(location);
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		
		Node node = new Node("Player");
		spatial = node;

		Sphere sphere = new Sphere(10, 10, .2f);
		Spatial spatial = new Geometry("PlayerGeometry", sphere);
		Material material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.randomColor());
		spatial.setMaterial(material);
		
		damageSound = AudioManager.newPositionalSoundEffect("Sound/Effects/player_damage.ogg");
		
		node.attachChild(spatial);
		node.attachChild(damageSound);
		
		node.move(0, SPAWN_HEIGHT, 0);
		
		return node;
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		updateFallingTimer(tpf);
		inventory.update(tpf);

		Main.getInstance().moveCameraTo(playerControl.getPhysicsLocation());

		updateExploration(tpf);
	}

	/**
	 * Updates the exploration.
	 * 
	 * @param tpf
	 * 		the ticks per frame
	 */
	protected void updateExploration(float tpf) {
		//We want to update exploration at an interval (for performance reasons)
		explorationTimer += tpf;
		if (explorationTimer < EXPLORATION_INTERVAL) return;

		explorationTimer = 0f;

		//Please note that the Z coordinate of the player is the Y coordinate of the tile.
		Level level = Main.getInstance().getCurrentGame().getLevel();
		int x = Math.round(getLocation().getX());
		int y = Math.round(getLocation().getZ());

		//Explore in a square around the player
		for (int dx = -EXPLORATION_RADIUS; dx < EXPLORATION_RADIUS; dx++) {
			int tileX = x + dx;
			if (tileX < 0 || tileX >= level.getWidth()) continue;

			for (int dy = -EXPLORATION_RADIUS; dy < EXPLORATION_RADIUS; dy++) {
				int tileY = y + dy;
				if (tileY < 0 || tileY >= level.getHeight()) continue;

				MazeTile tile = level.getTile(tileX, tileY);
				if (tile == null) continue;

				tile.setExplored(true);
			}
		}
	}

	/**
	 * Update the falling timer that triggers respawning the player.
	 * 
	 * @param tpf
	 *		the time per frame for this update
	 */
	protected void updateFallingTimer(float tpf) {
		Vector3f ownLoc = getLocation();
		if (fallingTimer < 0) {
			fallingTimer = 0;

			Vector3f move = ownLoc.subtract(resp);
			move(-move.x, -move.y, -move.z);
			takeDamage(1f);

			//Create a void platform at player location
			if (!(Main.getInstance().getCurrentGame().getLevel().isTileAtPosition(Math.round(ownLoc.x), Math.round(ownLoc.z)))) {
				VoidPlatform voidPlatform = new VoidPlatform();
				Vector3f voidPlatformLocation = ownLoc.clone();
				voidPlatformLocation.y = 0;
				voidPlatform.move(voidPlatformLocation);
				Main.getInstance().getCurrentGame().addEntity(voidPlatform);
			}
			
			return;
		}
		if (ownLoc.y < 0 && fallingTimer == 0) {
			resp = ownLoc.clone();
			resp.y = 5;
			fallingTimer = 2;
		}
		if (fallingTimer != 0) {
			fallingTimer -= tpf;
		}
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	/**
	 * Set character control.
	 *
	 * @param characterControl
	 *		control to set
	 */
	public void setCharacterControl(CharacterControl characterControl) {
		this.playerControl = characterControl;
	}

	/**
	 * Get the player hit box.
	 * 
	 * @return	
	 * 		player physics object
	 */
	@Override
	public CharacterControl getPhysicsObject() {
		if (spatial == null) getSpatial();

		if (playerControl != null) return playerControl;
		//create a shape that implements PhysicsControl
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(PLAYER_RADIUS, PLAYER_HEIGHT, PLAYER_GRAVITY_AXIS);
		playerControl = new CharacterControl(capsuleShape, PLAYER_STEP_HEIGHT);

		//Add physical constants of player
		playerControl.setJumpSpeed(JUMP_SPEED);
		playerControl.setFallSpeed(FALL_SPEED);
		playerControl.setGravity(PLAYER_GRAVITY);

		//set physics location of player
		playerControl.setPhysicsLocation(spatial.getLocalTranslation());
		return playerControl;
	}

	/**
	 * Player drops a bomb from his inventory.
	 */
	public void drop() {
		if (!inventory.isHolding()) return;
		inventory.drop();
	}

	/**
	 * Player picks up a nearby item.
	 * Also opens nearby doors if the player has the correct key.
	 */
	public void pickUp() {
		Set<Entity> set = Main.getInstance().getCurrentGame().getEntities();

		for (Entity ent : set) {
			if (!ent.collidesWithPlayer(INTERACT_RANGE)) continue;

			if (ent instanceof Holdable && !inventory.isHolding()) {
				inventory.pickUp((Holdable) ent);
				return;
			} else if (ent instanceof Key) {
				Key key = (Key) ent;
				inventory.add(new Key(key.getColor()));
				ent.setState(EntityState.DEAD);
				return;
			} else if (ent instanceof Door) {
				Door door = (Door) ent;
				if (inventory.containsColorKey(door.getColor())) {
					inventory.remove(new Key(door.getColor()));
					door.open();
					return;
				}
			}
		}
	}

	@Override
	public void move(float x, float y, float z) {
		getSpatial().move(x, y, z);
		getPhysicsObject().setPhysicsLocation(playerControl.getPhysicsLocation().add(x, y, z));
	}

	/**
	 * Method used for testing.
	 *
	 * @return
	 * 		the player's inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @param inventory
	 * 		inventory to be set
	 */
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public float getHealth() {
		return health;
	}

	@Override
	public void setHealth(float heal) {
		this.health = Math.min(PLAYER_MAX_HEALTH, health);
		updateObservers();
	}
	
	@Override
	public void takeDamage(float amount) {
		health -= amount;
		if (health < 0) {
			Main.getInstance().getCurrentGame().endGame(false);
		}
		if (amount > 0) AudioManager.ensurePlaying(damageSound);
		updateObservers();
	}
	
	/**
	 * Loads a player entity from an array of String data.
	 * 
	 * <p>Please note that the returned player is a dummy player, with no input attached to it.
	 * It's spatial also has no color and no material.
	 * 
	 * @param position
	 * 		the position of the player
	 * @param data
	 * 		the data of the player
	 * @return
	 * 		the player represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static VRPlayer loadEntity(Vector3f position, String[] data) {
		if (data.length != 4) throw new IllegalArgumentException("Invalid data length for loading player! Expected \"<X> <Y> <Z> Player\".");
		
		return new VRPlayer(position);
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}

	@Override
	public Set<Observer> getObservers() {
		return observers;
	}
}
