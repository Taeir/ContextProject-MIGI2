package nl.tudelft.contextproject.audio;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource.Status;

import nl.tudelft.contextproject.TestBase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for {@link BackgroundMusic}.
 */
public class BackgroundMusicTest extends TestBase {
	public AudioNode an;

	/**
	 * Enable testing mode.
	 */
	@BeforeClass
	public static void setUpClass() {
		BackgroundMusic.getInstance().setTesting(true);
	}
	
	/**
	 * Disable testing mode after all tests have run.
	 */
	@AfterClass
	public static void tearDownClass() {
		BackgroundMusic.getInstance().setTesting(false);
	}
	
	/**
	 * Creates the mocked AudioNode before each test.
	 */
	@Before
	public void setUp() {
		an = mock(AudioNode.class);
	}
	
	/**
	 * Cleans up after each test.
	 */
	@After
	public void tearDown() {
		//Stop playback
		BackgroundMusic.getInstance().stop();
	}
	
	/**
	 * Tests if {@link BackgroundMusic#playSong(AudioNode)} correctly stops the current song and
	 * starts the new song.
	 */
	@Test
	public void testPlaySong_playing() {
		AudioNode an2 = mock(AudioNode.class);
		BackgroundMusic.getInstance().playSong(an);
		BackgroundMusic.getInstance().playSong(an2);

		//The old song should have been stopped
		verify(an, times(1)).stop();

		//The new song should have been started
		verify(an2, times(1)).play();
	}

	/**
	 * Test method for {@link BackgroundMusic#next()}.
	 */
	@Test
	public void testNext() {
		//Start playback
		BackgroundMusic.getInstance().playSong(an);

		//Reset the mock
		reset(an);

		//Start the next song
		BackgroundMusic.getInstance().next();

		//Playback of the old song should be stopped
		verify(an, times(1)).stop();
	}

	/**
	 * Tests if {@link BackgroundMusic#start()} correctly resumes paused playback.
	 */
	@Test
	public void testStart_paused() {
		//Play the mocked song
		BackgroundMusic.getInstance().playSong(an);

		//Reset the mock
		reset(an);

		//Make the audioNode act as being paused
		when(an.getStatus()).thenReturn(Status.Paused);

		//Call start
		BackgroundMusic.getInstance().start();

		//Verify that play has been called to resume playback.
		verify(an, times(1)).play();
	}
	
	/**
	 * Tests if {@link BackgroundMusic#start()} correctly starts music when none was playing.
	 */
	@Test
	public void testStart_notPlaying() {
		//Call start
		BackgroundMusic.getInstance().start();
		
		AudioNode an = BackgroundMusic.getInstance().getCurrent();

		//Assert that a song is currently being played.
		assertNotNull(an);

		//Verify that play has been called to start playback.
		verify(an, times(1)).play();
	}
	
	/**
	 * Tests if {@link BackgroundMusic#start()} correctly resumes paused playback.
	 */
	@Test
	public void testStart_playing() {
		//Play the mocked song
		BackgroundMusic.getInstance().playSong(an);

		//Reset the mock
		reset(an);

		//Make the audioNode act as being playing
		when(an.getStatus()).thenReturn(Status.Playing);

		//Call start
		BackgroundMusic.getInstance().start();

		//Verify that play has not been called, as the song is already playing.
		verify(an, times(0)).play();
	}

	/**
	 * Tests if {@link BackgroundMusic#pause()} pauses the currently playing song.
	 */
	@Test
	public void testPause_playing() {
		//Play the mocked song
		BackgroundMusic.getInstance().playSong(an);

		//Reset the mock
		reset(an);

		//Call pause
		BackgroundMusic.getInstance().pause();

		//Verify that the song has indeed been paused.
		verify(an, times(1)).pause();
	}
	
	/**
	 * Tests if {@link BackgroundMusic#pause()} does nothing when not playing anything.
	 */
	@Test
	public void testPause_notPlaying() {
		//Play the mocked song and stop it again
		BackgroundMusic.getInstance().playSong(an);
		BackgroundMusic.getInstance().stop();

		//Reset the mock
		reset(an);

		//Call pause
		BackgroundMusic.getInstance().pause();

		//Verify that the song has NOT been paused
		verify(an, times(0)).pause();
	}

	/**
	 * Test method for {@link BackgroundMusic#stop()}.
	 */
	@Test
	public void testStop() {
		//Play the mocked song
		BackgroundMusic.getInstance().playSong(an);

		//Reset the mock
		reset(an);

		//Call stop
		BackgroundMusic.getInstance().stop();

		//Verify that the song has indeed been stopped.
		verify(an, times(1)).stop();

		//Reset the mock
		reset(an);

		//Calling stop again should have no effect
		BackgroundMusic.getInstance().stop();
		verifyZeroInteractions(an);
	}
	
	/**
	 * Tests if {@link BackgroundMusic#update()} does nothing when there is no current song.
	 */
	@Test
	public void testUpdate_noSong() {
		//Play the mocked song, and stop it again
		BackgroundMusic.getInstance().playSong(an);
		BackgroundMusic.getInstance().stop();

		//Reset the mock
		reset(an);

		//Call the update
		BackgroundMusic.getInstance().update();

		//The song should not have been changed any more
		verifyZeroInteractions(an);
	}

	/**
	 * Tests if {@link BackgroundMusic#update()} does nothing when the current song is still
	 * playing.
	 */
	@Test
	public void testUpdate_playing() {
		//Play the mocked song
		BackgroundMusic.getInstance().playSong(an);

		//Reset the mock
		reset(an);

		//Say that we are still playing
		when(an.getStatus()).thenReturn(Status.Playing);

		//Call the update
		BackgroundMusic.getInstance().update();

		//We should not have been stopped
		verify(an, times(0)).stop();
	}
	
	/**
	 * Tests if {@link BackgroundMusic#update()} correctly starts the next song when the
	 * current one has finished playing.
	 */
	@Test
	public void testUpdate_stopped() {
		//Play the mocked song
		BackgroundMusic.getInstance().playSong(an);

		//Reset the mock
		reset(an);

		//Say that we are done playing
		when(an.getStatus()).thenReturn(Status.Stopped);

		//Call the update
		BackgroundMusic.getInstance().update();

		//We should have been explicitly stopped by the method
		verify(an, times(1)).stop();
	}

}
