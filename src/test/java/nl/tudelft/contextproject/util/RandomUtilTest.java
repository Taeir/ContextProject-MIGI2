package nl.tudelft.contextproject.util;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

/**
 * Test class for RandomUtil.
 */
public class RandomUtilTest {

	/**
	 * Test a random integer in a interval seeded.
	 */
	@Test
	public void testGetRandomIntegerFromInterval() {
		Random rand = new Random(1L);
		assertEquals(39, RandomUtil.getRandomIntegerFromInterval(rand, 0, 42));
	}
	
	/**
	 * Test a random integer from exponentional distribution.
	 */
	@Test
	public void getRandomIntegerFromExponentionalDistribution1() {
		Random rand = new Random(1L);
		assertEquals(1, RandomUtil.getRandomIntegerFromExponentialDistribution(rand, 1, 5, 1.0));
	}
	
	/**
	 * Test a random integer from exponentional distribution.
	 */
	@Test
	public void getRandomIntegerFromExponentionalDistribution2() {
		Random rand = new Random(43534534L);
		assertEquals(2, RandomUtil.getRandomIntegerFromExponentialDistribution(rand, 1, 5, 2));
	}
	
	/**
	 * Test ensure interval with lower bound.
	 */
	@Test
	public void testEnsureIntervalLowerBound() {
		assertEquals(0, RandomUtil.ensureInterval(0, 0, 5));
	}
	
	/**
	 * Test ensure interval with upper bound.
	 */
	@Test
	public void testEnsureIntervalUpperBound() {
		assertEquals(5, RandomUtil.ensureInterval(5, 0, 5));
	}

	/**
	 * Test ensure interval with within interval.
	 */
	@Test
	public void testEnsureIntervalWithinInterval() {
		assertEquals(2, RandomUtil.ensureInterval(2, 0, 5));
	}
	
	/**
	 * Test ensure interval with outside interval, lower than lower bound.
	 */
	@Test
	public void testEnsureIntervalOutsideIntervalLower() {
		assertEquals(0, RandomUtil.ensureInterval(-5, 0, 5));
	}
	
	/**
	 * Test ensure interval with outside interval, above the upper bound.
	 */
	@Test
	public void testEnsureIntervalOutsideIntervalAbove() {
		assertEquals(5, RandomUtil.ensureInterval(6, 0, 5));
	}
}
