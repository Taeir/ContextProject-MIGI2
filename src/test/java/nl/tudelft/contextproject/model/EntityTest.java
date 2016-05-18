package nl.tudelft.contextproject.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jme3.math.Vector3f;

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
	
	@Test
	public void testMove() {
		setupEntity();
		Vector3f before = entity.getSpatial().getLocalTranslation();
		Vector3f expected = before.clone().add(1.23f, 2.34f, 3.45f);
		entity.move(1.23f, 2.34f, 3.45f);
		Vector3f after = entity.getSpatial().getLocalTranslation();
		assertEquals(expected.x, after.x, 10e-10);
		assertEquals(expected.y, after.y, 10e-10);
		assertEquals(expected.z, after.z, 10e-10);
				
	}
}
