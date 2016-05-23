package nl.tudelft.contextproject;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;

import com.jme3.input.InputManager;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;

import nl.tudelft.contextproject.audio.AudioTestUtil;
import nl.tudelft.contextproject.controller.Controller;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.controller.PauseController;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.TickListener;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suit for the Main class.
 */
public class MainTest {

	private Main main;

	/**
	 * Check if start is called when starting the game.
	 */
	@Test
	public void testMainStart() {
		Main mMock = mock(Main.class);
		Main.setInstance(mMock);
	    Main.main(new String[0]);
        verify(mMock, times(1)).start();
	}

	/**
	 * Setup that creates a fresch instance of main.
	 */
	@Before
	public void setUp() {
		Main.setInstance(AudioTestUtil.fakeMain());
		main = Main.getInstance();
	}
	
	/**
	 * Test if giving '--debugHud' shows the debug hud.
	 */
	@Test
	public void testMainDebugHud() {
		Main mMock = mock(Main.class);
		Main.setInstance(mMock);
		String[] args = {"a", "--debugHud", "b"};
	    Main.main(args);
	    assertTrue(Main.isDebugHudShown());
	}
	
	/**
	 * Test if debug hud is not shown when passing other arguments.
	 */
	@Test
	public void testMainHideDebugHud() {
		Main mMock = mock(Main.class);
		Main.setInstance(mMock);
		String[] args = {"a", "--debugHudd", "b"};
	    Main.main(args);
	    assertFalse(Main.isDebugHudShown());
	}
	
	/**
	 * Set a new controller without a previous one.
	 */
	@Test
	public void testSetControllerFromNull() {
		Controller c = mock(Controller.class);
		assertNull(main.getStateManager().getState(c.getClass()));
		assertTrue(main.setController(c));
		assertNotNull(main.getStateManager().getState(c.getClass()));
	}
	
	/**
	 * Test if setting a new controller attaches and detaches correctly.
	 */
	@Test
	public void testSetController() {
		Controller cOld = mock(Controller.class);
		Controller c = mock(Controller.class);
		
		main.setController(cOld);
		assertTrue(main.setController(c));

		assertFalse(main.getStateManager().attach(c));
		assertTrue(main.getStateManager().attach(cOld));
	}
	
	/**
	 * Test setting the same controller twice.
	 */
	@Test
	public void testSetControllerAgain() {
		Controller c = mock(Controller.class);
		
		main.setController(c);
		assertFalse(main.setController(c));
	}
	
	/**
	 * Try to get the current game from a state that does not have a current game.
	 */
	@Test
	public void testGetCurrentGameIllegalState() {
		Controller c = mock(Controller.class);		
		main.setController(c);		
		assertNull(main.getCurrentGame());
	}
	
	/**
	 * Get the current game from a GameController.
	 */
	@Test
	public void testGetCurrentGameGameController() {
		GameController c = mock(GameController.class);
		Game game = mock(Game.class);
		when(c.getGame()).thenReturn(game);
		main.setController(c);		
		assertEquals(game, main.getCurrentGame());
	}
	
	/**
	 * Get the current game from a PauseController.
	 */
	@Test
	public void testGetCurrentGamePauseController() {
		GameController gc = mock(GameController.class);
		PauseController c = mock(PauseController.class);
		when(c.getPausedController()).thenReturn(gc);
		Game game = mock(Game.class);
		when(gc.getGame()).thenReturn(game);
		main.setController(c);		
		assertEquals(game, main.getCurrentGame());
	}
	
	/**
	 * Check the game state without a state attached.
	 */
	@Test
	public void testGetGameStateNull() {
		assertNull(main.getGameState());
	}
	
	/**
	 * Test getting the gamestate.
	 */
	@Test
	public void testGetGameState() {
		Controller c = mock(Controller.class);
		when(c.getGameState()).thenReturn(GameState.RUNNING);
		main.setController(c);
		assertEquals(GameState.RUNNING, main.getGameState());
	}
	
	/**
	 * Test ticklisteners by verifying that only added ticklisteners receive updates.
	 */
	@Test
	public void testTickListeners() {
		main.setTickListeners(new LinkedList<TickListener>());
		TickListener tl = mock(TickListener.class);

		main.simpleUpdate(0.1f);
		verify(tl, times(0)).update(0.1f);
		
		main.attachTickListener(tl);
		main.simpleUpdate(0.1f);
		verify(tl, times(1)).update(0.1f);
		
		main.removeTickListener(tl);
		main.simpleUpdate(0.1f);
		verifyNoMoreInteractions(tl);
	}
	
	/**
	 * Test if seting the control mappings sets the mappings.
	 */
	@Test
	public void testSetupControlMappings() {
		InputManager im = mock(InputManager.class);
		main.setInputManager(im);
		main.setupControlMappings();
		verify(im, atLeast(1)).addMapping(anyString(), any(KeyTrigger.class));
	}
	
	/**
	 * Test if setting the RootNode sets the RootNode.
	 */
	@Test
	public void testSetRootNode() {
		Node node = mock(Node.class);
		main.setRootNode(node);
		assertEquals(node, main.getRootNode());
	}
	
	/**
	 * Test if setting the GuiNode sets the GuiNode.
	 */
	@Test
	public void testSetGuiNode() {
		Node node = mock(Node.class);
		main.setGuiNode(node);
		assertEquals(node, main.getGuiNode());
	}
}
