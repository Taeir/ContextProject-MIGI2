package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
	 * Checks if a Tile is a corridor, if true add a wall to all null TileTypes.
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
	 * 		random generator object that is used in maze creation
	 * @param usedNodes 
	 * 		list of used room nodes.
	 */
	public static void widenCorridors(MazeTile[][] map, Random rand, ArrayList<RoomNode> usedNodes) {
		int width = map.length;
		int heigth = map[0].length;
		HashSet<Vec2I> newCorridorTiles = new HashSet<Vec2I>(4 * width * heigth);
		CorridorType corridorType;
		//Generate new corridor tile positions
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < heigth; j++) {
				if (map[i][j] != null && map[i][j].getTileType() == TileType.CORRIDOR) {
					corridorType = CorridorType.getCorridorTypeFromMap(map, i, j);
					newCorridorTiles.addAll(CorridorExponentialExtension.getExtendLocationsUsingCorridorType(corridorType, new Vec2I(i, j), rand));
				}
			}
		}
		//Add new corridor tiles
		Iterator<Vec2I> it = newCorridorTiles.iterator();
		while (it.hasNext()) {
			placeCorridor(map, it.next(), usedNodes);
		}
	}
		
	/**
	 * Place corridor tile if maze tile location is null.
	 * 
	 * @param map
	 * 		map to check
	 * @param corridorLocation
	 * 		location to check
	 * @param usedNodes 
	 * 		list of used room nodes
	 */
	protected static void placeCorridor(MazeTile[][] map, Vec2I corridorLocation, ArrayList<RoomNode> usedNodes) {
		int x = corridorLocation.x;
		int y = corridorLocation.y;
		
		if (validLocation(map, x, y) && map[x][y] == null && checkIfNotInRoom(corridorLocation, usedNodes)) {
			map[x][y] = new MazeTile(x, y, TileType.CORRIDOR);
		}
	}

	/**
	 * Check if a location is not in a placed room.
	 * 
	 * @param corridorLocation
	 * 		location to check
	 * @param usedNodes
	 * 		used RoomNodes
	 * @return
	 * 		true if location is not in a placed room.
	 */
	protected static boolean checkIfNotInRoom(Vec2I corridorLocation, ArrayList<RoomNode> usedNodes) {
		for (RoomNode roomNode : usedNodes) {
			if (roomNode.locationInRoom(corridorLocation)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method that checks if coordinates lie within the map.
	 * 
	 * @param map
	 * 		map to check
	 * @param x
	 * 		x coordinate to check
	 * @param y
	 * 		y coordinate to check
	 * @return
	 * 		true if locations lies within the map
	 */
	protected static boolean validLocation(MazeTile[][] map, int x, int y) {
		if (x < 1 || y < 1) {
			return false;
		} else if (x < map.length - 1 && y < map[0].length - 1) {
			return true;
		}
		return false;
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
