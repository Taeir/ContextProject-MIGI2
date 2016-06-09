package nl.tudelft.contextproject.util.webinterface;

import java.util.Set;

import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.EntityType;
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

		for (Entity entity : entities) {
			if (entity.getType().getWebId() == 0) continue;
			
			JSONObject entityJson = entityToJson(entity);
			jArray.put(entityJson);
		}

		JSONObject entityJson = entityToJson(player);
		jArray.put(entityJson);

		return jArray;
	}

	/**
	 * Turn one entity into a json object.
	 *
	 * @param entity
	 * 		the entity to turn into a json
	 * @return
	 * 		the json
	 */
	protected static JSONObject entityToJson(Entity entity) {
		JSONObject json = new JSONObject();
		json.put("x", Math.round(entity.getLocation().getX()));
		json.put("y", Math.round(entity.getLocation().getZ()));
		json.put("t", entity.getType().getWebId());
		if (entity.getType() == EntityType.BOMB) {
			json.put("d", Math.round(((Bomb) entity).getTimer()));
		}
		return json;
	}
}
