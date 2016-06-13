package nl.tudelft.contextproject.util;

import java.util.ArrayList;
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
		double randomDoubleFromUniformeDistrubtion = rand.nextDouble();
		double doubleResult = -Math.log(1 - (1 - Math.exp(-lambda)) * randomDoubleFromUniformeDistrubtion) / lambda;
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
	
	/**
	 * Test method for testing the correctness of the exponentional distribution.
	 * 
	 * @param rand
	 * 		the integer to check
	 * @param lambda
	 * 		the parameter of the exponential distribution
	 * @param max
	 * 		maximum value
	 * @param numberOfSimulations
	 * 		the number of simulations that have to run
	 * @return
	 * 		the integer if it is in the interval, the lower bound if it is below the interval and the upper bound if it is above the interval
	 
	 */
	public static ArrayList<Integer> testExponentialDistribution(Random rand, int max, double lambda, int numberOfSimulations) {
		ArrayList<Integer> resultList = new ArrayList<Integer>(max + 1);
		
		for (int i = 0; i < numberOfSimulations; i++) {
			resultList.add(i, 0);
		}
		
		int simulation;
		for (int i = 0; i < numberOfSimulations; i++) {
			simulation = RandomUtil.getRandomIntegerFromExponentialDistribution(rand, 0, max, lambda);
			resultList.add(simulation, resultList.get(simulation) + 1);
		} 
		
		return resultList;
	}
	
	/**
	 * Test method that creates a string which shows the simulation of the exponential distribution.
	 * 
	 * @param seed
	 * 		the seed for the random
	 * @param max
	 * 		the maximum number that can be returned
	 * @param lambda
	 * 		the parameter of the exponential distribution
	 * @param numberOfSimulations
	 * 		the total number of simulations that should be run.
	 * @return
	 * 		a string that can be printed that shows the distribution.
	 */
	public static String testDistribution(Long seed, int max, double lambda, int numberOfSimulations) {
		Random rand = new Random(seed);
		ArrayList<Integer> resultList = RandomUtil.testExponentialDistribution(rand, max, lambda, numberOfSimulations);
		int size = resultList.size();
		int total = 0;
		for (Integer number : resultList) {
			total += number;
		}
		
		String result = "Test results of Test distribution = \n";
		for (int i = 0; i < size; i++) {
			result += "For the number " + i + " were " + resultList.get(i) + " occurenses, which is " + resultList.get(i) * 100.0 / total + "%.\n";
		}
		return result;
	}
}
