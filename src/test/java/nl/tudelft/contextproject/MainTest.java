package nl.tudelft.contextproject;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ConcurrentHashMap;

import com.jme3.input.DefaultJoystickAxis;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;

import nl.tudelft.contextproject.controller.GameThreadController;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.Observer;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suit for the Main class.
 */
public class MainTest extends TestBase {

	private Main main;
	
	/**
	 * Setup that stores the main instance for convenient access.
	 */
	@Before
	public void setUp() {
		main = Main.getInstance();
	}

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
	 * Set a new controller without a previous one.
	 */
	@Test
	public void testSetControllerFromNull() {
		GameThreadController c = mock(GameThreadController.class);
		assertNull(main.getStateManager().getState(c.getClass()));
		assertTrue(main.setController(c));
		assertNotNull(main.getStateManager().getState(c.getClass()));
	}
	
	/**
	 * Test if setting a new controller attaches and detaches correctly.
	 */
	@Test
	public void testSetController() {
		GameThreadController oldController = mock(GameThreadController.class);
		GameThreadController newController = mock(GameThreadController.class);

		main.setController(oldController);
		assertTrue(main.setController(newController));

		assertFalse(main.getStateManager().attach(newController));
		assertTrue(main.getStateManager().attach(oldController));
	}
	
	/**
	 * Test setting the same controller twice.
	 */
	@Test
	public void testSetControllerAgain() {
		GameThreadController controller = mock(GameThreadController.class);

		main.setController(controller);
		assertFalse(main.setController(controller));
	}
	
	/**
	 * Get the current game when there is no controller.
	 */
	@Test
	public void testGetCurrentGameNoController() {
		assertNull(main.getCurrentGame());
	}
	
	/**
	 * Get the current game from a GameController.
	 */
	@Test
	public void testGetCurrentGameGameController() {
		GameThreadController controller = mock(GameThreadController.class);
		Game game = mock(Game.class);
		when(controller.getGame()).thenReturn(game);
		main.setController(controller);		
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
		GameThreadController controller = mock(GameThreadController.class);
		when(controller.getGameState()).thenReturn(GameState.RUNNING);
		main.setController(controller);
		assertEquals(GameState.RUNNING, main.getGameState());
	}
	
	/**
	 * Test ticklisteners by verifying that only added ticklisteners receive updates.
	 */
	@Test
	public void testTickListeners() {
		main.setObservers(ConcurrentHashMap.newKeySet());
		Observer tickListener = mock(Observer.class);

		main.simpleUpdate(0.1f);
		verify(tickListener, times(0)).update(0.1f);

		main.registerObserver(tickListener);
		main.simpleUpdate(0.1f);
		verify(tickListener, times(1)).update(0.1f);

		main.removeObserver(tickListener);
		main.simpleUpdate(0.1f);
		verifyNoMoreInteractions(tickListener);
	}
	
	/**
	 * Test if setting the control mappings sets the mappings, when only keyboard is connected.
	 */
	@Test
	public void testSetupControlMappings_keyboard() {
		main.setupControlMappings();
		verify(main.getInputManager(), atLeast(1)).addMapping(anyString(), any(KeyTrigger.class));
	}
	
	/**
	 * Test if setting the control mappings sets the mappings, when a controller is connected.
	 */
	@Test
	public void testSetupControlMappings_controller() {
		Joystick joystick = mock(Joystick.class);
		when(joystick.getButton(anyString())).thenReturn(mock(JoystickButton.class));
		when(joystick.getXAxis()).thenReturn(mock(DefaultJoystickAxis.class));
		when(joystick.getYAxis()).thenReturn(mock(DefaultJoystickAxis.class));
		when(joystick.getPovXAxis()).thenReturn(mock(JoystickAxis.class));
		when(joystick.getPovYAxis()).thenReturn(mock(JoystickAxis.class));
		
		InputManager inputManager = main.getInputManager();
		Joystick[] sticks = new Joystick[] { joystick };
		when(inputManager.getJoysticks()).thenReturn(sticks);
		
		main.setupControlMappings();
		
		//Check that at least one keybinding, one controller button and one joystick axis have been mapped.
		verify(main.getInputManager(), atLeast(1)).addMapping(anyString(), any(KeyTrigger.class));
		verify(joystick.getButton(anyString()), atLeast(1)).assignButton(anyString());
		verify(joystick.getXAxis(), atLeast(1)).assignAxis(anyString(), anyString());
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
