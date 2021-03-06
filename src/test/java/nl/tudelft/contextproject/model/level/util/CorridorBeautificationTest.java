package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import nl.tudelft.contextproject.model.level.MSTBasedLevelFactoryTest;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.util.Vec2I;

/**
 * Test class for corridor beautification.
 */
public class CorridorBeautificationTest {

	/**
	 * Test widenCorridors with a vertical corridor.
	 * Should be null because location is not valid.
	 */
	@Test
	public void testWidenCorridorsVertical() {
		MazeTile[][] testMap = MSTBasedLevelFactoryTest.createBaseTileTypeMap();
		testMap[0][1] = new MazeTile(0, 1, TileType.CORRIDOR);
		testMap[2][1] = new MazeTile(2, 1, TileType.CORRIDOR);
		Random rand = new Random(1L);
		CorridorBeautification.widenCorridors(testMap, rand, new ArrayList<RoomNode>(0));
		assertNull(testMap[1][2]);
	}
	
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
		MazeTile[][] testMap = new MazeTile[3][3];
		assertTrue(CorridorBeautification.validLocation(testMap, 1, 1));
	}
	
	/**
	 * Test placeCorridor with incorrect location.
	 */
	@Test
	public void testPlaceCorridorIncorrectLocation() {
		MazeTile[][] testMap = new MazeTile[1][1];
		CorridorBeautification.placeCorridor(testMap, new Vec2I(-1, 0), new ArrayList<RoomNode>(0));
		assertNull(testMap[0][0]);
	}
	
	/**
	 * Test placeCorridor with not a null tile.
	 */
	@Test
	public void testPlaceCorridorNotNull() {
		MazeTile[][] testMap = new MazeTile[1][1];
		testMap[0][0] = new MazeTile(0, 0, TileType.FLOOR);
		CorridorBeautification.placeCorridor(testMap, new Vec2I(0, 0), new ArrayList<RoomNode>(0));
		assertEquals(TileType.FLOOR, testMap[0][0].getTileType());
	}
	
	/**
	 * Test placeCorridor with correct placement.
	 * Should be null as corridor location is invalid.
	 */
	@Test
	public void testPlaceCorridorCorrectPlacement() {
		MazeTile[][] testMap = new MazeTile[1][1];
		CorridorBeautification.placeCorridor(testMap, new Vec2I(0, 0), new ArrayList<RoomNode>(0));
		assertNull(testMap[0][0]);
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
		assertTrue(MSTBasedLevelFactoryTest.equalTileTypeMap(testMap2, testMap1));
	}
	
	/**
	 * Test if carving happens in all directions at the same time.
	 * Note that this shouldn't happen in game as it will close in the player, however
	 * this method should still make this possible.
	 */
	@Test
	public void testCarveCorridorAllDirections() {
		MazeTile[][] testMap1 = MSTBasedLevelFactoryTest.createBaseTileTypeMap();
		MazeTile[][] testMap2 = MSTBasedLevelFactoryTest.createBaseTileTypeMap();
		CorridorBeautification.carveCorridorWalls(testMap1);
		testMap2[0][1] = new MazeTile(0, 1, TileType.WALL);
		testMap2[1][0] = new MazeTile(1, 0, TileType.WALL);
		testMap2[1][2] = new MazeTile(1, 2, TileType.WALL);
		testMap2[2][1] = new MazeTile(2, 1, TileType.WALL);
		assertTrue(MSTBasedLevelFactoryTest.equalTileTypeMap(testMap2, testMap1));
	}
	
	
	/**
	 * Test simpleCorridorWidening in all directions.
	 */
	@Test
	public void testSimpleCorridorWideningAllDirections() {
		MazeTile[][] testMap1 = MSTBasedLevelFactoryTest.createBaseTileTypeMap();
		MazeTile[][] testMap2 = MSTBasedLevelFactoryTest.createBaseTileTypeMap();
		CorridorBeautification.simpleCorridorWidening(testMap1);
		testMap2[0][1] = new MazeTile(0, 1, TileType.CORRIDOR);
		testMap2[1][0] = new MazeTile(1, 0, TileType.CORRIDOR);
		testMap2[1][2] = new MazeTile(1, 2, TileType.CORRIDOR);
		testMap2[2][1] = new MazeTile(2, 1, TileType.CORRIDOR);
		assertTrue(MSTBasedLevelFactoryTest.equalTileTypeMap(testMap2, testMap1));
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
		assertTrue(MSTBasedLevelFactoryTest.equalTileTypeMap(testMap2, testMap1));
	}
}
