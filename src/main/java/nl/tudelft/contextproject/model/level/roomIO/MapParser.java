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
import nl.tudelft.contextproject.model.level.RoomTuple;
import nl.tudelft.contextproject.util.FileUtil;

/**
 * Reads all the rooms in a single map.
 */
public final class MapParser {
	
	/**
	 * Private constructor to avoid instantiation.
	 */
	private MapParser() {}
	
	/**
	 * Read a entire map folder and load all rooms in memory.
	 * Can have comments starting with # at the top of the .cmf map file.
	 * 
	 * @param mapFolder
	 * 		path of room folders and load file
	 * @param rooms
	 * 		list which should hold the rooms
	 * @return RoomTuple
	 * 		return a room tuple with the starting and end rooms.
	 * @throws IOException
	 * 		when an error occurs while reading the file
	 * @throws IllegalArgumentException
	 * 		when the format is incorrect
	 */
	public static RoomTuple readMap(String mapFolder, List<Room> rooms) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getMapFile(mapFolder)), StandardCharsets.UTF_8))) {
			//Skip the comment header
			String line = br.readLine();
			while (line != null && line.startsWith("#")) {
				line = br.readLine();
			}
			
			//Check format
			if (line == null) throw new IllegalArgumentException("First line must contain <StartRoom> <EndRoom> <Nr of rooms>");
			String[] tmp = line.split(" ");
			
			if (tmp.length != 3) throw new IllegalArgumentException("First line must contain <StartRoom> <EndRoom> <Nr of rooms>");
			
			//Read the rooms
			Room startRoom = new Room(mapFolder + tmp[0] + "/");
			Room treasureRoom = new Room(mapFolder + tmp[1] + "/");

			int noRooms = Integer.parseInt(tmp[2]);
			for (int i = 0; i < noRooms; i++) {
				line = br.readLine();
				if (line == null) throw new IllegalArgumentException("Not enough rooms in list!");
				
				rooms.add(new Room(mapFolder + line + "/"));
			}
			
			return new RoomTuple(startRoom, treasureRoom);
		}
	}
	
	/**
	 * Get the map file with room data from a folder.
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
			if (names[i].endsWith(".cmf")) return FileUtil.getFile(path + names[i]);
		}

		throw new FileNotFoundException("Could not find a '.cmf' file in " + path + ".");
	}
}
