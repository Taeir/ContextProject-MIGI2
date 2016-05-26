package nl.tudelft.contextproject.model.entities;

import java.util.Set;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Inventory;
import nl.tudelft.contextproject.model.PhysicsObject;

/**
 * Class representing the player wearing the VR headset.
 */
public class VRPlayer extends Entity implements ActionListener, PhysicsObject {
	/**
	 * Physics interaction constants.
	 */
	//This is the jump speed of the player, works with gravity.
	public static final float JUMP_SPEED = 13f;
	//The maximum fall speed, AKA terminal velocity.
	public static final float FALL_SPEED = 15f;
	//The gravity constant, how fast the player accelerate during falling.
	public static final float PLAYER_GRAVITY = 13f;

	/**
	 * Physical collision model.
	 */
	//Highest vertical step player can make, think of stairs.
	public static final float PLAYER_STEP_HEIGHT = 0.1f;
	//Collision radius of cylinder size.
	public static final float PLAYER_RADIUS = .5f;
	//Height of collision box.
	public static final float PLAYER_HEIGHT = 3f;
	//Gravity axis of the player, should not be changed!
	public static final int PLAYER_AXIS = 1;

	/**
	 * Movement control constants.
	 */
	//Left and Right movement speed multiplier.
	public static final float SIDE_WAY_SPEED_MULTIPLIER = .08f;
	//Up and Down movement speed multiplier.
	public static final float STRAIGHT_SPEED_MULTIPLIER = .1f;

	private Spatial spatial;
	private CharacterControl playerControl;
	private boolean left, right, up, down;
	private Vector3f walkDirection;
	private Inventory inventory;
	private float health;
	private int maxHealth = 3;
	private Vector3f resp;
	private float fallingTimer;
	/**
	 * Constructor for a default player.
	 * This player is (for now) a red sphere.
	 */
	public VRPlayer() { 
		inventory = new Inventory();
		health = 3f;
		//Set geometry of player
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		Sphere b = new Sphere(10, 10, .2f);
		spatial = new Geometry("blue cube", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.randomColor());
		spatial.setMaterial(mat);
		spatial.move(0, 10, 0);
		return spatial;
	}

	@Override
	public void update(float tdf) {
		updateFallingTimer(tdf);
		//TODO this will change after VR support is implemented
		if (health <= 0) {
			this.setState(EntityState.DEAD);
		}
		Vector3f camDir = Main.getInstance().getCamera().getDirection();
		Vector3f camLeft = Main.getInstance().getCamera().getLeft();
		walkDirection = new Vector3f();
		if (left) {
			walkDirection.addLocal(camLeft.normalizeLocal().multLocal(SIDE_WAY_SPEED_MULTIPLIER));
		}
		if (right) {
			walkDirection.addLocal(camLeft.negate().normalizeLocal().multLocal(SIDE_WAY_SPEED_MULTIPLIER));
		}
		if (up) {
			walkDirection.addLocal(new Vector3f(camDir.getX(), 0, camDir.getZ()).normalizeLocal().multLocal(STRAIGHT_SPEED_MULTIPLIER));
		}
		if (down) {
			walkDirection.addLocal(new Vector3f(-camDir.getX(), 0, -camDir.getZ()).normalizeLocal().multLocal(STRAIGHT_SPEED_MULTIPLIER));
		}

		playerControl.setWalkDirection(walkDirection);
		spatial.setLocalTranslation(playerControl.getPhysicsLocation().add(0, -2, 0));
		Main.getInstance().moveCameraTo(playerControl.getPhysicsLocation());
	}

	/**
	 * Update the falling timer that triggers respawining the player.
	 * 
	 * @param tpf
	 *		the time per frame for this update
	 */
	protected void updateFallingTimer(float tpf) {
		if (fallingTimer < 0) {
			fallingTimer = 0;
			Vector3f move = getLocation().subtract(resp);
			move(-move.x, -move.y, -move.z);
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
	 * @param characterControl
	 * 				control to set
	 */
	public void setCharacterControl(CharacterControl characterControl) {
		this.playerControl = characterControl;
	}

	/**
	 * Get the player hit box.
	 * 
	 * @return	
	 * 				player physics object
	 */
	@Override
	public CharacterControl getPhysicsObject() {
		if (spatial == null) {
			this.getSpatial();
		}
		if (playerControl != null) return playerControl;
		//create a shape that implements PhysicsControl
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(PLAYER_RADIUS, PLAYER_HEIGHT, PLAYER_AXIS);
		playerControl = new CharacterControl(capsuleShape, PLAYER_STEP_HEIGHT);

		//Add physical constants of player
		playerControl.setJumpSpeed(JUMP_SPEED);
		playerControl.setFallSpeed(FALL_SPEED);
		playerControl.setGravity(PLAYER_GRAVITY);

		//set physics location of player
		playerControl.setPhysicsLocation(spatial.getLocalTranslation());
		return playerControl;
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		//TODO This should probably be at another place after controller support is implemented.
		switch (name) {
		case "Left":
			left = isPressed;
			break;
		case "Right":
			right = isPressed;
			break;
		case "Up":
			up = isPressed;
			break;
		case "Down":
			down = isPressed;
			break;
		case "Jump":
			if (isPressed) { 
				playerControl.jump(); 
			}
			break;
		case "Bomb":
			if (isPressed) {
				dropBomb();
			}
			break;
		case "Pickup":
			if (isPressed) {
				pickUp();
			}
			break;
		default:
			//Do nothing otherwise
			break;
		}
	}

	/**
	 * Player drops a bomb from his inventory.
	 */
	public void dropBomb() {
		if (inventory.containsBomb()) {
			Bomb bomb = new Bomb();
			inventory.remove(bomb);
			Vector3f vec = this.getSpatial().getLocalTranslation();
			bomb.move((int) vec.x, (int) vec.y + 1, (int) vec.z);
			if (Main.getInstance().getCurrentGame() != null) {
				Main.getInstance().getCurrentGame().addEntity(bomb);
				bomb.activate();
			}
		}
	}
	
	/**
	 * Player picks up a nearby item.
	 * Also opens nearby doors if possible
	 */
	public void pickUp() {
		Set<Entity> set = Main.getInstance().getCurrentGame().getEntities();
		for (Entity ent : set) {
			 if (ent.collidesWithPlayer(2f)) {
				if (ent instanceof Bomb) {
					this.inventory.add(new Bomb());
					ent.setState(EntityState.DEAD);
					return;
				}
				if (ent instanceof Key) {
					Key key = (Key) ent;
					this.inventory.add(new Key(key.getColor()));
					ent.setState(EntityState.DEAD);
					return;
				}
				if (ent instanceof Door) {
					Door door = (Door) ent;
					if (inventory.containsColorKey(door.getColor())) {
						this.inventory.remove(inventory.getKey(door.getColor()));
						ent.setState(EntityState.DEAD);
						return;
					}
				}
			}
		}
	}

	@Override
	public void move(float x, float y, float z) {
		spatial.move(x, y, z);
		getPhysicsObject().setPhysicsLocation(playerControl.getPhysicsLocation().add(x, y, z));
	}

	/**
	 * Method used for testing.
	 * Returns the player's inventory.
	 * @return The player's inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Method used for testing.
	 * Sets a player's inventory
	 * @param inv
	 * 		Inventory to be set
	 */
	public void setInventory(Inventory inv) {
		inventory = inv;
	}
	
	/**
	 * Returns the player's health.
	 * @return The player's health
	 */
	public float getHealth() {
		return health;
	}
	
	/**
	 * Sets a player's health.
	 * @param heal
	 * 		Health to be set
	 */
	public void setHealth(float heal) {
		if (heal > maxHealth) {
			health = 3;
		} else {
			health = heal;
		}
	}
	
	/**
	 * Reduces a players health.
	 * @param amount the amount of damage taken
	 */
	public void takeDamage(Float amount) {
		health -= amount;
	}
}
