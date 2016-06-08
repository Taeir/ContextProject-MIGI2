package nl.tudelft.contextproject.model.level.util;

import java.util.Random;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Corridor beautifications utility class. 
 * Class that holds some methods to enhance the single tile wide corridor creation.
 */
public final class CorridorBeautification {

	/**
	 * Private constructor to prevent initialization.
	 */
	private CorridorBeautification() {}
	
	/**
	 * This method adds walls to corridors on the map.
	 * Checks if a Tile is a corridor, if true add a wall to all Null TileTypes.
	 *
	 * @param map
	 *		the map in which to place the corridor walls
	 */
	public static void carveCorridorWalls(MazeTile[][] map) {
		int width = map.length;
		int heigth = map[0].length;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < heigth; j++) {
				if (map[i][j] != null && map[i][j].getTileType() == TileType.CORRIDOR) {
					//Check North
					if (j != 0 && map[i][j - 1] == null) {
						map[i][j - 1] = new MazeTile(i, j - 1, TileType.WALL);
					}

					//Check South
					if (j != heigth - 1 && map[i][j + 1] == null) {
						map[i][j + 1] = new MazeTile(i, j + 1, TileType.WALL);
					}

					//Check West
					if (i != 0 && map[i - 1][j] == null) {
						map[i - 1][j] = new MazeTile(i - 1, j, TileType.WALL);
					}

					//Check East
					if (i != width - 1 && map[i + 1][j] == null) {
						map[i + 1][j] = new MazeTile(i + 1, j, TileType.WALL);
					}
				}
			}
		}
	}
	
	/**
	 * Widen the corridors by means of an exponential distribution.
	 *
	 * @param map
	 *		the map in which to place the corridor walls
	 * @param rand
	 * 		Random object that is used in maze creation
	 */
	public static void widenCorridors(MazeTile[][] map, Random rand) {
		int width = map.length;
		int heigth = map[0].length;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < heigth; j++) {
				if (map[i][j].getTileType() == TileType.CORRIDOR) {
					//Find out type of corridor
					
					//Deal vit with it
				}
			}
		}
	}
}
