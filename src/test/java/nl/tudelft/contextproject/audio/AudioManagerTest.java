package nl.tudelft.contextproject.audio;

import static org.mockito.Mockito.*;

import com.jme3.audio.AudioNode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link AudioManager}.
 */
public class AudioManagerTest {
	public AudioNode an;
	
	/**
	 * Mock an AudioNode before every test.
	 */
	@Before
	public void setUp() {
		an = mock(AudioNode.class);
	}
	
	/**
	 * Unregister the AudioNode after every test.
	 */
	@After
	public void tearDown() {
		//Unregister the mocked AudioNode, to prevent further unwanted interactions
		AudioManager.getInstance().unregisterVolume(an, SoundType.AMBIENT);
		AudioManager.getInstance().unregisterVolume(an, SoundType.BACKGROUND_MUSIC);
		AudioManager.getInstance().unregisterVolume(an, SoundType.EFFECT);
	}

	/**
	 * Tests if {@link AudioManager#registerVolume} registers nodes correctly, and that volumes
	 * of AudioNodes are updated when the global volume is.
	 */
	@Test
	public void testRegisterVolume() {
		AudioManager.getInstance().registerVolume(an, SoundType.AMBIENT);
		verify(an, times(1)).setVolume(SoundType.AMBIENT.getGain());

		reset(an);

		SoundType.AMBIENT.setGain(2.0f);
		verify(an, times(1)).setVolume(2.0f);
	}

	/**
	 * Tests if {@link AudioManager#unregisterVolume} unregisters nodes correctly, and that
	 * their volumes are no longer updated when the global volume is changed.
	 */
	@Test
	public void testUnregisterVolume() {
		AudioManager.getInstance().registerVolume(an, SoundType.AMBIENT);
		verify(an, times(1)).setVolume(SoundType.AMBIENT.getGain());

		reset(an);

		AudioManager.getInstance().unregisterVolume(an, SoundType.AMBIENT);
		SoundType.AMBIENT.setGain(2.0f);
		verify(an, times(0)).setVolume(anyFloat());
	}

	/**
	 * Tests if {@link AudioManager#addCaveFeel} works correctly.
	 */
	@Test
	public void testAddCaveFeel() {
		when(an.isPositional()).thenReturn(true);

		AudioManager.getInstance().addCaveFeel(an);

		verify(an).setReverbEnabled(true);
	}
	
	/**
	 * Tests if {@link AudioManager#addCaveFeel} correctly throws an exception when a non
	 * positional audio node is given.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddCaveFeel_nonPositional() {
		when(an.isPositional()).thenReturn(false);
		
		AudioManager.getInstance().addCaveFeel(an);
	}

	/**
	 * Tests {@link AudioManager#updateVolume}, to see if the correct AudioNodes are updated.
	 */
	@Test
	public void testUpdateVolume() {
		AudioNode anAmbient = mock(AudioNode.class);
		AudioNode anBackground = mock(AudioNode.class);

		AudioManager.getInstance().registerVolume(anAmbient, SoundType.AMBIENT);
		AudioManager.getInstance().registerVolume(anBackground, SoundType.BACKGROUND_MUSIC);

		reset(anAmbient);
		reset(anBackground);

		AudioManager.getInstance().updateVolume(SoundType.BACKGROUND_MUSIC);

		verify(anAmbient, times(0)).setVolume(anyFloat());
		verify(anBackground, times(1)).setVolume(anyFloat());

		AudioManager.getInstance().unregisterVolume(anAmbient, SoundType.AMBIENT);
		AudioManager.getInstance().unregisterVolume(anBackground, SoundType.BACKGROUND_MUSIC);
	}

}
