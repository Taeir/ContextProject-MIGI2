package nl.tudelft.contextproject.model;

import java.awt.Graphics2D;

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

/**
 * Class representing the player wearing the VR headset.
 */
public class VRPlayer extends Entity implements ActionListener {
	//Physics interaction constants
	public static final float JUMP_SPEED = 1f;
	public static final float FALL_SPEED = 1f;
	public static final float PLAYER_GRAVITY = 1f;

	//Physical collision model
	public static final float PLAYER_STEP_HEIGHT = 0.1f;
	public static final float PLAYER_RADIUS = .5f;
	public static final float PLAYER_HEIGHT = 1f;
	public static final int PLAYER_AXIS = 1;

	private Spatial spatial;
	private CharacterControl playerControl;
	private boolean left, right, up, down;
	Vector3f walkDirection;

	/**
	 * Constructor for a default player.
	 * This player is (for now) a red sphere.
	 */
	public VRPlayer() { 
		//Set geometry of player
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		Sphere b = new Sphere(10, 10, .2f);
		spatial = new Geometry("blue cube", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		spatial.setMaterial(mat);
		spatial.move(0, 10, 0);
		return spatial;
	}

	@Override
	public void update(float tdf) {
		Vector3f camDir = Main.getInstance().getCamera().getDirection();
		Vector3f camLeft = Main.getInstance().getCamera().getLeft();
		walkDirection = new Vector3f();
		if (left) {
			walkDirection.addLocal(camLeft.mult(.235f));
		}
		if (right) {
			walkDirection.addLocal(camLeft.negate().normalizeLocal().multLocal(.235f));
		}
		if (up) {
			walkDirection.addLocal(new Vector3f(camDir.getX(), 0, camDir.getZ()).normalizeLocal().multLocal(.5f));
		}
		if (down) {
			walkDirection.addLocal(new Vector3f(-camDir.getX(), 0, -camDir.getZ()).normalizeLocal().multLocal(.5f));
		}

		playerControl.setWalkDirection(walkDirection);
		Main.getInstance().moveCameraTo(playerControl.getPhysicsLocation());
	}

	@Override
	public void mapDraw(Graphics2D g, int resolution) {
		Vector3f trans = spatial.getLocalTranslation();
		int x = (int) trans.x * resolution;
		int y = (int) trans.y * resolution;
		int width = resolution / 2;
		int offset = resolution / 4;

		g.fillOval(x + offset, y + offset, width, width);
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
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

	/**
	 * On button press, what the player should do.
	 * @param name
	 * @param isPressed
	 * @param tpf
	 */
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("Left")) {
			left = isPressed;
		} else if (name.equals("Right")) {
			right = isPressed;
		} else if (name.equals("Up")) {
			up = isPressed;
		} else if (name.equals("Down")) {
			down = isPressed;
		} else if (name.equals("Jump")) {
			if (isPressed) { 
				playerControl.jump(); 
			}
		}
	}
}
