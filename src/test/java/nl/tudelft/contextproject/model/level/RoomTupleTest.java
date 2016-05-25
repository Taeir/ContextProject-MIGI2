package nl.tudelft.contextproject.model.level;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for RoomTuple
 */
public class RoomTupleTest {
	
	//Room folder of a correct Room;
	private static final String ROOM_FOLDER = "/maps/correctWithName/";
	
	private Room starterRoomSetUp;
	private Room treasureRoomSetUp;
	private RoomTuple roomTuple;
	
	/**
	 * Set up RoomTuple and Rooms for tests.
	 */
	@Before
	public void setUpTestRoom() {
		starterRoomSetUp = new Room(ROOM_FOLDER);
		treasureRoomSetUp = new Room(ROOM_FOLDER);
		roomTuple = new RoomTuple(starterRoomSetUp, treasureRoomSetUp);
	}

	@Test
	public void testRoomTuple() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStarterRoom() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetStarterRoom() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTreasureRoom() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTreasureRoom() {
		fail("Not yet implemented");
	}

}
