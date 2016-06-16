package nl.tudelft.contextproject.model.level.roomIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.util.ParserUtil;

/**
 * Utility class for reading lights from a file.
 */
public final class LightParser {
	private LightParser() {}
	
	/**
	 * Read the specified amount of lights from the bufferedReader.
	 *
	 * @param lights
	 * 		the list to add all the loaded lights to
	 * @param lightCount
	 * 		the amount of lights to be read
	 * @param br
	 * 		the bufferedReader to load the lights from
	 * @throws IOException
	 * 		when reading from the reader goes wrong
	 */
	public static void readLights(List<Light> lights, int lightCount, BufferedReader br) throws IOException {
		for (int i = 0; i < lightCount; i++) {
			String in = br.readLine();
			if (in == null) throw new IllegalArgumentException("Empty line where some data was expected when loading light[" + i + "].");

			String[] line = in.split(" ");
			if (line.length < 5) throw new IllegalArgumentException("You should at least give a position, type and color.");

			float posx = Float.parseFloat(line[0]);
			float posy = Float.parseFloat(line[1]);
			float posz = Float.parseFloat(line[2]);
			Vector3f position = new Vector3f(posx, posy, posz);
			lights.add(getLight(line[3], position, line[4], line));
		}
	}
	
	/**
	 * Get a light with the given type.
	 *
	 * @param type
	 * 		the type of the light to create
	 * @param position
	 * 		the location of the light
	 * @param color
	 * 		the color of the light in the format 'r/g/b/a'
	 * @param data
	 * 		additional data for creating the lights
	 * @return
	 * 		the created light instance.
	 */
	protected static Light getLight(String type, Vector3f position, String color, String[] data) {
		switch (type) {
			case "AmbientLight":
				AmbientLight ambientLight = new AmbientLight(ParserUtil.getColor(color));
				return ambientLight;
			case "PointLight":
				if (data.length != 6) throw new IllegalArgumentException("You should specify the radius for a pointLight.");

				PointLight pointLight = new PointLight();
				pointLight.setColor(ParserUtil.getColor(color));
				pointLight.setPosition(position);
				pointLight.setRadius(Float.parseFloat(data[5]));
				return pointLight;
			default:
				throw new IllegalArgumentException(type + " is not a known Light type!");
		}
	}
}
