package nl.tudelft.contextproject.model.level;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.logging.Log;
import nl.tudelft.contextproject.util.Size;

/**
 * Test class for Room.
 */
public class RoomTest extends TestBase {

	private static final String ROOM_FOLDER = "/maps/correctWithName/";
	private static final String ROOM_INCORRECT_FOLDER = "/maps/correct/";
	private Room testRoom;
	
	/**
	 * Setup logger to not log.
	 */
	@BeforeClass
	public static void setUpClass() {
		Log.getLog("MazeGeneration").setLevel(Level.OFF);
	}
	
	/**
	 * Create a basic room from test files.
	 */
	@Before
	public void setUpSimpleRoom() {
		testRoom = new Room(ROOM_FOLDER);
	}
	
	/**
	 * Test constructor on test room.
	 */
	@Test
	public void testRoomConstructorSize() {
		assertEquals(testRoom.size, new Size(2, 3));
	}
	
	/**
	 * Test IOException during reading wrong file name.
	 */
	@Test
	public void testRoomConstructorIncorrectFileName() {
		Room newRoom = new Room(ROOM_INCORRECT_FOLDER);
		assertEquals(newRoom.size, null);
	}

	/**
	 * Test size retrieval with incorrect file name.
	 * @throws IOException 
	 * 			should happen as room name is incorrect
	 */
	@Test(expected = IOException.class)
	public void testSetSizeFromIncorrectFileName() throws IOException {
		Size testSize = testRoom.getSizeFromFileName(ROOM_INCORRECT_FOLDER);
		assertEquals(testSize, new Size(2, 3));
	}

	/**
	 * Test size retrieval.
	 */ 
	@Test
	public void testSetSizeFromCorrectFileName() {
		Size testSize;
		try {
			testSize = testRoom.getSizeFromFileName(ROOM_FOLDER);
			assertEquals(testSize, new Size(2, 3));
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Test hash code.
	 */
	@Test
	public void testHashCode() {
		assertEquals(31 + Arrays.deepHashCode(testRoom.mazeTiles), testRoom.hashCode());
	}
	
	/**
	 * Test set of maze tiles.
	 */
	@Test
	public void testSetMazeTiles() {
		MazeTile[][] mazeTiles = new MazeTile[1][1];
		mazeTiles[0][0] = new MazeTile(0, 0, TileType.FLOOR);
		Size zeroSize = new Size(1, 1);
		testRoom.setMazeTiles(mazeTiles);
		testRoom.setSize(zeroSize);
		Room testRoom2 = new Room(ROOM_FOLDER);
		testRoom2.setMazeTiles(mazeTiles);
		testRoom2.setSize(zeroSize);
		assertEquals(testRoom, testRoom2);
	}
	
	/**
	 * Rest Equal rooms two equal rooms.
	 */
	@Test
	public void testEqualsEqualRooms() {
		Room testRoom2 = new Room(ROOM_FOLDER);
		assertEquals(testRoom, testRoom2);
	}
	
	/**
	 * Rest Equal rooms, copy.
	 */
	@Test
	public void testEqualsEqualCopy() {
		Room copy = testRoom.copy();
		assertEquals(testRoom, copy);
	}
	
	/**
	 * Rest Equal rooms same room.
	 */
	@Test
	public void testEqualsSameRoom() {
		assertEquals(testRoom, testRoom);
	}
	
	/**
	 * Rest Equal rooms same room.
	 */
	@Test
	public void testEqualsNull() {
		assertNotEquals(testRoom, null);
	}
	
	/**
	 * Rest Equal rooms other object.
	 */
	@Test
	public void testEqualsOtherObject() {
		assertNotEquals(testRoom, 5);
	}
	
	/**
	 * Rest Equal rooms not equal room.
	 */
	@Test
	public void testEqualsNotEqualSize() {
		Size size = new Size(0, 0);
		Room testRoom2 = new Room(ROOM_FOLDER);
		testRoom2.setSize(size);
		assertNotEquals(testRoom, testRoom2);
	}
}
