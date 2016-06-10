package nl.tudelft.contextproject.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.InputManager;
import com.jme3.input.controls.InputListener;
import com.jme3.light.Light;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.Drawable;

import org.junit.Test;

/**
 * Test class for the controller Class.
 */
public abstract class ControllerTest extends TestBase {

	/**
	 * Get a controller to test with.
	 *
	 * @return
	 * 		an instance of a Controller
	 */
	public abstract Controller getController();
	
	/**
	 * Get an instance of Main.
	 *
	 * @return
	 * 		the instance of Main
	 */
	public abstract Main getMain();
	
	/**
	 * Check if the state is set.
	 */
	@Test
	public void testGameStateNotNull_controller() {
		assertNotNull(getController().getGameState());
	}
	
	/**
	 * Test if attaching a Gui element adds the element.
	 */
	@Test
	public void testAddGuiElement_controller() {
		Controller controller = getController();
		Node node = mock(Node.class);
		controller.setGuiNode(node);
		Spatial spatial = mock(Spatial.class);
		
		controller.addGuiElement(spatial);
		verify(node, times(1)).attachChild(spatial);
	}
	
	/**
	 * Test if removing a Gui element removed it.
	 */
	@Test
	public void testRemoveGuiElement_controller() {
		Controller controller = getController();
		Node node = mock(Node.class);
		controller.setGuiNode(node);
		Spatial spatial = mock(Spatial.class);
		when(node.detachChild(any(Spatial.class))).thenReturn(12);
		assertTrue(controller.removeGuiElement(spatial));
		verify(node, times(1)).detachChild(spatial);
	}
	
	/**
	 * Test for removing a Gui element that was never added.
	 */
	@Test
	public void testRemoveGuiElementNonExistent_controller() {
		Controller controller = getController();
		Node node = mock(Node.class);
		controller.setGuiNode(node);
		Spatial spatial = mock(Spatial.class);

		when(node.detachChild(any(Spatial.class))).thenReturn(-1);
		assertFalse(controller.removeGuiElement(spatial));
		verify(node, times(1)).detachChild(spatial);
	}
	
	/**
	 * Test if using addDrawable adds the drawable.
	 */
	@Test
	public void testAddDrawable_controller() {
		Controller controller = getController();
		Node node = mock(Node.class);
		controller.setRootNode(node);
		Geometry geometry = mock(Geometry.class);
		BulletAppState phe = mock(BulletAppState.class);
		controller.setPhysicsEnvironmentNode(phe);
		PhysicsSpace phs = mock(PhysicsSpace.class);
		
		when(phe.getPhysicsSpace()).thenReturn(phs);
		
		Drawable drawable = mock(Drawable.class);
		when(drawable.getSpatial()).thenReturn(geometry);

		controller.addDrawable(drawable);
		verify(node, times(1)).attachChild(geometry);
	}
	
	/**
	 * Test if removeDrawable removes the drawable.
	 */
	@Test
	public void testRemoveDrawable_controller() {
		Controller controller = getController();
		Node node = mock(Node.class);
		controller.setRootNode(node);
		Geometry geometry = mock(Geometry.class);

		Drawable drawable = mock(Drawable.class);
		when(drawable.getSpatial()).thenReturn(geometry);

		when(node.detachChild(any(Spatial.class))).thenReturn(12);
		assertTrue(controller.removeDrawable(drawable));
		verify(node, times(1)).detachChild(geometry);
	}
	
	/**
	 * Test removing a drawable that was never added.
	 */
	@Test
	public void testRemoveDrawableNonExistent_controller() {
		Controller controller = getController();
		Node node = mock(Node.class);
		controller.setRootNode(node);
		Geometry geometry = mock(Geometry.class);

		Drawable drawable = mock(Drawable.class);
		when(drawable.getSpatial()).thenReturn(geometry);

		when(node.detachChild(any(Spatial.class))).thenReturn(-1);
		assertFalse(controller.removeDrawable(drawable));
		verify(node, times(1)).detachChild(geometry);
	}
	
	/**
	 * Test if initializing creates the correct structure in the root and gui Node.
	 */
	@Test
	public void testInitialize_controller() {
		Main app = getMain();

		Controller controller = new Controller(app, null) {
			@Override
			public void update(float tpf) { }
			
			@Override
			public GameState getGameState() {
				return null;
			}
			
		};
		
		AppStateManager stateManager = mock(AppStateManager.class);
		Node node1 = mock(Node.class);
		app.setRootNode(node1);
		Node node2 = mock(Node.class);
		app.setGuiNode(node2);

		controller.initialize(stateManager, app);
		
		verify(node1, times(1)).attachChild(any(Node.class));
		verify(node2, times(1)).attachChild(any(Node.class));
	}
	
	/**
	 * Test if adding a light adds the light.
	 */
	@Test
	public void testAddLight_controller() {
		Controller controller = getController();
		Node node = mock(Node.class);
		controller.setRootNode(node);
		Light light = mock(Light.class);
		
		controller.addLight(light);
		verify(node, times(1)).addLight(light);
	}
	
	/**
	 * Test if removing lights removes the light.
	 */
	@Test
	public void testRemoveLight_controller() {
		Controller controller = getController();
		Node node = mock(Node.class);
		controller.setRootNode(node);
		Light light = mock(Light.class);
		
		controller.removeLight(light);
		verify(node, times(1)).removeLight(light);
	}
	
	/**
	 * Test if adding an input listener adds the input listener.
	 */
	@Test
	public void testAddInputListener_controller() {
		Controller controller = getController();
		InputManager inputManager = mock(InputManager.class);
		controller.setInputManager(inputManager);
		InputListener inputListener = mock(InputListener.class);
		
		controller.addInputListener(inputListener, "hello");
		verify(inputManager, times(1)).addListener(inputListener, "hello");
	}
	
	/**
	 * Test if removing an inputListener removes it.
	 */
	@Test
	public void testRemoveInputListener_controller() {
		Controller controller = getController();
		InputManager inputManager = mock(InputManager.class);
		controller.setInputManager(inputManager);
		InputListener inputListener = mock(InputListener.class);
		
		controller.removeInputListener(inputListener);
		verify(inputManager, times(1)).removeListener(inputListener);
	}
}