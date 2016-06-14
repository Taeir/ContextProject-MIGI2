package nl.tudelft.contextproject.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.Observer;

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
	 * Test loading an observer that throws a {@link IllegalMonitorStateException} when updated.
	 *
	 * @throws ScriptLoaderException
	 * 		this should not happen
	 */
	@Test (expected = IllegalMonitorStateException.class)
	public void testGetCorrectTickLister() throws ScriptLoaderException {
		Observer observer = scriptLoader.getInstanceOf("TestObserver", Observer.class);
		assertNotNull(observer);
		observer.update(.5f);
	}
	
	/**
	 * Get an Observer from a class that is not an Observer.
	 *
	 * @throws ScriptLoaderException
	 * 		this should happen
	 */
	@Test (expected = ScriptLoaderException.class)
	public void testGetNotATickLister() throws ScriptLoaderException {
		scriptLoader.getInstanceOf("NotAnObserver", Observer.class);
	}
	
	/**
	 * Get an Observer from a file that does not exist.
	 *
	 * @throws ScriptLoaderException
	 * 		this should happen
	 */
	@Test (expected = ScriptLoaderException.class)
	public void testGetNonExistingTickLister() throws ScriptLoaderException {
		scriptLoader.getInstanceOf("IDoNotExist", Observer.class);
	}
	
	/**
	 * Get an Observer from an Observer with private constructor.
	 *
	 * @throws ScriptLoaderException
	 * 		this should happen
	 */
	@Test (expected = ScriptLoaderException.class)
	public void testGetObjectWithPrivateConstructor() throws ScriptLoaderException {
		scriptLoader.getInstanceOf("NotAnObserver", Object.class);
	}
	
	/**
	 * Test loading an Observer that throws a {@link IllegalMonitorStateException} when updated statically.
	 *
	 * @throws ScriptLoaderException
	 * 		this should not happen.
	 */
	@Test (expected = IllegalMonitorStateException.class)
	public void testStaticGetInstance() throws ScriptLoaderException {
		Observer observer = ScriptLoader.getInstanceFrom(ScriptLoaderTest.class.getResource("/").getPath(), "TestObserver", Observer.class);
		observer.update(.5f);
	}
}
