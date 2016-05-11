package nl.tudelft.contextproject.model.level.temporaryRandomGenerator;

import java.util.Random;

/**
 * Class which is used to create random levels.
 */
public final class GeneratorHelper {
    private static Random rand;

    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST;

        /**
         * Get the direction for a Direction if it is rotated right "times" times.
         * @param times
         *          The amount of times to rate right.
         * @return
         *          The direction found after the rotation.
         */
        public Direction getRotated(int times) {
            return values()[((ordinal()) + times) % 4];
        }
    }

    /**
     * Avoid instantiation of the helper.
     */
    private GeneratorHelper() {}

    /**
     * Return a random number between *and including* two values.
     * @param min
     *          The minimum value the random number can be.
     * @param max
     *          The maximum value the random number can be.
     * @return
     *          The random number.
     */
    public static int getRandom(int min, int max) {
        if (rand == null) {
            createRNG(System.currentTimeMillis());
        }
        return (rand.nextInt((max - min) + 1) + min);
    }

    /**
     * If the random number generator has not been instantiated yet,
     * created one with the given seed.
     * @param seed
     *          The seed to use for creation of the RNG.
     */
    public static void createRNG(long seed) {
        if (rand == null) {
            rand = new Random(seed);
        }
    }
}
