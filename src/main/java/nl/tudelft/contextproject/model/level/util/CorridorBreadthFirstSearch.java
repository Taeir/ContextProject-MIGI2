package nl.tudelft.contextproject.model.level.util;

import nl.tudelft.contextproject.model.level.MazeTile;

/**
 * Corridor utility class.
 */
public final class CorridorBreadthFirstSearch {
	
	/**
	 * Private constructor to prevent initialization.
	 */
	private CorridorBreadthFirstSearch() {
		//To prevent initialization
	}
	
	/**
	 * Carve a corridor from start Vec2I to end Vec2I.
	 * @param mazeTiles
	 * 		maze tiles to carve in
	 * @param start
	 * 		start location of corridor (should be inside the wall of the room)
	 * @param end
	 * 		end location of corridor (should be inside the wall of the room)
	 */
	public void creatCorridor(MazeTile[][] mazeTiles, Vec2I start, Vec2I end) {
		
	}

}
