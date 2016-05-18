package nl.tudelft.contextproject.roomIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import nl.tudelft.contextproject.model.Door;
import nl.tudelft.contextproject.model.Entity;
import nl.tudelft.contextproject.model.Key;

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
	 * @throws IOException when reading from the reader goes wrong.
	 */
	public static void readEntities(List<Entity> entities, int entityCount, int xOffset, int yOffset, BufferedReader br) throws IOException {
		for (int i = 0; i < entityCount; i++) {
			String in = br.readLine();
			if (in == null) throw new IllegalArgumentException("Empty line where some data was expected when loading entity[" + i + "].");
			String[] line = in.split(" ");
			if (line.length < 4) throw new IllegalArgumentException("You must specify at least the location and entity type for entity[" + i + "].");
			float posx = Float.valueOf(line[0]) + xOffset;
			float posy = Float.valueOf(line[1]);
			float posz = Float.valueOf(line[2]) + yOffset;
			entities.add(getEntity(line[3], posx, posy, posz, line));				
		}
	}

	/**
	 * Create an entity with specified type and location.
	 * @param type The type of the entity to create.
	 * @param x The x location of the entity.
	 * @param y The y location of the entity.
	 * @param z The z location of the entity.
	 * @param data The full data array from the file, can be used to get additional information. 
	 * @return The created entity.
	 */
	protected static Entity getEntity(String type, float x, float y, float z, String[] data) {
		Entity e;
		switch (type) {
			case "Key":
				e = new Key();
				break;
			case "Door":
				e = new Door();
				break;
			default:
				throw new IllegalArgumentException(type + " is not a known Entity type!");
		}
		e.move(x, y, z);
		return e;
	}
}
