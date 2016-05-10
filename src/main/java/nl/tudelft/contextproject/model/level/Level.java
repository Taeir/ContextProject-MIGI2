package nl.tudelft.contextproject.model.level;

import java.util.LinkedList;
import java.util.List;

import com.jme3.light.Light;

/**
 * Class representing the entire level in the game.
 * An instance of this contains all the tiles in the maze and (in the future)
 * players and other entities.
 */
public class Level {
	private Room[] rooms;
	
	/**
	 * Constructor to create a Level with specific mazeTiles.
	 * @param p The player that is placed in the maze.
	 */
	public Level(Room[] rooms) {
		this.rooms = rooms;
	}

	/**
	 * Getter for the lights.
	 * @return A list with all lights in the scene.
	 */
	public List<Light> getLights() {
		List<Light> lightList = new LinkedList<Light>();
		for (int i = 0; i < rooms.length; i++) {
			lightList.addAll(rooms[i].getLights());
		}
		return lightList;
	}
	
	public Room[] getRooms() {
		return rooms;
	}
}
