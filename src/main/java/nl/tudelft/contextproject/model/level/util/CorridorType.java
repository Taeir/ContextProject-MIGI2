package nl.tudelft.contextproject.model.level.util;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Enum for different kinds of corridor types.
 * Used in CorridorBeautification.
 * <P>
 * In this class, the java doc each enum represents a certain configuration of a corridor.
 * Each corridor is drawn, with A being the current corridor tile and C the corridor tiles around it.
 * The horizontal corridors and vertical corridors are just as shown, but the corner corridors are defined
 * from the direction where the extension will take place, as it was easier to keep track of the position that way.
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
		boolean downPossible  = down < mapHeigth;
		boolean leftPossible  = left >= 0;
		boolean rightPossible = right < mapWidth;
		System.out.println("UP: " + upPossible + ", DP: " + downPossible + ", LP: " + leftPossible + ", RP: " + rightPossible);

		if (upPossible && checkTileType(map[up][y])) {
			if (downPossible && checkTileType(map[down][y])) {
				return VERTICAL;
			} else if (leftPossible && checkTileType(map[x][left])) {
				return LOWER_RIGHT;
			} else if (rightPossible && checkTileType(map[x][right])) {
				return LOWER_LEFT;
			} 
		}
		
		if (downPossible && checkTileType(map[down][y])) {
			if (leftPossible && checkTileType(map[x][left])) {
				return UPPER_RIGHT;
			} else if (rightPossible && checkTileType(map[x][right])) {
				return UPPER_LEFT;
			}
		}
		
		if (leftPossible && rightPossible && checkTileType(map[x][left]) && checkTileType(map[x][right])) {
			return HORIZONTAL;
		} 
		return NOT_DEFINED;
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
