package nl.tudelft.contextproject;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Abstract test class for Key.
 */
public abstract class KeyTest extends EntityTest {

	private Key key;
	@Before
	
	@Override
	public Drawable getDrawable() {
		return getKey();
	}
	
	/**
	 * Getter for a specific instance of Key.
	 * @return an Key to test with.
	 */
	public abstract Key getKey();

	/**
	 * create a new (clean) Key to test with.
	 */
	public void setupKey() {
		key = getKey();
	}
	
	/**
	 * Test if the initial state of an Key is NEW.
	 */
	@Test
	public void testInitialState() {
		setupKey();
		assertEquals(key.getState(), EntityState.NEW);
	}
	
	/**
	 * Test if setting a new state changes the state.
	 */
	@Test
	public void testSetState() {
		setupKey();
		key.setState(EntityState.ALIVE);
		assertEquals(key.getState(), EntityState.ALIVE);
	}
}
