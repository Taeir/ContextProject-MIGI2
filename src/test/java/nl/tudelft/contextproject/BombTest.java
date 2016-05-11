package nl.tudelft.contextproject;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Abstract test class for bomb.
 */
public abstract class BombTest extends EntityTest{
	
	private Bomb bomb;
	@Before
	public Drawable getDrawable() {
		return getBomb();
	}
	
	/**
	 * Getter for a specific instance of bomb.
	 * @return an bomb to test with.
	 */
	public abstract Bomb getBomb();

	/**
	 * create a new (clean) bomb to test with.
	 */
	public void setupbomb() {
		bomb = getBomb();
	}
	
	/**
	 * Test if the initial state of an bomb is NEW.
	 */
	@Test
	public void testInitialState() {
		setupbomb();
		assertEquals(bomb.getState(), EntityState.NEW);
	}
	
	/**
	 * Test if setting a new state changes the state.
	 */
	@Test
	public void testSetState() {
		setupbomb();
		bomb.setState(EntityState.ALIVE);
		assertEquals(bomb.getState(), EntityState.ALIVE);
	}
}
