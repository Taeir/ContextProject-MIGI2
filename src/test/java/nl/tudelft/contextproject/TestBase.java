package nl.tudelft.contextproject;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import nl.tudelft.contextproject.test.TestUtil;

/**
 * Base test class.
 * Each test class should extend this class as it contains lines that every
 * test class should run in @beforeClass. 
 */
public abstract class TestBase {
	
	private static Main main;

	/**
	 * Test Class setup method.
	 * Each test class will run this method.
	 * 
	 * This method will do two things:
	 * 
	 * Firstly, it will turn off the JME logging features, so 
	 * mocked models do not generate WARNING level logs, which makes the test output
	 * more readable.
	 * 
	 * Secondly, ensures that {@link Main#getInstance()} is properly set up before any tests run.
	 * 
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		//Set logger level
		Logger.getLogger("com.jme3").setLevel(Level.OFF);

		//Store the old Main instance
		main = Main.getInstance();

		//Clear the instance
		Main.setInstance(null);

		//Ensure the main is mocked
		TestUtil.ensureMainMocked(true);
	}

	/**
	 * Test Class tear down method
	 * Restores the original Main instance after all tests are done.
	 */
	@AfterClass
	public static void tearDownAfterClass() {
		//Restore the old main
		Main.setInstance(main);
	}


}
