package nl.tudelft.contextproject.model.level.roomIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

import com.jme3.math.Vector3f;


import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Direction;
import nl.tudelft.contextproject.model.entities.Door;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.Key;
import nl.tudelft.contextproject.model.entities.KillerBunny;
import nl.tudelft.contextproject.model.entities.LandMine;
import nl.tudelft.contextproject.model.entities.Pitfall;
import nl.tudelft.contextproject.model.entities.PlayerTrigger;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.model.entities.WallFrame;
import nl.tudelft.contextproject.util.ParserUtil;
import nl.tudelft.contextproject.util.ScriptLoader;
import nl.tudelft.contextproject.util.ScriptLoaderException;

/**
 * Utility class to read entities from a bufferedReader.
 */
public final class EntityParser {

	/**
	 * Private constructor to avoid instantiation.
	 */
	private EntityParser() {}

	/**
	 * Read the specified amount of entities and add them to the set of entities.
	 *
	 * @param entities
	 * 		the set to add all loaded entities to
	 * @param entityCount
	 * 		the number of entities to load
	 * @param xOffset
	 * 		the horizontal offset used for all entities
	 * @param yOffset
	 * 		the vertical offset used for all entities
	 * @param br
	 * 		the bufferedReader to read from
	 * @param path
	 * 		the path of the room folder
	 * @throws IOException
	 * 		when reading from the reader goes wrong
	 * @throws ScriptLoaderException
	 * 		when loading of a script goes wrong
	 */
	public static void readEntities(Set<Entity> entities, int entityCount, int xOffset, int yOffset, 
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
	 *
	 * @param type
	 * 		the type of the entity to create
	 * @param x
	 * 		the x location of the entity
	 * @param y
	 * 		the y location of the entity
	 * @param z
	 * 		the z location of the entity
	 * @param data
	 * 		the full data array from the file, can be used to get additional information
	 * @param path
	 * 		the path of the file folder
	 * @return
	 * 		the created entity
	 * @throws ScriptLoaderException
	 * 		when the loading of scripts goes wrong
	 */
	protected static Entity getEntity(String type, float x, float y, float z, String[] data, String path) throws ScriptLoaderException {
		ScriptLoader sl = new ScriptLoader(EntityParser.class.getResource(path).getPath());
		Entity e;
		if (data == null) throw new IllegalArgumentException("Data is null");

		switch (type) {
			case "Key":
				if (data.length < 5) throw new IllegalArgumentException("Key must specify at least 5 values.");
				e = new Key(ParserUtil.getColor(data[4]));
				break;
			case "Door":
				if (data.length < 5) throw new IllegalArgumentException("Door must specify at least 5 values.");
				e = new Door(ParserUtil.getColor(data[4]));
				break;
			case "Bomb":
				e = new Bomb();
				break;
			case "PlayerTrigger":
				if (data.length < 7) throw new IllegalArgumentException("PlayerTrigger must specify at least 7 values.");
				e = new PlayerTrigger(Float.parseFloat(data[4]), Float.parseFloat(data[5]),	sl.getInstanceOf(data[6], TickListener.class));
				break;
			case "Pitfall":
				if (data.length < 5) throw new IllegalArgumentException("Pitfall must specify at least 4 values.");
				e = new Pitfall(Float.parseFloat(data[4]));
				break;
			case "LandMine":
				e = new LandMine();
				break;
			case "WallFrame":
				if (data.length < 8) throw new IllegalArgumentException("WallFrame must specify at least 8 values.");
				e = new WallFrame(new Vector3f(x, y, z), path + data[5], Direction.valueOf(data[4]), Float.parseFloat(data[6]), Float.parseFloat(data[7]));
				return e;
			case "KillerBunny":
				e = new KillerBunny(new Vector3f(x, y, z));
				return e;
			default:
				throw new IllegalArgumentException(type + " is not a known Entity type!");
		}

		e.move(x, y, z);
		return e;
	}
}
