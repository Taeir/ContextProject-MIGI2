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
	
	/**
	 * Return a random integer that is equal or lower than the max Integer and from an exponential probability distribution.
	 * The parameter lambda is of the distribution is equal to the lambda parameter in the method.
	 * Uses the ensureInterval method to return an integer that is not higher than max Integer.
	 *  
	 * @param rand
	 * 		the Random generator to use
	 * @param min
	 * 		minimum value that can be returned
	 * @param max
	 * 		maximum value that can be returned
	 * @param lambda
	 * 		the parameter of the exponential distribution
	 * @return
	 * 		the integer between zero and max, with generated from the exponential distribution
	 */
	public static int getRandomIntegerFromExponentialDistribution(Random rand, int min, int max, double lambda) {
		double doubleResult = Math.log(1 - rand.nextDouble()) / (-lambda);
		return RandomUtil.ensureInterval((int) Math.round(doubleResult), min, max);
	}

	/**
	 * Return an integer that is equal or higher than the lowerBound or equal and lower than the upperBound.
	 * 
	 * @param inputInteger
	 * 		the integer to check
	 * @param lowerBound
	 * 		lower bound of the interval
	 * @param upperBound
	 * 		upper bound of the interval
	 * @return
	 * 		the integer if it is in the interval, the lower bound if it is below the interval and the upper bound if it is above the interval
	 */
	public static int ensureInterval(int inputInteger, int lowerBound, int upperBound) {
		if (inputInteger < lowerBound) {
			return lowerBound;
		} else if (inputInteger > upperBound) {
			return upperBound;
		} else {
			return inputInteger;
		}
	}
}
