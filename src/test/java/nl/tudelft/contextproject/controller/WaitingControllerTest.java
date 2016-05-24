package nl.tudelft.contextproject.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;

/**
 * Test class for the WaitingController.
 */
public class WaitingControllerTest extends TestBase {
	private WaitingController instance;

	/**
	 * Create a new instance of the controller for each test.
	 */
	@Before
	public void setUp() {
		instance = new WaitingController(Main.getInstance());
	}
	
	/**
	 * Test if creating a WaitingController creates a full game.
	 */
	@Test
	public void testFileReading() {
		assertFalse(instance.getGame().getEntities().isEmpty());
		assertNotNull(instance.getGame().getPlayer());
		assertNotNull(instance.getGame().getLevel());
	}
	
	/**
	 * Test if the game state is WAITING.
	 */
	@Test
	public void testGetGameState() {
		assertEquals(GameState.WAITING, instance.getGameState());
	}
}
