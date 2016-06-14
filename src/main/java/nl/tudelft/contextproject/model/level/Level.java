package nl.tudelft.contextproject.model.level;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jme3.light.Light;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.model.entities.Entity;

/**
 * Class representing the entire level in the game.
 * An instance of this class contains all the tiles and entities in the maze.
 */
public class Level {
	private MazeTile[][] mazeTiles;
	private List<Light> lightList;
	private Set<Entity> entities;
	private Vector3f playerSpawnPosition;

	/**
	 * Constructor to create a level with specific entities, mazeTiles and lights.
	 *
	 * @param maze
	 * 		the set of tiles to include in the level
	 * @param lights
	 * 		a list with all the lights in the level
	 * @param entities
	 * 		the set of entities to include in the level
	 */
	public Level(MazeTile[][] maze, List<Light> lights, Set<Entity> entities) {
		this(maze, lights, entities, new Vector3f());
	}
	
	/**
	 * Constructor to create a maze with specific entities, mazeTiles, lights and a player spawn
	 * position.
	 *
	 * @param maze
	 * 		the set of tiles to include in the level
	 * @param lights
	 * 		a list with all the lights in the level
	 * @param entities
	 * 		the set of entities to include in the level
	 * @param playerSpawnPosition
	 * 		the position the player will spawn at
	 */
	public Level(MazeTile[][] maze, List<Light> lights, Set<Entity> entities, Vector3f playerSpawnPosition) {
		this.mazeTiles = maze;
		this.lightList = lights;
		this.entities = entities;
		this.playerSpawnPosition = playerSpawnPosition;
	}

	/**
	 * @return
	 * 		the height of the maze
	 */
	public int getHeight() {
		return mazeTiles[0].length;
	}

	/**
	 * @return
	 * 		the width of the maze
	 */
	public int getWidth() {
		return mazeTiles.length;
	}
	
	/**
	 * @return
	 * 		the position at which the player will spawn in this level
	 */
	public Vector3f getPlayerSpawnPosition() {
		return playerSpawnPosition;
	}

	/**
	 * Checks if there is a tile in the maze at the specified position.
	 *
	 * @param x
	 * 		the x-location in the maze
	 * @param y
	 * 		the y-location in the maze
	 * @return
	 * 		true when there is a tile at that position, false otherwise
	 */
	public boolean isTileAtPosition(int x, int y) {
		if (x >= getWidth() || x < 0) return false;
		if (y >= getHeight() || y < 0) return false;
		
		return mazeTiles[x][y] != null;
	}

	/**
	 * Getter for a tile at a certain position.
	 *
	 * @param x
	 * 		the x-location in the maze
	 * @param y
	 * 		the y-location in the maze
	 * @return
	 * 		the tile at the specified location or null when no tile is present
	 */
	public MazeTile getTile(int x, int y) {
		return mazeTiles[x][y];
	}

	/**
	 * @return
	 * 		a list with all lights in the scene
	 */
	public List<Light> getLights() {
		return lightList;
	}
	
	/**
	 * @return
	 * 		the set of entities of this level
	 */
	public Set<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * Returns a JSONObject which represents this level in JSON for the web interface.
	 * 
	 * @return
	 * 		a JSONObject representing this Level
	 */
	public JSONObject toWebJSON() {
		JSONObject json = new JSONObject();

		json.put("width", getWidth());
		json.put("height", getHeight());


		//Store all the tiles in a JSONObject.
		JSONObject jsonTiles = new JSONObject();
		for (int x = 0; x < mazeTiles.length; x++) {
			MazeTile[] column = mazeTiles[x];

			JSONArray jArray = new JSONArray();

			for (MazeTile tile : column) {
				if (tile == null) {
					jArray.put(0);
				} else {
					jArray.put(tile.getTileType().getJsonId());
				}
			}

			jsonTiles.put("" + x, jArray);
		}

		json.put("tiles", jsonTiles);
		
		return json;
	}
	
	/**
	 * Returns a JSONObject with the locations of explored tiles of this Level.
	 * This is used by the web interface.
	 * 
	 * @return
	 * 		a JSONObject with the locations of explored tiles
	 */
	public JSONObject toExploredWebJSON() {
		JSONObject json = new JSONObject();
		for (int x = 0; x < mazeTiles.length; x++) {
			MazeTile[] column = mazeTiles[x];

			JSONArray jArray = new JSONArray();
			
			for (int y = 0; y < column.length; y++) {
				//We are only interested in explored tiles
				if (column[y] == null || !column[y].isExplored()) continue;
				
				jArray.put(y);
			}

			if (jArray.length() != 0) {
				json.put("" + x, jArray);
			}
		}
		
		return json;
	}
}