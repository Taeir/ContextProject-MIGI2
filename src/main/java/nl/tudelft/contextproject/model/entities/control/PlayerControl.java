package nl.tudelft.contextproject.model.entities.control;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

import jmevr.app.VRApplication;

import nl.tudelft.contextproject.audio.AudioManager;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.moving.VRPlayer;

/**
 * {@link EntityControl} that moves the player.
 */
public class PlayerControl implements EntityControl, ActionListener {

	//Movement control constants.
	public static final float SIDE_WAY_SPEED_MULTIPLIER = .03f;
	public static final float STRAIGHT_SPEED_MULTIPLIER = .04f;

	private Vector3f walkDirection;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	private Spatial spatial;
	private CharacterControl playerControl;
	private VRPlayer owner;
	private AudioNode walkSound;

	@Override
	public void move(float tpf) {
		if (VRApplication.getVRViewManager() != null) {
			Camera camera = VRApplication.getVRViewManager().getCamLeft();
			Vector3f camDir = camera.getDirection();
			Vector3f camLeft = camera.getLeft().mult(2.0f);
			walkDirection = new Vector3f();

			if (left) {
				walkDirection.addLocal(camLeft.normalizeLocal().multLocal(SIDE_WAY_SPEED_MULTIPLIER));
			}
			if (right) {
				walkDirection.addLocal(camLeft.negateLocal().normalizeLocal().multLocal(SIDE_WAY_SPEED_MULTIPLIER));
			}
			if (up) {
				walkDirection.addLocal(new Vector3f(camDir.getX() * 2f, 0, camDir.getZ() * 2f).normalizeLocal().multLocal(STRAIGHT_SPEED_MULTIPLIER));
			}
			if (down) {
				walkDirection.addLocal(new Vector3f(-camDir.getX() * 2f, 0, -camDir.getZ() * 2f).normalizeLocal().multLocal(STRAIGHT_SPEED_MULTIPLIER));
			}

			playerControl.setWalkDirection(walkDirection);
			spatial.setLocalTranslation(playerControl.getPhysicsLocation().add(0, -2, 0));
		}
	}

	@Override
	public void setOwner(Entity owner) {
		if (!(owner instanceof VRPlayer)) throw new IllegalArgumentException("The owner must be a player.");
		this.owner = (VRPlayer) owner;
		spatial = owner.getSpatial();
		playerControl = this.owner.getPhysicsObject();
	}
	
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
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
			case "Drop":
				if (isPressed) {
					owner.drop();
				}
				break;
			case "Pickup":
				if (isPressed) {
					owner.pickUp();
				}
				break;
			default:
				break;
		}
		
		updateWalkingSound();
	}
	
	/**
	 * Starts/stops the walking sound.
	 */
	public void updateWalkingSound() {
		if (walkSound == null) {
			walkSound = AudioManager.newPositionalSoundEffect("Sound/Effects/walking.ogg");
			walkSound.setPositional(false);
			walkSound.setReverbEnabled(false);
		}
		
		if (!left && !right && !up && !down) {
			AudioManager.stop(walkSound);
		} else {
			AudioManager.ensurePlaying(walkSound);
		}
	}
}
