package nl.tudelft.contextproject.input;

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
 * VRLook manager, controller input for virtual reality.
 */
public class VRLookManager implements AnalogListener {
	private Camera cam;
	private Vector3f initialUpVec;
	private float rotationSpeed = 0.4f;

	/**
	 * Creates a new NoVRMouseManager to control the given Camera object.
	 * 
	 * @param cam
	 * 		the camera to control
	 */
	public VRLookManager(Object cam) {
		if (!(cam instanceof Camera)) throw new IllegalArgumentException("Not a camera!");
		this.cam = (Camera) cam;
		initialUpVec = new Vector3f(0, 1, 0);
	}

	/**
	 * Registers the NoVRMouseManager with the input manager.
	 * 
	 * @param inputManager
	 * 		the inputManager to register
	 */
	public void registerWithInput(InputManager inputManager) {
		inputManager.addListener(this, CameraInput.FLYCAM_LEFT, CameraInput.FLYCAM_RIGHT);
		
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
			// And the right stick control the camera
			joystick.getAxis(JoystickAxis.Z_AXIS).assignAxis(CameraInput.FLYCAM_RIGHT, CameraInput.FLYCAM_LEFT);
		} else {
			joystick.getXAxis().assignAxis(CameraInput.FLYCAM_RIGHT, CameraInput.FLYCAM_LEFT);
		}
	}
	
	/**
	 * Rotates the camera with the given value.
	 * 
	 * @param value
	 * 		the value to rotate with
	 */
	protected void rotateCamera(float value) {
    	Matrix3f mat = new Matrix3f();
		mat.fromAngleNormalAxis(rotationSpeed * value, initialUpVec);

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
			rotateCamera(value);
		} else if (name.equals(CameraInput.FLYCAM_RIGHT)) {
			rotateCamera(-value);
		}
	}
}
