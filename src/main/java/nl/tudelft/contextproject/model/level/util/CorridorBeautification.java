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
	 * Minimum extension of corridors.
	 */
	public static final int MINIMUM_EXTENSION = 0;
	
	/**
	 * Maximum extension of corridors.
	 */
	public static final int MAXIMUM_EXTENSION = 3;
	
	/**
	 * Parameter of exponential distribution used in corridor extending with exponential distribution.
	 */
	public static final double LAMBA = 1.0;
	
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
		CorridorType corridorType;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < heigth; j++) {
				if (map[i][j].getTileType() == TileType.CORRIDOR) {
					corridorType = CorridorType.getCorridorTypeFromMap(map, i, j);
					
					//Deal vit with it
				}
			}
		}
	}
		
	/** 
	 * Simply extend each corridor by 1 in each direction, if possible.
	 * For this method an new maze tile map is created which holds the extra
	 * corridors until all extra corridors have been created, this is to prevent
	 * placing extra corridors on extra corridors, filling the entire map with corridors.
	 * <p>
	 * This method can be called after each other on the same map to keep extending the corridors
	 * by 1 extra corridor tile in each direction.
	 * 
	 * @param map
	 *		the map in which to place the corridor tiles
	 */
	public static void simpleCorridorWidening(MazeTile[][] map) {
		int width = map.length;
		int heigth = map[0].length;
		MazeTile[][] extraCorridorMap = new MazeTile[heigth][width];
		
		//Add new corridors if possible to the extraCorridorMap
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < heigth; j++) {
				if (map[i][j] != null && map[i][j].getTileType() == TileType.CORRIDOR) {
					//Check North
					if (j != 0 && map[i][j - 1] == null) {
						extraCorridorMap[i][j - 1] = new MazeTile(i, j - 1, TileType.CORRIDOR);
					}

					//Check South
					if (j != heigth - 1 && map[i][j + 1] == null) {
						extraCorridorMap[i][j + 1] = new MazeTile(i, j + 1, TileType.CORRIDOR);
					}

					//Check West
					if (i != 0 && map[i - 1][j] == null) {
						extraCorridorMap[i - 1][j] = new MazeTile(i - 1, j, TileType.CORRIDOR);
					}

					//Check East
					if (i != width - 1 && map[i + 1][j] == null) {
						extraCorridorMap[i + 1][j] = new MazeTile(i + 1, j, TileType.CORRIDOR);
					}
				}
			}
		}
		
		//Add all extra corridor tiles to actual map
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < heigth; j++) {
				if (extraCorridorMap[i][j] != null) map[i][j] = new MazeTile(i, j, TileType.CORRIDOR);
			}
		}
	}
}
