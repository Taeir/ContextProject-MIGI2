package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Test class for corridor beautification.
 */
public class CorridorBeautificationTest {

	
	/**
	 * Test validLocation with an x that is too small.
	 */
	@Test
	public void testValidLocationXInvalidTooSmall() {
		MazeTile[][] testMap = new MazeTile[1][1];
		assertFalse(CorridorBeautification.validLocation(testMap, -1, 0));
	}
	
	/**
	 * Test validLocation with an x that is too large.
	 */
	@Test
	public void testValidLocationXInvalidToolarge() {
		MazeTile[][] testMap = new MazeTile[1][1];
		assertFalse(CorridorBeautification.validLocation(testMap, 1, 0));
	}
	
	/**
	 * Test validLocation with an y that is too small.
	 */
	@Test
	public void testValidLocationYInvalidTooSmall() {
		MazeTile[][] testMap = new MazeTile[1][1];
		assertFalse(CorridorBeautification.validLocation(testMap, 0, -1));
	}
	
	/**
	 * Test validLocation with an y that is too large.
	 */
	@Test
	public void testValidLocationYInvalidTooLarge() {
		MazeTile[][] testMap = new MazeTile[1][1];
		assertFalse(CorridorBeautification.validLocation(testMap, 0, 1));
	}
	
	/**
	 * Test validLocation with valid location.
	 */
	@Test
	public void testValidLocationValid() {
		MazeTile[][] testMap = new MazeTile[1][1];
		assertTrue(CorridorBeautification.validLocation(testMap, 0, 0));
	}
	
	/**
	 * Test placeCorridor with incorrect location.
	 */
	@Test
	public void testPlaceCorridorIncorrectLocation() {
		MazeTile[][] testMap = new MazeTile[1][1];
		CorridorBeautification.placeCorridor(testMap, new Vec2I(-1, 0));
		assertNull(testMap[0][0]);
	}
	
	/**
	 * Test placeCorridor with not a null tile.
	 */
	@Test
	public void testPlaceCorridorNotNull() {
		MazeTile[][] testMap = new MazeTile[1][1];
		testMap[0][0] = new MazeTile(0, 0, TileType.FLOOR);
		CorridorBeautification.placeCorridor(testMap, new Vec2I(0, 0));
		assertEquals(TileType.FLOOR, testMap[0][0].getTileType());
	}
	
	/**
	 * Test placeCorridor with correct placement.
	 */
	@Test
	public void testPlaceCorridorCorrectPlacement() {
		MazeTile[][] testMap = new MazeTile[1][1];
		CorridorBeautification.placeCorridor(testMap, new Vec2I(0, 0));
		assertEquals(TileType.CORRIDOR, testMap[0][0].getTileType());
	}
	
	/**
	 * Test if a corridor North of a tile is carved correctly.
	 */
	@Test
	public void testCarveCorridorNorthCorrect() {
		MazeTile[][] testMap = new MazeTile[1][2];
		testMap[0][1] = new MazeTile(0, 1, TileType.CORRIDOR);
		CorridorBeautification.carveCorridorWalls(testMap);
		assertEquals(TileType.WALL, testMap[0][0].getTileType());
	}
	
	/**
	 * Test if a corridor North of a tile is not carved if not possible.
	 */
	@Test
	public void testCarveCorridorNorthAlreadyFilled() {
		MazeTile[][] testMap = new MazeTile[1][2];
		testMap[0][0] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[0][1] = new MazeTile(0, 1, TileType.CORRIDOR);
		CorridorBeautification.carveCorridorWalls(testMap);
		assertEquals(TileType.FLOOR, testMap[0][0].getTileType());
	}
	
	/**
	 * Test if a corridor North of a tile is carved correctly.
	 */
	@Test
	public void testCarveCorridorSouthCorrect() {
		MazeTile[][] testMap = new MazeTile[1][2];
		testMap[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		CorridorBeautification.carveCorridorWalls(testMap);
		assertEquals(TileType.WALL, testMap[0][1].getTileType());
	}
	
	/**
	 * Test if a corridor South of a tile is not carved if not possible.
	 */
	@Test
	public void testCarveCorridorSouthAlreadyFilled() {
		MazeTile[][] testMap = new MazeTile[1][2];
		testMap[0][1] = new MazeTile(0, 1, TileType.FLOOR);
		testMap[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		CorridorBeautification.carveCorridorWalls(testMap);
		assertEquals(TileType.FLOOR, testMap[0][1].getTileType());
	}
	
	/**
	 * Test if a corridor West of a tile is carved correctly.
	 */
	@Test
	public void testCarveCorridorWestCorrect() {
		MazeTile[][] testMap = new MazeTile[2][1];
		testMap[1][0] = new MazeTile(1, 0, TileType.CORRIDOR);
		CorridorBeautification.carveCorridorWalls(testMap);
		assertEquals(TileType.WALL, testMap[0][0].getTileType());
	}
	
	/**
	 * Test if a corridor West of a tile is not carved if not possible.
	 */
	@Test
	public void testCarveCorridorWestAlreadyFilled() {
		MazeTile[][] testMap = new MazeTile[2][1];
		testMap[0][0] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[1][0] = new MazeTile(1, 0, TileType.CORRIDOR);
		CorridorBeautification.carveCorridorWalls(testMap);
		assertEquals(TileType.FLOOR, testMap[0][0].getTileType());
	}
	
	/**
	 * Test if a corridor East of a tile is carved correctly.
	 */
	@Test
	public void testCarveCorridorEastCorrect() {
		MazeTile[][] testMap = new MazeTile[2][1];
		testMap[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		CorridorBeautification.carveCorridorWalls(testMap);
		assertEquals(TileType.WALL, testMap[1][0].getTileType());
	}
	
	/**
	 * Test if a corridor East of a tile is not carved if not possible.
	 */
	@Test
	public void testCarveCorridorEastAlreadyFilled() {
		MazeTile[][] testMap = new MazeTile[2][1];
		testMap[1][0] = new MazeTile(1, 0, TileType.FLOOR);
		testMap[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		CorridorBeautification.carveCorridorWalls(testMap);
		assertEquals(TileType.FLOOR, testMap[1][0].getTileType());
	}
	
	/**
	 * Test if corridor carving does nothing if corridor is one edge of the map.
	 */
	@Test
	public void testCarveCorridorAllDirectionsNotPossible() {
		MazeTile[][] testMap1 = new MazeTile[1][1];
		MazeTile[][] testMap2 = new MazeTile[1][1];
		testMap1[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		testMap2[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		CorridorBeautification.carveCorridorWalls(testMap1);
		assertTrue(equalTileTypeMap(testMap2, testMap1));
	}
	
	/**
	 * Test if carving happens in all directions at the same time.
	 * Note that this shouldn't happen in game as it will close in the player, however
	 * this method should still make this possible.
	 */
	@Test
	public void testCarveCorridorAllDirections() {
		MazeTile[][] testMap1 = createBaseTileTypeMap();
		MazeTile[][] testMap2 = createBaseTileTypeMap();
		CorridorBeautification.carveCorridorWalls(testMap1);
		testMap2[0][1] = new MazeTile(0, 1, TileType.WALL);
		testMap2[1][0] = new MazeTile(1, 0, TileType.WALL);
		testMap2[1][2] = new MazeTile(1, 2, TileType.WALL);
		testMap2[2][1] = new MazeTile(2, 1, TileType.WALL);
		assertTrue(equalTileTypeMap(testMap2, testMap1));
	}
	
	
	/**
	 * Test simpleCorridorWidening in all directions.
	 */
	@Test
	public void testSimpleCorridorWideningAllDirections() {
		MazeTile[][] testMap1 = createBaseTileTypeMap();
		MazeTile[][] testMap2 = createBaseTileTypeMap();
		CorridorBeautification.simpleCorridorWidening(testMap1);
		testMap2[0][1] = new MazeTile(0, 1, TileType.CORRIDOR);
		testMap2[1][0] = new MazeTile(1, 0, TileType.CORRIDOR);
		testMap2[1][2] = new MazeTile(1, 2, TileType.CORRIDOR);
		testMap2[2][1] = new MazeTile(2, 1, TileType.CORRIDOR);
		assertTrue(equalTileTypeMap(testMap2, testMap1));
	}
	
	/**
	 * Test if a corridor North of a tile is carved correctly.
	 */
	@Test
	public void testSimpleCorridorWideningNorthCorrect() {
		MazeTile[][] testMap = new MazeTile[2][2];
		testMap[0][1] = new MazeTile(0, 1, TileType.CORRIDOR);
		CorridorBeautification.simpleCorridorWidening(testMap);
		assertEquals(TileType.CORRIDOR, testMap[0][0].getTileType());
	}
	
	/**
	 * Test if a corridor North of a tile is not carved if not possible.
	 */
	@Test
	public void testSimpleCorridorWideningNorthAlreadyFilled() {
		MazeTile[][] testMap = new MazeTile[2][2];
		testMap[0][0] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[0][1] = new MazeTile(0, 1, TileType.CORRIDOR);
		CorridorBeautification.carveCorridorWalls(testMap);
		assertEquals(TileType.FLOOR, testMap[0][0].getTileType());
	}
	
	/**
	 * Test if a corridor North of a tile is carved correctly.
	 */
	@Test
	public void testSimpleCorridorWideningSouthCorrect() {
		MazeTile[][] testMap = new MazeTile[2][2];
		testMap[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		CorridorBeautification.simpleCorridorWidening(testMap);
		assertEquals(TileType.CORRIDOR, testMap[0][1].getTileType());
	}
	
	/**
	 * Test if a corridor South of a tile is not carved if not possible.
	 */
	@Test
	public void testSimpleCorridorWideningSouthAlreadyFilled() {
		MazeTile[][] testMap = new MazeTile[2][2];
		testMap[0][1] = new MazeTile(0, 1, TileType.FLOOR);
		testMap[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		CorridorBeautification.simpleCorridorWidening(testMap);
		assertEquals(TileType.FLOOR, testMap[0][1].getTileType());
	}
	
	/**
	 * Test if a corridor West of a tile is carved correctly.
	 */
	@Test
	public void testSimpleCorridorWideningWestCorrect() {
		MazeTile[][] testMap = new MazeTile[2][2];
		testMap[1][0] = new MazeTile(1, 0, TileType.CORRIDOR);
		CorridorBeautification.simpleCorridorWidening(testMap);
		assertEquals(TileType.CORRIDOR, testMap[0][0].getTileType());
	}
	
	/**
	 * Test if a corridor West of a tile is not carved if not possible.
	 */
	@Test
	public void testSimpleCorridorWideningAlreadyFilled() {
		MazeTile[][] testMap = new MazeTile[2][2];
		testMap[0][0] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[1][0] = new MazeTile(1, 0, TileType.CORRIDOR);
		CorridorBeautification.simpleCorridorWidening(testMap);
		assertEquals(TileType.FLOOR, testMap[0][0].getTileType());
	}
	
	/**
	 * Test if a corridor East of a tile is carved correctly.
	 */
	@Test
	public void testSimpleCorridorWideningEastCorrect() {
		MazeTile[][] testMap = new MazeTile[2][2];
		testMap[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		CorridorBeautification.simpleCorridorWidening(testMap);
		assertEquals(TileType.CORRIDOR, testMap[1][0].getTileType());
	}
	
	/**
	 * Test if a corridor East of a tile is not carved if not possible.
	 */
	@Test
	public void testSimpleCorridorWideningEastAlreadyFilled() {
		MazeTile[][] testMap = new MazeTile[2][2];
		testMap[1][0] = new MazeTile(1, 0, TileType.FLOOR);
		testMap[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		CorridorBeautification.simpleCorridorWidening(testMap);
		assertEquals(TileType.FLOOR, testMap[1][0].getTileType());
	}
	
	/**
	 * Test if corridor carving does nothing if corridor is one edge of the map.
	 */
	@Test
	public void testSimpleCorridorWideningAllDirectionsNotPossible() {
		MazeTile[][] testMap1 = new MazeTile[1][1];
		MazeTile[][] testMap2 = new MazeTile[1][1];
		testMap1[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		testMap2[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		CorridorBeautification.simpleCorridorWidening(testMap1);
		assertTrue(equalTileTypeMap(testMap2, testMap1));
	}
	
	/**
	 * Test if two equaltileTypesMap are the same.
	 * Does not check for null or maps of size zero.
	 * 
	 * @param map1
	 * 		how the map should look like
	 * @param map2
	 * 		the map that is checked
	 * @return
	 * 		boolean true if map1 == map2
	 */
	public boolean equalTileTypeMap(MazeTile[][] map1, MazeTile[][] map2) {
		int width = map1.length;
		int height = map1[0].length;
		if (width != map2.length || height != map2[0].length) {
			return false;
		}
		MazeTile mazeTile1, mazeTile2;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				mazeTile1 = map1[i][j];
				mazeTile2 = map2[i][j];
				if (mazeTile1 == null && mazeTile2 == null) {
					continue;
				}
				if (mazeTile1 != null && map2[i][j] == null
					|| map1[i][j] == null && map2[i][j] != null
					&& map1[i][j].getTileType() != map2[i][j].getTileType()) {
					return false;
				}
			}
		}

		return true;
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
