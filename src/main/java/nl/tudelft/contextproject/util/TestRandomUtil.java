package nl.tudelft.contextproject.util;

/**
 * Temporary test class for RandomUtil.
 */
public final class TestRandomUtil {

	/**
	 * Private constructor to prevent initialization.
	 */
	private TestRandomUtil() {};
	
	/**
	 * Main method.
	 * @param args
	 * 		not used here
	 */
	public static void main(String[] args) {
		String toPrint = RandomUtil.testDistribution(0L, 20, 3.0, 1000000);
		System.out.println(toPrint);
	}

}
