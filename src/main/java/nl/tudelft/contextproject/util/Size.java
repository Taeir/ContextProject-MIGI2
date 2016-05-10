package nl.tudelft.contextproject.util;

import lombok.Getter;
import lombok.ToString;

/**
 * Class for storing a pair of width and height values.
 * This class makes use of the lombok ToString and getter tags, which means
 * these methods are available but aren't described in the code.
 */
@ToString
public class Size {
    @Getter private final int width;
    @Getter private final int height;

    /**
     * Create a "Size" pair.
     * @param width - The width contained in the pair.
     * @param height - The height contained in the pair.
     */
    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
