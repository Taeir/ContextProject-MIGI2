package nl.tudelft.contextproject.model.level;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Room.
 */
public class RoomTest {

	private static final String ROOM_FOLDER = "/maps/correctWithName/";
	
	private Room testRoom;
	
	
	@Before
	public void setUpSimpleRoom() {
		testRoom = new Room(ROOM_FOLDER);
	}
	
	/**
	 * Test constructor on test room.
	 */
	@Test
	public void testRoomConstructorSize() {
		
	}

	@Test
	public void testSetSizeFromFileName() {
		fail("Not yet implemented");
	}

}
