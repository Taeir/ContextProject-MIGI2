package nl.tudelft.contextproject.model.level.util;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.util.Vec2I;

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
	 * Note that this method had to be split up a lot to reduce complexity, this method
	 * is now less efficient, but easier to test and more robust.
	 * 
	 * @param map
	 * 		map to check
	 * @param location
	 * 		location to check
	 * @return
	 * 		corridor type
	 */
	public static CorridorType getCorridorTypeFromMap(MazeTile[][] map, Vec2I location) {
		if (verticalCorridorCheck(map, location)) {
			return VERTICAL;
		} else if (horizontalCorridorCheck(map, location)) {
			return HORIZONTAL;
		} else if (lowerRightCorridorCheck(map, location)) {
			return LOWER_RIGHT;
		} else if (lowerLeftCorridorCheck(map, location)) {
			return LOWER_LEFT;
		} else if (upperRightCorridorCheck(map, location)) {
			return UPPER_RIGHT;
		} else if (upperLeftCorridorCheck(map, location)) {
			return UPPER_LEFT;
		} else {
			return NOT_DEFINED;
		}
	}

	/**
	 * Check if corridor is an upper left corridor.
	 * 
	 * @param map
	 * 		map to check corridor tile on
	 * @param location
	 * 		location to check
	 * @return
	 * 		true if corridor is a upper left corridor
	 */
	private static boolean upperLeftCorridorCheck(MazeTile[][] map, Vec2I location) {
		int mapWidth  = map.length;
		int mapHeigth = map[0].length;

		int x = location.x;
		int y = location.y;
		
		int down  = x + 1;
		int right = y + 1;

		boolean downPossible  = bottomAndRightBoundCheck(mapHeigth, down);
		boolean rightPossible = bottomAndRightBoundCheck(mapWidth, right);
		
		return downPossible && rightPossible && checkTileType(map[down][y]) && checkTileType(map[x][right]);
	}

	/**
	 * Check if corridor is an upper right corridor.
	 * 
	 * @param map
	 * 		map to check corridor tile on
	 * @param location
	 * 		location to check
	 * @return
	 * 		true if corridor is a upper right corridor
	 */
	private static boolean upperRightCorridorCheck(MazeTile[][] map, Vec2I location) {
		int mapHeigth = map[0].length;

		int x = location.x;
		int y = location.y;
		
		int down  = x + 1;
		int left  = y - 1;

		boolean downPossible  = bottomAndRightBoundCheck(mapHeigth, down);
		boolean leftPossible  = topAndLeftBoundCheck(left);
		
		return downPossible && leftPossible && checkTileType(map[down][y]) && checkTileType(map[x][left]);
	}


	/**
	 * Check if corridor is an lower left corridor.
	 * 
	 * @param map
	 * 		map to check corridor tile on
	 * @param location
	 * 		location to check
	 * @return
	 * 		true if corridor is a lower left  corridor
	 */
	private static boolean lowerLeftCorridorCheck(MazeTile[][] map, Vec2I location) {
		int mapWidth  = map.length;
		
		int x = location.x;
		int y = location.y;

		int up    = x - 1;
		int right = y + 1;

		boolean upPossible    = topAndLeftBoundCheck(up);
		boolean rightPossible = bottomAndRightBoundCheck(mapWidth, right);
		
		return upPossible && rightPossible && checkTileType(map[up][y]) && checkTileType(map[x][right]);
	}

	/**
	 * Check if corridor is an lower right corridor.
	 * 
	 * @param map
	 * 		map to check corridor tile on
	 * @param location
	 * 		location to check
	 * @return
	 * 		true if corridor is a lower right  corridor
	 */
	private static boolean lowerRightCorridorCheck(MazeTile[][] map, Vec2I location) {
		int x = location.x;
		int y = location.y;
		
		int up    = x - 1;
		int left  = y - 1;

		boolean upPossible    = topAndLeftBoundCheck(up);
		boolean leftPossible  = topAndLeftBoundCheck(left);
		
		return upPossible && leftPossible && checkTileType(map[up][y]) && checkTileType(map[x][left]);
	}

	/**
	 * Check if corridor is an horizontal corridor.
	 * 
	 * @param map
	 * 		map to check corridor tile on
	 * @param location
	 * 		location to check
	 * @return
	 * 		true if corridor is a horizontal corridor
	 */
	private static boolean horizontalCorridorCheck(MazeTile[][] map, Vec2I location) {
		int mapWidth  = map.length;
		
		int x = location.x;
		int y = location.y;
	
		int left  = y - 1;
		int right = y + 1;

		boolean leftPossible  = topAndLeftBoundCheck(left);
		boolean rightPossible = bottomAndRightBoundCheck(mapWidth, right);
		
		return leftPossible && rightPossible && checkTileType(map[x][left]) && checkTileType(map[x][right]);
	}

	/**
	 * Check if corridor is a vertical corridor.
	 * 
	 * @param map
	 * 		map to check corridor tile on
	 * @param location
	 * 		location to check
	 * @return
	 * 		true if corridor is a vertical corridor
	 */
	private static boolean verticalCorridorCheck(MazeTile[][] map, Vec2I location) {
		int mapHeigth = map[0].length;
		
		int x = location.x;
		int y = location.y;
		
		int up   = x - 1;
		int down = x + 1;

		boolean upPossible    = topAndLeftBoundCheck(up);
		boolean downPossible  = bottomAndRightBoundCheck(mapHeigth, down);
		
		return upPossible && downPossible && checkTileType(map[up][y]) && checkTileType(map[down][y]);
	}

	/**
	 * Check if bottom or right is within bounds.
	 * 
	 * @param relevantMapSize
	 * 		relevant map size, width if checking right, height if checking bottom
	 * @param direction
	 * 		off set in the direction that is checked in
	 * @return
	 * 		true if within map size
	 */
	private static boolean bottomAndRightBoundCheck(int relevantMapSize, int direction) {
		return direction < relevantMapSize;
	}

	/**
	 * Check if direction is equal or above zero.
	 * 
	 * @param direction
	 * 		off set in the direction that is checked in
	 * @return
	 * 		true if within bounds
	 */
	private static boolean topAndLeftBoundCheck(int direction) {
		return direction >= 0;
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
