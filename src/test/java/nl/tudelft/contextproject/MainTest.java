package nl.tudelft.contextproject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import nl.tudelft.contextproject.level.Level;
import nl.tudelft.contextproject.level.MazeTile;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Main class.
 */
public class MainTest {
	
	/**
	 * Setup method for this test suit.
	 * Sets the instance to a fresh instance.
	 */
	@Before
	public void setup() {
		Main.setInstance(null);
	}
	
	/**
	 * Check if start is called when starting the game.
	 */
	@Test
	public void testMain() {
		Main mMock = mock(Main.class);
		Main.setInstance(mMock);
	    Main.main(new String[0]);
        verify(mMock, times(1)).start();
	}
	
	/**
	 * Test the simpleUpdate on the player.
	 */
	@Test
	public void testSimpleUpdatePlayer() {
		VRPlayer player = mock(VRPlayer.class);
		Level level = new Level(null, player, new HashSet<Entity>(), null);
		Main.getInstance().setLevel(level);
        Main.getInstance().simpleUpdate(0.5f);
        verify(player, times(1)).simpleUpdate(0.5f);
	}
	
	/**
	 * Test if a NEW entity is updated correctly.
	 */
	@Test
	public void testSimpleUpdateEntityNEW() {
		Set<Entity> set = new HashSet<Entity>();
		Entity eMock = mock(Entity.class);
		Node rn = mock(Node.class);
		Geometry geom = mock(Geometry.class);
		
		when(eMock.getState()).thenReturn(EntityState.NEW);
		when(eMock.getGeometry()).thenReturn(geom);
		
		set.add(eMock);
		Level level = new Level(null, null, set, null);
		
		Main.getInstance().setLevel(level);
		Main.getInstance().setRootNode(rn);
        Main.getInstance().updateEntities(0.5f);
        
        verify(eMock, times(1)).simpleUpdate(0.5f);
        verify(eMock, times(1)).setState(EntityState.ALIVE);
        
        verify(rn, times(1)).attachChild(geom);        
	}
	
	/**
	 * Test if a DEAD entity is removed correctly.
	 */
	@Test
	public void testSimpleUpdateEntityDEAD() {
		Set<Entity> set = new HashSet<Entity>();
		Entity eMock = mock(Entity.class);
		Node rn = mock(Node.class);
		Geometry geom = mock(Geometry.class);
		
		when(eMock.getState()).thenReturn(EntityState.DEAD);
		when(eMock.getGeometry()).thenReturn(geom);
		
		set.add(eMock);
		Level level = new Level(null, null, set, null);
		
		Main.getInstance().setLevel(level);
		Main.getInstance().setRootNode(rn);
        Main.getInstance().updateEntities(0.5f);
        
        verify(eMock, times(0)).simpleUpdate(0.5f);
        
        verify(rn, times(1)).detachChild(geom); 
        assertEquals(false, set.contains(eMock));
	}
	
	/**
	 * Test if an ALIVE entity is updated correctly.
	 */
	@Test
	public void testSimpleUpdateEntityALIVE() {
		Set<Entity> set = new HashSet<Entity>();
		Entity eMock = mock(Entity.class);
		
		when(eMock.getState()).thenReturn(EntityState.ALIVE);
		
		set.add(eMock);
		Level level = new Level(null, null, set, null);
		
		Main.getInstance().setLevel(level);
        Main.getInstance().updateEntities(0.5f);
        
        verify(eMock, times(1)).simpleUpdate(0.5f);
	}
	
	/**
	 * Test the setters and getters for the level.
	 */
	@Test
	public void testLevel() {
		Level level = new Level(null, null, null, null);
		Main.getInstance().setLevel(level);
		assertEquals(level, Main.getInstance().getLevel());
	}
	
	/**
	 * Test if clearing a level clears correctly.
	 */
	@Test
	public void testClearLevel() {
		Node rn = mock(Node.class);
		Light lMock = mock(Light.class);
		
		LightList list = new LightList(rn);
		list.add(lMock);
		when(rn.getLocalLightList()).thenReturn(list);
		
		Main.getInstance().setRootNode(rn);		
        Main.getInstance().clearLevel();
        
        verify(rn, times(1)).detachAllChildren();
        verify(rn, times(1)).removeLight(lMock);
	}
	
	/**
	 * Test attaching an unset level to the renderer.
	 */
	@Test (expected = IllegalStateException.class)
	public void testAttachLevelNull() {
		Main.getInstance().setLevel(null);
		Main.getInstance().attachLevel();
	}
	
	/**
	 * Test if attaching a level correctly adds all elements to the renderer.
	 */
	@Test
	public void testAttachLevel() {
		Node rn = mock(Node.class);
		Light lMock = mock(Light.class);
		MazeTile t = mock(MazeTile.class);
		VRPlayer player = mock(VRPlayer.class);
		Geometry geom = mock(Geometry.class);
		Geometry playerGeom = mock(Geometry.class);
		
		when(t.getGeometry()).thenReturn(geom);
		when(player.getGeometry()).thenReturn(playerGeom);
		
		List<Light> lights = new ArrayList<>();
		lights.add(lMock);
		lights.add(lMock);
		MazeTile[][] tiles = {{t, null, t}, {t, t, t}};
		Level level = new Level(tiles, player, null, lights);
		
		Main.getInstance().setRootNode(rn);
		Main.getInstance().setLevel(level);
		Main.getInstance().attachLevel();
        
        verify(rn, times(5)).attachChild(geom);
        verify(rn, times(1)).attachChild(playerGeom);
        verify(rn, times(2)).addLight(lMock);
	}



}