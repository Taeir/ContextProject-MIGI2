package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Test class for CorridorType enum.
 */
public class CorridorTypeTest {

	/**
	 * Test getCorridorTypeFromMap Vertical corridor.
	 */
	@Test
	public void testGetCorridorTypeFromMapVertical() {
		MazeTile[][] testMap = createBaseTileTypeMap();
		testMap[0][1] = new MazeTile(0, 0, TileType.CORRIDOR);
		testMap[2][1] = new MazeTile(0, 0, TileType.CORRIDOR);
		assertEquals(CorridorType.VERTICAL, CorridorType.getCorridorTypeFromMap(testMap, 1, 1));	
	}
	
	/**
	 * Test getCorridorTypeFromMap Horizontal corridor.
	 */
	@Test
	public void testGetCorridorTypeFromMapHorizontal() {
		MazeTile[][] testMap = createBaseTileTypeMap();
		testMap[1][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		testMap[1][2] = new MazeTile(0, 0, TileType.CORRIDOR);
		assertEquals(CorridorType.HORIZONTAL, CorridorType.getCorridorTypeFromMap(testMap, 1, 1));	
	}
	
	/**
	 * Test getCorridorTypeFromMap lower right corridor.
	 */
	@Test
	public void testGetCorridorTypeFromMapLowerRight() {
		MazeTile[][] testMap = createBaseTileTypeMap();
		testMap[1][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		testMap[0][1] = new MazeTile(0, 0, TileType.CORRIDOR);
		assertEquals(CorridorType.LOWER_RIGHT, CorridorType.getCorridorTypeFromMap(testMap, 1, 1));	
	}
	
	/**
	 * Test getCorridorTypeFromMap lower right corridor.
	 */
	@Test
	public void testGetCorridorTypeFromMapLowerLeft() {
		MazeTile[][] testMap = createBaseTileTypeMap();
		testMap[0][1] = new MazeTile(0, 0, TileType.CORRIDOR);
		testMap[1][2] = new MazeTile(0, 0, TileType.CORRIDOR);
		assertEquals(CorridorType.LOWER_LEFT, CorridorType.getCorridorTypeFromMap(testMap, 1, 1));	
	}
	
	/**
	 * Test getCorridorTypeFromMap lower right corridor.
	 */
	@Test
	public void testGetCorridorTypeFromMapUpperLeft() {
		MazeTile[][] testMap = createBaseTileTypeMap();
		testMap[2][1] = new MazeTile(0, 0, TileType.CORRIDOR);
		testMap[1][2] = new MazeTile(0, 0, TileType.CORRIDOR);
		assertEquals(CorridorType.UPPER_LEFT, CorridorType.getCorridorTypeFromMap(testMap, 1, 1));	
	}
	
	/**
	 * Test getCorridorTypeFromMap lower right corridor.
	 */
	@Test
	public void testGetCorridorTypeFromMapUpperRight() {
		MazeTile[][] testMap = createBaseTileTypeMap();
		testMap[2][1] = new MazeTile(0, 0, TileType.CORRIDOR);
		testMap[1][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		assertEquals(CorridorType.UPPER_RIGHT, CorridorType.getCorridorTypeFromMap(testMap, 1, 1));	
	}
	
	/**
	 * Test getCorridorTypeFromMap other corridor, big.
	 */
	@Test
	public void testGetCorridorTypeFromMapNonBig() {
		MazeTile[][] testMap = createBaseTileTypeMap();
		assertEquals(CorridorType.NOT_DEFINED, CorridorType.getCorridorTypeFromMap(testMap, 1, 1));	
	}
	
	/**
	 * Test getCorridorTypeFromMap other corridor, big.
	 */
	@Test
	public void testGetCorridorTypeFromMapNonSmall() {
		MazeTile[][] testMap = new MazeTile[1][1];
		testMap[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		assertEquals(CorridorType.NOT_DEFINED, CorridorType.getCorridorTypeFromMap(testMap, 0, 0));	
	}

	/**
	 * Test checkTileType with null.
	 */
	@Test
	public void testCheckTileTypeNull() {
		assertFalse(CorridorType.checkTileType(null));
	}
	
	/**
	 * Test checkTileType with floor tile.
	 */
	@Test
	public void testCheckTileTypeFloor() {
		assertFalse(CorridorType.checkTileType(new MazeTile(0, 0, TileType.FLOOR)));
	}
	
	/**
	 * Test checkTileType with corridor tile.
	 */
	@Test
	public void testCheckTileTypeCorridor() {
		assertTrue(CorridorType.checkTileType(new MazeTile(0, 0, TileType.CORRIDOR)));
	}
	
	/**
	 * Test checkTileType with corridor tile.
	 */
	@Test
	public void testCheckTileTypeDoorEntrance() {
		assertTrue(CorridorType.checkTileType(new MazeTile(0, 0, TileType.DOOR_ENTRANCE)));
	}
	
	/**
	 * Test checkTileType with corridor tile.
	 */
	@Test
	public void testCheckTileTypeDoorExit() {
		assertTrue(CorridorType.checkTileType(new MazeTile(0, 0, TileType.DOOR_EXIT)));
	}
	
	/**
	 * Create a basic TileType map with a corridor.
	 * 
	 * @return
	 * 		a 3 by 3 map which looks like NNN, NCN, NNN where N is null and C a corridor TileType
	 */
	public MazeTile[][] createBaseTileTypeMap() {
		MazeTile[][] map = new MazeTile[3][3];
		map[1][1] = new MazeTile(1, 1, TileType.CORRIDOR);
		return map;
	}

}
