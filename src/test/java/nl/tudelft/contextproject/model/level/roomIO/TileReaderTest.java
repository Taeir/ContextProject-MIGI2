package nl.tudelft.contextproject.model.level.roomIO;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Test class for TileReader.
 */
public class TileReaderTest {

	/**
	 * Test reading an empty tile.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test
	public void testGetTileEmpty() throws IOException {
		MazeTile[][] tiles = new MazeTile[1][1];
		String in = "#";
		BufferedReader br = new BufferedReader(new StringReader(in));
		TileReader.readTiles(tiles, 1, 1, 0, 0, br);
		assertNull(tiles[0][0]);
	}
	
	/**
	 * Test reading a Wall tile.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test
	public void testGetTileWall() throws IOException {
		MazeTile[][] tiles = new MazeTile[1][1];
		String in = "WALL";
		BufferedReader br = new BufferedReader(new StringReader(in));
		TileReader.readTiles(tiles, 1, 1, 0, 0, br);
		assertEquals(TileType.WALL, tiles[0][0].getTileType());
	}
	
	/**
	 * Test reading a Corridor tile.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test
	public void testGetTileCorridor() throws IOException {
		MazeTile[][] tiles = new MazeTile[1][1];
		String in = "CORRIDOR";
		BufferedReader br = new BufferedReader(new StringReader(in));
		TileReader.readTiles(tiles, 1, 1, 0, 0, br);
		assertEquals(TileType.CORRIDOR, tiles[0][0].getTileType());
	}
	
	/**
	 * Test reading a Floor tile.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test
	public void testGetTileFloor() throws IOException {
		MazeTile[][] tiles = new MazeTile[1][1];
		String in = "FLOOR";
		BufferedReader br = new BufferedReader(new StringReader(in));
		TileReader.readTiles(tiles, 1, 1, 0, 0, br);
		assertEquals(TileType.FLOOR, tiles[0][0].getTileType());
	}
	
	/**
	 * Test reading a non existent tile type.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetTileNonExistent() throws IOException {
		MazeTile[][] tiles = new MazeTile[1][1];
		String in = "THIS_TILETYPE_DOES_NOT_EXIST";
		BufferedReader br = new BufferedReader(new StringReader(in));
		TileReader.readTiles(tiles, 1, 1, 0, 0, br);
	}
	
	/**
	 * Test reading a row of two tiles, but only one is given.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetTileTooFewTilesInARow() throws IOException {
		MazeTile[][] tiles = new MazeTile[1][1];
		String in = "FLOOR";
		BufferedReader br = new BufferedReader(new StringReader(in));
		TileReader.readTiles(tiles, 2, 1, 0, 0, br);
	}
	
	/**
	 * Test reading two rows of tiles, but only one is given.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testGetTileTooFewRows() throws IOException {
		MazeTile[][] tiles = new MazeTile[1][1];
		String in = "FLOOR";
		BufferedReader br = new BufferedReader(new StringReader(in));
		TileReader.readTiles(tiles, 1, 2, 0, 0, br);
	}

}
