package nl.tudelft.contextproject.roomIO;

import java.io.BufferedReader;
import java.io.IOException;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Utility class for reading MazeTiles.
 */
public final class TileReader {
	private TileReader() {}
	
	/**
	 * Read a given array of MazeTiles with a specified offset.
	 * @param tiles	The array to store the tiles in.
	 * @param width The width of the array to read.
	 * @param height The height of the array to read.
	 * @param xOffset The horizontal offset to apply on the tiles.
	 * @param yOffset The vertical offset to apply to the tiles.
	 * @param br The BufferedReader used to get the input.
	 * @throws IOException when reading from the reader goes wrong.
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
					tiles[posx][posy] = null;		// always override old value
				} else {
					tiles[posx][posy] = new MazeTile(posx, posy, TileType.valueOf(line[x]));
				}
			}
		}
	}
}
