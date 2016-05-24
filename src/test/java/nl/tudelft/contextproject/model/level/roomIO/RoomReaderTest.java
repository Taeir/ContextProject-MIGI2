package nl.tudelft.contextproject.model.level.roomIO;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import com.jme3.light.Light;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.level.MazeTile;

/**
 * Test class for the roomLoader class.
 */
public class RoomReaderTest extends TestBase {
	
	/**
	 * Test loading a correct file.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test
	public void testCorrectFile() throws IOException {
		MazeTile[][] tiles = new MazeTile[50][50];
		Set<Entity> entities = ConcurrentHashMap.newKeySet();
		ArrayList<Light> lights = new ArrayList<>();
		RoomReader.importFile("/maps/correct/", tiles, entities, lights, 0, 0);
		// no errors occurred.
		assertTrue(entities.size() > 0);
		assertTrue(lights.size() > 0);
	}
	
	/**
	 * Test loading a non existent file.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test (expected = FileNotFoundException.class)
	public void testNonExistentFile() throws IOException {
		RoomReader.importFile("/maps/fileNotFound/", null, null, null, 0, 0);
	}
	
	/**
	 * Test loading empty file.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testEmptyFile() throws IOException {
		RoomReader.importFile("/maps/emptyMap/", null, null, null, 0, 0);
	}
	
	/**
	 * Test loading a file with a too small header.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testSmallHeaderFile() throws IOException {
		RoomReader.importFile("/maps/smallHeader/", null, null, null, 0, 0);
	}
	
	/**
	 * Check dimensions for a too wide room.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testDimentionsTooWide() {
		MazeTile[][] tiles = new MazeTile[9][10];
		RoomReader.checkDimensions(10, 0, tiles);
	}
	
	/**
	 * Check dimensions for a too high room.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testDimentionsTooHigh() {
		MazeTile[][] tiles = new MazeTile[10][9];
		RoomReader.checkDimensions(0, 10, tiles);
	}
}
