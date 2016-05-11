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
     * @param width
     *              The height of the size.
     * @param height
     *              The width of the size.
     */
    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Get the width of the Size.
     * @return
     *          The width of the Size.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Get the height of the Size.
     * @return
     *          The height of the Size.
     */
    public int getHeight() {
        return this.height;
    }
}