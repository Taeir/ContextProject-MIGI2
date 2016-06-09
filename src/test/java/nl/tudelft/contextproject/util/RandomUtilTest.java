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

}
