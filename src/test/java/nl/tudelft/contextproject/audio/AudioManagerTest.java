package nl.tudelft.contextproject.audio;

import static org.mockito.Mockito.*;

import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.Listener;

import nl.tudelft.contextproject.TestBase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link AudioManager}.
 */
public class AudioManagerTest extends TestBase {
	public AudioNode audioNode;
	public Listener listener;
	
	/**
	 * Mock an AudioNode before every test, and reinitialize the AudioManager.
	 */
	@Before
	public void setUp() {
		audioNode = mock(AudioNode.class);
		listener = mock(Listener.class);
		AudioManager.getInstance().init(mock(AudioRenderer.class), listener);
	}
	
	/**
	 * Unregister the AudioNode after every test.
	 */
	@After
	public void tearDown() {
		//Unregister the mocked AudioNode, to prevent further unwanted interactions
		AudioManager.getInstance().unregisterVolume(audioNode, SoundType.AMBIENT);
		AudioManager.getInstance().unregisterVolume(audioNode, SoundType.BACKGROUND_MUSIC);
		AudioManager.getInstance().unregisterVolume(audioNode, SoundType.EFFECT);
	}

	/**
	 * Tests if {@link AudioManager#registerVolume} registers nodes correctly, and that volumes
	 * of AudioNodes are updated when the global volume is.
	 */
	@Test
	public void testRegisterVolume() {
		//Register the volume
		AudioManager.getInstance().registerVolume(audioNode, SoundType.AMBIENT);

		//Registering should set the volume to the correct type.
		verify(audioNode, times(1)).setVolume(SoundType.AMBIENT.getGain());

		//Reset the mock
		reset(audioNode);

		//Change the gain
		SoundType.AMBIENT.setGain(2.0f);

		//The volume should have been updated
		verify(audioNode, times(1)).setVolume(2.0f);
	}

	/**
	 * Tests if {@link AudioManager#unregisterVolume} unregisters nodes correctly, and that
	 * their volumes are no longer updated when the global volume is changed.
	 */
	@Test
	public void testUnregisterVolume() {
		//Register the volume
		AudioManager.getInstance().registerVolume(audioNode, SoundType.AMBIENT);

		//Registering should set the volume to the correct type.
		verify(audioNode, times(1)).setVolume(SoundType.AMBIENT.getGain());

		//Reset the mock
		reset(audioNode);

		//Unregister the AudioNode
		AudioManager.getInstance().unregisterVolume(audioNode, SoundType.AMBIENT);

		//Change the gain
		SoundType.AMBIENT.setGain(2.0f);

		//The volume should not have been updated
		verify(audioNode, times(0)).setVolume(anyFloat());
	}

	/**
	 * Tests if {@link AudioManager#addCaveFeel} works correctly.
	 */
	@Test
	public void testAddCaveFeel() {
		//Mock a positional AudioNode
		when(audioNode.isPositional()).thenReturn(true);

		AudioManager.getInstance().addCaveFeel(audioNode);

		//The reverb should have been enabled
		verify(audioNode).setReverbEnabled(true);
	}
	
	/**
	 * Tests if {@link AudioManager#addCaveFeel} correctly throws an exception when a non
	 * positional audio node is given.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddCaveFeel_nonPositional() {
		//Mock a non-positional AudioNode
		when(audioNode.isPositional()).thenReturn(false);

		//Adding the cave feel should be impossible
		AudioManager.getInstance().addCaveFeel(audioNode);
	}

	/**
	 * Tests {@link AudioManager#updateVolume}, to see if the correct AudioNodes are updated.
	 */
	@Test
	public void testUpdateVolume() {
		AudioNode anAmbient = mock(AudioNode.class);
		AudioNode anBackground = mock(AudioNode.class);

		//Register two AudioNodes for different SoundTypes.
		AudioManager.getInstance().registerVolume(anAmbient, SoundType.AMBIENT);
		AudioManager.getInstance().registerVolume(anBackground, SoundType.BACKGROUND_MUSIC);

		//Reset the mocks
		reset(anAmbient);
		reset(anBackground);

		//Update the background sounds.
		AudioManager.getInstance().updateVolume(SoundType.BACKGROUND_MUSIC);

		//The ambient sound should not have been updated, the background sound should have been.
		verify(anAmbient, times(0)).setVolume(anyFloat());
		verify(anBackground, times(1)).setVolume(anyFloat());

		//Clean up
		AudioManager.getInstance().unregisterVolume(anAmbient, SoundType.AMBIENT);
		AudioManager.getInstance().unregisterVolume(anBackground, SoundType.BACKGROUND_MUSIC);
	}
	
	/**
	 * Tests if {@link AudioManager#update(float)} correctly updates the Listener.
	 */
	@Test
	public void testUpdate() {
		AudioManager.getInstance().update(10f);
		
		verify(listener, times(1)).setLocation(any());
		verify(listener, times(1)).setRotation(any());
	}

}
