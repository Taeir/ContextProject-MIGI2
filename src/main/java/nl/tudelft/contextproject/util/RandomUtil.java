package nl.tudelft.contextproject.util;

import java.util.Random;

/**
 * Contains useful methods for random calls.
 */
public final class RandomUtil {
	
	/**
	 * Private constructor.
	 */
	private RandomUtil() {
		//To prevent initialization.
	}
	
	/**
	 * Return a random number between two values.
	 *
	 * @param rand
	 * 		Random object
	 * @param min
	 *		The minimum value the random number can be
	 * @param max
	 *		The maximum value the random number can be
	 * @return
	 *		The random number
	 */
	protected static int getRandomIntegerFromInterval(Random rand, int min, int max) {
		return (rand.nextInt((max - min)) + min);
	}
}
