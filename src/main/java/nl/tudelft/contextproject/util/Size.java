package nl.tudelft.contextproject.util;

import lombok.Getter;
import lombok.ToString;
import lombok.AllArgsConstructor;

/**
 * Class for storing a pair of width and height values.
 * This class makes use of the lombok ToString, getter and constructor tags, which means
 * these methods are available but aren't described in the code.
 */
@ToString
@AllArgsConstructor
public class Size {
    @Getter private final int width;
    @Getter private final int height;
}