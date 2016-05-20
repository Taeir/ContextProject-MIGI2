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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for {@link BackgroundMusic}.
 */
public class BackgroundMusicTest {
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

		verify(an, times(1)).stop();
		verify(an2, times(1)).play();
	}

	/**
	 * Test method for {@link BackgroundMusic#next()}.
	 */
	@Test
	public void testNext() {
		BackgroundMusic.getInstance().playSong(an);

		reset(an);

		BackgroundMusic.getInstance().next();
		verify(an, times(1)).stop();
	}

	/**
	 * Tests if {@link BackgroundMusic#start()} correctly resumes paused playback.
	 */
	@Test
	public void testStart_paused() {
		BackgroundMusic.getInstance().playSong(an);

		reset(an);

		when(an.getStatus()).thenReturn(Status.Paused);

		BackgroundMusic.getInstance().start();
		verify(an, times(1)).play();
	}
	
	/**
	 * Tests if {@link BackgroundMusic#start()} correctly starts music when none was playing.
	 */
	@Test
	public void testStart_notPlaying() {
		BackgroundMusic.getInstance().start();
		
		AudioNode an = BackgroundMusic.getInstance().getCurrent();

		assertNotNull(an);
		verify(an, times(1)).play();
	}
	
	/**
	 * Tests if {@link BackgroundMusic#start()} correctly resumes paused playback.
	 */
	@Test
	public void testStart_playing() {
		BackgroundMusic.getInstance().playSong(an);

		reset(an);

		when(an.getStatus()).thenReturn(Status.Playing);

		BackgroundMusic.getInstance().start();

		verify(an, times(0)).play();
	}

	/**
	 * Tests if {@link BackgroundMusic#pause()} pauses the currently playing song.
	 */
	@Test
	public void testPause_playing() {
		BackgroundMusic.getInstance().playSong(an);

		reset(an);
		BackgroundMusic.getInstance().pause();

		verify(an, times(1)).pause();
	}
	
	/**
	 * Tests if {@link BackgroundMusic#pause()} does nothing when not playing anything.
	 */
	@Test
	public void testPause_notPlaying() {
		BackgroundMusic.getInstance().playSong(an);
		BackgroundMusic.getInstance().stop();

		reset(an);
		BackgroundMusic.getInstance().pause();

		verify(an, times(0)).pause();
	}

	/**
	 * Test method for {@link BackgroundMusic#stop()}.
	 */
	@Test
	public void testStop() {
		BackgroundMusic.getInstance().playSong(an);

		reset(an);
		BackgroundMusic.getInstance().stop();

		verify(an, times(1)).stop();

		reset(an);
		BackgroundMusic.getInstance().stop();

		verifyZeroInteractions(an);
	}
	
	/**
	 * Tests if {@link BackgroundMusic#update()} does nothing when there is no current song.
	 */
	@Test
	public void testUpdate_noSong() {
		BackgroundMusic.getInstance().playSong(an);
		BackgroundMusic.getInstance().stop();

		reset(an);
		BackgroundMusic.getInstance().update();

		verifyZeroInteractions(an);
	}

	/**
	 * Tests if {@link BackgroundMusic#update()} does nothing when the current song is still
	 * playing.
	 */
	@Test
	public void testUpdate_playing() {
		BackgroundMusic.getInstance().playSong(an);

		reset(an);

		when(an.getStatus()).thenReturn(Status.Playing);

		BackgroundMusic.getInstance().update();

		verify(an, times(0)).stop();
	}
	
	/**
	 * Tests if {@link BackgroundMusic#update()} correctly starts the next song when the
	 * current one has finished playing.
	 */
	@Test
	public void testUpdate_stopped() {
		BackgroundMusic.getInstance().playSong(an);

		reset(an);

		when(an.getStatus()).thenReturn(Status.Stopped);

		BackgroundMusic.getInstance().update();

		verify(an, times(1)).stop();
	}

}
