package nl.tudelft.contextproject.util;

import com.jme3.math.ColorRGBA;

import nl.tudelft.contextproject.TestBase;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for the ReaderUtil class.
 */
public class ParserUtilTest extends TestBase {

	/**
	 * Test if getting a random color gives us a color.
	 */
	@Test
	public void testGetColorRandom() {
		ColorRGBA res = ParserUtil.getColor("randomColor");
		assertNotNull(res);
	}

	/**
	 * Test getting a simple color.
	 */
	@Test
	public void testGetColor() {
		ColorRGBA res = ParserUtil.getColor(".5/.3/.2/.1");
		assertEquals(.5, res.getRed(), 1e-6);
		assertEquals(.3, res.getGreen(), 1e-6);
		assertEquals(.2, res.getBlue(), 1e-6);
		assertEquals(.1, res.getAlpha(), 1e-6);
	}

	/**
	 * Test for getting a color with a missing argument.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetColorToFewArguments() {
		ParserUtil.getColor(".5/.3/.2");
	}
}
