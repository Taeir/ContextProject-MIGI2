package nl.tudelft.contextproject.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

/**
 * Test for the Size class.
 */
public class SizeTest extends TestBase {

	private static final int WIDTH = 1;
	private static final int HEIGHT = 1;
	
	private Size size;
	
	/**
	 * Test setup.
	 */
	@Before
	public void testSetup() {
		size = new Size(WIDTH, HEIGHT);
	}
	
	/**
	 * Test for checking the constructor and getters for Size.
	 */
	@Test
	public void testConstructor() {
		assertEquals(size.getWidth(), WIDTH);
		assertEquals(size.getHeight(), HEIGHT);
	}
	
	/**
	 * Test equal with equal method.
	 */
	@Test
	public void testEqualsEqual() {
		Size testSize = new Size(WIDTH, HEIGHT);
		assertEquals(size, testSize);
	}
	
	/**
	 * Test same object with equal method.
	 * 
	 * NOTE: This test might seem pointless, but it tests our equals method for same objects.
	 */
	@Test
	public void testEqualsSame() {
		assertEquals(size, size);
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
		Size testSize = new Size(WIDTH + 1, HEIGHT);
		assertFalse(size.equals(testSize));
	}
	
	/**
	 * Test different width Size with equal method.
	 */
	@Test
	public void testEqualsDifferentHeigth() {
		Size testSize = new Size(WIDTH, HEIGHT + 1);
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
