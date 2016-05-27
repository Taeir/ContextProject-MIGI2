package nl.tudelft.contextproject.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.VRPlayer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test for the JSONUtil class.
 */
public class JSONUtilTest extends TestBase {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private JSONObject mockedJSONObject;
	private File testFile;

	/**
	 * Set up all objects used in testing.
	 */
	@Before
	public void setUp() {
		mockedJSONObject = mock(JSONObject.class);
		testFile = new File("UtilTestFile.json");
		when(mockedJSONObject.toString(anyInt())).thenReturn("{}");
	}

	/**
	 * Remove the test file if it has been created.
	 */
	@After
	public void tearDown() {
		if (testFile.exists()) {
			testFile.delete();
		}
	}

	/**
	 * Test for checking if an IOException occurs if one tries
	 * to load a non-existing file.
	 *
	 * @throws IOException
	 *      the expected exception
	 */
	@Test
	public void testLoadFileNotFound() throws IOException {
		thrown.expect(IOException.class);
		File notFound = new File("NotARealFile.json");
		JSONUtil.load(notFound);
	}

	/**
	 * Test for loading an existing file.
	 *
	 * @throws IOException
	 *      file not found
	 */
	@Test
	public void testLoadExistingFile() throws IOException {
		JSONUtil.save(mockedJSONObject, testFile);
		assertNotNull(JSONUtil.load(testFile));
	}

	/**
	 * Test saving of a file.
	 *
	 * @throws IOException
	 *      if file writing goes wrong
	 */
	@Test
	public void testSaveFile() throws IOException {
		JSONUtil.save(mockedJSONObject, testFile);
	}

	/**
	 * Test for getting a json representing all entities.
	 */
	@Test
	public void testEntitiesToJson() {
		Set<Entity> set = ConcurrentHashMap.newKeySet();

		Bomb bomb = new Bomb();
		set.add(bomb);

		JSONArray jArray = JSONUtil.entitiesToJson(set, new VRPlayer());
		assertNotNull(jArray.get(0));
	}


	/**
	 * Test for getting a json representing an entity.
	 */
	@Test
	public void testEntityToJson() {
		Bomb bomb = new Bomb();
		JSONObject json = JSONUtil.entityToJson(bomb);
		assertEquals(json.getInt("x"), 0);
		assertEquals(json.getInt("y"), 0);
		assertEquals(json.getInt("type"), 1);
	}
}
