package nl.tudelft.contextproject.roomIO;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jme3.light.Light;

import lombok.SneakyThrows;
import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Entity;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for the roomLoader class.
 */
public class RoomReaderTest {
	private static Main main;

	/**
	 * Ensures that {@link Main#getInstance()} is properly set up before any tests run.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		main = Main.getInstance();
		Main.setInstance(null);
		TestUtil.ensureMainMocked(true);
	}

	/**
	 * Restores the original Main instance after all tests are done.
	 */
	@AfterClass
	public static void tearDownAfterClass() {
		Main.setInstance(main);
	}
	
	/**
	 * Test loading a correct file.
	 * @throws IOException This should not happen.
	 */
	@Test
	@SneakyThrows(URISyntaxException.class)
	public void testCorrectFile() throws IOException {
		File file = new File(RoomReader.class.getResource("/maps/correct.crf").toURI());
		MazeTile[][] tiles = new MazeTile[50][50];
		ArrayList<Entity> entities = new ArrayList<>();
		ArrayList<Light> lights = new ArrayList<>();
		RoomReader.importFile(file, tiles, entities, lights, 0, 0);
		// no errors occurred.
		assertTrue(entities.size() > 0);
		assertTrue(lights.size() > 0);
	}
	
	/**
	 * Test loading a non existent file.
	 * @throws IOException This should not happen.
	 */
	@Test (expected = FileNotFoundException.class)
	public void testNonExistentFile() throws IOException {
		File file = new File("IDoNotExist.crf");
		RoomReader.importFile(file, null, null, null, 0, 0);
	}
	
	/**
	 * Test loading empty file.
	 * @throws IOException This should not happen.
	 */
	@Test (expected = IllegalArgumentException.class)
	@SneakyThrows(URISyntaxException.class)
	public void testEmptyFile() throws IOException {
		File file = new File(getClass().getResource("/dummy.txt").toURI());
		RoomReader.importFile(file, null, null, null, 0, 0);
	}
	
	/**
	 * Test loading a file with a too small header.
	 * @throws IOException This should not happen.
	 */
	@Test (expected = IllegalArgumentException.class)
	@SneakyThrows(URISyntaxException.class)
	public void testSmallHeaderFile() throws IOException {
		File file = new File(getClass().getResource("/maps/smallHeader.crf").toURI());
		RoomReader.importFile(file, null, null, null, 0, 0);
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
