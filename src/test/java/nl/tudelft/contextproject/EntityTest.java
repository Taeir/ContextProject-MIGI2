package nl.tudelft.contextproject;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Abstract test class for Entity.
 */
public abstract class EntityTest extends DrawableTest {
	
	private Entity entity;
	
	@Override
	public Drawable getDrawable() {
		return getEntity();
	}
	
	/**
	 * Getter for a specific instance of Entity.
	 * @return an Entity to test with.
	 */
	public abstract Entity getEntity();

	/**
	 * create a new (clean) entity to test with.
	 */
	public void setupEntity() {
		entity = getEntity();
	}
	
	/**
	 * Test if the initial state of an entity is NEW.
	 */
	@Test
	public void testInitialState() {
		setupEntity();
		assertEquals(entity.getState(), EntityState.NEW);
	}
	
	/**
	 * Test if setting a new state changes the state.
	 */
	@Test
	public void testSetState() {
		setupEntity();
		entity.setState(EntityState.ALIVE);
		assertEquals(entity.getState(), EntityState.ALIVE);
	}
}
