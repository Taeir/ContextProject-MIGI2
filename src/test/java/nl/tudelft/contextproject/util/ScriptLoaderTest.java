package nl.tudelft.contextproject.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import nl.tudelft.contextproject.model.TickListener;

public class ScriptLoaderTest {

	private ScriptLoader sl;

	@Before
	public void setUp() throws ScriptLoaderException {
		sl = new ScriptLoader(ScriptLoaderTest.class.getResource("/scripts").getPath());
	}
	
	@Test (expected = IllegalMonitorStateException.class)
	public void testGetCorrectTickLister() throws ScriptLoaderException {
		TickListener tl = sl.getInstanceOf("TestTickListener", TickListener.class);
		assertNotNull(tl);
		tl.update(.5f);
	}
	
	@Test (expected = ScriptLoaderException.class)
	public void testGetNotATickLister() throws ScriptLoaderException {
		sl.getInstanceOf("NotATickListener", TickListener.class);
	}
	
	@Test (expected = ScriptLoaderException.class)
	public void testGetNonExistingTickLister() throws ScriptLoaderException {
		sl.getInstanceOf("IDoNotExist", TickListener.class);
	}
	
	@Test (expected = ScriptLoaderException.class)
	public void testGetObjectWithPrivateConstructor() throws ScriptLoaderException {
		sl.getInstanceOf("NotATickListener", Object.class);
	}
	
	@Test (expected = IllegalMonitorStateException.class)
	public void testStaticGetInstance() throws ScriptLoaderException {
		TickListener tl = ScriptLoader.getInstanceFrom(ScriptLoaderTest.class.getResource("/scripts").getPath(), "TestTickListener", TickListener.class);
		tl.update(.5f);
	}
}
