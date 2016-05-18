package nl.tudelft.contextproject.roomIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;

/**
 * Utility class for reading lights from a file.
 */
public final class LightReader {
	private LightReader() {}
	
	/**
	 * Read the specified amount of lights from the bufferedReader.
	 * @param lights The list to add all the loaded lights to.
	 * @param lightCount The amount of lights to be read.
	 * @param xOffset The horizontal offset to apply on the lights.
	 * @param yOffset The vertical offset to apply to the lights.
	 * @param br The bufferedReader to load the lights from.
	 * @throws IOException when reading from the reader goes wrong.
	 */
	public static void readLights(List<Light> lights, int lightCount, int xOffset, int yOffset, BufferedReader br) throws IOException {
		for (int i = 0; i < lightCount; i++) {
			String in = br.readLine();
			if (in == null) throw new IllegalArgumentException("Empty line where some data was expected when loading light[" + i + "].");
			String[] line = in.split(" ");
			if (line.length < 5) throw new IllegalArgumentException("You should at least give a position, type and color.");
			float posx = Float.valueOf(line[0]) + xOffset;
			float posy = Float.valueOf(line[1]);
			float posz = Float.valueOf(line[2]) + yOffset;
			lights.add(getLight(line[3], posx, posy, posz, line[4], line));				
		}
	}
	
	/**
	 * Get a light with the given type.
	 * @param type The type of the light to create.
	 * @param x The x location of the light.
	 * @param y The y location of the light.
	 * @param z The z location of the light.
	 * @param color The color of the light in the format 'r/g/b/a'
	 * @param data Additional data for creating the lights.
	 * @return The created light instance.
	 */
	protected static Light getLight(String type, float x, float y, float z, String color, String[] data) {
		switch (type) {
			case "Ambient":
				AmbientLight al = new AmbientLight(getColor(color));
				return al;
			case "PointLight":
				if (data.length != 6) throw new IllegalArgumentException("You should specify the radius for a pointLight.");
				PointLight pl = new PointLight();
				pl.setColor(getColor(color));
				pl.setRadius(Float.valueOf(data[5]));
				return pl;
	
			default:
				throw new IllegalArgumentException(type + " is not a known Light type!");
		}
	}

	/**
	 * Parse a ColorRGBA from a string describing the color.
	 * @param color The string describing the color.
	 * @return The ColorRGBA generated from the input.
	 */
	protected static ColorRGBA getColor(String color) {
		String[] tmp = color.split("/");
		if (tmp.length != 4) throw new IllegalArgumentException("A color must have 4 components!");
		
		float r = Float.valueOf(tmp[0]);
		float g = Float.valueOf(tmp[1]);
		float b = Float.valueOf(tmp[2]);
		float a = Float.valueOf(tmp[3]);
		
		return new ColorRGBA(r, g, b, a);
	}
}
