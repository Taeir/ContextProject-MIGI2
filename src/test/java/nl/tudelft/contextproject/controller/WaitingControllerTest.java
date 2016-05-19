package nl.tudelft.contextproject.controller;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for the WaitingController.
 */
public class WaitingControllerTest {
	private static Main main;
	private WaitingController instance;
	
	/**
	 * Mock the main class and save the old main for restoring.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		main = Main.getInstance();
		Main.setInstance(null);
		TestUtil.ensureMainMocked(true);
	}
	
	/**
	 * Restores the original Main instance after all tests are done.
	 */
	@AfterClass
	public static void tearDownAfterClass() {
		Main.setInstance(main);
	}
	
	/**
	 * Create a new instance of the controller for each test.
	 * @throws FileNotFoundException This should not happen.
	 */
	@Before
	public void setUp() throws FileNotFoundException {
		instance = new WaitingController(Main.getInstance());
	}
	
	/**
	 * Test if the game state is WAITING.
	 */
	@Test
	public void testGetGameState() {
		assertEquals(GameState.WAITING, instance.getGameState());
	}
}
