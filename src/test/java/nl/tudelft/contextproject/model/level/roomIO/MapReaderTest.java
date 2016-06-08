package nl.tudelft.contextproject.model.level.roomIO;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.model.level.Room;
import nl.tudelft.contextproject.model.level.RoomTuple;

/**
 * Test class for MapReader.
 */
public class MapReaderTest {

	/**
	 * Location of the test map.
	 */
	private static final String TEST_MAP_LOCATION = "/maps/testGridMap/";

	private ArrayList<Room> rooms;
	
	/**
	 * Set up a room array for mapreader tests.
	 */
	@Before
	public void setUp() {
		rooms = new ArrayList<Room>();
	}
	
	/**
	 * Test reading of a correct map.
	 * Tests if starter room is correct.
	 */
	@Test
	public void testReadMapStartRoomCorrect() {
		try {
			RoomTuple startAndTreasureRoom = MapParser.readMap(TEST_MAP_LOCATION, rooms);
			Room startRoom = new Room(TEST_MAP_LOCATION + "startroom/");
			assertTrue(startAndTreasureRoom.getStarterRoom().equals(startRoom));
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Test reading of a correct map.
	 * Tests if treasureRoom is correct.
	 */
	@Test
	public void testReadMapTreasureRoomCorrect() {
		try {
			RoomTuple startAndTreasureRoom = MapParser.readMap(TEST_MAP_LOCATION, rooms);
			Room treasureRoom = new Room(TEST_MAP_LOCATION + "endroom/");
			assertTrue(startAndTreasureRoom.getTreasureRoom().equals(treasureRoom));
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Test reading of a correct map.
	 * Tests if a room is correct.
	 */
	@Test
	public void testReadMapARoomCorrect() {
		try {
			MapParser.readMap(TEST_MAP_LOCATION, rooms);
			Room room = new Room(TEST_MAP_LOCATION + "room1/");
			assertTrue(rooms.get(0).equals(room));
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Test reading of a incorrect map file (.cmf).
	 * Tests exception of no starterRoom.
	 * 
	 * @throws IOException
	 * 		should happen here
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadMapExceptionEmpty() throws IOException {
		MapParser.readMap("/maps/incorrectMapFiles/empty/", rooms);
	}
	
	/**
	 * Test reading of a incorrect map file (.cmf).
	 * Tests exception of no starterRoom definition.
	 * @throws IOException
	 * 			should happen here
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadMapExceptionNoStartRoom() throws IOException {
		MapParser.readMap("/maps/incorrectMapFiles/noStartRoom/", rooms);
	}
	
	/**
	 * Test reading of a incorrect map file (.cmf).
	 * Tests exception of no treasureRoom.
	 * 
	 * @throws IOException
	 * 		should happen here
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadMapExceptionNoEndRoom1() throws IOException {
		MapParser.readMap("/maps/incorrectMapFiles/noTreasureRoom1/", rooms);
	}
	
	/**
	 * Test reading of a incorrect map file (.cmf).
	 * Tests exception of no treasureRoom definition.
	 * 
	 * @throws IOException
	 * 		should happen here
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadMapExceptionNoEndRoom2() throws IOException {
		MapParser.readMap("/maps/incorrectMapFiles/noTreasureRoom2/", rooms);
	}
	
	/**
	 * Test reading of a incorrect map file (.cmf).
	 * Tests exception of no extra rooms.
	 * 
	 * @throws IOException
	 * 		should happen here
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadMapExceptionNoExtra1() throws IOException {
		MapParser.readMap("/maps/incorrectMapFiles/noRoomCount1/", rooms);
	}
	
	/**
	 * Test reading of a incorrect map file (.cmf).
	 * Tests exception of no extra rooms definition.
	 * 
	 * @throws IOException
	 * 		should happen here
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadMapExceptionNoExtra2() throws IOException {
		MapParser.readMap("/maps/incorrectMapFiles/noRoomCount2/", rooms);
	}
	
	/**
	 * Test reading of a incorrect map file (.cmf).
	 * Tests exception of when there are not enough extra rooms.
	 * 
	 * @throws IOException
	 * 		should happen here
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadMapExceptionNoExtra3() throws IOException {
		MapParser.readMap("/maps/incorrectMapFiles/noRoom1/", rooms);
	}

	/**
	 * Try to get gridTestMap.
	 */
	@Test
	public void testGetMapFile() {
		try {
			File testFile = MapParser.getMapFile(TEST_MAP_LOCATION);
			assertTrue(testFile.exists());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test exception if .cmf file does not exist.
	 * 
	 * @throws FileNotFoundException 
	 * 		this should happen here
	 */
	@Test (expected = FileNotFoundException.class)
	public void testGetMapFileException() throws FileNotFoundException {
		File testFile = MapParser.getMapFile("/maps/correctWithName/");
		assertTrue(testFile.exists());
	}
	
	/**
	 * Test exception if path does not exist.
	 * 
	 * @throws FileNotFoundException 
	 * 		this should happen here
	 */
	@Test (expected = FileNotFoundException.class)
	public void testGetMapFileExceptionNotAPAd() throws FileNotFoundException {
		File testFile = MapParser.getMapFile("notAPad");
		assertTrue(testFile.exists());
	}

}
