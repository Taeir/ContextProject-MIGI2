package nl.tudelft.contextproject.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.Controller;

import org.junit.Test;

public abstract class ControllerTest {

	public abstract Controller getController();
	public abstract Main getMain();
	
	@Test
	public void testGameStateNotNull() {
		assertNotNull(getController().getGameState());
	}
	
	@Test
	public void testAddGuiElement() {
		Controller c = getController();
		Node gn = mock(Node.class);
		c.setGuiNode(gn);
		Spatial s = mock(Spatial.class);
		
		c.addGuiElement(s);
		verify(gn, times(1)).attachChild(s);
	}
	
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
	
	@Test
	public void testAddDrawable() {
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
			
		};		
		c.addDrawable(d);
		verify(rn, times(1)).attachChild(geom);
	}
	

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
			
		};	
		when(rn.detachChild(any(Spatial.class))).thenReturn(12);
		assertTrue(c.removeDrawable(d));
		verify(rn, times(1)).detachChild(geom);
	}
	
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
			
		};		
		when(rn.detachChild(any(Spatial.class))).thenReturn(-1);
		assertFalse(c.removeDrawable(d));
		verify(rn, times(1)).detachChild(geom);
	}
	
//	@Test
//	public void testInitialize() {
//		Main app = getMain();
//		Controller c = new Controller(app, null) {
//			@Override
//			public void update(float tpf) { }
//			
//			@Override
//			public GameState getGameState() {
//				return null;
//			}
//			
//		};
//		
//		AppStateManager stateManager = mock(AppStateManager.class);
//		Node rn = mock(Node.class);
//		app.setRootNode(rn);
//		Node gn = mock(Node.class);
//		app.setGuiNode(gn);
//
//		c.initialize(stateManager, app);
//		
//		verify(rn, times(1)).attachChild(any(Node.class));
//		verify(gn, times(1)).attachChild(any(Node.class));
//	}
	
	@Test
	public void testAddLight() {
		Controller c = getController();
		Node rn = mock(Node.class);
		c.setRootNode(rn);
		Light l = mock(Light.class);
		
		c.addLight(l);
		verify(rn, times(1)).addLight(l);
	}
	
	@Test
	public void testRemoveLight() {
		Controller c = getController();
		Node rn = mock(Node.class);
		c.setRootNode(rn);
		Light l = mock(Light.class);
		
		c.removeLight(l);
		verify(rn, times(1)).removeLight(l);
	}
	
//	@Test
//	public void testAddInputListener() {
//		Controller c = getController();
//		Node rn = mock(Node.class);
//		c.setRootNode(rn);
//		Light l = mock(Light.class);
//		
//		c.addInputListener(l, mappings);
//		verify(rn, times(1)).addLight(l);
//	}
//	
//	@Test
//	public void testRemoveInputListener() {
//		Controller c = getController();
//		Node rn = mock(Node.class);
//		c.setRootNode(rn);
//		Light l = mock(Light.class);
//		
//		c.removeLight(l);
//		verify(rn, times(1)).removeLight(l);
//	}
}