package nl.tudelft.contextproject.util.webinterface;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import nl.tudelft.contextproject.model.entities.Entity;

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

		for (Entity e : entities) {
			JSONObject entity = entityToJson(e);
			jArray.put(entity);
		}

		JSONObject entity = entityToJson(player);
		jArray.put(entity);

		return jArray;
	}

	/**
	 * Turn one entity into a json object.
	 *
	 * @param e
	 * 		the entity to turn into a json
	 * @return
	 * 		the json
	 */
	protected static JSONObject entityToJson(Entity e) {
		JSONObject entity = new JSONObject();
		entity.put("x", Math.round(e.getLocation().getX()));
		entity.put("y", Math.round(e.getLocation().getZ()));
		entity.put("type", e.getType().getWebId());
		return entity;
	}
}
