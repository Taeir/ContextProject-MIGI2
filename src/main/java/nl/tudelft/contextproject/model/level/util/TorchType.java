package nl.tudelft.contextproject.model.level.util;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Enum that represents the different configurations of point light models.
 */
public enum TorchType {

	/**
	 * A torch that hangs to the wall from North direction.
	 */
	NORTH_WALL_LIGHT,

	/**
	 * A torch that hangs to the wall from the South rotation.
	 */
	SOUTH_WALL_LIGHT,

	/**
	 * A torch that hangs to the wall from the West rotation.
	 */
	WEST_WALL_LIGHT,

	/**
	 * A torch that hangs to the wall from the East rotation.
	 */
	EAST_WALL_LIGHT,

	/**
	 * A torch that hangs from the ceiling, where rotation does not matter.
	 */
	CEILING_LIGHT;

	/**
	 * Constructor to complete enum body.
	 */
	TorchType() {};

	/**
	 * Find out the torch type from the maze and a location.
	 * 
	 * @param map
	 * 		the map to check
	 * @param location
	 * 		location of the point light.
	 * @return
	 * 		torch type of the point light.
	 */
	public static TorchType getTorchType(MazeTile[][] map, Vec2I location) {
		int width = map.length;
		int heigth = map[0].length;
		int i = location.x;
		int j = location.y;

		//Check North
		if (j != 0 && map[i][j - 1] != null && map[i][j - 1].getTileType() == TileType.WALL) {
			return TorchType.NORTH_WALL_LIGHT;
		}

		//Check South
		if (j != heigth - 1 && map[i][j + 1] != null && map[i][j + 1].getTileType() == TileType.WALL) {
			return TorchType.SOUTH_WALL_LIGHT;
		}

		//Check West
		if (i != 0 && map[i - 1][j] != null && map[i - 1][j].getTileType() == TileType.WALL) {
			return TorchType.WEST_WALL_LIGHT;
		}

		//Check East
		if (i != width - 1 && map[i + 1][j] != null && map[i + 1][j].getTileType() == TileType.WALL) {
			return TorchType.EAST_WALL_LIGHT;
		}

		return TorchType.CEILING_LIGHT;
	}
}
