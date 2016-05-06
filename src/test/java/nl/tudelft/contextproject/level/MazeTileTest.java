package nl.tudelft.contextproject.level;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Drawable;
import nl.tudelft.contextproject.DrawableTest;

public class MazeTileTest extends DrawableTest {
	MazeTile tile;

	@Before
	public void setup() {
		tile = new MazeTile(10, 123);
	}

	@Override
	public Drawable getDrawable() {
		return tile;
	}
	
	@Test
	public void testIsExplored() {
		assertFalse(tile.isExplored());
	}
	
	@Test
	public void testSetExploredNoChange() {
		assertFalse(tile.setExplored(false));
		assertFalse(tile.isExplored());
	}
	
	@Test
	public void testSetExplored() {
		assertFalse(tile.setExplored(true));
		assertTrue(tile.isExplored());
	}
	
	@Test
	public void testGetGeometryPosition() {
		setupGeometryMock();
		assertEquals(new Vector3f(10, 123, 0), tile.getGeometry().getLocalTranslation());		
	}

}
