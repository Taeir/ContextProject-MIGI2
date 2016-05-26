package nl.tudelft.contextproject.model.level.roomIO;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Door;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.Key;
import nl.tudelft.contextproject.util.ScriptLoaderException;

/**
 * Test class for the entityReader class.
 */
public class EntityParserTest extends TestBase {
	
	/**
	 * Test if getting a non existent entity throws an exception.
	 *
	 * @throws ScriptLoaderException
	 * 		this should not happen
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetEntityNotSupported() throws ScriptLoaderException {
		EntityParser.getEntity("NonExistingType", 0, 0, 0, null, "/");
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
		Entity res = EntityParser.getEntity("Bomb", 0, 0, 0, data, "/");
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
		Entity res = EntityParser.getEntity("Key", 0, 0, 0, data, "/");
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
		Entity res = EntityParser.getEntity("Door", 0, 0, 0, data, "/");
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
		EntityParser.readEntities(entities, 1, 0, 0, br, "/");
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
		EntityParser.readEntities(entities, 1, 0, 0, br, "/");
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
		EntityParser.readEntities(entities, 1, 0, 0, br, "/");
	}
}
