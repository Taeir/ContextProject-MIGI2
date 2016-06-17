package nl.tudelft.contextproject.model.level.util;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.model.entities.environment.Torch;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.util.Vec2I;

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
	CEILING_LIGHT,
	
	/**
	 * A torch that is not yet defined in the game.
	 */
	PLACE_HOLDER_TORCH;

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
		int i = location.x;
		int j = location.y;

		//Check North
		if (checkForNorthWall(map, i, j)) {
			return TorchType.NORTH_WALL_LIGHT;
		}

		//Check South
		if (checkForSouthWall(map, i, j)) {
			return TorchType.SOUTH_WALL_LIGHT;
		}

		//Check West
		if (checkForWestWall(map, i, j)) {
			return TorchType.WEST_WALL_LIGHT;
		}

		//Check East
		if (checkForEastWall(map, i, j)) {
			return TorchType.EAST_WALL_LIGHT;
		}

		return TorchType.CEILING_LIGHT;
	}

	/**
	 * Check for an East wall.
	 * 
	 * @param map
	 * 		map to check
	 * @param x
	 * 		x coordinate of torch
	 * @param y
	 * 		y coordinate of torch
	 * @return
	 * 		true if there is an East wall
	 */
	private static boolean checkForEastWall(MazeTile[][] map, int x, int y) {
		int width = map.length;
		return x != width - 1 && map[x + 1][y] != null && map[x + 1][y].getTileType() == TileType.WALL;
	}

	/**
	 * Check for a West wall.
	 * 
	 * @param map
	 * 		map to check
	 * @param x
	 * 		x coordinate of torch
	 * @param y
	 * 		y coordinate of torch
	 * @return
	 * 		true if there is a West wall
	 */
	private static boolean checkForWestWall(MazeTile[][] map, int x, int y) {
		return x != 0 && map[x - 1][y] != null && map[x - 1][y].getTileType() == TileType.WALL;
	}

	/**
	 * Check for a South wall.
	 * 
	 * @param map
	 * 		map to check
	 * @param x
	 * 		x coordinate of torch
	 * @param y
	 * 		y coordinate of torch
	 * @return
	 * 		true if there is a South wall
	 */
	private static boolean checkForSouthWall(MazeTile[][] map, int x, int y) {
		int heigth = map[0].length;
		return y != heigth - 1 && map[x][y + 1] != null && map[x][y + 1].getTileType() == TileType.WALL;
	}

	/**
	 * Check for a North wall.
	 * 
	 * @param map
	 * 		map to check
	 * @param x
	 * 		x coordinate of torch
	 * @param y
	 * 		y coordinate of torch
	 * @return
	 * 		true if there is a North wall
	 */
	private static boolean checkForNorthWall(MazeTile[][] map, int x, int y) {
		return y != 0 && map[x][y - 1] != null && map[x][y - 1].getTileType() == TileType.WALL;
	}
	
	/**
	 * Create a new Torch entity based on the TorchType.
	 * Rotates the torch in the correct direction and moves it to the location on the map.
	 * 
	 * @param torchType
	 * 		type of torch 
	 * @param location
	 * 		location of Torch
	 * @return
	 * 		new Torch entity with the correct
	 * @throws IllegalArgumentException
	 * 		if non-existing torch type is given.
	 */
	public static Torch createTorchOfTorchType(TorchType torchType, Vector3f location) throws IllegalArgumentException {
		Torch torch;
		switch (torchType) {
			case NORTH_WALL_LIGHT:
				torch = new Torch(true);
				torch.rotateSouth();
				break;
			case SOUTH_WALL_LIGHT:
				torch = new Torch(true);
				torch.rotateNorth();
				break;
			case WEST_WALL_LIGHT:
				torch = new Torch(true);
				torch.rotateEast();
				break;
			case EAST_WALL_LIGHT:
				torch = new Torch(true);
				torch.rotateWest();
				break;
			case CEILING_LIGHT:
				torch = new Torch(false);
				break;
			default:
				throw new IllegalArgumentException("Create torch type called with an Illegal torch type argument.");
		}
		
		torch.move(location);
		return torch;
	}
}
