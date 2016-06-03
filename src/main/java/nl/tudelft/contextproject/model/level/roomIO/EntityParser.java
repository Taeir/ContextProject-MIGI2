package nl.tudelft.contextproject.model.level.roomIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.EntityType;
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
			
			String[] data = in.split(" ");
			if (data.length < 4) throw new IllegalArgumentException("You must specify at least the location and entity type for entity[" + i + "].");
			
			//Load the position
			float posx = Float.parseFloat(data[0]) + xOffset;
			float posy = Float.parseFloat(data[1]);
			float posz = Float.parseFloat(data[2]) + yOffset;
			Vector3f position = new Vector3f(posx, posy, posz);

			//Get the entity type
			EntityType type = EntityType.getType(data[3]);
			
			//Store the path into where the entity name used to be.
			data[3] = path;
			
			//Load the entity and add it
			Entity entity = type.loadEntity(position, data);
			entities.add(entity);
		}
	}
}
