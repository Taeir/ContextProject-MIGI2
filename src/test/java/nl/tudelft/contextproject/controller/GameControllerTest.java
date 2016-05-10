package nl.tudelft.contextproject.controller;
//package nl.tudelft.contextproject;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import com.jme3.light.Light;
//import com.jme3.scene.Geometry;
//import com.jme3.scene.Node;
//import nl.tudelft.contextproject.level.Level;
//import nl.tudelft.contextproject.level.LevelFactory;
//import nl.tudelft.contextproject.level.MazeTile;
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * Tests for the GameController class.
// */
//public class GameControllerTest extends ControllerTest {
//	private GameController controller;
//	private Main main;
//	private Level level;
//	
//	@Before
//	public void setUp() {
//		main = mock(Main.class);
//		LevelFactory factory = mock(LevelFactory.class);
//		level = mock(Level.class);
//		
//		controller = new GameController(main, factory);
//		controller.setLevel(level);
//	}
//	
//	/**
//	 * Test the simpleUpdate on the player.
//	 */
//	@Test
//	public void testSimpleUpdatePlayer() {
//		VRPlayer player = mock(VRPlayer.class);
//		Game game = new Level(null, player, new HashSet<Entity>(), null);
//		controller.setLevel(level);
//        controller.update(0.5f);
//        verify(player, times(1)).update(0.5f);
//	}
//	
//	/**
//	 * Test if a NEW entity is updated correctly.
//	 */
//	@Test
//	public void testSimpleUpdateEntityNEW() {
//		Set<Entity> set = new HashSet<Entity>();
//		Entity eMock = mock(Entity.class);
//		Node rn = mock(Node.class);
//		Geometry geom = mock(Geometry.class);
//		
//		when(eMock.getState()).thenReturn(EntityState.NEW);
//		when(eMock.getGeometry()).thenReturn(geom);
//		
//		set.add(eMock);
//		Level level = new Level(null, null, set, null);
//		
//		controller.setLevel(level);
//		controller.setRootNode(rn);
//        controller.updateEntities(0.5f);
//        
//        verify(eMock, times(1)).update(0.5f);
//        verify(eMock, times(1)).setState(EntityState.ALIVE);
//        
//        verify(rn, times(1)).attachChild(geom);        
//	}
//	
//	/**
//	 * Test if a DEAD entity is removed correctly.
//	 */
//	@Test
//	public void testSimpleUpdateEntityDEAD() {
//		Set<Entity> set = new HashSet<Entity>();
//		Entity eMock = mock(Entity.class);
//		Node rn = mock(Node.class);
//		Geometry geom = mock(Geometry.class);
//		
//		when(eMock.getState()).thenReturn(EntityState.DEAD);
//		when(eMock.getGeometry()).thenReturn(geom);
//		
//		set.add(eMock);
//		Level level = new Level(null, null, set, null);
//		
//		controller.setLevel(level);
//		controller.setRootNode(rn);
//        controller.updateEntities(0.5f);
//        
//        verify(eMock, times(0)).update(0.5f);
//        
//        verify(rn, times(1)).detachChild(geom); 
//        assertFalse(set.contains(eMock));
//	}
//	
//	/**
//	 * Test if an ALIVE entity is updated correctly.
//	 */
//	@Test
//	public void testSimpleUpdateEntityALIVE() {
//		Set<Entity> set = new HashSet<Entity>();
//		Entity eMock = mock(Entity.class);
//		
//		when(eMock.getState()).thenReturn(EntityState.ALIVE);
//		
//		set.add(eMock);
//		Level level = new Level(null, null, set, null);
//		
//		controller.setLevel(level);
//        controller.updateEntities(0.5f);
//        
//        verify(eMock, times(1)).update(0.5f);
//	}
//	
//	/**
//	 * Test the setters and getters for the level.
//	 */
//	@Test
//	public void testLevel() {
//		Level level = new Level(null, null, null, null);
//		controller.setLevel(level);
//		assertEquals(level, controller.getLevel());
//	}
//	
//	/**
//	 * Test attaching an unset level to the renderer.
//	 */
//	@Test (expected = IllegalStateException.class)
//	public void testAttachLevelNull() {
//		controller.setLevel(null);
//		controller.attachLevel();
//	}
//	
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
//		Level level = new Level(tiles, player, null, lights);
//		
//		controller.setRootNode(rn);
//		controller.setLevel(level);
//		controller.attachLevel();
//        
//        verify(rn, times(5)).attachChild(geom);
//        verify(rn, times(1)).attachChild(playerGeom);
//        verify(rn, times(2)).addLight(lMock);
//	}
//
//	@Override
//	public Controller getController() {
//		return controller;
//	}
//
//	@Override
//	public Main getMain() {
//		return main;
//	}
//}