package nl.tudelft.contextproject.model.level.roomIO;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.util.Size;

/**
 * Test class for TileReader.
 */
public class TileParserTest extends TestBase {

	/**
	 * Test reading an empty tile.
	 *
	 * @throws IOException
	 * 		this should not happen
	 */
	@Test
	public void testGetTileEmpty() throws IOException {
		MazeTile[][] tiles = new MazeTile[1][1];
		String input = TileParser.EMPTY_FORMAT;
		BufferedReader br = new BufferedReader(new StringReader(input));
		TileParser.readTiles(tiles, new Size(1, 1), br);
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
		String input = TileParser.WALL_FORMAT;
		BufferedReader br = new BufferedReader(new StringReader(input));
		TileParser.readTiles(tiles, new Size(1, 1), br);
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
		String input = TileParser.CORRIDOR_FORMAT;
		BufferedReader br = new BufferedReader(new StringReader(input));
		TileParser.readTiles(tiles, new Size(1, 1), br);
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
		String input = TileParser.FLOOR_FORMAT;
		BufferedReader br = new BufferedReader(new StringReader(input));
		TileParser.readTiles(tiles, new Size(1, 1), br);
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
		TileParser.readTiles(tiles, new Size(1, 1), br);
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
		String in = TileParser.FLOOR_FORMAT;
		BufferedReader br = new BufferedReader(new StringReader(in));
		TileParser.readTiles(tiles, new Size(2, 1), br);
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
		TileParser.readTiles(tiles, new Size(1, 2), br);
	}
	
	/**
	 * Test translate method of wall.
	 */
	@Test
	public void testTranslateWall() {
		assertEquals(TileType.WALL, TileParser.format(TileParser.WALL_FORMAT));
	}
	
	/**
	 * Test translate method of floor.
	 */
	@Test
	public void testTranslateFloor() {
		assertEquals(TileType.FLOOR, TileParser.format(TileParser.FLOOR_FORMAT));
	}
	
	/**
	 * Test translate method of corridor.
	 */
	@Test
	public void testTranslateCorridor() {
		assertEquals(TileType.CORRIDOR, TileParser.format(TileParser.CORRIDOR_FORMAT));
	}
	
	/**
	 * Test translate method of door entrance.
	 */
	@Test
	public void testTranslateDoorEntrance() {
		assertEquals(TileType.DOOR_ENTRANCE, TileParser.format(TileParser.DOOR_ENTRANCE_FORMAT));
	}
	
	/**
	 * Test translate method of door exit.
	 */
	@Test
	public void testTranslateDoorExit() {
		assertEquals(TileType.DOOR_EXIT, TileParser.format(TileParser.DOOR_EXIT_FORMAT));
	}

}
