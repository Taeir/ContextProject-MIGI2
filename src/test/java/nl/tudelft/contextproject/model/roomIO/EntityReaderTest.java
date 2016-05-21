package nl.tudelft.contextproject.model.roomIO;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Door;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.Key;
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
	 *
	 * @throws ScriptLoaderException
	 * 		this should not happen
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetEntityNotSupported() throws ScriptLoaderException {
		EntityReader.getEntity("NonExistingType", 0, 0, 0, null, "/");
	}
	
	/**
	 * Test getting a bomb.
	 *
	 * @throws ScriptLoaderException
	 * 		this should not happen
	 */
	@Test
	public void testGetEntityBomb() throws ScriptLoaderException {
		String[] data = new String[]{"9.5 ", ".5 ", "5.5 ", "Key ", "1/0/0/0"};
		Entity res = EntityReader.getEntity("Bomb", 0, 0, 0, data, "/");
		assertEquals(Bomb.class, res.getClass());
	}
	
	/**
	 * Test getting a key.
	 *
	 * @throws ScriptLoaderException
	 * 		this should not happen
	 */
	@Test
	public void testGetEntityKey() throws ScriptLoaderException {
		String[] data = new String[]{"9.5 ", ".5 ", "5.5 ", "Door ", "1/0/0/0"};
		Entity res = EntityReader.getEntity("Key", 0, 0, 0, data, "/");
		assertEquals(Key.class, res.getClass());
	}
	
	/**
	 * Test getting a door.
	 *
	 * @throws ScriptLoaderException
	 * 		this should not happen
	 */
	@Test
	public void testGetEntityDoor() throws ScriptLoaderException {
		String[] data = new String[]{"9.5 ", ".5", "5.5 ", "Door ", "1/0/0/0"};
		Entity res = EntityReader.getEntity("Door", 0, 0, 0, data, "/");
		assertEquals(Door.class, res.getClass());
	}
	
	/**
	 * Test reading an entity from a string.
	 *
	 * @throws IOException
	 * 		should not happen
	 * @throws ScriptLoaderException
	 * 		this should not happen
	 */
	@Test
	public void testReadEntities() throws IOException, ScriptLoaderException {
		Set<Entity> entities = ConcurrentHashMap.newKeySet();
		String in = "0 1 2 Door 1/0/0/0";
		BufferedReader br = new BufferedReader(new StringReader(in));
		EntityReader.readEntities(entities, 1, 0, 0, br, "/");
		assertEquals(1, entities.size());
		assertEquals(Door.class, entities.iterator().next().getClass());
	}
	
	/**
	 * Test reading an empty string.
	 *
	 * @throws IOException
	 * 		this should not happen
	 * @throws ScriptLoaderException
	 * 		this should not happen
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadEntitiesNull() throws IOException, ScriptLoaderException {
		Set<Entity> entities = ConcurrentHashMap.newKeySet();
		String in = "";
		BufferedReader br = new BufferedReader(new StringReader(in));
		EntityReader.readEntities(entities, 1, 0, 0, br, "/");
	}
	
	/**
	 * Test reading a string with too few elements.
	 *
	 * @throws IOException
	 * 		this should not happen
	 * @throws ScriptLoaderException
	 * 		this should not happen
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadEntitiesTooFewArguments() throws IOException, ScriptLoaderException {
		Set<Entity> entities = ConcurrentHashMap.newKeySet();
		String in = "1 1 EntityName";
		BufferedReader br = new BufferedReader(new StringReader(in));
		EntityReader.readEntities(entities, 1, 0, 0, br, "/");
	}
}
