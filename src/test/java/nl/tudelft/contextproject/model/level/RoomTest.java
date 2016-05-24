package nl.tudelft.contextproject.model.level;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.util.Size;

/**
 * Test class for Room.
 */
public class RoomTest extends TestBase {

	private static final String ROOM_FOLDER = "/maps/correctWithName/";
	
	private Room testRoom;
	
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
	 * (expected = IOException.class)
	 */
	@Test
	public void testRoomConstructorIncorrectFileName() {
		Room newRoom = new Room("/maps/correct/");
		assertEquals(newRoom.size, null);
	}

	/**
	 * Test size retrieval.
	 */
	@Test
	public void testSetSizeFromFileName() {
		fail("Not yet implemented");
	}

}
