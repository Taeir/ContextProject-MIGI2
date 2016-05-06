package nl.tudelft.contextproject;

import org.junit.Before;
import org.junit.Test;

import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import nl.tudelft.contextproject.level.Level;
import nl.tudelft.contextproject.level.MazeTile;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainTest {
	private Main mockedMain;
	
	@Before
	public void setup() {
		mockedMain = mock(Main.class);
		Main.setInstance(mockedMain);
	}
	
	@Test
	public void testMain() {
	    Main.main(new String[0]);
        verify(mockedMain, times(1)).start();
	}
	
	@Test
	public void testSimpleUpdatePlayer() {
		VRPlayer player = mock(VRPlayer.class);
		Level level = new Level(null, player, new HashSet<Entity>(), null);
		Main.setInstance(null);		// remove mocked main
		Main.getInstance().setLevel(level);
        Main.getInstance().simpleUpdate(0.5f);
        verify(player, times(1)).simpleUpdate(0.5f);
	}
	
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
		
		
		Main.setInstance(new Main());
		Main.getInstance().setLevel(level);
		Main.getInstance().setRootNode(rn);
        Main.getInstance().updateEntities(0.5f);
        
        verify(eMock, times(1)).simpleUpdate(0.5f);
        verify(eMock, times(1)).setState(EntityState.ALIVE);
        
        verify(rn, times(1)).attachChild(geom);        
	}
	
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
		
		
		Main.setInstance(new Main());
		Main.getInstance().setLevel(level);
		Main.getInstance().setRootNode(rn);
        Main.getInstance().updateEntities(0.5f);
        
        verify(eMock, times(0)).simpleUpdate(0.5f);
        
        verify(rn, times(1)).detachChild(geom); 
        assertEquals(false, set.contains(eMock));
	}
	
	@Test
	public void testSimpleUpdateEntityALIVE() {
		Set<Entity> set = new HashSet<Entity>();
		Entity eMock = mock(Entity.class);
		
		when(eMock.getState()).thenReturn(EntityState.ALIVE);
		
		set.add(eMock);
		Level level = new Level(null, null, set, null);
		
		
		Main.setInstance(new Main());
		Main.getInstance().setLevel(level);
        Main.getInstance().updateEntities(0.5f);
        
        verify(eMock, times(1)).simpleUpdate(0.5f);
	}
	
	@Test
	public void testLevel() {
		Level level = new Level(null, null, null, null);
		Main.setInstance(null);
		Main.getInstance().setLevel(level);
		assertEquals(level, Main.getInstance().getLevel());
	}
	
	@Test
	public void testClearLevel() {
		Node rn = mock(Node.class);
		Light lMock = mock(Light.class);
		
		LightList list = new LightList(rn);
		list.add(lMock);
		when(rn.getLocalLightList()).thenReturn(list);
		
		
		Main.setInstance(new Main());
		Main.getInstance().setRootNode(rn);
		
        Main.getInstance().clearLevel();
        
        verify(rn, times(1)).detachAllChildren();
        verify(rn, times(1)).removeLight(lMock);
	}
	
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
		MazeTile[][] tiles = {{t, null, t}, {t, t, t}};
		Level level = new Level(tiles, player, null, lights);
		
		Main.setInstance(null);
		Main.getInstance().setRootNode(rn);
		Main.getInstance().setLevel(level);
		Main.getInstance().attachLevel();
        
        verify(rn, times(5)).attachChild(geom);
        verify(rn, times(1)).attachChild(playerGeom);
        verify(rn, times(1)).addLight(lMock);
	}



}