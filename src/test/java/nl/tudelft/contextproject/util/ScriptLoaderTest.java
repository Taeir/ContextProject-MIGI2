package nl.tudelft.contextproject.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.TickListener;

/**
 * Test class for {@link ScriptLoader}.
 */
public class ScriptLoaderTest extends TestBase {

	private ScriptLoader scriptLoader;

	/**
	 * Create a fresh instance for each test.
	 *
	 * @throws ScriptLoaderException
	 * 		this should not happen
	 */
	@Before
	public void setUp() throws ScriptLoaderException {
		scriptLoader = new ScriptLoader(ScriptLoaderTest.class.getResource("/").getPath());
	}
	
	/**
	 * Test loading a tickListener that throws a {@link IllegalMonitorStateException} when updated.
	 *
	 * @throws ScriptLoaderException
	 * 		this should not happen
	 */
	@Test (expected = IllegalMonitorStateException.class)
	public void testGetCorrectTickLister() throws ScriptLoaderException {
		TickListener tickListener = scriptLoader.getInstanceOf("TestTickListener", TickListener.class);
		assertNotNull(tickListener);
		tickListener.update(.5f);
	}
	
	/**
	 * Get a TickListener from a class that is not a tickListener.
	 *
	 * @throws ScriptLoaderException
	 * 		this should happen
	 */
	@Test (expected = ScriptLoaderException.class)
	public void testGetNotATickLister() throws ScriptLoaderException {
		scriptLoader.getInstanceOf("NotATickListener", TickListener.class);
	}
	
	/**
	 * Get a TickListener from a file that does not exist.
	 *
	 * @throws ScriptLoaderException
	 * 		this should happen
	 */
	@Test (expected = ScriptLoaderException.class)
	public void testGetNonExistingTickLister() throws ScriptLoaderException {
		scriptLoader.getInstanceOf("IDoNotExist", TickListener.class);
	}
	
	/**
	 * Get a TickListener from a tickListener with private constructor.
	 *
	 * @throws ScriptLoaderException
	 * 		this should happen
	 */
	@Test (expected = ScriptLoaderException.class)
	public void testGetObjectWithPrivateConstructor() throws ScriptLoaderException {
		scriptLoader.getInstanceOf("NotATickListener", Object.class);
	}
	
	/**
	 * Test loading a tickListener that throws a {@link IllegalMonitorStateException} when updated the static way.
	 *
	 * @throws ScriptLoaderException
	 * 		this should not happen.
	 */
	@Test (expected = IllegalMonitorStateException.class)
	public void testStaticGetInstance() throws ScriptLoaderException {
		TickListener tickListener = ScriptLoader.getInstanceFrom(ScriptLoaderTest.class.getResource("/").getPath(), "TestTickListener", TickListener.class);
		tickListener.update(.5f);
	}
}
