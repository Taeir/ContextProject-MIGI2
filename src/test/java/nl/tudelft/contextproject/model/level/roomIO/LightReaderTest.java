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

/**
 * Test class for the lightReader class.
 */
public class LightReaderTest {

	/**
	 * Test if getting non existent light type throws an exception.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetLightNonExistent() {
		LightReader.getLight("BrightLight", 0, 0, 0, "color", null);
	}
	
	/**
	 * Test if getting a pointlight returns the correct light.
	 */
	@Test
	public void testGetPointLight() {
		String[] data = {"", "", "", "", "", "20"};
		Light res = LightReader.getLight("PointLight", 0, 0, 0, "randomColor", data);
		assertEquals(PointLight.class, res.getClass());
		assertEquals(20f, ((PointLight) res).getRadius(), 1e-10);
	}
	
	/**
	 * Test if getting a point light without radius throws an exception.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetPointLightTooFewArguments() {
		String[] data = {"", "", "", "", ""};
		LightReader.getLight("PointLight", 0, 0, 0, "randomColor", data);
	}
	
	/**
	 * Test if getting a ambient light returns the correct light.
	 */
	@Test
	public void testGetAmbientLight() {
		Light res = LightReader.getLight("AmbientLight", 0, 0, 0, "randomColor", null);
		assertEquals(AmbientLight.class, res.getClass());
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
		String in = "0 1 2 AmbientLight randomColor";
		BufferedReader br = new BufferedReader(new StringReader(in));
		LightReader.readLights(lights, 1, 0, 0, br);
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
		LightReader.readLights(lights, 1, 0, 0, br);
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
		String in = "0 2 AmbientLight randomColor";
		BufferedReader br = new BufferedReader(new StringReader(in));
		LightReader.readLights(lights, 1, 0, 0, br);
	}

}
