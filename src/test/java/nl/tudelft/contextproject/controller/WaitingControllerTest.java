package nl.tudelft.contextproject.controller;

import static org.junit.Assert.*;

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
	 */
	@Before
	public void setUp() {
		instance = new WaitingController(Main.getInstance());
	}
	
	/**
	 * Test if the game state is WAITING.
	 */
	@Test
	public void testGetGameState() {
		assertEquals(GameState.WAITING, instance.getGameState());
	}
	
	/**
	 * Test getting the map file.
	 */
	@Test
	public void testGetFile() {
		assertTrue(WaitingController.getFile().getName().endsWith(".crf"));
		assertTrue(WaitingController.getFile().getParent().endsWith("\\maps")
				   || WaitingController.getFile().getParent().endsWith("/maps"));
	}
}
