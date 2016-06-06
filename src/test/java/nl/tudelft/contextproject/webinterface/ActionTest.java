package nl.tudelft.contextproject.webinterface;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Test class for {@link Action}.
 */
public class ActionTest {

	/**
	 * Tests if {@link Action#getAction} works correctly for action 0.
	 */
	@Test
	public void testAction0() {
		assertSame(Action.getAction(0), Action.PLACEBOMB);
	}
	
	/**
	 * Tests if {@link Action#getAction} works correctly for action 1.
	 */
	@Test
	public void testAction1() {
		assertSame(Action.getAction(1), Action.PLACEPITFALL);
	}
	
	/**
	 * Tests if {@link Action#getAction} works correctly for action 2.
	 */
	@Test
	public void testAction2() {
		assertSame(Action.getAction(2), Action.PLACEMINE);
	}
	
	/**
	 * Tests if {@link Action#getAction} works correctly for action 3.
	 */
	@Test
	public void testAction3() {
		assertSame(Action.getAction(3), Action.SPAWNENEMY);
	}
	
	/**
	 * Tests if {@link Action#getAction} works correctly for action 4.
	 */
	@Test
	public void testAction4() {
		assertSame(Action.getAction(4), Action.DROPBAIT);
	}
	
	/**
	 * Tests if {@link Action#getAction} works correctly for action 5.
	 */
	@Test
	public void testAction5() {
		assertSame(Action.getAction(5), Action.PLACETILE);
	}
	
	/**
	 * Tests if {@link Action#getAction} works correctly for action 6.
	 */
	@Test
	public void testAction6() {
		assertSame(Action.getAction(6), Action.OPENGATE);
	}
	
	/**
	 * Tests if {@link Action#getAction} works correctly for action 7.
	 */
	@Test
	public void testAction7() {
		assertSame(Action.getAction(7), Action.DROPBOX);
	}
	
	/**
	 * Tests if {@link Action#getAction} fails as intended for negative numbers.
	 */
	@Test
	public void testAction_negative() {
		assertSame(Action.getAction(-1), Action.INVALID);
	}
	
	/**
	 * Tests if {@link Action#getAction} fails as intended for too large numbers.
	 */
	@Test
	public void testAction_tooBig() {
		assertSame(Action.getAction(Action.values().length), Action.INVALID);
	}
}
