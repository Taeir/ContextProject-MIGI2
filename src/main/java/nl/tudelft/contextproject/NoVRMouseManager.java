package nl.tudelft.contextproject;

import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * Manager which adds mouse controls to look around when no VR device is attached.
 * 
 * <p><b>THIS CLASS IS FOR TESTING PURPOSES ONLY! IT GIVES THE USER THE ABILITY TO USE THE
 * MOUSE/CONTROLLER TO LOOK AROUND, TO BE ABLE TO TEST WITHOUT OCULUS.</b>
 */
public class NoVRMouseManager implements AnalogListener {
	private Camera cam;
	private Vector3f initialUpVec;
	private float rotationSpeed = 1f;
	private float moveSpeed = 3f;
	private boolean invertY;

	/**
	 * Creates a new NoVRMouseManager to control the given Camera object.
	 * 
	 * @param cam
	 * 		the camera to control
	 */
	public NoVRMouseManager(Camera cam) {
		this.cam = cam;
		initialUpVec = new Vector3f(0, 1, 0);
		//initialUpVec = cam.getUp().clone();
	}

	/**
	 * Registers the NoVRMouseManager with the input manager.
	 * 
	 * @param inputManager
	 * 		the inputManager to register
	 */
	public void registerWithInput(InputManager inputManager) {
		//Add rotation mappings
		inputManager.addMapping(CameraInput.FLYCAM_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true),
				new KeyTrigger(KeyInput.KEY_LEFT));

		inputManager.addMapping(CameraInput.FLYCAM_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false),
				new KeyTrigger(KeyInput.KEY_RIGHT));

		inputManager.addMapping(CameraInput.FLYCAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false),
				new KeyTrigger(KeyInput.KEY_UP));

		inputManager.addMapping(CameraInput.FLYCAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true),
				new KeyTrigger(KeyInput.KEY_DOWN));

		inputManager.addListener(this, CameraInput.FLYCAM_LEFT,
				CameraInput.FLYCAM_RIGHT,
				CameraInput.FLYCAM_UP,
				CameraInput.FLYCAM_DOWN);

		inputManager.setCursorVisible(false);

		Joystick[] joysticks = inputManager.getJoysticks();
		if (joysticks != null && joysticks.length > 0) {
			for (Joystick j : joysticks) {
				mapJoystick(j);
			}
		}
	}

	/**
	 * Map the joystick controls to events.
	 * 
	 * @param joystick
	 * 		the joystick to map
	 */
	protected void mapJoystick(Joystick joystick) {

		// Map it differently if there are Z axis
		if (joystick.getAxis(JoystickAxis.Z_ROTATION) != null && joystick.getAxis(JoystickAxis.Z_AXIS) != null) {

			// Make the left stick move
			joystick.getXAxis().assignAxis(CameraInput.FLYCAM_STRAFERIGHT, CameraInput.FLYCAM_STRAFELEFT);
			joystick.getYAxis().assignAxis(CameraInput.FLYCAM_BACKWARD, CameraInput.FLYCAM_FORWARD);

			// And the right stick control the camera                       
			joystick.getAxis(JoystickAxis.Z_ROTATION).assignAxis(CameraInput.FLYCAM_UP, CameraInput.FLYCAM_DOWN);
			joystick.getAxis(JoystickAxis.Z_AXIS).assignAxis(CameraInput.FLYCAM_RIGHT, CameraInput.FLYCAM_LEFT);

			// And let the dpad be up and down           
			joystick.getPovYAxis().assignAxis(CameraInput.FLYCAM_RISE, CameraInput.FLYCAM_LOWER);

			if (joystick.getButton("Button 8") != null) {
				// Let the standard select button be the y invert toggle
				joystick.getButton("Button 8").assignButton(CameraInput.FLYCAM_INVERTY);
			}

		} else {
			joystick.getPovXAxis().assignAxis(CameraInput.FLYCAM_STRAFERIGHT, CameraInput.FLYCAM_STRAFELEFT);
			joystick.getPovYAxis().assignAxis(CameraInput.FLYCAM_FORWARD, CameraInput.FLYCAM_BACKWARD);
			joystick.getXAxis().assignAxis(CameraInput.FLYCAM_RIGHT, CameraInput.FLYCAM_LEFT);
			joystick.getYAxis().assignAxis(CameraInput.FLYCAM_UP, CameraInput.FLYCAM_DOWN);
		}
	}

	/**
	 * Rotates the camera with the given value along the given axis.
	 * 
	 * @param value
	 * 		the value to rotate with
	 * @param axis
	 * 		the axis to move along
	 */
	protected void rotateCamera(float value, Vector3f axis) {
		Matrix3f mat = new Matrix3f();
		mat.fromAngleNormalAxis(rotationSpeed * value, axis);

		Vector3f up = cam.getUp();
		Vector3f left = cam.getLeft();
		Vector3f dir = cam.getDirection();

		mat.mult(up, up);
		mat.mult(left, left);
		mat.mult(dir, dir);

		Quaternion q = new Quaternion();
		q.fromAxes(left, up, dir);
		q.normalizeLocal();

		cam.setAxes(q);
	}
	
	/**
	 * Moves the camera a certain value in a certain direction.
	 * 
	 * @param value
	 * 		the amount to move
	 * @param sideways
	 * 		if true, moves the camera sideways
	 */
	protected void moveCamera(float value, boolean sideways) {
		Vector3f vel = new Vector3f();
		Vector3f pos = cam.getLocation().clone();

		if (sideways) {
			cam.getLeft(vel);
		} else {
			cam.getDirection(vel);
		}
		vel.multLocal(value * moveSpeed);

		//if (motionAllowed != null)
		//    motionAllowed.checkMotionAllowed(pos, vel);
		//else
		pos.addLocal(vel);

		cam.setLocation(pos);
	}

	@Override
	public void onAnalog(String name, float value, float tpf) {
		if (name.equals(CameraInput.FLYCAM_LEFT)) {
			rotateCamera(value, initialUpVec);
		} else if (name.equals(CameraInput.FLYCAM_RIGHT)) {
			rotateCamera(-value, initialUpVec);
		} else if (name.equals(CameraInput.FLYCAM_UP)) {
			rotateCamera(-value * (invertY ? -1 : 1), cam.getLeft());
		} else if (name.equals(CameraInput.FLYCAM_DOWN)) {
			rotateCamera(value * (invertY ? -1 : 1), cam.getLeft());
		} else if (name.equals(CameraInput.FLYCAM_FORWARD)) {
			moveCamera(value, false);
		} else if (name.equals(CameraInput.FLYCAM_BACKWARD)) {
			moveCamera(-value, false);
		} else if (name.equals(CameraInput.FLYCAM_STRAFELEFT)) {
			moveCamera(value, true);
		} else if (name.equals(CameraInput.FLYCAM_STRAFERIGHT)) {
			moveCamera(-value, true);
		}
	}
}
