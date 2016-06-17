package nl.tudelft.contextproject.model.level.roomIO;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.TestBase;

/**
 * Test class for the lightReader class.
 */
public class LightParserTest extends TestBase {

	/**
	 * Test if getting non existent light type throws an exception.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetLightNonExistent() {
		LightParser.getLight("BrightLight", new Vector3f(), "color", null);
	}
	
	/**
	 * Test if getting a pointlight returns the correct light.
	 */
	@Test
	public void testGetPointLight() {
		String[] data = {"", "", "", "", "", "20"};
		Light light = LightParser.getLight("PointLight", new Vector3f(), "randomColor", data);
		assertEquals(PointLight.class, light.getClass());
		assertEquals(20f, ((PointLight) light).getRadius(), 1e-10);
	}
	
	/**
	 * Test if getting a point light without radius throws an exception.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetPointLightTooFewArguments() {
		String[] data = {"", "", "", "", ""};
		LightParser.getLight("PointLight", new Vector3f(), "randomColor", data);
	}
	
	/**
	 * Test if getting a ambient light returns the correct light.
	 */
	@Test
	public void testGetAmbientLight() {
		Light light = LightParser.getLight("AmbientLight", new Vector3f(), "randomColor", null);
		assertEquals(AmbientLight.class, light.getClass());
	}
	
	/**
	 * Read an ambient light from a string.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test
	public void testReadLights() throws IOException {
		ArrayList<Light> lights = new ArrayList<>();
		String input = "0 1 2 AmbientLight randomColor";
		BufferedReader br = new BufferedReader(new StringReader(input));
		LightParser.readLights(lights, 1, br);
		assertEquals(1, lights.size());
		assertEquals(AmbientLight.class, lights.get(0).getClass());
	}
	
	/**
	 * Read an empty string.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadLightsEmpty() throws IOException {
		ArrayList<Light> lights = new ArrayList<>();
		String in = "";
		BufferedReader br = new BufferedReader(new StringReader(in));
		LightParser.readLights(lights, 1, br);
	}
	
	/**
	 * Read a string with too few arguments.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testReadLightsTooFewArguments() throws IOException {
		ArrayList<Light> lights = new ArrayList<>();
		String input = "0 2 AmbientLight randomColor";
		BufferedReader br = new BufferedReader(new StringReader(input));
		LightParser.readLights(lights, 1, br);
	}

}
