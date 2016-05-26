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
	 */
	@Test
	public void testReadMap() {
		ArrayList<Room> rooms = new ArrayList<Room>();
		//treasureRoom
		try {
			RoomTuple startAndTreasureRoom = MapReader.readMap(TEST_MAP_LOCATION, rooms);
			
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
