package nl.tudelft.contextproject;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Abstract test class for Entity.
 */
public abstract class EntityTest {
	
	/**
	 * Getter for a specific instance of Entity.
	 * @return an Entity to test with.
	 */
	public abstract Entity getEntity();

	/**
	 * Test if the initial state of an entity is NEW.
	 */
	@Test
	public void testInitialState() {
		Entity e = getEntity();
		assertEquals(e.getState(), EntityState.NEW);
	}
	
	/**
	 * Test if setting a new state changes the state.
	 */
	@Test
	public void testSetState() {
		Entity e = getEntity();
		e.setState(EntityState.ALIVE);
		assertEquals(e.getState(), EntityState.ALIVE);
	}
}
