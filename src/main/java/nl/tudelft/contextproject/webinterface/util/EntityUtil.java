package nl.tudelft.contextproject.webinterface.util;

import java.util.Set;

import org.json.JSONArray;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.exploding.Bomb;

/**
 * Class used to make entities usable for the web interface.
 */
public final class EntityUtil {

	/**
	 * Private constructor to avoid initialization.
	 */
	private EntityUtil() { }
	
	/**
	 * Convert a set of entities to a JSONArray representing this list.
	 *
	 * @param entities
	 * 		the entities to convert
	 * @param player
	 * 		the player
	 * @return
	 * 		a JSONArray representing the entities
	 */
	public static JSONArray entitiesToJson(Set<Entity> entities, Entity player) {
		JSONArray jArray = new JSONArray();

		for (Entity entity : entities) {
			if (entity.getType().getWebId() == 0) continue;
			
			JSONArray entityJson = entityToJson(entity);
			jArray.put(entityJson);
		}

		JSONArray entityJson = entityToJson(player);
		jArray.put(entityJson);

		return jArray;
	}

	/**
	 * Turn one entity into a json array.
	 *
	 * @param entity
	 * 		the entity to turn into json
	 * @return
	 * 		the json
	 */
	protected static JSONArray entityToJson(Entity entity) {
		JSONArray json = new JSONArray();
		json.put(entity.getType().getWebId());
		json.put(Math.round(entity.getLocation().getX()));
		json.put(Math.round(entity.getLocation().getZ()));
		if (entity instanceof Bomb) {
			json.put(Math.round(((Bomb) entity).getTimer()));
		}
		return json;
	}
}
