package nl.tudelft.contextproject.model.level;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.util.Size;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Class for testing the RandomLevelFactory.
 */
@SuppressWarnings("deprecation")
public class RandomLevelFactoryTest extends TestBase {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private RandomLevelFactory factory;

	/**
	 * Create the factory used for all the testing and set the seed
	 * to 0.
	 * Also creates a 3 by 3 TileType map.
	 */
	@Before
	public void setUp() {
		factory = new RandomLevelFactory(1, false);
		factory.createRNG(0);
	}


	/**
	 * Test to check you can't spawn less than 1 room.
	 */
	@Test
	public void testInvalidAmount() {
		thrown.expect(IllegalArgumentException.class);
		new RandomLevelFactory(-3, false);
	}

	/**
	 * Test to check the loading of rooms.
	 */
	@Test
	public void testLoadRooms() {
		assertNotNull(factory.loadRooms().get(0) != null);
	}

	/**
	 * Test for carveRooms.
	 */
	@Test
	public void testCarveRooms() {
		ArrayList<GeneratorRoom> rooms = new ArrayList<>();
		rooms.add(new GeneratorRoom(0, 0, new Size(3, 3)));
		assertSame(TileType.WALL, factory.carveRooms(rooms, 3, 3)[0][0]);
		assertSame(TileType.FLOOR, factory.carveRooms(rooms, 3, 3)[1][1]);
	}

	/**
	 * Test for carveCorridors given a list of one element.
	 */
	@Test
	public void testCarveCorridorsOne() {
		TileType[][] expected = new TileType[0][0];
		ArrayList<GeneratorRoom> rooms = new ArrayList<>();
		rooms.add(new GeneratorRoom(0, 0, new Size(3, 3)));
		assertSame(expected, factory.carveCorridors(expected, rooms));
	}

	/**
	 * Test for carveCorridors given a list of two elements.
	 */
	@Test
	public void testCarveCorridorsTwoFirstOption() {
		ArrayList<GeneratorRoom> rooms = new ArrayList<>();
		TileType[][] map = new TileType[2][2];
		map[0][0] = TileType.WALL;
		map[1][1] = TileType.WALL;
		rooms.add(new GeneratorRoom(0, 0, new Size(1, 1)));
		rooms.add(new GeneratorRoom(1, 1, new Size(1, 1)));
		assertSame(TileType.CORRIDOR, factory.carveCorridors(map, rooms)[1][0]);
	}

	/**
	 * Test for carveCorridors given a list of two elements.
	 */
	@Test
	public void testCarveCorridorsTwoSecondOption() {
		ArrayList<GeneratorRoom> rooms = new ArrayList<>();
		TileType[][] map = new TileType[2][2];
		map[0][0] = TileType.WALL;
		map[1][1] = TileType.WALL;
		rooms.add(new GeneratorRoom(0, 0, new Size(1, 1)));
		rooms.add(new GeneratorRoom(1, 1, new Size(1, 1)));

		/**
		 * Get two random numbers to further the RNG.
		 */
		factory.getRandom(0, 5);
		factory.getRandom(0, 5);

		assertSame(TileType.CORRIDOR, factory.carveCorridors(map, rooms)[0][1]);
	}

	/**
	 * Test for getting a random size from a list of sizes.
	 */
	@Test
	public void testGetRandomSizeAllowDuplicates() {
		ArrayList<Size> sizes = new ArrayList<>();
		sizes.add(new Size(0, 0));
		Size selected = factory.getRandomSize(sizes, false);
		assertSame(selected.getWidth(), 0);
		assertSame(selected.getHeight(), 0);
		assertSame(sizes.size(), 0);
	}

	/**
	 * Test for getting a random size from a list of sizes.
	 */
	@Test
	public void testGetRandomSizeDenyDuplicates() {
		ArrayList<Size> sizes = new ArrayList<>();
		sizes.add(new Size(0, 0));
		Size selected = factory.getRandomSize(sizes, true);
		assertSame(selected.getWidth(), 0);
		assertSame(selected.getHeight(), 0);
		assertSame(sizes.size(), 1);
	}

	/**
	 * Test for create with invalid parameters.
	 */
	@Test
	public void testCreateIncorrect() {
		thrown.expect(IllegalArgumentException.class);
		RandomLevelFactory invalid = new RandomLevelFactory(Integer.MAX_VALUE, false);
		invalid.create();
	}

	/**
	 * Test for create with valid parameters.
	 * As the static final parameters will probably change during development it is
	 * pointless to check the entire creation process at this time.
	 * Just checking it goes right as enough.
	 */
	@Test
	public void testCreateCorrect() {
		assertSame(factory.create().size(), 1);
	}

	/**
	 * Test for creating an entire level.
	 * As the static final parameters will probably change during development it is
	 * pointless to check the entire creation process at this time.
	 * Just checking it goes right as enough.
	 */
	@Test
	public void testGenerateSeeded() {
		assertNotNull(factory.generateSeeded(0));
	}

	/**
	 * Test if a corridor North of a tile is carved correctly.
	 */
	@Test
	public void testCarveCorridorNorthCorrect() {
		TileType[][] testMap = new TileType[1][2];
		testMap[0][1] = TileType.CORRIDOR;
		RandomLevelFactory.carveCorridorWalls(testMap);
		assertEquals(TileType.WALL, testMap[0][0]);
	}
	
	/**
	 * Test if a corridor North of a tile is not carved if not possible.
	 */
	@Test
	public void testCarveCorridorNorthAlreadyFilled() {
		TileType[][] testMap = new TileType[1][2];
		testMap[0][0] = TileType.FLOOR;
		testMap[0][1] = TileType.CORRIDOR;
		RandomLevelFactory.carveCorridorWalls(testMap);
		assertEquals(TileType.FLOOR, testMap[0][0]);
	}
	
	/**
	 * Test if a corridor North of a tile is carved correctly.
	 */
	@Test
	public void testCarveCorridorSouthCorrect() {
		TileType[][] testMap = new TileType[1][2];
		testMap[0][0] = TileType.CORRIDOR;
		RandomLevelFactory.carveCorridorWalls(testMap);
		assertEquals(TileType.WALL, testMap[0][1]);
	}
	
	/**
	 * Test if a corridor South of a tile is not carved if not possible.
	 */
	@Test
	public void testCarveCorridorSouthAlreadyFilled() {
		TileType[][] testMap = new TileType[1][2];
		testMap[0][1] = TileType.FLOOR;
		testMap[0][0] = TileType.CORRIDOR;
		RandomLevelFactory.carveCorridorWalls(testMap);
		assertEquals(TileType.FLOOR, testMap[0][1]);
	}
	
	/**
	 * Test if a corridor West of a tile is carved correctly.
	 */
	@Test
	public void testCarveCorridorWestCorrect() {
		TileType[][] testMap = new TileType[2][1];
		testMap[1][0] = TileType.CORRIDOR;
		RandomLevelFactory.carveCorridorWalls(testMap);
		assertEquals(TileType.WALL, testMap[0][0]);
	}
	
	/**
	 * Test if a corridor West of a tile is not carved if not possible.
	 */
	@Test
	public void testCarveCorridorWestAlreadyFilled() {
		TileType[][] testMap = new TileType[2][1];
		testMap[0][0] = TileType.FLOOR;
		testMap[1][0] = TileType.CORRIDOR;
		RandomLevelFactory.carveCorridorWalls(testMap);
		assertEquals(TileType.FLOOR, testMap[0][0]);
	}
	
	/**
	 * Test if a corridor East of a tile is carved correctly.
	 */
	@Test
	public void testCarveCorridorEastCorrect() {
		TileType[][] testMap = new TileType[2][1];
		testMap[0][0] = TileType.CORRIDOR;
		RandomLevelFactory.carveCorridorWalls(testMap);
		assertEquals(TileType.WALL, testMap[1][0]);
	}
	
	/**
	 * Test if a corridor East of a tile is not carved if not possible.
	 */
	@Test
	public void testCarveCorridorEastAlreadyFilled() {
		TileType[][] testMap = new TileType[2][1];
		testMap[1][0] = TileType.FLOOR;
		testMap[0][0] = TileType.CORRIDOR;
		RandomLevelFactory.carveCorridorWalls(testMap);
		assertEquals(TileType.FLOOR, testMap[1][0]);
	}
	
	/**
	 * Test if corridor carving does nothing if corridor is one edge of the map.
	 */
	@Test
	public void testCarveCorridorAllDirectionsNotPossible() {
		TileType[][] testMap1 = new TileType[1][1];
		TileType[][] testMap2 = new TileType[1][1];
		testMap1[0][0] = TileType.CORRIDOR;
		testMap2[0][0] = TileType.CORRIDOR;
		RandomLevelFactory.carveCorridorWalls(testMap1);
		assertTrue(equalTileTypeMap(testMap2, testMap1));
	}
	
	/**
	 * Test if carving happens in all directions at the same time.
	 * Note that this shouldn't happen in game as it will close in the player, however
	 * this method should still make this possible.
	 */
	@Test
	public void testCarveCorridorAllDirections() {
		TileType[][] testMap1 = createBaseTileTypeMap();
		TileType[][] testMap2 = createBaseTileTypeMap();
		RandomLevelFactory.carveCorridorWalls(testMap1);
		testMap2[0][1] = TileType.WALL;
		testMap2[1][0] = TileType.WALL;
		testMap2[1][2] = TileType.WALL;
		testMap2[2][1] = TileType.WALL;
		assertTrue(equalTileTypeMap(testMap2, testMap1));
	}

	/**
	 * Test if two equaltileTypesMap are the same.
	 * Does not check for null or maps of size zero.
	 * @param map1
	 * 			how the map should look like
	 * @param map2
	 * 			the map that is checked
	 * @return
	 * 			boolean true if map1 == map2
	 */
	public boolean equalTileTypeMap(TileType[][] map1, TileType[][] map2) {
		int width = map1.length;
		int height = map1[0].length;
		if (width != map2.length || height != map2[0].length) {
			return false;
		}

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (map1[i][j] != map2[i][j]) {
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
	public TileType[][] createBaseTileTypeMap() {
		TileType[][] map = new TileType[3][3];
		map[1][1] = TileType.CORRIDOR;
		return map;
	}

}