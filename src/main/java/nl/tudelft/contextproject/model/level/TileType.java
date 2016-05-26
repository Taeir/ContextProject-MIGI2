package nl.tudelft.contextproject.model.level;

import com.jme3.math.ColorRGBA;

/**
 * Enum representing the possible TileTypes in the game.
 */
public enum TileType {
    FLOOR(1, 0, ColorRGBA.Green),
    WALL(2, 3, ColorRGBA.Blue),
    CORRIDOR(3, 0, ColorRGBA.Red);
	
	private int jsonid;
	private int height;
	private ColorRGBA color;
	
	/**
	 * @param jsonid
	 *		the id of the TileType when encoded in JSON
	 * @param height
	 * 		the height of the tile
	 * @param color
	 * 		the color of the tile
	 */
	TileType(int jsonid, int height, ColorRGBA color) {
		this.jsonid = jsonid;
		this.height = height;
		this.color = color;
	}
	
	/**
	 * @return
	 * 		the ID of this TileType in JSON.
	 */
	public int getJsonId() {
		return this.jsonid;
	}

	/**
	 * Get the color of a certain {@link TileType}.
	 * 
	 * @return
	 * 		the color of the {@link TileType}
	 */
	public ColorRGBA getColor() {
		return color;
	}

	/**
	 * Get the height of a {@link TileType}.
	 * 
	 * @return
	 * 		The height of this tile.
	 */
	public int getHeight() {
		return height;
	}
}