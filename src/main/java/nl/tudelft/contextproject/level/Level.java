package nl.tudelft.contextproject.level;

import nl.tudelft.contextproject.VRPlayer;

/**
 * Class representing the entire level in the game.
 * An instance of this contains all the tiles in the maze and (in the future)
 * players and other entities.
 */
public class Level {
	private Room[] rooms;
	private VRPlayer player;
	
	/**
	 * Constructor to create a Level with specific mazeTiles.
	 * @param p The player that is placed in the maze.
	 */
	public Level(VRPlayer p, Room[] rooms) {
		this.player = p;
		this.rooms = rooms;
	}

	public VRPlayer getPlayer() {
		return player;
	}

	public Room[] getRooms() {
		return rooms;
	}
}
