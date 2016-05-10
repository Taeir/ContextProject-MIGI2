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
	
//	@Test
//	public void testInitialize() {
//		Controller c = getController();
//		Main app = getMain();
//		
//		AppStateManager stateManager = mock(AppStateManager.class);
//		c.initialize(stateManager, app);
//		Node rn = mock(Node.class);
//		app.setRootNode(rn);
//		Node gn = mock(Node.class);
//		app.setGuiNode(gn);
//		
//		verify(rn.attachChild(any(Node.class)), times(1));
//		verify(gn.attachChild(any(Node.class)), times(1));
//	}
}