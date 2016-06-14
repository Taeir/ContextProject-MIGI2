package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Enum for different kinds of corridor types.
 * Used in CorridorBeautification.
 * <P>
 * In this class, the java doc each enum represents a certain configuration of a corridor.
 * Each corridor is drawn, with A being the current corridor tile and C the corridor tiles around it.
 * 
 */
public enum CorridorType {
	
	/**
	 * A Vertical corridor.
	 * <p>
	 * &ensp;C&ensp;<br>
	 * &ensp;A&ensp;<br>
	 * &ensp;C&ensp;<br>
	 */
	VERTICAL,
	
	/**
	 * An Horizontal corridor.
	 * <p>
	 * &ensp;&ensp;&ensp;<br>
	 * CAC<br>
	 * &ensp;&ensp;&ensp;<br>
	 * <br>
	 */
	HORIZONTAL,
	
	/**
	 * An Upper Right corridor.
	 * <p>
	 * &ensp;&ensp;&ensp;<br>
	 * CA&ensp;<br>
	 * &ensp;C&ensp;<br>
	 * <br>
	 */
	UPPER_RIGHT,
	
	/**
	 * An Upper Left corridor.
	 * <p>
	 * &ensp;&ensp;&ensp;<br>
	 * &ensp;AC<br>
	 * &ensp;C&ensp;<br>
	 * <br>
	 */
	UPPER_LEFT,
	
	/**
	 * A Lower Right corridor.
	 * <p>
	 * &ensp;C&ensp;<br>
	 * CA&ensp;<br>
	 * &ensp;&ensp;&ensp;<br>
	 * <br>
	 */
	LOWER_RIGHT,
	
	/**
	 * A Lower Left corridor.
	 * <p>
	 * &ensp;C&ensp;<br>
	 * &ensp;AC<br>
	 * &ensp;&ensp;&ensp;<br>
	 * <br>
	 */
	LOWER_LEFT,
	
	/**
	 * A Not defined corridor.
	 * <p>
	 * ???<br>
	 * ?A?<br>
	 * ???<br>
	 * <br>
	 */
	NOT_DEFINED;
	
	/**
	 * Get CorridorType from Map.
	 * Scan for each possible corridor tile around the x, y corridor tile.
	 * 
	 * @param map
	 * 		map to check
	 * @param x
	 * 		x coordinate
	 * @param y
	 * 		y coordinate
	 * @return
	 * 		corridor type
	 */
	public static CorridorType getCorridorTypeFromMap(MazeTile[][] map, int x, int y) {
		int mapWidth = map.length;
		int mapHeigth = map[0].length;
		
		int up = x - 1;
		int down = x + 1;
		int left = y - 1;
		int right = y + 1;
		
		boolean upPossible    = up >= 0;
		boolean downPossible  = down + 1 < mapHeigth;
		boolean leftPossible  = left >= 0;
		boolean rightPossible = right < mapWidth;
		

		if (upPossible && checkTileType(map[up][y])) {
			if (downPossible && checkTileType(map[down][y])) {
				return VERTICAL;
			} else if (leftPossible && checkTileType(map[x][left])) {
				return LOWER_RIGHT;
			} else if (rightPossible && checkTileType(map[x][right])) {
				return LOWER_LEFT;
			} 
		} else if (downPossible) {
			if (checkTileType(map[down][y])) {
				if (leftPossible && checkTileType(map[x][left])) {
					return UPPER_RIGHT;
				} else if (rightPossible && checkTileType(map[x][right])) {
					return UPPER_RIGHT;
				}
			}
		} else if (leftPossible && rightPossible && checkTileType(map[x][left]) && checkTileType(map[x][right])) {
			return HORIZONTAL;
		} 
		return NOT_DEFINED;
	}

	/**
	 * Find out what extend method to use depending on each corridor type.
	 * Will return that method's extensions, consisting out of a list of Vec2Is where the extra corridor tiles should be placed.
	 * Returns an empty list if tile type is invalid.
	 * 
	 * @param type
	 * 		the type of corridor
	 * @param location
	 * 		the location of the corridor tile that is extended
	 * @return
	 * 		a list of locations in the map that should be turned into corridors
	 */
	public ArrayList<Vec2I> getExtendLocationsUsingCorridorType(CorridorType type, Vec2I location) {
		switch (type) {
			case HORIZONTAL:
				return horizontalExtension(location);
			case VERTICAL:
				return verticalExtension(location);
			case UPPER_LEFT:
				return upperLeftExtension(location);
			case UPPER_RIGHT:
				return upperRightExtension(location);
			case LOWER_LEFT:
				return lowerLeftExtension(location);
			case LOWER_RIGHT:
				return lowerRightExtension(location);
			default:
				return new ArrayList<Vec2I>(0);
		}
	}
	
	/**
	 * Extend from the horizontal direction.
	 * 
	 * @param location
	 * 		location of original corridor tile
	 * @return
	 * 		list of location on which the new corridor tiles should be placed
	 */
	protected ArrayList<Vec2I> horizontalExtension(Vec2I location) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Extend from the vertical direction.
	 * 
	 * @param location
	 * 		location of original corridor tile
	 * @return
	 * 		list of location on which the new corridor tiles should be placed
	 */
	protected ArrayList<Vec2I> verticalExtension(Vec2I location) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Extend in the lower left direction.
	 * 
	 * @param location
	 * 		location of original corridor tile
	 * @return
	 * 		list of location on which the new corridor tiles should be placed
	 */
	protected ArrayList<Vec2I> lowerLeftExtension(Vec2I location) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Extend in the lower right direction.
	 * 
	 * @param location
	 * 		location of original corridor tile
	 * @return
	 * 		list of location on which the new corridor tiles should be placed
	 */
	protected ArrayList<Vec2I> lowerRightExtension(Vec2I location) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Extend in the upper left direction.
	 * 
	 * @param location
	 * 		location of original corridor tile
	 * @return
	 * 		list of location on which the new corridor tiles should be placed
	 */
	protected ArrayList<Vec2I> upperLeftExtension(Vec2I location) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Extend in the upper right direction.
	 * 
	 * @param location
	 * 		location of original corridor tile
	 * @return
	 * 		list of location on which the new corridor tiles should be placed
	 */
	protected ArrayList<Vec2I> upperRightExtension(Vec2I location) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Check from the tile is a valid tile.
	 * 
	 * @param mazeTile
	 * 		MazeTile to check
	 * @return
	 * 		true if tile is a corridor, door entrance or a door exit.
	 */
	protected static boolean checkTileType(MazeTile mazeTile) {
		if (mazeTile != null 
				&& (mazeTile.getTileType() == TileType.CORRIDOR
				 || mazeTile.getTileType() == TileType.DOOR_ENTRANCE
				 || mazeTile.getTileType() == TileType.DOOR_EXIT)) {
			return true;
		}
		return false;
	}

}
