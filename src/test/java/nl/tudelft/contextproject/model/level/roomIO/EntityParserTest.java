package nl.tudelft.contextproject.model.level.roomIO;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import lombok.SneakyThrows;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Door;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.Key;
import nl.tudelft.contextproject.model.entities.KillerBunny;
import nl.tudelft.contextproject.model.entities.LandMine;
import nl.tudelft.contextproject.model.entities.Pitfall;
import nl.tudelft.contextproject.model.entities.VoidPlatform;
import nl.tudelft.contextproject.model.entities.WallFrame;
import nl.tudelft.contextproject.test.TestUtil;
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
	public void testGetEntityDataNull() throws ScriptLoaderException {
		EntityParser.getEntity("NULLDATA", 0, 0, 0, null, "/");
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
	 * Test getting a VoidPlatform.
	 *
	 * @throws ScriptLoaderException
	 * 		this should not happen
	 */
	@Test
	public void testGetEntityVoidPlatform() throws ScriptLoaderException {
		String[] data = new String[]{"9.5 ", ".5", "5.5", "VoidPlatform "};
		Entity res = EntityParser.getEntity("VoidPlatform", 0, 0, 0, data, "/");
		assertEquals(VoidPlatform.class, res.getClass());
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
	
	/**
	 * Test getting a pitfall entity.
	 * 
	 * <p> The {@link ScriptLoaderException} should never happen as no script is loaded. </p>
	 */
	@SneakyThrows
	@Test
	public void testGetEntityPitfall() {
		String[] data = {"9.5 ", ".5", "5.5", "Pitfall", "1"};
		Entity res = EntityParser.getEntity("Pitfall", 0, 0, 0, data, "/");
		assertEquals(Pitfall.class, res.getClass());
	}
	
	/**
	 * Test parsing a {@link Pitfall} without the width value.
	 * 
	 * <p> The {@link ScriptLoaderException} should never happen as no script is loaded. </p>
	 */
	@SneakyThrows
	@Test (expected = IllegalArgumentException.class)
	public void testGetEntityPitfallNotEnoughArguments() {
		String[] data = {"9.5 ", ".5", "5.5", "Pitfall"};
		EntityParser.getEntity("Pitfall", 0, 0, 0, data, "/");
	}
	
	/**
	 * Test parsing a {@link WallFrame}.
	 * 
	 * <p> The {@link ScriptLoaderException} should never happen as no script is loaded. </p>
	 */
	@SneakyThrows
	@Test
	public void testGetEntityWallFrame() {
		String[] data = {"9.5 ", ".5", "5.5", "WallFrame", "NORTH", "logo.png", "1", "1"};
		Entity res = EntityParser.getEntity("WallFrame", 0, 0, 0, data, "/");
		assertEquals(WallFrame.class, res.getClass());
	}
	
	/**
	 * Test parsing a {@link WallFrame} with missing arguments.
	 * 
	 * <p> The {@link ScriptLoaderException} should never happen as no script is loaded. </p>
	 */
	@SneakyThrows
	@Test (expected = IllegalArgumentException.class)
	public void testGetEntityWallFrameNotEnoughArguments() {
		String[] data = {"9.5 ", ".5", "5.5", "WallFrame"};
		EntityParser.getEntity("WallFrame", 0, 0, 0, data, "/");
	}
	
	/**
	 * Test loading a non existing entity type.
	 * 
	 * <p> The {@link ScriptLoaderException} should never happen as no script is loaded. </p>
	 */
	@SneakyThrows
	@Test (expected = IllegalArgumentException.class)
	public void testGetEntityNonExistent() {
		String[] data = {"9.5 ", ".5", "5.5", "NonExistentEntity"};
		EntityParser.getEntity("NonExistentEntity", 0, 0, 0, data, "/");
	}
	
	/**
	 * Test parsing a {@link LandMine}.
	 * 
	 * <p> The {@link ScriptLoaderException} should never happen as no script is loaded. </p>
	 */
	@SneakyThrows
	@Test
	public void testGetEntityLandMine() {
		String[] data = {"9.5 ", ".5", "5.5", "LandMine"};
		Entity res = EntityParser.getEntity("LandMine", 0, 0, 0, data, "/");
		assertEquals(LandMine.class, res.getClass());
	}
	
	/**
	 * Test parsing  {@link KillerBunny}.
	 * 
	 * <p> The {@link ScriptLoaderException} should never happen as no script is loaded. </p>
	 */
	@SneakyThrows
	@Test
	public void testGetEntityKillerBunny() {
		TestUtil.mockGame();
		String[] data = {"9.5 ", ".5", "5.5", "KillerBunny"};
		Entity res = EntityParser.getEntity("KillerBunny", 0, 0, 0, data, "/");
		assertEquals(KillerBunny.class, res.getClass());
	}
}
