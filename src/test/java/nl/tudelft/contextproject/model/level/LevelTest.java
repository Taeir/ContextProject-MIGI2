package nl.tudelft.contextproject.model.level;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.jme3.light.Light;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Level.
 */
public class LevelTest {

	private Light lMock;
	private MazeTile tMock1;
	private MazeTile tMock2;
	private Level level;

	/**
	 * Setup method.
	 * Creates fresh mocks and levels for each test.
	 */
	@Before
	public void setUp() {
		lMock = mock(Light.class);
		tMock1 = mock(MazeTile.class);
		tMock2 = mock(MazeTile.class);

		List<Light> lights = new ArrayList<>();
		lights.add(lMock);
		MazeTile[][] tiles = {{tMock1, null, tMock2},
							  {tMock2, tMock1, null}};
		level = new Level(tiles,  lights);
	}

	/**
	 * Test for the width calculation of the level.
	 */
	@Test
	public void testGetWidth() {
		assertEquals(2, level.getWidth());
	}

	/**
	 * Test for the height calculation of the level.
	 */
	@Test
	public void testGetHeight() {
		assertEquals(3, level.getHeight());
	}

	/**
	 * Test for existing tile in the maze.
	 */
	@Test
	public void testIsTileAtPosition() {
		assertTrue(level.isTileAtPosition(0, 0));
		assertEquals(tMock1, level.getTile(0, 0));
	}

	/**
	 * Test for correct coordinate order in getting a tile.
	 */
	@Test
	public void testIsTileAtPosition2() {
		assertTrue(level.isTileAtPosition(0, 2));
		assertEquals(tMock2, level.getTile(0, 2));
	}

	/**
	 * Test existence on an empty location.
	 */
	@Test
	public void testIsTileNotAtPosition() {
		assertFalse(level.isTileAtPosition(1, 2));
		assertEquals(null, level.getTile(1, 2));
	}

	/**
	 * Test the getter for the lights in the level.
	 */
	@Test
	public void testGetLights() {
		assertEquals(1, level.getLights().size());
		assertTrue(level.getLights().contains(lMock));
	}

}
