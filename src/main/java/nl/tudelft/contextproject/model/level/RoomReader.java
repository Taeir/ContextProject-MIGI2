package nl.tudelft.contextproject.model.level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;

import nl.tudelft.contextproject.model.Door;
import nl.tudelft.contextproject.model.Entity;
import nl.tudelft.contextproject.model.Key;

public class RoomReader {
	public static void importFile(File file, MazeTile[][] tiles, List<Entity> entities, List<Light> lights, int xOffset, int yOffset) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			while (line != null && line.startsWith("#")) {
				line = br.readLine();
			}
			if (line == null) throw new IllegalArgumentException("The file cannot be empty!");
			String[] tmp = line.split(" ");
			int width = Integer.valueOf(tmp[0]);
			int height = Integer.valueOf(tmp[1]);
			int entityCount = Integer.valueOf(tmp[2]);
			int lightCount = Integer.valueOf(tmp[3]);
			
			readTiles(tiles, width, height, xOffset, yOffset, br);
			readEntities(entities, entityCount, xOffset, yOffset, br);
			readLights(lights, lightCount, xOffset, yOffset, br);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	private static void readLights(List<Light> lights, int lightCount, int xOffset, int yOffset, BufferedReader br) throws IOException {
		for (int i = 0; i < lightCount; i++) {
			String in = br.readLine();
			if (in == null) throw new IllegalArgumentException(""); //TODO
			String[] line = in.split(" ");
			if (line.length < 4) throw new IllegalArgumentException(""); //TODO
			float posx = Float.valueOf(line[0]) + xOffset;
			float posy = Float.valueOf(line[1]);
			float posz = Float.valueOf(line[2]) + yOffset;
			lights.add(getLight(line[3], posx, posy, posz, line[4], line));				
		}
	}

	private static Light getLight(String type, float x, float y, float z, String color, String[] line) {
		switch (type) {
		case "Ambient":
			AmbientLight al = new AmbientLight(getColor(color));
			return al;
		case "PointLight":
			PointLight pl = new PointLight();
			pl.setColor(getColor(color));
			pl.setRadius(Float.valueOf(line[5]));
			return pl;

		default:
			throw new IllegalArgumentException(type + " is not a known Entity type!");
	}
	}

	private static ColorRGBA getColor(String color) {
		String[] tmp = color.split("/");
		if (tmp.length != 4) throw new IllegalArgumentException("A color must have 4 components");
		
		float r = Float.valueOf(tmp[0]);
		float g = Float.valueOf(tmp[1]);
		float b = Float.valueOf(tmp[2]);
		float a = Float.valueOf(tmp[3]);
		
		return new ColorRGBA(r, g, b, a);
	}

	private static void readEntities(List<Entity> entities, int entityCount, int xOffset, int yOffset, BufferedReader br) throws IOException {
		for (int i = 0; i < entityCount; i++) {
			String in = br.readLine();
			if (in == null) throw new IllegalArgumentException(""); //TODO
			String[] line = in.split(" ");
			if (line.length < 3) throw new IllegalArgumentException(""); //TODO
			int posx = Integer.valueOf(line[0]) + xOffset;
			int posy = Integer.valueOf(line[1]) + yOffset;
			entities.add(getEntity(line[2], posx, posy, line));				
		}
	}

	private static Entity getEntity(String type, int x, int y, String[] line) {
		switch (type) {
			case "Key":
				Key key = new Key();
				key.move(x, 0, y);
				return key;
			case "Door":
				Door d = new Door();
				d.move(x, 0, y);
				return d;
	
			default:
				throw new IllegalArgumentException(type + " is not a known Entity type!");
		}
	}

	private static void readTiles(MazeTile[][] tiles, int width, int height, int xOffset, int yOffset, BufferedReader br) throws IOException {
		//TODO support rotations?
		for (int y = 0; y < height; y++) {
			String in = br.readLine();
			if (in == null) throw new IllegalArgumentException(""); //TODO
			String[] line = in.split(" ");
			if (line.length < width) throw new IllegalArgumentException(""); //TODO
			for (int x = 0; x < line.length; x++) {
				int posx = x + xOffset;
				int posy = y + yOffset;
				tiles[posx][posy] = new MazeTile(posx, posy, TileType.valueOf(line[x]));
			}
		}
	}
}
