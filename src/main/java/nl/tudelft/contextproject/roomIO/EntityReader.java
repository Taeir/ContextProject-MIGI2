package nl.tudelft.contextproject.roomIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.model.Bomb;
import nl.tudelft.contextproject.model.Direction;
import nl.tudelft.contextproject.model.Door;
import nl.tudelft.contextproject.model.Entity;
import nl.tudelft.contextproject.model.Key;
import nl.tudelft.contextproject.model.PlayerTrigger;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.model.WallFrame;
import nl.tudelft.contextproject.util.ScriptLoader;
import nl.tudelft.contextproject.util.ScriptLoaderException;

/**
 * Utility class to read entities from a buffetredReader.
 */
public final class EntityReader {
	private EntityReader() {}
	
	/**
	 * Read the specified amount of entities and add them to the list of entities.
	 * @param entities The list to add all loaded entities to.
	 * @param entityCount The number of entities to load.
	 * @param xOffset The horizontal offset used for all entities.
	 * @param yOffset The vertical offset used for all entities.
	 * @param br The bufferedReader to read from.
	 * @param path The path of the room folder.
	 * @throws IOException When reading from the reader goes wrong.
	 * @throws ScriptLoaderException When loading of a script goes wrong.
	 */
	public static void readEntities(List<Entity> entities, int entityCount, int xOffset, int yOffset, 
			BufferedReader br, String path) throws IOException, ScriptLoaderException {
		for (int i = 0; i < entityCount; i++) {
			String in = br.readLine();
			if (in == null) throw new IllegalArgumentException("Empty line where some data was expected when loading entity[" + i + "].");
			String[] line = in.split(" ");
			
			if (line.length < 4) throw new IllegalArgumentException("You must specify at least the location and entity type for entity[" + i + "].");
			float posx = Float.parseFloat(line[0]) + xOffset;
			float posy = Float.parseFloat(line[1]);
			float posz = Float.parseFloat(line[2]) + yOffset;
			
			entities.add(getEntity(line[3], posx, posy, posz, line, path));				
		}
	}

	/**
	 * Create an entity with specified type and location.
	 * @param type The type of the entity to create.
	 * @param x The x location of the entity.
	 * @param y The y location of the entity.
	 * @param z The z location of the entity.
	 * @param data The full data array from the file, can be used to get additional information.
	 * @param path The path of the file folder. 
	 * @return The created entity.
	 * @throws ScriptLoaderException When the loading of scripts goes wrong.
	 */
	protected static Entity getEntity(String type, float x, float y, float z, String[] data, String path) throws ScriptLoaderException {
		ScriptLoader sl = new ScriptLoader(EntityReader.class.getResource(path).getPath());
		Entity e;
		switch (type) {
			case "Key":
				e = new Key();
				break;
			case "Door":
				e = new Door();
				break;
			case "Bomb":
				e = new Bomb();
				break;
			case "PlayerTrigger":
				if (data.length < 7) throw new IllegalArgumentException("PlayerTrigger must specify at least 7 values.");
				e = new PlayerTrigger(Float.parseFloat(data[4]), Float.parseFloat(data[5]), 
						sl.getInstanceOf(data[6], TickListener.class), new Vector3f(x, y, z));
				return e;
			case "WallFrame":
				e = new WallFrame(new Vector3f(x, y, z), path + data[5], Direction.valueOf(data[4]));
				return e;
			default:
				throw new IllegalArgumentException(type + " is not a known Entity type!");
		}
		e.move(x, y, z);
		return e;
	}
}
