package nl.tudelft.contextproject.util;

/**
 * Class for storing a "Size" pair, simply consisting
 * of two integers representing the width and height.
 */
public class Size {
	private final int width;
	private final int height;

	/**
	 * Constructor for creating a "Size" pair.
	 *
	 * @param width
	 * 		the height of the Size
	 * @param height
	 * 		the width of the Size
	 */
	public Size(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * @return
	 * 		the width of the Size
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * @return
	 * 		the height of the Size
	 */
	public int getHeight() {
		return this.height;
	}
}