package nl.tudelft.contextproject.model.level.roomIO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

/**
 * Test class for MapReader.
 */
public class MapReaderTest {

	//Location of the test map
	private static final String TEST_MAP_LOCATION = "/maps/testGridMap/";

	/**
	 * Test reading of a map.
	 */
	public void testReadMap() {
		
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
