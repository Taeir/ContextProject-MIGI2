package nl.tudelft.contextproject.model.level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jme3.light.Light;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.level.roomIO.RoomParser;
import nl.tudelft.contextproject.model.level.util.Vec2I;
import nl.tudelft.contextproject.util.Size;

/**
 * Class that represent a room used in level creation.
 * 
 * This class is a wrapper for the level reader so that it can be used with the
 * level creation class.
 */
public class Room {

	/**
	 * Pattern needed to read format from map file.
	 */
	//TODO: Refactor parser classes so that size only is defined at one location.
	private static final Pattern PATTERN = Pattern.compile("(?<width>\\d+)x(?<height>\\d+)_.*");

	public Size size;
	public Set<Entity> entities;
	public List<Light> lights;
	public MazeTile[][] mazeTiles;
	public ArrayList<Vec2I> entranceDoorsLocations;
	public ArrayList<Vec2I> exitDoorLocations;

	/**
	 * Original folder of the map file.
	 */
	public String folder;
	
	/**
	 * Constructor will load room from files using RoomIO.
	 * 
	 * @param folder
	 * 		fileName of the room
	 */
	public Room(String folder) {
		this.folder = folder;
		entities = new HashSet<Entity>();
		lights = new ArrayList<Light>();
		try {
			size = getSizeFromFileName(folder);
			mazeTiles = new MazeTile[size.getWidth()][size.getHeight()];
			RoomParser.importFile(folder, mazeTiles, entities, lights, 0, 0);	
			setDoors();
		} catch (IOException e) {
			Logger.getLogger("MazeGeneration").severe("Unable to correctly read name!");
		}
	}

	/**
	 * Get size of room from folder name.
	 * 
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
		String fileName = RoomParser.getMapFile(folder).getName();
		Matcher m = PATTERN.matcher(fileName);
		if (m.matches()) {
			width = Integer.parseInt(m.group("width"));
			height = Integer.parseInt(m.group("height"));
			return size = new Size(width, height);
		} else {
			throw new IOException("Expected a .crf file present in the folder with the correct name");
		}
	}

	/**
	 * Set the entrance and exit doors of the room.
	 * Needed during graph creation.
	 */
	public void setDoors() {
		entranceDoorsLocations = new ArrayList<Vec2I>();
		exitDoorLocations = new ArrayList<Vec2I>();

		for (int i = 0; i < size.getWidth(); i++) {
			for (int j = 0; j < size.getHeight(); j++) {
				if (mazeTiles[i][j] != null) {
					TileType mazeTile = mazeTiles[i][j].getTileType();
					if (mazeTile == TileType.DOOR_ENTRANCE) {
						entranceDoorsLocations.add(new Vec2I(i, j));
					} else if (mazeTile == TileType.DOOR_EXIT) {
						exitDoorLocations.add(new Vec2I(i, j));
					}
				}
			}
		}
	}

	/**
	 * Set mazeTiles array.
	 * 
	 * @param mazeTiles
	 * 		maze tiles to set
	 */
	public void setMazeTiles(MazeTile[][] mazeTiles) {
		this.mazeTiles = mazeTiles;
	}

	/**
	 * Simple hash code of tiles.
	 */
	@Override
	public int hashCode() {
		return Arrays.deepHashCode(mazeTiles);
	}

	/**
	 * Simple equals used for testing.
	 * Only the TileType[][] array has to be equal;
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Room other = (Room) obj;
		if (!other.size.equals(size)) return false;

		for (int i = 0; i < size.getWidth(); i++) {
			for (int j = 0; j < size.getHeight(); j++) {
				if (mazeTiles[i][j] == null) {
					if (other.mazeTiles[i][j] != null) return false;
				} else if (other.mazeTiles[i][j] != null) {
					return false;
				}
				
				if (mazeTiles[i][j].getTileType() != other.mazeTiles[i][j].getTileType()) return false;
			}
		}

		return true;
	}

	/**
	 * Set size for testing.
	 * 
	 * @param size
	 * 		size to set
	 */
	public void setSize(Size size) {
		this.size = size;
	}

	/**
	 * Create a copy of the room.
	 * 
	 * @return
	 * 		copy of the room.
	 */
	public Room copy() {
		return new Room(folder);
	}
}
