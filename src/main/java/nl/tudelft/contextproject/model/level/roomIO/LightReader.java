package nl.tudelft.contextproject.model.level.roomIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import nl.tudelft.contextproject.util.ReaderUtil;

/**
 * Utility class for reading lights from a file.
 */
public final class LightReader {
	private LightReader() {}
	
	/**
	 * Read the specified amount of lights from the bufferedReader.
	 *
	 * @param lights
	 * 		the list to add all the loaded lights to
	 * @param lightCount
	 * 		the amount of lights to be read
	 * @param xOffset
	 * 		the horizontal offset to apply on the lights
	 * @param yOffset
	 * 		the vertical offset to apply to the lights
	 * @param br
	 * 		the bufferedReader to load the lights from
	 * @throws IOException
	 * 		when reading from the reader goes wrong
	 */
	public static void readLights(List<Light> lights, int lightCount, int xOffset, int yOffset, BufferedReader br) throws IOException {
		for (int i = 0; i < lightCount; i++) {
			String in = br.readLine();
			if (in == null) throw new IllegalArgumentException("Empty line where some data was expected when loading light[" + i + "].");

			String[] line = in.split(" ");
			if (line.length < 5) throw new IllegalArgumentException("You should at least give a position, type and color.");

			float posx = Float.parseFloat(line[0]) + xOffset;
			float posy = Float.parseFloat(line[1]);
			float posz = Float.parseFloat(line[2]) + yOffset;
			lights.add(getLight(line[3], posx, posy, posz, line[4], line));				
		}
	}
	
	/**
	 * Get a light with the given type.
	 *
	 * @param type
	 * 		the type of the light to create
	 * @param x
	 * 		the x location of the light
	 * @param y
	 * 		the y location of the light
	 * @param z
	 * 		the z location of the light
	 * @param color
	 * 		the color of the light in the format 'r/g/b/a'
	 * @param data
	 * 		additional data for creating the lights
	 * @return
	 * 		the created light instance.
	 */
	protected static Light getLight(String type, float x, float y, float z, String color, String[] data) {
		switch (type) {
			case "AmbientLight":
				AmbientLight al = new AmbientLight(ReaderUtil.getColor(color));
				return al;
			case "PointLight":
				if (data.length != 6) throw new IllegalArgumentException("You should specify the radius for a pointLight.");

				PointLight pl = new PointLight();
				pl.setColor(ReaderUtil.getColor(color));
				pl.setRadius(Float.parseFloat(data[5]));
				return pl;
			default:
				throw new IllegalArgumentException(type + " is not a known Light type!");
		}
	}
}
