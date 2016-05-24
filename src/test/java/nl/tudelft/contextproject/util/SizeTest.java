package nl.tudelft.contextproject.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

/**
 * Test for the Size class.
 */
public class SizeTest extends TestBase {

	private static final int W = 1;
	private static final int H = 1;
	
	private Size size;
	
	/**
	 * Test setup.
	 */
	@Before
	public void testSetup() {
		size = new Size(W, H);
	}
	
	/**
	 * Test for checking the constructor and getters for Size.
	 */
	@Test
	public void testConstructor() {
		assertEquals(size.getWidth(), W);
		assertEquals(size.getHeight(), H);
	}
	
	/**
	 * Test equal with equal method.
	 */
	@Test
	public void testEqualsEqual() {
		Size testSize = new Size(W, H);
		assertEquals(size, testSize);
	}
	
	/**
	 * Test same object with equal method.
	 */
	@Test
	public void testEqualsSame() {
		assertTrue(size.equals(size));
	}
	
	/**
	 * Test Null object with equal method.
	 */
	@Test
	public void testEqualsNul() {
		assertFalse(size.equals(null));
	}
	
	/**
	 * Test different object with equal method.
	 */
	@Test
	public void testEqualsDifferentClass() {
		assertFalse(size.equals(new Double(0.0)));
	}
	
	/**
	 * Test different width Size with equal method.
	 */
	@Test
	public void testEqualsDifferentWeigth() {
		Size testSize = new Size(W + 1, H);
		assertFalse(size.equals(testSize));
	}
	
	/**
	 * Test different width Size with equal method.
	 */
	@Test
	public void testEqualsDifferentHeigth() {
		Size testSize = new Size(W, H + 1);
		assertFalse(size.equals(testSize));
	}
	
	/**
	 * Test hash code.
	 */
	@Test
	public void testHashCode() {
		assertEquals(size.hashCode(), 993);
	}
}
