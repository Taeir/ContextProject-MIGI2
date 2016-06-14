package nl.tudelft.contextproject.model.level;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.entities.Entity;

import org.junit.Before;
import org.junit.Test;


/**
 * Testing class for the DrawableFilter class.
 */
public class DrawableFilterTest extends TestBase {
	private DrawableFilter filter;
	private Entity entity;

	/**
	 * Setup method.
	 * Creates fresh instances to test with.
	 */
	@Before
	public void setUp() {
		filter = new DrawableFilter(true);
		entity = mock(Entity.class);
	}
	
	/**
	 * Test for an entity not whitelisted.
	 */
	@Test
	public void testNotInFilter() {
		assertFalse(filter.entity(entity));
	}
	
	/**
	 * Test for an entity that is on the whitelist.
	 */
	@Test
	public void testInFilter() {
		filter.addEntity(entity);
		assertTrue(filter.entity(entity));
	}
	
	/**
	 * Test for filtering a different entity with the same type as one on the whitelist.
	 */
	@Test
	public void testInFilterSameType() {
		filter.addEntity(entity);
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
