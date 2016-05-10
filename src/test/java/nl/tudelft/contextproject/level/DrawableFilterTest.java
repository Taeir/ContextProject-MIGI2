package nl.tudelft.contextproject.level;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import nl.tudelft.contextproject.Entity;

import org.junit.Before;
import org.junit.Test;


/**
 * Testing class for the DrawableFilter class.
 */
public class DrawableFilterTest {
	private DrawableFilter filter;
	private Entity e;

	/**
	 * Setup method.
	 * Creates fresh instances to test with.
	 */
	@Before
	public void setUp() {
		filter = new DrawableFilter(true);
		e = mock(Entity.class);
	}
	
	/**
	 * Test for an entity not whitelisted.
	 */
	@Test
	public void testNotInFilter() {
		assertFalse(filter.entity(e));
	}
	
	/**
	 * Test for an entity that is on the whitelist.
	 */
	@Test
	public void testInFilter() {
		filter.addEntity(e);
		assertTrue(filter.entity(e));
	}
	
	/**
	 * Test for filtering a different entity with the same type as one on the whitelist.
	 */
	@Test
	public void testInFilterSameType() {
		filter.addEntity(e);
		assertTrue(filter.entity(mock(Entity.class)));
	}
	
	/**
	 * Test getter for hideExplored.
	 */
	@Test
	public void testHideUnexplored() {
		assertTrue(filter.hideUnexplored());
	}
	
	/**
	 * Test getter for hideExplored.
	 */
	@Test
	public void testShowUnexplored() {
		filter = new DrawableFilter(false);
		assertFalse(filter.hideUnexplored());
	}

}
