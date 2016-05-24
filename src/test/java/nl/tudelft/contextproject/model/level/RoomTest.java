package nl.tudelft.contextproject.model.level;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.util.Size;

/**
 * Test class for Room.
 */
public class RoomTest {

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
		assertEquals(testRoom.size, new Size(2, 2));
	}

	@Test
	public void testSetSizeFromFileName() {
		fail("Not yet implemented");
	}

}
