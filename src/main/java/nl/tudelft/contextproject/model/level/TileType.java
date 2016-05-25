package nl.tudelft.contextproject.model.level;

/**
 * Enum representing the possible TileTypes in the game.
 */
public enum TileType {
	EMPTY(0),
	FLOOR(1),
	WALL(2),
	CORRIDOR(3),
	DOOR_ENTRANCE(4),
	DOOR_EXIT(5);

	private int jsonid;
	
	/**
	 * @param jsonid
	 *		the id of the TileType when encoded in JSON
	 */
	TileType(int jsonid) {
		this.jsonid = jsonid;
	}
	
	/**
	 * @return
	 * 		the ID of this TileType in JSON.
	 */
	public int getJsonId() {
		return this.jsonid;
	}
}