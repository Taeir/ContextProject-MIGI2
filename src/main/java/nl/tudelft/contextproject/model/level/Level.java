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
	private List<Light> lightList;
	
	/**
	 * Constructor to create a Level with specific rooms.
	 * @param rooms An array of rooms to be placed in the level
	 */
	public Level(Room[] rooms) {
		this.rooms = rooms;
		this.lightList = new LinkedList<>();
	}
	
	/**
	 * Constructor to create a Level with specific rooms and lights.
	 * @param rooms An array of rooms to be placed in the level
	 * @param lights A list of all the lights in the level.
	 */
	public Level(Room[] rooms, List<Light> lights) {
		this.rooms = rooms;
		this.lightList = lights;
	}

	/**
	 * Getter for the lights.
	 * @return A list with all lights in the scene.
	 */
	public List<Light> getLights() {
		return lightList;
	}
	
	/**
	 * Add a light to the level.
	 * @param l Light to ad to the level.
	 */
	public void addLight(Light l) {
		lightList.add(l);
	}
	
	/**
	 * Get all the rooms in this level.
	 * @return An array of rooms.
	 */
	public Room[] getRooms() {
		return rooms;
	}
}
