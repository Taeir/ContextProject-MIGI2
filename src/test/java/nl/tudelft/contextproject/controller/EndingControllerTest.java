package nl.tudelft.contextproject.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.app.state.AppStateManager;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;

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
		
		AppStateManager asm = Mockito.mock(AppStateManager.class);
		instance.initialize(asm, Main.getInstance());
	}
	
	/**
	 * Test if the game state is ENDED.
	 */
	@Test
	public void testGetGameState() {
		assertEquals(GameState.ENDED, instance.getGameState());
	}
}
