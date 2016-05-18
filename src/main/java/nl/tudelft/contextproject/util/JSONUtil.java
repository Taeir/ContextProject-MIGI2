package nl.tudelft.contextproject.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import nl.tudelft.contextproject.model.Entity;
import nl.tudelft.contextproject.model.level.Level;
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

    public static JSONObject entitiesToJson(List<Entity> entities, Level level) {
        JSONObject json = new JSONObject();
        json.put("width", level.getWidth());
        json.put("height", level.getHeight());

        int[][] located = new int[level.getWidth()][level.getHeight()];
        JSONObject jsonEntities = new JSONObject();

        for (Entity e : entities) {
            located[(int)Math.floor(e.getLocation().getX())]
                    [(int)Math.floor(e.getLocation().getZ())] = 1;
        }

        for (int x = 0; x < located.length; x++) {
            JSONArray jArray = new JSONArray();
            for (int y = 0; y < located[0].length; y++) {
                jArray.put(located[x][y]);
            }
            jsonEntities.put("" + x, jArray);
        }

        json.put("entities", jsonEntities);

        return json;
    }
}
