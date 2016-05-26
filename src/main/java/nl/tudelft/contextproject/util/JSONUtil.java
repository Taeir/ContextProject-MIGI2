package nl.tudelft.contextproject.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Set;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.util.webinterface.EntityUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create easy overhead for loading and saving of JSON objects.
 */
public final class JSONUtil {

	/**
	 * Private constructor to avoid initialisation.
	 */
	private JSONUtil() {}

	/**
	 * Parse a .json file to a {@link JSONObject} for easy manipulation of data.
	 *
	 * @param file
	 * 		the file which one wants to load
	 * @return
	 * 		the JSONObject contained in the file
	 * @throws IOException
	 * 		file is not found.
	 */
	public static JSONObject load(File file) throws IOException {
		byte[] asBytes = Files.readAllBytes(file.toPath());
		String asString = new String(asBytes, Charset.forName("UTF-8"));
		return new JSONObject(asString);
	}

	/**
	 * Write a JSON object to a desired file.
	 *
	 * @param jsObject
	 * 		the object to write
	 * @param file
	 * 		the file to write to
	 * @throws IOException
	 * 		writing the file goes wrong
	 * @throws JSONException
	 * 		the JSON object is not in the correct format
	 */
	public static void save(JSONObject jsObject, File file) throws IOException, JSONException {
		JSONObject.testValidity(jsObject);

		OutputStreamWriter oWriter =
				new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"));
		BufferedWriter bWriter = new BufferedWriter(oWriter);
		bWriter.write(jsObject.toString(4));
		bWriter.close();
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
