package nl.tudelft.contextproject.model.level;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.util.log.Log;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
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
		Logger.getLogger("MazeGeneration").setLevel(Level.OFF);
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
		assertTrue(testRoom.size.equals(new Size(2, 3)));
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
		assertTrue(testSize.equals(new Size(2, 3)));
	}

	/**
	 * Test size retrieval.
	 */ 
	@Test
	public void testSetSizeFromCorrectFileName() {
		Size testSize;
		try {
			testSize = testRoom.getSizeFromFileName(ROOM_FOLDER);
			assertTrue(testSize.equals(new Size(2, 3)));
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}
	}
}
