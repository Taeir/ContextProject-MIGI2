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

		//Check North
		if (checkForNorthWall(map, location)) {
			return TorchType.NORTH_WALL_LIGHT;
		}

		//Check South
		if (checkForSouthWall(map, location)) {
			return TorchType.SOUTH_WALL_LIGHT;
		}

		//Check West
		if (checkForWestWall(map, location)) {
			return TorchType.WEST_WALL_LIGHT;
		}

		//Check East
		if (checkForEastWall(map, location)) {
			return TorchType.EAST_WALL_LIGHT;
		}

		return TorchType.CEILING_LIGHT;
	}

	/**
	 * Check for an East wall.
	 * 
	 * @param map
	 * 		map to check
	 * @param location
	 * 		location to check
	 * @return
	 * 		true if there is an East wall
	 */
	private static boolean checkForEastWall(MazeTile[][] map, Vec2I location) {
		int width = map.length;
		int x = location.x;
		int y = location.y;
		
		return x != width - 1 && map[x + 1][y] != null && map[x + 1][y].getTileType() == TileType.WALL;
	}

	/**
	 * Check for a West wall.
	 * 
	 * @param map
	 * 		map to check
	 * @param location
	 * 		location to check
	 * @return
	 * 		true if there is a West wall
	 */
	private static boolean checkForWestWall(MazeTile[][] map, Vec2I location) {
		int x = location.x;
		int y = location.y;
		
		return x != 0 && map[x - 1][y] != null && map[x - 1][y].getTileType() == TileType.WALL;
	}

	/**
	 * Check for a South wall.
	 * 
	 * @param map
	 * 		map to check
	 * @param location
	 * 		location to check
	 * @return
	 * 		true if there is a South wall
	 */
	private static boolean checkForSouthWall(MazeTile[][] map, Vec2I location) {
		int heigth = map[0].length;
		int x = location.x;
		int y = location.y;
		
		return y != heigth - 1 && map[x][y + 1] != null && map[x][y + 1].getTileType() == TileType.WALL;
	}

	/**
	 * Check for a North wall.
	 * 
	 * @param map
	 * 		map to check
	 * @param location
	 * 		location to check
	 * @return
	 * 		true if there is a North wall
	 */
	private static boolean checkForNorthWall(MazeTile[][] map, Vec2I location) {
		int x = location.x;
		int y = location.y;
		
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
