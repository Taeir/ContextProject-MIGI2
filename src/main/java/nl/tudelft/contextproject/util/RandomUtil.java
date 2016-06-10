package nl.tudelft.contextproject.util;

import java.util.Random;

/**
 * Contains useful methods for random calls.
 */
public final class RandomUtil {
	
	/**
	 * Private constructor.
	 * To prevent initialization. 
	 */
	private RandomUtil() {}
	
	/**
	 * Return a random number between two values.
	 *
	 * @param rand
	 * 		Random object
	 * @param min
	 *		the minimum value the random number can be
	 * @param max
	 *		the maximum value the random number can be
	 * @return
	 *		the random number
	 */
	public static int getRandomIntegerFromInterval(Random rand, int min, int max) {
		return (rand.nextInt((max - min)) + min);
	}
	
	
}
