package nl.tudelft.contextproject.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for the ClassLoaderException.
 */
public class ScriptLoaderExceptionTest {

	/**
	 * Test if constructing with messages stores the message correctly.
	 */
	@Test
	public void testConstructor() {
		ScriptLoaderException e = new ScriptLoaderException("hello");
		assertEquals("hello", e.getMessage());
	}

}
