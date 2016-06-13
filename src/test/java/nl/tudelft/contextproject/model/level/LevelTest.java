package nl.tudelft.contextproject.model.level;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jme3.light.Light;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.entities.Entity;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Level.
 */
public class LevelTest extends TestBase {

	private Light lightMock;
	private MazeTile tileMock1;
	private MazeTile tileMock2;
	private Entity entityMock;
	
	private Level level;

	/**
	 * Setup method.
	 * Creates fresh mocks and levels for each test.
	 */
	@Before
	public void setUp() {
		lightMock = mock(Light.class);
		tileMock1 = mock(MazeTile.class);
		tileMock2 = mock(MazeTile.class);
		entityMock = mock(Entity.class);

		TileType type = TileType.FLOOR;
		when(tileMock1.getTileType()).thenReturn(type);
		when(tileMock1.isExplored()).thenReturn(true);
		
		type = TileType.WALL;
		when(tileMock2.getTileType()).thenReturn(type);
		
		MazeTile[][] tiles = {{tileMock1, null, tileMock2},
							  {tileMock2, tileMock1, null}};
		
		List<Light> lights = new ArrayList<>();
		lights.add(lightMock);
		
		Set<Entity> entities = new HashSet<>();
		entities.add(entityMock);
		
		level = new Level(tiles,  lights, entities, new Vector3f(1, 1, 1));
	}

	/**
	 * Test for the width calculation of the level.
	 */
	@Test
	public void testGetWidth() {
		assertEquals(2, level.getWidth());
	}

	/**
	 * Test for the height calculation of the level.
	 */
	@Test
	public void testGetHeight() {
		assertEquals(3, level.getHeight());
	}

	/**
	 * Test for existing tile in the maze.
	 */
	@Test
	public void testIsTileAtPosition() {
		assertTrue(level.isTileAtPosition(0, 0));
		assertEquals(tileMock1, level.getTile(0, 0));
	}

	/**
	 * Test for correct coordinate order in getting a tile.
	 */
	@Test
	public void testIsTileAtPosition2() {
		assertTrue(level.isTileAtPosition(0, 2));
		assertEquals(tileMock2, level.getTile(0, 2));
	}

	/**
	 * Test existence on an empty location.
	 */
	@Test
	public void testIsTileNotAtPosition() {
		assertFalse(level.isTileAtPosition(1, 2));
		assertEquals(null, level.getTile(1, 2));
	}

	/**
	 * Test the getter for the lights in the level.
	 */
	@Test
	public void testGetLights() {
		assertEquals(1, level.getLights().size());
		assertTrue(level.getLights().contains(lightMock));
	}

	/**
	 * Test the getter for the entities in the level.
	 */
	@Test
	public void testGetEntities() {
		assertEquals(1, level.getEntities().size());
		assertTrue(level.getEntities().contains(entityMock));
	}
	
	/**
	 * Test the getter for the player spawn position in the level.
	 */
	@Test
	public void testGetPlayerSpawnPosition() {
		assertEquals(new Vector3f(1, 1, 1), level.getPlayerSpawnPosition());
	}
	
	/**
	 * Test if {@link Level#toWebJSON()} works properly.
	 */
	@Test
	public void testToWebJSON() {
		JSONObject json = level.toWebJSON();

		assertEquals(2, json.get("width"));
		assertEquals(3, json.get("height"));

		JSONObject tiles = json.getJSONObject("tiles");
		assertEquals(2, tiles.length());
		assertEquals(3, tiles.getJSONArray("0").length());
	}
	
	/**
	 * Test if {@link Level#toExploredWebJSON()} works properly.
	 */
	@Test
	public void testToExploredWebJSON() {
		JSONObject json = level.toExploredWebJSON();
		assertEquals(0, json.getJSONArray("0").get(0));
		assertEquals(1, json.getJSONArray("1").get(0));
	}
}
