package nl.tudelft.contextproject.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test for the Size class.
 */
public class SizeTest {

	/**
	 * Test for checking the constructor and getters for Size.
	 */
	@Test
	public void testConstructor() {
		Size testSize = new Size(3, 5);
		assertEquals(testSize.getWidth(), 3);
		assertEquals(testSize.getHeight(), 5);
	}
}
