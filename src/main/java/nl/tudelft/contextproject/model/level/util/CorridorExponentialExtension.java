package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;
import java.util.Random;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.util.RandomUtil;

/**
 * Utility class that contains methods which expand corridors using an exponential distribution.
 */
public final class CorridorExponentialExtension {
	
	/**
	 * Private constructor to prevent initialization.
	 */
	private CorridorExponentialExtension() {};

	/**
	 * Find out what extend method to use depending on each corridor type.
	 * Will return that method's extensions, consisting out of a list of Vec2Is where the extra corridor tiles should be placed.
	 * Returns an empty list if tile type is invalid.
	 * 
	 * @param type
	 * 		the type of corridor
	 * @param location
	 * 		the location of the corridor tile that is extended
	 * @param rand
	 * 		random generator to use
	 * @return
	 * 		a list of locations in the map that should be turned into corridors
	 */
	public static ArrayList<Vec2I> getExtendLocationsUsingCorridorType(CorridorType type, Vec2I location, Random rand) {
		switch (type) {
			case HORIZONTAL:
				return horizontalExtension(location, rand);
			case VERTICAL:
				return verticalExtension(location, rand);
			case UPPER_LEFT:
				return upperLeftExtension(location, rand);
			case UPPER_RIGHT:
				return upperRightExtension(location, rand);
			case LOWER_LEFT:
				return lowerLeftExtension(location, rand);
			case LOWER_RIGHT:
				return lowerRightExtension(location, rand);
			default:
				return new ArrayList<Vec2I>(0);
		}
	}
	
	/**
	 * Extend from the horizontal direction.
	 * 
	 * @param location
	 * 		location of original corridor tile
	 * @param rand
	 * 		random generator to use
	 * @return
	 * 		list of location on which the new corridor tiles should be placed
	 */
	protected static ArrayList<Vec2I> horizontalExtension(Vec2I location, Random rand) {
		int x = location.x;
		int y = location.y;
		
		int numberOfUpExtensions = getExponentialNumber(rand);
		int numberOfDownExtensions = getExponentialNumber(rand);
		ArrayList<Vec2I> extensions = new ArrayList<Vec2I>(numberOfUpExtensions + numberOfDownExtensions);
		
		//Extend left
		for (int i = 1; i < numberOfDownExtensions; i++) {
			extensions.add(new Vec2I(x - i, y));
		}
		
		//Extend right
		for (int i = 1; i < numberOfUpExtensions; i++) {
			extensions.add(new Vec2I(x + i, y));
		}
		return extensions;
	}
	
	/**
	 * Extend from the vertical direction.
	 * 
	 * @param location
	 * 		location of original corridor tile
	 * @param rand
	 * 		random generator to use
	 * @return
	 * 		list of location on which the new corridor tiles should be placed
	 */
	protected static ArrayList<Vec2I> verticalExtension(Vec2I location, Random rand) {
		int x = location.x;
		int y = location.y;
		
		int numberOfLeftExtensions = getExponentialNumber(rand);
		int numberOfRightExtensions = getExponentialNumber(rand);
		ArrayList<Vec2I> extensions = new ArrayList<Vec2I>(numberOfLeftExtensions + numberOfRightExtensions);
		
		//Extend left
		for (int i = 1; i < numberOfLeftExtensions; i++) {
			extensions.add(new Vec2I(x, y - i));
		}
		
		//Extend right
		for (int i = 1; i < numberOfRightExtensions; i++) {
			extensions.add(new Vec2I(x, y + i));
		}
		return extensions;
	}
	
	/**
	 * Extend in the lower left direction.
	 * 
	 * @param location
	 * 		location of original corridor tile
	 * @param rand
	 * 		random generator to use
	 * @return
	 * 		list of location on which the new corridor tiles should be placed
	 */
	protected static ArrayList<Vec2I> lowerLeftExtension(Vec2I location, Random rand) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Extend in the lower right direction.
	 * 
	 * @param location
	 * 		location of original corridor tile
	 * @param rand
	 * 		random generator to use
	 * @return
	 * 		list of location on which the new corridor tiles should be placed
	 */
	protected static ArrayList<Vec2I> lowerRightExtension(Vec2I location, Random rand) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Extend in the upper left direction.
	 * 
	 * @param location
	 * 		location of original corridor tile
	 * @param rand
	 * 		random generator to use
	 * @return
	 * 		list of location on which the new corridor tiles should be placed
	 */
	protected static ArrayList<Vec2I> upperLeftExtension(Vec2I location, Random rand) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Extend in the upper right direction.
	 * 
	 * @param location
	 * 		location of original corridor tile
	 * @param rand
	 * 		random generator to use
	 * @return
	 * 		list of location on which the new corridor tiles should be placed
	 */
	protected static ArrayList<Vec2I> upperRightExtension(Vec2I location, Random rand) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Get the number required for all extension operations.
	 * 
	 * @param rand
	 * 		random generator to use
	 * @return
	 * 		the required exponential extension number
	 */
	protected static int getExponentialNumber(Random rand) {
		return RandomUtil.getRandomIntegerFromExponentialDistribution(rand, 
				CorridorBeautification.MINIMUM_EXTENSION, 
				CorridorBeautification.MINIMUM_EXTENSION, 
				CorridorBeautification.LAMBA);
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
