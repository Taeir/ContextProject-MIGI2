package nl.tudelft.contextproject.model.level;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

/**
 * Test class for the Minimum Spanning Tree Level Factory. 
 */
public class MSTBasedLevelFactoryTest extends TestBase {

	//Location of the test map
	private static final String TEST_MAP_LOCATION = "/maps/testMap2/";
	
	private MSTBasedLevelFactory factoryMST;
	private Room startRoom;
	private Room endRoom;
	private Room room1;
	
	/**
	 * Set up factory.
	 */
	@Before
	public void setUpBeforeTest() {
		factoryMST = new MSTBasedLevelFactory(TEST_MAP_LOCATION);
		startRoom = new Room(TEST_MAP_LOCATION + "startroom/");
		endRoom = new Room(TEST_MAP_LOCATION + "endroom/");
		room1 = new Room(TEST_MAP_LOCATION + "room1/");
	}
	
	/**
	 * Test constructor, initialization of lights array list.
	 */
	@Test
	public void testMSTBasedLevelFactoryConstructorLighting() {
		assertNotNull(factoryMST.lights);
	}

	/**
	 * Test constructor, initialization of MazeTiles array list.
	 */
	@Test
	public void testMSTBasedLevelFactoryConstructorMazeTiles() {
		assertNotNull(factoryMST.mazeTiles);
	}

	/**
	 * Test if a corridor North of a tile is carved correctly.
	 */
	@Test
	public void testCarveCorridorNorthCorrect() {
		MazeTile[][] testMap = new MazeTile[1][2];
		testMap[0][1] = new MazeTile(0, 1, TileType.CORRIDOR);
		MSTBasedLevelFactory.carveCorridorWalls(testMap);
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
		MSTBasedLevelFactory.carveCorridorWalls(testMap);
		assertEquals(TileType.FLOOR, testMap[0][0].getTileType());
	}
	
	/**
	 * Test if a corridor North of a tile is carved correctly.
	 */
	@Test
	public void testCarveCorridorSouthCorrect() {
		MazeTile[][] testMap = new MazeTile[1][2];
		testMap[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		MSTBasedLevelFactory.carveCorridorWalls(testMap);
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
		MSTBasedLevelFactory.carveCorridorWalls(testMap);
		assertEquals(TileType.FLOOR, testMap[0][1].getTileType());
	}
	
	/**
	 * Test if a corridor West of a tile is carved correctly.
	 */
	@Test
	public void testCarveCorridorWestCorrect() {
		MazeTile[][] testMap = new MazeTile[2][1];
		testMap[1][0] = new MazeTile(1, 0, TileType.CORRIDOR);
		MSTBasedLevelFactory.carveCorridorWalls(testMap);
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
		MSTBasedLevelFactory.carveCorridorWalls(testMap);
		assertEquals(TileType.FLOOR, testMap[0][0].getTileType());
	}
	
	/**
	 * Test if a corridor East of a tile is carved correctly.
	 */
	@Test
	public void testCarveCorridorEastCorrect() {
		MazeTile[][] testMap = new MazeTile[2][1];
		testMap[0][0] = new MazeTile(0, 0, TileType.CORRIDOR);
		MSTBasedLevelFactory.carveCorridorWalls(testMap);
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
		MSTBasedLevelFactory.carveCorridorWalls(testMap);
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
		MSTBasedLevelFactory.carveCorridorWalls(testMap1);
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
		MSTBasedLevelFactory.carveCorridorWalls(testMap1);
		testMap2[0][1] = new MazeTile(0, 1, TileType.WALL);
		testMap2[1][0] = new MazeTile(1, 0, TileType.WALL);
		testMap2[1][2] = new MazeTile(1, 2, TileType.WALL);
		testMap2[2][1] = new MazeTile(2, 1, TileType.WALL);
		assertTrue(equalTileTypeMap(testMap2, testMap1));
	}

	/**
	 * Test the map creation with a simple map folder.
	 * There should be 2 chosen edges.
	 */
	@Test
	public void testSmokeTestOfMapCreationEdges() {
		factoryMST.generateSeeded(0);
		assertEquals(2, factoryMST.chosenEdges.size());
	}
	
	/**
	 * Test the map creation with a simple map folder.
	 * There should be 3 room nodes.
	 */
	@Test
	public void testSmokeTestOfMapCreationRoomNodes() {
		factoryMST.generateSeeded(0);
		assertEquals(3, factoryMST.usedNodes.size());
	}
	
	/**
	 * Test the map creation with a simple map folder.
	 * There should be 2 used ExitPoints.
	 */
	@Test
	public void testSmokeTestOfMapCreationExitPoints() {
		factoryMST.generateSeeded(0);
		assertEquals(2, factoryMST.usedExitPoints.size());
	}
	
	/**
	 * Test the map creation with a simple map folder.
	 * There should be 2 used EntryPoints
	 */
	@Test
	public void testSmokeTestOfMapCreationEntryPoints() {
		factoryMST.generateSeeded(0);
		assertEquals(2, factoryMST.usedEntrancePoints.size());
	}

	/**
	 * Test initializeBuilder method, baseNodes not null.
	 */
	@Test
	public void testInitializeBuilderBaseNodes() {
		assertNotNull(factoryMST.baseNodes);
	}
	
	/**
	 * Test initializeBuilder method, useNodes not null.
	 */
	@Test
	public void testInitializeBuilderUsedNodes() {
		assertNotNull(factoryMST.usedNodes);
	}
	
	/**
	 * Test initializeBuilder method, edges not null.
	 */
	@Test
	public void testInitializeBuilderEdges() {
		assertNotNull(factoryMST.edges);
	}
	
	/**
	 * Test initializeBuilder method, UsedEntrancePoints not null.
	 */
	@Test
	public void testInitializeBuilderUsedEntrancePoints() {
		assertNotNull(factoryMST.usedEntrancePoints);
	}

	/**
	 * Test initializeBuilder method, UsedExitPoints not null.
	 */
	@Test
	public void testInitializeBuilderUsedExitPoints() {
		assertNotNull(factoryMST.usedExitPoints);
	}
	
	/**
	 * Test initializeBuilder method, startAndEndRooms not null.
	 */
	@Test
	public void testInitializeBuilderStartAndEndRooms() {
		assertNotNull(factoryMST.startAndEndRooms);
	}
	
	/**
	 * Test initializeBuilder method, start and treasure room.
	 * Check StarterRoom.
	 */
	@Test
	public void testInitializeBuilderStartAndTreasureRoomSetStart() {
		assertEquals(startRoom, factoryMST.startAndEndRooms.getStarterRoom());
	}
	
	/**
	 * Test initializeBuilder method, start and treasure room.
	 * Check TreasureRoom.
	 */
	@Test
	public void testInitializeBuilderStartAndTreasureRoomSetEnd() {
		assertEquals(endRoom, factoryMST.startAndEndRooms.getTreasureRoom());
	}
	
	/**
	 * Test initializeBuilder method, start and treasure room.
	 * Check the extra room, Room1.
	 */
	@Test
	public void testInitializeBuilderStartAndTreasureRoomSetRoom1() {
		assertEquals(room1, factoryMST.baseNodes.get(0).room);
	}
	
	/**
	 * Tests of a factory that gets passed an incorrect map.
	 * Test reading of a incorrect map file (.cmf).
	 * Tests exception of no starterRoom definition.
	 * 
	 * @throws IOException
	 * 		should happen here
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testInitializeBuilderReadMapExceptionNoStartRoom() throws IOException {
		MSTBasedLevelFactory willNotInitialize = new MSTBasedLevelFactory("/maps/incorrectMapFiles/noStartRoom/");
		assertNotNull(willNotInitialize);
	}
	
	/**
	 * Test if random field is set with seed.
	 */
	@Test
	public void testCreateRNG() {
		factoryMST.createRNG(0L);
		assertNotNull(factoryMST.rand);
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
	 * @return
	 * 		a 3 by 3 map which looks like NNN, NCN, NNN where N is null and C a corridor TileType
	 */
	public MazeTile[][] createBaseTileTypeMap() {
		MazeTile[][] map = new MazeTile[3][3];
		map[1][1] = new MazeTile(1, 1, TileType.CORRIDOR);
		return map;
	}
}
