package nl.tudelft.contextproject.model.level.roomIO;

import java.io.BufferedReader;
import java.io.IOException;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Utility class for reading MazeTiles.
 */
public final class TileReader {
	
	//Corridor character for .crf files.
	protected static final String CORRIDOR_TRANSLATION = "C";
	//Corridor character for .crf files.
	protected static final String FLOOR_TRANSLATION = "O";
	//Corridor character for .crf files.
	protected static final String WALL_TRANSLATION = "W";
	
	/**
	 * Private constructor to prevent instantiation.
	 */
	private TileReader() {}
	
	/**
	 * Read a given array of MazeTiles with a specified offset.
	 *
	 * @param tiles
	 * 		the array to store the tiles in
	 * @param width
	 * 		the width of the array to read
	 * @param height
	 * 		the height of the array to read
	 * @param xOffset
	 * 		the horizontal offset to apply on the tiles
	 * @param yOffset
	 * 		the vertical offset to apply to the tiles
	 * @param br
	 * 		the BufferedReader used to get the input
	 * @throws IOException
	 * 		when reading from the reader goes wrong
	 */
	public static void readTiles(MazeTile[][] tiles, int width, int height, int xOffset, int yOffset, BufferedReader br) throws IOException {
		for (int y = 0; y < height; y++) {
			String in = br.readLine();
			if (in == null) throw new IllegalArgumentException("Empty line where some data was expected when loading tile row " + y + ".");

			String[] line = in.split(" ");
			if (line.length < width) {
				throw new IllegalArgumentException("There are not enoug tiles in this row! expected " + width + ", but was " + line.length + ".");
			}

			for (int x = 0; x < line.length; x++) {
				int posx = x + xOffset;
				int posy = y + yOffset;
				if (line[x].equals("#")) {
					// always overwrite old value
					tiles[posx][posy] = null;
				} else {
					tiles[posx][posy] = new MazeTile(posx, posy, translate(line[x]));
				}
			}
		}
	}

	/**
	 * Translate the map file to actual type.
	 * @param string
	 * 			read type
	 * @return
	 * 			TileType value
	 */
	public static TileType translate(String string) {
		switch (string) {
			case WALL_TRANSLATION :
				return TileType.WALL;
			case FLOOR_TRANSLATION:
				return TileType.FLOOR;
			case CORRIDOR_TRANSLATION:
				return TileType.CORRIDOR;
			default:
				return TileType.EMPTY;
		}
	}
}
