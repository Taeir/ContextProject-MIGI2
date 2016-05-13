package nl.tudelft.contextproject.model.level;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jme3.light.Light;

/**
 * Class representing the entire level in the game.
 * An instance of this contains all the tiles in the maze and (in the future)
 * players and other entities.
 */
public class Level {
	private MazeTile[][] mazeTiles;
	private List<Light> lightList;

	/**
	 * Constructor to create a maze with specific mazeTiles.
	 * @param maze The set of tiles to include in the maze.
	 * @param lights A list with all the lights in the level.
	 */
	public Level(MazeTile[][] maze, List<Light> lights) {
		this.mazeTiles = maze;
		this.lightList = lights;
	}

	/**
	 * Getter for the height of the maze.
	 * @return the height of the maze.
	 */
	public int getHeight() {
		return mazeTiles[0].length;
	}

	/**
	 * Getter for the width of the maze.
	 * @return the width of the maze.
	 */
	public int getWidth() {
		return mazeTiles.length;
	}

	/**
	 * Checks if there is a tile in the maze at the specified position.
	 * @param x the x-location in the maze
	 * @param y the y-location in the maze
	 * @return true when there is a tile at that position, false otherwise.
	 */
	public boolean isTileAtPosition(int x, int y) {
		return mazeTiles[x][y] != null;
	}

	/**
	 * Getter for a tile at a certain position.
	 * @param x the x-location in the maze
	 * @param y the y-location in the maze
	 * @return the tile at the specified location or null when no tile is present.
	 */
	public MazeTile getTile(int x, int y) {
		return mazeTiles[x][y];
	}

	/**
	 * Getter for the lights.
	 * @return A list with all lights in the scene.
	 */
	public List<Light> getLights() {
		return lightList;
	}
	
	/**
	 * Returns a JSONObject which represents this level in JSON for the web interface.
	 * 
	 * @return
	 * 		a JSONObject representing this Level
	 */
	public JSONObject toWebJSON() {
		//Create a JSONObject
		JSONObject json = new JSONObject();
		
		//Set the width and height
		json.put("width", getWidth());
		json.put("height", getHeight());
		
		//Store all the tiles in a JSONObject.
		JSONObject jsonTiles = new JSONObject();
		for (int x = 0; x < mazeTiles.length; x++) {
			MazeTile[] row = mazeTiles[x];
			
			//Create a new JSONArray for this row
			JSONArray jArray = new JSONArray();
			
			//Add all the tiles in the row to the JSONArray.
			for (MazeTile tile : row) {
				if (tile == null) {
					jArray.put(0);
				} else {
					jArray.put(tile.getTileType().getJsonId());
				}
			}
			
			//Add the array to the outer object
			jsonTiles.put("" + x, jArray);
		}
		
		//Add the tiles
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
			MazeTile[] row = mazeTiles[x];
			
			//Create a new JSONArray for this row
			JSONArray jArray = new JSONArray();
			
			for (int y = 0; y < row.length; y++) {
				//We are only interested in explored tiles
				if (row[y] == null || !row[y].isExplored()) continue;
				
				jArray.put(y);
			}
			
			//Add the JSONArray only if it contains elements
			if (jArray.length() != 0) {
				json.put("" + x, jArray);
			}
		}
		
		return json;
	}
}