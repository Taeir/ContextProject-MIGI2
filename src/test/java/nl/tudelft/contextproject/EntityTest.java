package nl.tudelft.contextproject;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Abstract test class for Entity.
 */
public abstract class EntityTest {
	
	private Entity entity;
	
	/**
	 * Getter for a specific instance of Entity.
	 * @return an Entity to test with.
	 */
	public abstract Entity getEntity();

	/**
	 * create a new (clean) entity to test with.
	 */
	@Before
	public void setUp() {
		entity = getEntity();
	}
	
	/**
	 * Test if the initial state of an entity is NEW.
	 */
	@Test
	public void testInitialState() {
		assertEquals(entity.getState(), EntityState.NEW);
	}
	
	/**
	 * Test if setting a new state changes the state.
	 */
	@Test
	public void testSetState() {
		entity.setState(EntityState.ALIVE);
		assertEquals(entity.getState(), EntityState.ALIVE);
	}
}
