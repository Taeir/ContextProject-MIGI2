package nl.tudelft.contextproject.roomIO;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Bomb;
import nl.tudelft.contextproject.model.Door;
import nl.tudelft.contextproject.model.Entity;
import nl.tudelft.contextproject.model.Key;
import nl.tudelft.contextproject.test.TestUtil;
import nl.tudelft.contextproject.util.ScriptLoaderException;

/**
 * Test class for the entityReader class.
 */
public class EntityReaderTest {
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
	 * Test if getting a non existent entity throws an exception.
	 * @throws ScriptLoaderException This should not happen.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetEntityNotSupported() throws ScriptLoaderException {
		EntityReader.getEntity("NonExistingType", 0, 0, 0, null, "/");
	}
	
	/**
	 * Test getting a bomb.
	 * @throws ScriptLoaderException This should not happen.
	 */
	@Test
	public void testGetEntityBomb() throws ScriptLoaderException {
		Entity res = EntityReader.getEntity("Bomb", 0, 0, 0, null, "/");
		assertEquals(Bomb.class, res.getClass());
	}
	
	/**
	 * Test getting a key.
	 * @throws ScriptLoaderException This should not happen.
	 */
	@Test
	public void testGetEntityKey() throws ScriptLoaderException {
		Entity res = EntityReader.getEntity("Key", 0, 0, 0, null, "/");
		assertEquals(Key.class, res.getClass());
	}
	
	/**
	 * Test getting a door.
	 * @throws ScriptLoaderException This should not happen.
	 */
	@Test
	public void testGetEntityDoor() throws ScriptLoaderException {
		Entity res = EntityReader.getEntity("Door", 0, 0, 0, null, "/");
		assertEquals(Door.class, res.getClass());
	}
	
	/**
	 * Test reading an entity from a string.
	 * @throws IOException Should not happen.
	 * @throws ScriptLoaderException This should not happen.
	 */
	@Test
	public void testReadEntities() throws IOException, ScriptLoaderException {
		ArrayList<Entity> entities = new ArrayList<>();
		String in = "0 1 2 Door";
		BufferedReader br = new BufferedReader(new StringReader(in));
		EntityReader.readEntities(entities, 1, 0, 0, br, "/");
		assertEquals(1, entities.size());
		assertEquals(Door.class, entities.get(0).getClass());
	}
	
	/**
	 * Test reading an empty string.
	 * @throws IOException Should not happen.
	 * @throws ScriptLoaderException This should not happen.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadEntitiesNull() throws IOException, ScriptLoaderException {
		ArrayList<Entity> entities = new ArrayList<>();
		String in = "";
		BufferedReader br = new BufferedReader(new StringReader(in));
		EntityReader.readEntities(entities, 1, 0, 0, br, "/");
	}
	
	/**
	 * Test reading a string with too few elements.
	 * @throws IOException Should not happen.
	 * @throws ScriptLoaderException This should not happen.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadEntitiesTooFewArguments() throws IOException, ScriptLoaderException {
		ArrayList<Entity> entities = new ArrayList<>();
		String in = "1 1 EntityName";
		BufferedReader br = new BufferedReader(new StringReader(in));
		EntityReader.readEntities(entities, 1, 0, 0, br, "/");
	}
}
