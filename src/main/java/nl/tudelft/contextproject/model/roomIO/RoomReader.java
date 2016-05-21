package nl.tudelft.contextproject.model.roomIO;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

import com.jme3.light.Light;

import nl.tudelft.contextproject.util.FileUtil;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.util.ScriptLoaderException;

/**
 * Utility class for loading rooms/levels from file.
 */
public final class RoomReader {

	/**
	 * Private constructor to avoid instantiation.
	 */
	private RoomReader() {}
	
	/**
	 * Import a room from the specified file.
	 * Note that you can also use this method to load entire levels by using a (0,0) offset.
	 *
	 * @param folder
	 * 		the folder to load the room from
	 * @param tiles
	 * 		the MazeTile array to store the loaded tiles in
	 * @param entities
	 * 		the set to add all the loaded entities to
	 * @param lights
	 * 		the list to add all the loaded lights to
	 * @param xOffset
	 * 		the horizontal offset that is used for moving all loaded items
	 * @param yOffset
	 * 		the vertical offset that is used for moving all loaded items
	 * @throws IOException
	 * 		when something goes wrong
	 */
	public static void importFile(String folder, MazeTile[][] tiles, Set<Entity> entities, List<Light> lights, int xOffset, int yOffset) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getMapFile(folder)), "UTF-8"))) {
			String line = br.readLine();

			while (line != null && line.startsWith("#")) {
				line = br.readLine();
			}

			if (line == null) throw new IllegalArgumentException("The file cannot be empty!");

			String[] tmp = line.split(" ");
			if (tmp.length != 4) throw new IllegalArgumentException("You should specify the width , height, entity- and light count.");
			
			int width = Integer.parseInt(tmp[0]);
			int height = Integer.parseInt(tmp[1]);			
			checkDimensions(width + xOffset, height + yOffset, tiles);
			
			//TODO support rotations?
			TileReader.readTiles(tiles, width, height, xOffset, yOffset, br);

			try {
				EntityReader.readEntities(entities, Integer.parseInt(tmp[2]), xOffset, yOffset, br, folder);
			} catch (ScriptLoaderException e) {
				e.printStackTrace();
			}

			LightReader.readLights(lights, Integer.parseInt(tmp[3]), xOffset, yOffset, br);
		}
	}
	
	/**
	 * Check if the room will fit in the level.
	 *
	 * @param width
	 * 		the right most position of the room to be loaded
	 * @param height
	 * 		the bottom most position of the room to be loaded
	 * @param tiles
	 * 		the MazeTile array that must hold the room
	 */
	protected static void checkDimensions(int width, int height, MazeTile[][] tiles) {
		if (tiles.length < width) {
			throw new IllegalArgumentException("This room gets wider than the level! Choose a smaller horizontal offset or a smaller room.");
		}
		if (tiles[0].length < height) {
			throw new IllegalArgumentException("This room gets higher than the level! Choose a smaller vertical offset or a smaller room.");
		}
	}
	
	/**
	 * Get the file with room data from a folder.
	 * Uses the FileUtil class to safely retrieve the file.
	 * 
	 * @param path
	 * 		the folder to look in
	 * @return
	 * 		the file with the room data
	 * @throws FileNotFoundException
	 * 		when the file is not found in the specified folder
	 */
	public static File getMapFile(String path) throws FileNotFoundException {
		String[] names = FileUtil.getFileNames(path);
		
		if (names == null) throw new FileNotFoundException(path + " is not a folder.");

		for (int i = 0; i < names.length; i++) {
			if (names[i].endsWith(".crf")) return FileUtil.getFile(path + names[i]);
		}

		throw new FileNotFoundException("Could not find a '.crf' file in " + path + ".");
	}
}
