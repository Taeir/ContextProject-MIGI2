package nl.tudelft.contextproject.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.jme3.app.state.AppStateManager;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.hud.HUD;

/**
 * Test class for {@link EndingController}.
 */
public class EndingControllerTest extends TestBase {
	private EndingController instance;

	/**
	 * Create a new instance of the controller for each test.
	 */
	@Before
	public void setUp() {
		instance = new EndingController(Main.getInstance(), true);
		
		AppStateManager appStateManager = mock(AppStateManager.class);
		instance.setHUD(mock(HUD.class));
		instance.initialize(appStateManager, Main.getInstance());
	}
	
	/**
	 * Test if the game state is ENDED.
	 */
	@Test
	public void testGetGameState() {
		assertEquals(GameState.ENDED, instance.getGameState());
	}

	/**
	 * Test if the didElvesWin method works.
	 */
	@Test
	public void testDidElvesWin() {
		assertTrue(instance.didElvesWin());
	}
}
