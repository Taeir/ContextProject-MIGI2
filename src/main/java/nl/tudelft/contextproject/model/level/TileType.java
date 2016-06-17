package nl.tudelft.contextproject.model.level;

import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;

import nl.tudelft.contextproject.Main;

/**
 * Enum representing the possible TileTypes in the game.
 * <p>
 * PMD error: The String literal "Textures/grasstexture.png" appears 4 times in this file; the first occurrence is on line 13
 * 		This string will change later in some positions 
 * 		and it is a bit weird to define an constant that references a texture.
 */
public enum TileType {

	FLOOR(1, 0, ColorRGBA.Green, TileType.GRASS_TEXTURE_LOCATION),
	WALL(2, 3, ColorRGBA.Gray, TileType.WALL_TEXTURE_LOCATION),
	CORRIDOR(3, 0, ColorRGBA.Green, TileType.GRASS_TEXTURE_LOCATION),
	DOOR_ENTRANCE(3, 0, ColorRGBA.Green, TileType.GRASS_TEXTURE_LOCATION),
	DOOR_EXIT(3, 0, ColorRGBA.Green, TileType.GRASS_TEXTURE_LOCATION);

	private static final String GRASS_TEXTURE_LOCATION = "Textures/grasstexture.png";
	private static final String WALL_TEXTURE_LOCATION = "Textures/walltexture.png";
	
	private int jsonid;
	private int height;
	private ColorRGBA color;
	private Texture texture;

	/**
	 * @param jsonid
	 *		the id of the TileType when encoded in JSON
	 * @param height
	 * 		the height of the tile
	 * @param color
	 * 		the color of the tile
	 * @param texture
	 * 		the texture location of this tile
	 */
	TileType(int jsonid, int height, ColorRGBA color, String texture) {
		this.jsonid = jsonid;
		this.height = height;
		this.color = color;
		this.texture = Main.getInstance().getAssetManager().loadTexture(texture);
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

	/**
	 * Get the texture of this tile.
	 * 
	 * @return
	 * 		the texture of this tile
	 */
	public Texture getTexture() {
		return texture;
	}
}