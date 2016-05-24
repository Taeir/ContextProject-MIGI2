package nl.tudelft.contextproject.model.level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jme3.light.Light;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.level.roomIO.RoomReader;
import nl.tudelft.contextproject.util.Size;

/**
 * Class that represent a room used in level creation.
 * 
 * This class is a wrapper for the level reader so that it can be used with the
 * level creation class.
 */
public class Room {

	private static final Pattern PATTERN = Pattern.compile("(?<width>\\d+)x(?<height>\\d+)_.*");

	//Room size 
	public Size size;

	//List of entities in the room
	public Set<Entity> entities;

	//List of lights in the room
	public List<Light> lights;

	//2d representation of tiles in the room
	public TileType[][] tiles;
	
	//2d representation of mazeTiles in the room
	public MazeTile[][] mazeTiles;

	/**
	 * Constructor will load room from files using RoomIO.
	 * @param folder
	 * 		fileName of the room
	 */
	public Room(String folder) {
		entities = new HashSet<Entity>();
		lights = new ArrayList<Light>();
		try {
			size = getSizeFromFileName(folder);
			tiles = new TileType[size.getWidth()][size.getHeight()];
			mazeTiles = new MazeTile[size.getWidth()][size.getHeight()];
			RoomReader.importFile(folder, mazeTiles, entities, lights, 0, 0);	
		} catch (IOException e) {
			Logger.getLogger("MazeGeneration").severe("Unable to correctly read name!");
		}
	}

	/**
	 * Get size of room from folder name.
	 * @param folder
	 * 		folder of the Room
	 * @return
	 * 		size of room
	 * @throws IOException 
	 * 		if pattern of roomFolder does not match
	 */
	protected Size getSizeFromFileName(String folder) throws IOException {
		int width = 0;
		int height = 0;
		String fileName = RoomReader.getMapFile(folder).getName();
		Matcher m = PATTERN.matcher(fileName);
		if (m.matches()) {
			width = Integer.parseInt(m.group("width"));
			height = Integer.parseInt(m.group("height"));
			return size = new Size(width, height);
		} else {
			throw new IOException("Expected a .crf file present in the folder with the correct name");
		}
	}




}
