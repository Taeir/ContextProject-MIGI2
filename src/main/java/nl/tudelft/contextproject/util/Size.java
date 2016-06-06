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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Size other = (Size) obj;
		if (height != other.height)
			return false;
		if (width != other.width)
			return false;
		return true;
	}
	
	
}