package nl.tudelft.contextproject.level;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.Entity;

public class DrawableFilterTest {
	private DrawableFilter filter;
	private Entity e;

	@Before
	public void setup() {
		filter = new DrawableFilter(true);
		e = mock(Entity.class);
	}
	
	@Test
	public void testNotInFilter() {
		assertFalse(filter.entity(e));
	}
	
	@Test
	public void testInFilter() {
		filter.addEntity(e);
		assertTrue(filter.entity(e));
	}
	
	@Test
	public void testInFilterSameType() {
		filter.addEntity(e);
		assertTrue(filter.entity(mock(Entity.class)));
	}
	
	@Test
	public void testHideUnexplored() {
		assertTrue(filter.hideUnexplored());
	}
	
	@Test
	public void testShowUnexplored() {
		filter = new DrawableFilter(false);
		assertFalse(filter.hideUnexplored());
	}

}
