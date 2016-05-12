package nl.tudelft.contextproject.model.level;

import static org.junit.Assert.*;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.model.Drawable;
import nl.tudelft.contextproject.model.DrawableTest;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MazeTiles.
 */
public class MazeTileTest extends DrawableTest {
	private MazeTile tileCorridor;

	/**
	 * Create a fresh mazeTile for each test.
	 */
	@Before
	public void setUp() {
		tileCorridor = new MazeTile(10, 123, TileType.CORRIDOR);
	}

	@Override
	public Drawable getDrawable() {
		return tileCorridor;
	}
	
	/**
	 * Test for default exploration value.
	 */
	@Test
	public void testIsExplored() {
		assertFalse(tileCorridor.isExplored());
	}

	/**
	 * Test for setting explored value for a tile with no change in value.
	 */
	@Test
	public void testSetExploredNoChange() {
		assertFalse(tileCorridor.setExplored(false));
		assertFalse(tileCorridor.isExplored());
	}

	/**
	 * Test for setting the explored value to a new value.
	 */
	@Test
	public void testSetExplored() {
		assertFalse(tileCorridor.setExplored(true));
		assertTrue(tileCorridor.isExplored());
	}
	
	/**
	 * Check if the position of the tile is according to the values given when constructiong.
	 */
	@Test
	public void testGetGeometryPosition() {
		setupGeometryMock();
		MazeTile tileFloor = new MazeTile(10, 123, TileType.FLOOR);
		MazeTile tileWall = new MazeTile(10, 123, TileType.WALL);

		assertEquals(new Vector3f(10, 123, 0), tileCorridor.getSpatial().getLocalTranslation());
		assertEquals(new Vector3f(10, 123, 0), tileFloor.getSpatial().getLocalTranslation());
		assertEquals(new Vector3f(10, 123, 3), tileWall.getSpatial().getLocalTranslation());
	}

}
