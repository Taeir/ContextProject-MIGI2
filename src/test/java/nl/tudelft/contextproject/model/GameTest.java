package nl.tudelft.contextproject.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.controller.GameController;

/**
 * Test class for {@link Game}.
 */
public class GameTest extends TestBase {

	private Game instance;
	private GameController controller;
	
	/**
	 * Create a fresh game for each test.
	 */
	@Before
	public void setUp() {
		controller = mock(GameController.class);
		instance = new Game(null, controller, 10);
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
