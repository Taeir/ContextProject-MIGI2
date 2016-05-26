package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.level.Room;

/**
 * Test class for RoomNode.
 */
public class RoomNodeTest extends TestBase {

	private Room room;
	private RoomNode roomNode;
	
	/**
	 * Set up RoomNode.
	 */
	@Before
	public void roomNodeSetUp() {
		room = new Room("/maps/correctWithName/");
		roomNode = new RoomNode(room);
	}
	
	/**
	 * Test constructor.
	 */
	@Test
	public void testRoomNode() {
		assertTrue(room.equals(roomNode.room));
	}

	/**
	 * Test correct number of outgoing edges of map.
	 */
	@Test
	public void testGetNumberOfOutgoingConnections() {
		assertEquals(1, roomNode.getNumberOfOutgoingConnections());
	}

	/**
	 * Test correct number of incoming edges of the map.
	 */
	@Test
	public void testGetNumberOfIncommingConnections() {
		assertEquals(2, roomNode.getNumberOfIncommingConnections());
	}

}
