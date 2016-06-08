package nl.tudelft.contextproject.input;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.renderer.Camera;

import nl.tudelft.contextproject.TestBase;

/**
 * Test class for VR Look Manager.
 */
public class VRLookManagerTest extends TestBase {

	private VRLookManager vrManager;
	/**
	 * Set up a VRLookMAnager.
	 */
	@Before
	public void setupTest() {
		Camera mockedCam = mock(Camera.class);
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

	@Test
	public void testRotateCamera() {
		fail("Not yet implemented");
	}

	@Test
	public void testOnAnalog() {
		fail("Not yet implemented");
	}

}
