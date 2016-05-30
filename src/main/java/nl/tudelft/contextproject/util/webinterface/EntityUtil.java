package nl.tudelft.contextproject.util.webinterface;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import nl.tudelft.contextproject.model.entities.Entity;

/**
 * Class used to make entities usable for the web interface.
 */
public final class EntityUtil {
	private static final int DEFAULT = 0;
	private static final int BOMB = 1;
	private static final int DOOR = 2;
	private static final int KEY = 3;
	private static final int VRPLAYER = 4;
	private static final int PLAYERTRIGGER = 5;
	private static final int PITFALL = 6;
	private static final int LANDMINE = 7;
	private static final int CARROT = 8;
	private static final int KILLERBUNNY = 9;

	/**
	 * Private constructor to avoid initialization.
	 */
	private EntityUtil() {}

	/**
	 * Get the ID for a certain class.
	 *
	 * @param entity
	 * 		the classname of the entity
	 * @return
	 * 		an int representing the entity
	 */
	public static int getJSONCoded(String entity) {
		switch (entity) {
			case "Bomb":
				return BOMB;
			case "Door":
				return DOOR;
			case "Key":
				return KEY;
			case "VRPlayer":
				return VRPLAYER;
			case "PlayerTrigger":
				return PLAYERTRIGGER;
			case "Pitfall":
				return PITFALL;
			case "LandMine":
				return LANDMINE;
			case "Carrot":
				return CARROT;
			case "KillerBunny":
				return KILLERBUNNY;
			default:
				return DEFAULT;
		}
	}
	
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
		entity.put("type", EntityUtil.getJSONCoded(e.getClass().getSimpleName()));
		return entity;
	}
}
