package nl.tudelft.contextproject.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import nl.tudelft.contextproject.model.TickListener;

/**
 * Test class for {@link ScriptLoader}.
 */
public class ScriptLoaderTest {

	private ScriptLoader sl;

	/**
	 * Create a fresh instance for each test.
	 * @throws ScriptLoaderException This should not happen.
	 */
	@Before
	public void setUp() throws ScriptLoaderException {
		sl = new ScriptLoader(ScriptLoaderTest.class.getResource("/").getPath());
	}
	
	/**
	 * Test loading a tickListener that throws a {@link IllegalMonitorStateException} when updated.
	 * @throws ScriptLoaderException This should not happen.
	 */
	@Test (expected = IllegalMonitorStateException.class)
	public void testGetCorrectTickLister() throws ScriptLoaderException {
		TickListener tl = sl.getInstanceOf("TestTickListener", TickListener.class);
		assertNotNull(tl);
		tl.update(.5f);
	}
	
	/**
	 * Get a TickListener from a class that is not a tickListener.
	 * @throws ScriptLoaderException This should happen
	 */
	@Test (expected = ScriptLoaderException.class)
	public void testGetNotATickLister() throws ScriptLoaderException {
		sl.getInstanceOf("NotATickListener", TickListener.class);
	}
	
	/**
	 * Get a TickListener from a file that does not exist.
	 * @throws ScriptLoaderException This should happen
	 */
	@Test (expected = ScriptLoaderException.class)
	public void testGetNonExistingTickLister() throws ScriptLoaderException {
		sl.getInstanceOf("IDoNotExist", TickListener.class);
	}
	
	/**
	 * Get a TickListener from a tickListener with private constructor.
	 * @throws ScriptLoaderException This should happen
	 */
	@Test (expected = ScriptLoaderException.class)
	public void testGetObjectWithPrivateConstructor() throws ScriptLoaderException {
		sl.getInstanceOf("NotATickListener", Object.class);
	}
	
	/**
	 * Test loading a tickListener that throws a {@link IllegalMonitorStateException} when updated the static way.
	 * @throws ScriptLoaderException This should not happen.
	 */
	@Test (expected = IllegalMonitorStateException.class)
	public void testStaticGetInstance() throws ScriptLoaderException {
		TickListener tl = ScriptLoader.getInstanceFrom(ScriptLoaderTest.class.getResource("/").getPath(), "TestTickListener", TickListener.class);
		tl.update(.5f);
	}
}
