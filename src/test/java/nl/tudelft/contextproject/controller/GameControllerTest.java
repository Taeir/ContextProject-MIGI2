package nl.tudelft.contextproject.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.jme3.light.Light;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Entity;
import nl.tudelft.contextproject.model.EntityState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.VRPlayer;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.LevelFactory;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.Room;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the GameController class.
 */
public class GameControllerTest extends ControllerTest {
	private GameController controller;
	private Main main;
	private Level level;
	
	@Before
	public void setUp() {
		main = mock(Main.class);
		LevelFactory factory = mock(LevelFactory.class);
		level = mock(Level.class);
		
		controller = new GameController(main, factory);
		controller.setLevel(level);
	}
	
//	/**
//	 * Test the update on the player.
//	 */
//	@Test
//	public void testUpdatePlayer() {
//		VRPlayer player = mock(VRPlayer.class);
//		controller.setLevel(level);
//        controller.update(0.5f);
//        verify(player, times(1)).update(0.5f);
//	}
	
	/**
	 * Test if a NEW entity is updated correctly.
	 */
	@Test
	public void testUpdateEntityNEW() {
		List<Entity> list = controller.getGame().getEntities();
		Entity eMock = mock(Entity.class);
		Node rn = mock(Node.class);
		Geometry geom = mock(Geometry.class);
		
		when(eMock.getState()).thenReturn(EntityState.NEW);
		when(eMock.getGeometry()).thenReturn(geom);
		
		list.add(eMock);
		
		controller.setRootNode(rn);
        controller.updateEntities(0.5f);
        
        verify(eMock, times(1)).update(0.5f);
        verify(eMock, times(1)).setState(EntityState.ALIVE);
        
        verify(rn, times(1)).attachChild(geom);        
	}
	
	/**
	 * Test if a DEAD entity is removed correctly.
	 */
	@Test
	public void testUpdateEntityDEAD() {
		List<Entity> list = controller.getGame().getEntities();
		Entity eMock = mock(Entity.class);
		Node rn = mock(Node.class);
		Geometry geom = mock(Geometry.class);
		
		when(eMock.getState()).thenReturn(EntityState.DEAD);
		when(eMock.getGeometry()).thenReturn(geom);
		
		list.add(eMock);
		
		controller.setLevel(level);
		controller.setRootNode(rn);
        controller.updateEntities(0.5f);
        
        verify(eMock, times(0)).update(0.5f);
        
        verify(rn, times(1)).detachChild(geom); 
        assertFalse(list.contains(eMock));
	}
	
	/**
	 * Test if an ALIVE entity is updated correctly.
	 */
	@Test
	public void testUpdateEntityALIVE() {
		List<Entity> list = controller.getGame().getEntities();
		Entity eMock = mock(Entity.class);
		
		when(eMock.getState()).thenReturn(EntityState.ALIVE);
		
		list.add(eMock);
		controller.setLevel(level);
        controller.updateEntities(0.5f);
        
        verify(eMock, times(1)).update(0.5f);
	}
	
	/**
	 * Test the setters and getters for the level.
	 */
	@Test
	public void testLevel() {
		Level level = new Level(null);
		controller.setLevel(level);
		assertEquals(level, controller.getLevel());
	}
	
	/**
	 * Test attaching an unset level to the renderer.
	 */
	@Test (expected = IllegalStateException.class)
	public void testAttachLevelNull() {
		controller.setLevel(null);
		controller.attachLevel();
	}
	
//	/**
//	 * Test if attaching a level correctly adds all elements to the renderer.
//	 */
//	@Test
//	public void testAttachLevel() {
//		Node rn = mock(Node.class);
//		Light lMock = mock(Light.class);
//		MazeTile t = mock(MazeTile.class);
//		VRPlayer player = mock(VRPlayer.class);
//		Geometry geom = mock(Geometry.class);
//		Geometry playerGeom = mock(Geometry.class);
//		
//		when(t.getGeometry()).thenReturn(geom);
//		when(player.getGeometry()).thenReturn(playerGeom);
//		
//		List<Light> lights = new ArrayList<>();
//		lights.add(lMock);
//		lights.add(lMock);
//		MazeTile[][] tiles = {{t, null, t}, {t, t, t}};
//		Room[] rooms = {new Room(tiles, lights)};
//		Game game = new Game(new Level(rooms));
//		
//		controller.setRootNode(rn);
//		controller.setGame(game);
//		controller.attachLevel();
//        
//        verify(rn, times(5)).attachChild(geom);
//        verify(rn, times(1)).attachChild(playerGeom);
//        verify(rn, times(2)).addLight(lMock);
//	}

	@Override
	public Controller getController() {
		return controller;
	}

	@Override
	public Main getMain() {
		return main;
	}
}