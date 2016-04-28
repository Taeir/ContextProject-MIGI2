package nl.tudelft.contextproject;

import com.jme3.asset.AssetManager;

/**
 * Class representing the entire level in the game.
 * An instance of this contains all the tiles in the maze and (in the future)
 * players and other entities.
 */
public class Level {
	private MazeTile[][] mazeTiles;
	
	/**
	 * Constructor that creates a level with specified dimensions.
	 * NOTE: this will later be replaced by a number of rooms.
	 * @param width the with of the maze
	 * @param height the height of the maze
	 */
	public Level(int width, int height) {
		AssetManager am = Main.getInstance().getAssetManager();
		mazeTiles = new MazeTile[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (Math.random() < .3f) {
					mazeTiles[x][y] = new MazeTile(am);
				}
			}
		}
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
}
