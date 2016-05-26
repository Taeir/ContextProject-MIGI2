package nl.tudelft.contextproject.model.level.roomIO;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import nl.tudelft.contextproject.model.level.Room;
import nl.tudelft.contextproject.model.level.RoomTuple;

/**
 * Test class for MapReader.
 */
public class MapReaderTest {

	//Location of the test map
	private static final String TEST_MAP_LOCATION = "/maps/testGridMap/";

	/**
	 * Test reading of a correct map.
	 * Tests if starter room is correct.
	 */
	@Test
	public void testReadMapStartRoomCorrect() {
		ArrayList<Room> rooms = new ArrayList<Room>();
		//treasureRoom
		try {
			RoomTuple startAndTreasureRoom = MapReader.readMap(TEST_MAP_LOCATION, rooms);
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
		ArrayList<Room> rooms = new ArrayList<Room>();
		try {
			RoomTuple startAndTreasureRoom = MapReader.readMap(TEST_MAP_LOCATION, rooms);
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
		ArrayList<Room> rooms = new ArrayList<Room>();
		try {
			MapReader.readMap(TEST_MAP_LOCATION, rooms);
			Room room = new Room(TEST_MAP_LOCATION + "room1/");
			assertTrue(rooms.get(0).equals(room));
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Try to get gridTestMap.
	 */
	@Test
	public void testGetMapFile() {
		try {
			File testFile = MapReader.getMapFile(TEST_MAP_LOCATION);
			assertTrue(testFile.exists());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test exception if .cmf file does not exist.
	 * @throws FileNotFoundException 
	 * 			this should happen here
	 */
	@Test (expected = FileNotFoundException.class)
	public void testGetMapFileException() throws FileNotFoundException {
		File testFile = MapReader.getMapFile("/maps/correctWithName/");
		assertTrue(testFile.exists());
	}

}
