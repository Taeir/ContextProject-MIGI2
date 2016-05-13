package nl.tudelft.contextproject.controller;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.scene.Node;

import nl.tudelft.contextproject.Main;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;

/**
 * Test class for the PauseController class.
 */
public class PauseControllerTest extends ControllerTest {

	private PauseController controller;
	private Node rootNode;
	private Node guiNode;
	private Main main;
	private InputManager inputManager;
	private GameController old;

	/**
	 * Setup all the mocks for each test.
	 */
	@SuppressWarnings("deprecation") @Before
	public void setUp() {
		controller = getController();
		main = Main.getInstance();
		main.setAssetManager(mock(AssetManager.class));
		rootNode = mock(Node.class);
		guiNode = mock(Node.class);
		inputManager = mock(InputManager.class);
		controller.setRootNode(rootNode);
		controller.setGuiNode(guiNode);
		controller.setInputManager(inputManager);
	}
	
	/**
	 * Test that updating does not change the root and Gui node.
	 */
	@Test
	public void testUpdate() {
		controller.update(0.1f);
		verifyZeroInteractions(rootNode);
		verifyZeroInteractions(guiNode);
	}
	
	/**
	 * Test if initializing adds a keyListener for unpausing the game and adds the displayed text.
	 */
	@Test
	public void testInitialize() {
		BitmapFont bf = mock(BitmapFont.class);
		when(main.getAssetManager().loadFont(anyString())).thenReturn(bf);
		BitmapText text = mock(BitmapText.class);
		when(bf.createLabel(anyString())).thenReturn(text);
		
		AppStateManager sm = mock(AppStateManager.class);
		controller.initialize(sm, main);
		verify(inputManager, times(1)).addListener(any(InputListener.class), eq("pause"));
		verify(text, times(1)).setLocalTranslation(anyFloat(), anyFloat(), eq(0f));
		verify(guiNode, times(1)).attachChild(text);
	}
	
	/**
	 * Test if initializing adds a keyListener for unpausing the game and adds the displayed text.
	 */
	@Test
	public void testInitializeInputListener() {
		BitmapFont bf = mock(BitmapFont.class);
		when(main.getAssetManager().loadFont(anyString())).thenReturn(bf);
		BitmapText text = mock(BitmapText.class);
		when(bf.createLabel(anyString())).thenReturn(text);
		
		AppStateManager sm = mock(AppStateManager.class);
		controller.initialize(sm, main);
		
		ArgumentCaptor<InputListener> captor = ArgumentCaptor.forClass(InputListener.class);
		verify(inputManager).addListener(captor.capture(), anyString());

		ActionListener l = (ActionListener) captor.getValue();
		
		l.onAction("", false, 0.1f);
		assertNull(main.getStateManager().getState(PauseController.class));
		assertNotNull(main.getStateManager().getState(old.getClass()));
	}
	
	/**
	 * Test if pausedController returns the controller given to the constructor.
	 */
	@Test
	public void testGetPausedController() {
		assertEquals(old, controller.getPausedController());
	}
	
	@Override
	public PauseController getController() {
		old = mock(GameController.class);
		return new PauseController(old, getMain());
	}

	@Override
	public Main getMain() {
		Main.setInstance(new Main());
		return Main.getInstance();
	}

}