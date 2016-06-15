package nl.tudelft.contextproject.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.controller.GameThreadController;
import nl.tudelft.contextproject.model.level.Level;

/**
 * Test class for {@link Game}.
 */
public class GameTest extends TestBase {

	private Game instance;
	private GameThreadController controller;
	
	/**
	 * Create a fresh game for each test.
	 */
	@Before
	public void setUp() {
		controller = mock(GameThreadController.class);

		Level level = mock(Level.class);
		when(level.getPlayerSpawnPosition()).thenReturn(new Vector3f());
		instance = new Game(level, controller, 10);
	}

	/**
	 * Test if decrementing the timer works.
	 */
	@Test
	public void testTimerDecrement() {
		instance.update(1f);
		assertEquals(9f, instance.getTimeRemaining(), 1e-8);
	}
	
	/**
	 * Test if triggering the game ending works.
	 */
	@Test
	public void testTimerTrigger() {
		instance.update(11f);
		verify(controller, times(1)).gameEnded(false);
	}

}
