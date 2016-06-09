package nl.tudelft.contextproject.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Inventory;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.model.TickProducer;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.entities.control.PlayerControl;

/**
 * Class representing the player wearing the VR headset.
 */
public class VRPlayer extends MovingEntity implements PhysicsObject, TickProducer, Health {

	//Physics interaction constants.
	public static final float JUMP_SPEED = 7f;
	//Terminal velocity of the player
	public static final float FALL_SPEED = 15f;
	//How fast the player accelerates while falling
	public static final float PLAYER_GRAVITY = 13f;


	//Physical collision model.

	//Highest vertical step player can make, think of stairs.
	public static final float PLAYER_STEP_HEIGHT = 0.1f;
	public static final float PLAYER_RADIUS = .5f;
	public static final float PLAYER_HEIGHT = 3f;
	//SHOULD NOT BE CHANGED
	public static final int PLAYER_GRAVITY_AXIS = 1;

	//Constants for exploration.

	public static final float EXPLORATION_INTERVAL = 0.5f;
	public static final int EXPLORATION_RADIUS = 5;
	
	//Health constants
	public static final float PLAYER_MAX_HEALTH = 5f;
	public static final float PLAYER_HEALTH = 5f;
	
	//The range in which the player can interact with entities (e.g. picking up bombs/keys and opening doors)
	public static final float INTERACT_RANGE = 2f;
	
	//The height at which the player is spawned in the map
	public static final float SPAWN_HEIGHT = 2f;

	private Spatial spatial;
	private CharacterControl playerControl;
	private Inventory inventory;
	private Vector3f resp;
	private float fallingTimer;
	private float explorationTimer;
	private float health;
	private List<TickListener> listeners;

	/**
	 * Constructor for a default player.
	 * This player is (for now) a sphere.
	 */
	public VRPlayer() { 
		super(new PlayerControl());
		health = PLAYER_HEALTH;
		inventory = new Inventory();
		listeners = new ArrayList<>();
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;

		Sphere b = new Sphere(10, 10, .2f);
		spatial = new Geometry("blue cube", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.randomColor());
		spatial.setMaterial(mat);
		spatial.move(0, SPAWN_HEIGHT, 0);
		return spatial;
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
		if (fallingTimer < 0) {
			fallingTimer = 0;

			Vector3f move = getLocation().subtract(resp);
			move(-move.x, -move.y, -move.z);
			takeDamage(1f);
			
			//Create a void platform at player location
			VoidPlatform vp = new VoidPlatform();
			Vector3f vploc = getLocation().clone();
			vploc.y = 0;
			vp.move(vploc);
			Main.getInstance().getCurrentGame().addEntity(vp);
			
			return;
		}
		if (getLocation().y < 0 && fallingTimer == 0) {
			resp = getLocation().clone();
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
					ent.setState(EntityState.DEAD);
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
	 * @param inv
	 * 		inventory to be set
	 */
	public void setInventory(Inventory inv) {
		inventory = inv;
	}

	@Override
	public float getHealth() {
		return health;
	}

	@Override
	public void setHealth(float heal) {
		this.health = Math.min(PLAYER_MAX_HEALTH, health);
		updateTickListeners();
	}
	
	@Override
	public void takeDamage(float amount) {
		health -= amount;
		if (health < 0) {
			Main.getInstance().getCurrentGame().endGame(false);
		}
		updateTickListeners();
	}
	
	/**
	 * Loads a player entity from an array of String data.
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
		
		VRPlayer player = new VRPlayer();
		player.move(position);
		
		return player;
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}

	@Override
	public List<TickListener> getTickListeners() {
		return listeners;
	}
}
