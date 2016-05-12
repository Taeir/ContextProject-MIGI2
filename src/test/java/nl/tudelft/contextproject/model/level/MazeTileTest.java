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
	MazeTile tile;

	/**
	 * Create a fresh mazeTile for each test.
	 */
	@Before
	public void setUp() {
		tile = new MazeTile(10, 123, MazeTile.MAX_HEIGHT);
	}

	@Override
	public Drawable getDrawable() {
		return tile;
	}
	
	/**
	 * Test for default exploration value.
	 */
	@Test
	public void testIsExplored() {
		assertFalse(tile.isExplored());
	}

	/**
	 * Test for setting explored value for a tile with no change in value.
	 */
	@Test
	public void testSetExploredNoChange() {
		assertFalse(tile.setExplored(false));
		assertFalse(tile.isExplored());
	}

	/**
	 * Test for setting the explored value to a new value.
	 */
	@Test
	public void testSetExplored() {
		assertFalse(tile.setExplored(true));
		assertTrue(tile.isExplored());
	}
	
	/**
	 * Check if the position of the tile is according to the values given when constructiong.
	 */
	@Test
	public void testGetGeometryPosition() {
		setupGeometryMock();
		assertEquals(new Vector3f(10, 123, MazeTile.MAX_HEIGHT), tile.getSpatial().getLocalTranslation());
	}

}
