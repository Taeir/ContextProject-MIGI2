package nl.tudelft.contextproject.model.level.roomIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.moving.VRPlayer;
import nl.tudelft.contextproject.model.entities.util.EntityType;
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
	 * @param br
	 * 		the bufferedReader to read from
	 * @param path
	 * 		the path of the room folder
	 * @throws IOException
	 * 		when reading from the reader goes wrong
	 * @throws ScriptLoaderException
	 * 		when loading of a script goes wrong
	 */
	public static void readEntities(Set<Entity> entities, int entityCount, BufferedReader br, String path) throws IOException, ScriptLoaderException {

		for (int i = 0; i < entityCount; i++) {
			String in = br.readLine();
			if (in == null) throw new IllegalArgumentException("Empty line where some data was expected when loading entity[" + i + "].");
			
			String[] data = in.split(" ");
			if (data.length < 4) throw new IllegalArgumentException("You must specify at least the location and entity type for entity[" + i + "].");
			
			//Load the position
			float posx = Float.parseFloat(data[0]);
			float posy = Float.parseFloat(data[1]);
			float posz = Float.parseFloat(data[2]);
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
	
	/**
	 * Removes all VRPlayers from the given collection, chooses a random player from these, and
	 * returns it's location.
	 * 
	 * <p>This method allows setting the player spawn location in the room files. This can be done
	 * by simply adding a player entity in the room file with its location:
	 * <pre>1 2 1 Player</pre>
	 * 
	 * <p>If there are no player entities in the given list, this method returns the location
	 * <code>(0, 0, 0)</code>.
	 * 
	 * @param entities
	 * 		the entities to work with
	 * @param random
	 * 		the random to use for determining the player location
	 * 
	 * @return
	 * 		a randomly selected player spawn location
	 */
	public static Vector3f getPlayerSpawnLocation(Collection<Entity> entities, Random random) {
		List<VRPlayer> players = new ArrayList<>();
		Iterator<Entity> it = entities.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			if (!(entity instanceof VRPlayer)) continue;
			
			players.add((VRPlayer) entity);
			it.remove();
		}
		
		if (players.isEmpty()) return new Vector3f();
		
		//Choose a random player
		int index = random.nextInt(players.size());
		return players.get(index).getLocation();
	}
}
