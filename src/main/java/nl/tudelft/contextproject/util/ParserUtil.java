package nl.tudelft.contextproject.util;

import com.jme3.math.ColorRGBA;

/**
 * Utility used by the roomIO.
 */
public final class ParserUtil {

	/**
	 * Private constructor to avoid instantiation.
	 */
	private ParserUtil() {}

	/**
	 * Parse a ColorRGBA from a string describing the color.
	 *
	 * @param color
	 * 		the string describing the color
	 * @return
	 * 		the ColorRGBA generated from the input
	 */
	public static ColorRGBA getColor(String color) {
		if (color.equals("randomColor")) return ColorRGBA.randomColor();

		String[] tmp = color.split("/");
		if (tmp.length != 4) throw new IllegalArgumentException("A color must have 4 components!");

		float r = Float.parseFloat(tmp[0]);
		float g = Float.parseFloat(tmp[1]);
		float b = Float.parseFloat(tmp[2]);
		float a = Float.parseFloat(tmp[3]);

		return new ColorRGBA(r, g, b, a);
	}
}