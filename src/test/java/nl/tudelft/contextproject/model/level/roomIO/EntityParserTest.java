package nl.tudelft.contextproject.model.level.roomIO;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.entities.Door;
import nl.tudelft.contextproject.model.entities.Entity;

/**
 * Test class for the entityReader class.
 */
public class EntityParserTest extends TestBase {
	
	private Set<Entity> entities;

	/**
	 * Creates an entities list before every test.
	 */
	@Before
	public void setUp() {
		entities = new HashSet<>();
	}
	
	/**
	 * Test reading an entity from a string.
	 *
	 * @throws IOException
	 * 		should not happen
	 */
	@Test
	public void testReadEntities() throws IOException {
		String in = "0 1 2 Door 1/0/0/0";
		EntityParser.readEntities(entities, 1, 0, 0, createReader(in), "/");
		
		assertEquals(1, entities.size());
		assertEquals(Door.class, entities.iterator().next().getClass());
	}
	
	/**
	 * Test reading an invalid entity from a string.
	 *
	 * @throws IOException
	 * 		should not happen
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReadEntitiesInvalidEntity() throws IOException {
		String in = "0 1 2 NotAnEntity";
		EntityParser.readEntities(entities, 1, 0, 0, createReader(in), "/");
	}
	
	/**
	 * Test reading an empty string.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReadEntitiesNull() throws IOException {
		String in = "";
		EntityParser.readEntities(entities, 1, 0, 0, createReader(in), "/");
	}
	
	/**
	 * Test reading a string with too few elements.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testReadEntitiesTooFewArguments() throws IOException {
		String in = "1 1 EntityName";
		EntityParser.readEntities(entities, 1, 0, 0, createReader(in), "/");
	}
	
	/**
	 * Creates a BufferedReader for the given string.
	 * 
	 * @param string
	 * 		the string to create a reader for
	 * @return
	 * 		a new BufferedReader over a StringReader over the given string
	 */
	private BufferedReader createReader(String string) {
		return new BufferedReader(new StringReader(string));
	}
}
