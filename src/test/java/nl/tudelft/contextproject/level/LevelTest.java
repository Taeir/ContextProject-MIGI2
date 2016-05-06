package nl.tudelft.contextproject.level;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.jme3.light.Light;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import nl.tudelft.contextproject.Entity;
import nl.tudelft.contextproject.EntityState;
import nl.tudelft.contextproject.VRPlayer;

public class LevelTest {

	private Light lMock;
	private MazeTile tMock1;
	private MazeTile tMock2;
	private VRPlayer pMock;
	private Entity eMock;
	private Level level;

	@Before
	public void setup() {
		lMock = mock(Light.class);
		tMock1 = mock(MazeTile.class);
		tMock2 = mock(MazeTile.class);
		pMock = mock(VRPlayer.class);
		eMock = mock(Entity.class);
		
		Set<Entity> entities = new HashSet<>();
		entities.add(eMock);
		
		List<Light> lights = new ArrayList<>();
		lights.add(lMock);
		MazeTile[][] tiles = {{tMock1, null, tMock2}, 
							  {tMock2, tMock1, null}};
		level = new Level(tiles, pMock, entities, lights);
	}

	@Test
	public void testGetEntitiesSize() {
		assertEquals(1, level.getEntities().size());
		level.getEntities().add(mock(Entity.class));
		assertEquals(2, level.getEntities().size());
	}
	
	@Test
	public void testGetPlayer() {
		assertEquals(pMock, level.getPlayer());
	}
	
	@Test
	public void testGetWidth() {
		assertEquals(2, level.getWidth());
	}
	
	@Test
	public void testGetHeight() {
		assertEquals(3, level.getHeight());
	}
	
	@Test
	public void testIsTileAtPosition() {
		assertTrue(level.isTileAtPosition(0, 0));
		assertEquals(tMock1, level.getTile(0, 0));
	}

	@Test
	public void testIsTileAtPosition2() {
		assertTrue(level.isTileAtPosition(0, 2));
		assertEquals(tMock2, level.getTile(0, 2));
	}

	@Test
	public void testIsTileNotAtPosition() {
		assertFalse(level.isTileAtPosition(1, 2));
		assertEquals(null, level.getTile(1, 2));
	}
	
	@Test
	public void testGetLights() {
		assertEquals(1, level.getLights().size());
		assertTrue(level.getLights().contains(lMock));
	}

}
