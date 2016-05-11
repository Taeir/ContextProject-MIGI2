package nl.tudelft.contextproject.util;

import lombok.Data;

/**
 * Class for storing a pair of width and height values.
 * This class makes use of the lombok data tag, which means
 * constructors, getters and equals are available.
 */
@Data
public class Size {
    private final int width, height;
}