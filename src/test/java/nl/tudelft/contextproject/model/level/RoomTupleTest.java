package nl.tudelft.contextproject.model.level;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

/**
 * Test class for RoomTuple.
 */
public class RoomTupleTest extends TestBase {
	
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

	/**
	 * Test constructor.
	 */
	@Test
	public void testRoomTuple() {
		assertTrue(roomTuple.getStarterRoom().equals(starterRoomSetUp));
		assertTrue(roomTuple.getTreasureRoom().equals(treasureRoomSetUp));
	}

	/**
	 * Test getStarterRoom.
	 */
	@Test
	public void testGetStarterRoom() {
		assertTrue(roomTuple.getStarterRoom().equals(starterRoomSetUp));
	}

	/**
	 * Test Set starterRoom.
	 */
	@Test
	public void testSetStarterRoom() {
		roomTuple.setStarterRoom(treasureRoomSetUp);
		assertTrue(roomTuple.getStarterRoom().equals(treasureRoomSetUp));
	}

	/**
	 * Test getTreasureRoom.
	 */
	@Test
	public void testGetTreasureRoom() {
		assertTrue(roomTuple.getTreasureRoom().equals(treasureRoomSetUp));
	}

	/**
	 * Test setTreasureRoom.
	 */
	@Test
	public void testSetTreasureRoom() {
		roomTuple.setTreasureRoom(starterRoomSetUp);
		assertTrue(roomTuple.getTreasureRoom().equals(starterRoomSetUp));
	}

}
