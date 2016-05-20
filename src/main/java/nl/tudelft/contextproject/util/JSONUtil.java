package nl.tudelft.contextproject.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Set;

import nl.tudelft.contextproject.model.Entity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create easy overhead for loading and saving of JSON objects.
 */
public final class JSONUtil {

    /**
     * Avoid initialization.
     */
    private JSONUtil() {}

    /**
     * Parse a .json file to a {@link JSONObject} for easy manipulation of data.
     * @param file - The file which one wants to load.
     * @return - The JSONObject contained in the file.
     * @throws IOException - File is not found.
     */
    public static JSONObject load(File file) throws IOException {
        byte[] asBytes = Files.readAllBytes(file.toPath());
        String asString = new String(asBytes, Charset.forName("UTF-8"));
        return new JSONObject(asString);
    }

    /**
     * Write a JSON object to a desired file.
     * @param jsObject - The object to write.
     * @param file - The file to write to.
     * @throws IOException - Writing the file goes wrong.
     * @throws JSONException - The JSON object is not in the correct format.
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
     * Convert a set of entities to a JSONObject representing this list.
     * 
     * @param entities
     *          the entities to convert
     * @return
     *          a JSONObject representing the entities
     */
    public static JSONObject entitiesToJson(Set<Entity> entities) {
        JSONObject json = new JSONObject();

        JSONArray jArray = new JSONArray();
        for (Entity e : entities) {
            JSONObject entity = new JSONObject();
            entity.put("x", (int) Math.floor(e.getLocation().getX()));
            entity.put("y", (int) Math.floor(e.getLocation().getZ()));
            entity.put("type", EntityUtil.getJSONCoded(e.getClass().getSimpleName()));
            jArray.put(entity);
        }

        json.put("entities", jArray);
        return json;
    }
}
