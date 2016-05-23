package nl.tudelft.contextproject.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

/**
 * Test for the Size class.
 */
public class SizeTest extends TestBase {

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
