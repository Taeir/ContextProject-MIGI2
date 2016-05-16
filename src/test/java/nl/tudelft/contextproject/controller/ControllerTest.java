package nl.tudelft.contextproject.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Graphics2D;

import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.input.InputManager;
import com.jme3.input.controls.InputListener;
import com.jme3.light.Light;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Drawable;

import org.junit.Test;

/**
 * Test class for the controller Class.
 */
public abstract class ControllerTest {

	/**
	 * Get a controller to test with.
	 * @return An instance of a Controller.
	 */
	public abstract Controller getController();
	
	/**
	 * Get an instance of Main.
	 * @return The instance of Main.
	 */
	public abstract Main getMain();
	
	/**
	 * Check if the state is set.
	 */
	@Test
	public void testGameStateNotNull() {
		assertNotNull(getController().getGameState());
	}
	
	/**
	 * Test if attaching a Gui element adds the element.
	 */
	@Test
	public void testAddGuiElement() {
		Controller c = getController();
		Node gn = mock(Node.class);
		c.setGuiNode(gn);
		Spatial s = mock(Spatial.class);
		
		c.addGuiElement(s);
		verify(gn, times(1)).attachChild(s);
	}
	
	/**
	 * Test if removing a Gui element removed it.
	 */
	@Test
	public void testRemoveGuiElement() {
		Controller c = getController();
		Node gn = mock(Node.class);
		c.setGuiNode(gn);
		Spatial s = mock(Spatial.class);
		when(gn.detachChild(any(Spatial.class))).thenReturn(12);
		assertTrue(c.removeGuiElement(s));
		verify(gn, times(1)).detachChild(s);
	}
	
	/**
	 * Test for removing a Gui element that was never added.
	 */
	@Test
	public void testRemoveGuiElementNonExistent() {
		Controller c = getController();
		Node gn = mock(Node.class);
		c.setGuiNode(gn);
		Spatial s = mock(Spatial.class);
		when(gn.detachChild(any(Spatial.class))).thenReturn(-1);
		assertFalse(c.removeGuiElement(s));
		verify(gn, times(1)).detachChild(s);
	}
	
	/**
	 * Test if using addDrawable adds the drawable.
	 */
	@Test
	public void testAddDrawable() {
		Controller c = getController();
		Node rn = mock(Node.class);
		c.setRootNode(rn);
		Geometry geom = mock(Geometry.class);
		BulletAppState phe = mock(BulletAppState.class);
		c.setPhysicsEnvironmentNode(phe);
		PhysicsSpace phs = mock(PhysicsSpace.class);
		
		when(phe.getPhysicsSpace()).thenReturn(phs);
		
		Drawable d = new Drawable() {
			@Override
			public Spatial getSpatial() {
				return geom;
			}
			
			@Override
			public void setSpatial(Spatial spatial) { }
			
			@Override
			public void mapDraw(Graphics2D g, int resolution) { }
		};		
		c.addDrawable(d);
		verify(rn, times(1)).attachChild(geom);
	}
	
	/**
	 * Test if removeDrawable removes the drawable.
	 */
	@Test
	public void testRemoveDrawable() {
		Controller c = getController();
		Node rn = mock(Node.class);
		c.setRootNode(rn);
		Geometry geom = mock(Geometry.class);
		Drawable d = new Drawable() {
			@Override
			public Geometry getSpatial() {
				return geom;
			}

			@Override
			public void setSpatial(Spatial spatial) { }
			
			@Override
			public void mapDraw(Graphics2D g, int resolution) { }
		};	
		when(rn.detachChild(any(Spatial.class))).thenReturn(12);
		assertTrue(c.removeDrawable(d));
		verify(rn, times(1)).detachChild(geom);
	}
	
	/**
	 * Test removing a drawable that was never added.
	 */
	@Test
	public void testRemoveDrawableNonExistent() {
		Controller c = getController();
		Node rn = mock(Node.class);
		c.setRootNode(rn);
		Geometry geom = mock(Geometry.class);
		Drawable d = new Drawable() {
			@Override
			public Spatial getSpatial() {
				return geom;
			}

			@Override
			public void setSpatial(Spatial spatial) { }
			
			@Override
			public void mapDraw(Graphics2D g, int resolution) { }
		};		
		when(rn.detachChild(any(Spatial.class))).thenReturn(-1);
		assertFalse(c.removeDrawable(d));
		verify(rn, times(1)).detachChild(geom);
	}
	
	/**
	 * Test if initializing creates the correct structure in the root and gui Node.
	 */
	@Test
	public void testInitialize() {
		Main app = getMain();
		Controller c = new Controller(app, null) {
			@Override
			public void update(float tpf) { }
			
			@Override
			public GameState getGameState() {
				return null;
			}
			
		};
		
		AppStateManager stateManager = mock(AppStateManager.class);
		Node rn = mock(Node.class);
		app.setRootNode(rn);
		Node gn = mock(Node.class);
		app.setGuiNode(gn);

		c.initialize(stateManager, app);
		
		verify(rn, times(1)).attachChild(any(Node.class));
		verify(gn, times(1)).attachChild(any(Node.class));
	}
	
	/**
	 * Test if adding a light adds the light.
	 */
	@Test
	public void testAddLight() {
		Controller c = getController();
		Node rn = mock(Node.class);
		c.setRootNode(rn);
		Light l = mock(Light.class);
		
		c.addLight(l);
		verify(rn, times(1)).addLight(l);
	}
	
	/**
	 * Test if removing lights removes the light.
	 */
	@Test
	public void testRemoveLight() {
		Controller c = getController();
		Node rn = mock(Node.class);
		c.setRootNode(rn);
		Light l = mock(Light.class);
		
		c.removeLight(l);
		verify(rn, times(1)).removeLight(l);
	}
	
	/**
	 * Test if adding an input listener adds the input listener.
	 */
	@Test
	public void testAddInputListener() {
		Controller c = getController();
		InputManager im = mock(InputManager.class);
		c.setInputManager(im);
		InputListener l = mock(InputListener.class);
		
		c.addInputListener(l, "hello");
		verify(im, times(1)).addListener(l, "hello");
	}
	
	/**
	 * Test if removing an inputListener removes it.
	 */
	@Test
	public void testRemoveInputListener() {

		Controller c = getController();
		InputManager im = mock(InputManager.class);
		c.setInputManager(im);
		InputListener l = mock(InputListener.class);
		
		c.removeInputListener(l);
		verify(im, times(1)).removeListener(l);
	}
}