package nl.tudelft.contextproject.model.level.roomIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import nl.tudelft.contextproject.model.level.Room;
import nl.tudelft.contextproject.util.FileUtil;
import nl.tudelft.contextproject.util.ScriptLoaderException;

/**
 * Reads all the rooms in a single map.
 */
public final class MapReader {
	
	/**
	 * Private constructor to avoid instantiation.
	 */
	private MapReader() {}
	
	/**
	 * Read a entire map folder and load all rooms in memory.
	 * @param mapFolder
	 * 			place of room folders and load file
	 * @param rooms
	 * 			list which should hold the room
	 * @param startRoom
	 * 			starting room
	 * @param treasureRoom
	 * 			final maze room
	 * @throws IOException
	 * 			when wrong format is delivered 
	 */
	public void readMap(String mapFolder, List<Room> rooms, Room startRoom, Room treasureRoom) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getMapFile(map)), StandardCharsets.UTF_8))) {
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
