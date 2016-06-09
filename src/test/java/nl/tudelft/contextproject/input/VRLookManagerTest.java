package nl.tudelft.contextproject.input;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import nl.tudelft.contextproject.TestBase;

/**
 * Test class for VR Look Manager.
 */
public class VRLookManagerTest extends TestBase {

	private VRLookManager vrManager;
	private Camera mockedCam;
	
	/**
	 * Set up a VRLookMAnager.
	 */
	@Before
	public void setupTest() {
		mockedCam = mock(Camera.class);
		when(mockedCam.getUp()).thenReturn(new Vector3f());
		when(mockedCam.getLeft()).thenReturn(new Vector3f());
		when(mockedCam.getDirection()).thenReturn(new Vector3f());
		vrManager = spy(new VRLookManager(mockedCam));
	}
	
	/**
	 * Register with input test.
	 */
	@Test
	public void testRegisterWithInput() {
		doNothing().when(vrManager).mapJoystick(any());
		InputManager mockedInputManager = mock(InputManager.class);
		
		when(mockedInputManager.getJoysticks()).thenReturn(new Joystick[] {null});
		vrManager.registerWithInput(mockedInputManager);
		
		verify(mockedInputManager).addListener(vrManager,  CameraInput.FLYCAM_LEFT, CameraInput.FLYCAM_RIGHT);
		verify(vrManager).mapJoystick(null);
	}

	/**
	 * Test rotate camera.
	 */
	@Test
	public void testRotateCamera() {
		vrManager.rotateCamera(0.0f);
		verify(mockedCam).setAxes(any());
	}

	/**
	 * Test OnAnalog with fly camera Left.
	 */
	@Test
	public void testOnAnalogFlyCameLeft() {
		String name = CameraInput.FLYCAM_LEFT;
		Float value = 1.0f;
		vrManager.onAnalog(name, value, 0);
		verify(vrManager).rotateCamera(value);
	}
	
	/**
	 * Test OnAnalog with fly camera Right.
	 */
	@Test
	public void testOnAnalogFlyCameRight() {
		String name = CameraInput.FLYCAM_RIGHT;
		Float value = 1.0f;
		vrManager.onAnalog(name, value, 0);
		verify(vrManager).rotateCamera(-value);
	}
}
