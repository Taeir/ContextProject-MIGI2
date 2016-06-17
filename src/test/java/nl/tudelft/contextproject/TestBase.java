package nl.tudelft.contextproject;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mockito;

import nl.tudelft.contextproject.test.TestUtil;

/**
 * Base test class.
 * Each test class should extend this class as it ensures that the Main class is properly set up
 * for testing, and that jMonkey does not generate extensive warnings.
 */
public abstract class TestBase {
	private Main main;
	private boolean mockGame;
	
	/**
	 * Creates the TestBase without mocking the game.
	 */
	public TestBase() {
		this(false);
	}
	
	/**
	 * Creates a TestBase, optionally mocking the game.
	 * 
	 * @param mockGame
	 * 		true if the game should be mocked, false otherwise
	 */
	public TestBase(boolean mockGame) {
		this.mockGame = mockGame;
	}
	
	/**
	 * Sets if the game should be mocked before each test.
	 * 
	 * @param mockGame
	 * 		if the game should be mocked
	 */
	protected void setMockGame(boolean mockGame) {
		this.mockGame = mockGame;
	}
	

	/**
	 * Ensures that the main instance is set up before every test.
	 */
	@Before
	public void setUpMain() {
		main = TestUtil.setupMainForTesting();
		
		if (mockGame) {
			TestUtil.mockGame();
		}
	}

	/**
	 * Ensures that the main instance is cleaned up after every test.
	 */
	@After
	public void tearDownMain() {
		Mockito.reset(main);
		main = null;
		
		TestUtil.cleanupMain();
	}
	
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
		//Ensure strong reference
		Logger.getGlobal();
		//Set logger level
		Logger.getLogger("com.jme3").setLevel(Level.OFF);
		
		//Create a global main for this class
		TestUtil.recreateGlobalMain();
	}
}
