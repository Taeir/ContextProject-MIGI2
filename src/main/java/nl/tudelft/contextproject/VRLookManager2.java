package nl.tudelft.contextproject;

import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * Hacky VR look manager used during play testing.
 * TODO: Must be refactored out during refactoring phase.
 */
public class VRLookManager2 implements AnalogListener {
	private Camera cam;
	private Vector3f initialUpVec;
	private float rotationSpeed = 1f;
	private boolean invertY;

	/**
	 * Creates a new NoVRMouseManager to control the given Camera object.
	 * 
	 * @param cam
	 * 		the camera to control
	 */
	public VRLookManager2(Camera cam) {
		this.cam = cam;
		initialUpVec = new Vector3f(0, 1, 0);
	}

	/**
	 * Registers the NoVRMouseManager with the input manager.
	 * 
	 * @param inputManager
	 * 		the inputManager to register
	 */
	public void registerWithInput(InputManager inputManager) {
		inputManager.addListener(this, CameraInput.FLYCAM_LEFT,
				CameraInput.FLYCAM_RIGHT);

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
		//Invert the Y axis, so that up is up and down is down.
		invertY = false;

		// Map it differently if there are Z axis
		if (joystick.getAxis(JoystickAxis.Z_ROTATION) != null && joystick.getAxis(JoystickAxis.Z_AXIS) != null) {

			// And the right stick control the camera                       
			joystick.getAxis(JoystickAxis.Z_ROTATION).assignAxis(CameraInput.FLYCAM_UP, CameraInput.FLYCAM_DOWN);
			joystick.getAxis(JoystickAxis.Z_AXIS).assignAxis(CameraInput.FLYCAM_RIGHT, CameraInput.FLYCAM_LEFT);

			if (joystick.getButton("Button 8") != null) {
				// Let the standard select button be the y invert toggle
				joystick.getButton("Button 8").assignButton(CameraInput.FLYCAM_INVERTY);
			}

		} else {
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
		}
	}
}
