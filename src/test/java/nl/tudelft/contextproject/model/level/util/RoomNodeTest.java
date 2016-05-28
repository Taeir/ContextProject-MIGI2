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
	
	/**
	 * Test xSize, with Rotation of 0 degrees.
	 */
	@Test 
	public void testXSize0() {
		assertEquals(room.size.getWidth(), roomNode.xSize(RoomRotation.ROTATION_0));
	}
	
	/**
	 * Test xSize, with Rotation of 90 degrees.
	 */
	@Test 
	public void testXSize90() {
		assertEquals(room.size.getHeight(), roomNode.xSize(RoomRotation.ROTATION_90));
	}
	
	/**
	 * Test xSize, with Rotation of 180 degrees.
	 */
	@Test 
	public void testXSize180() {
		assertEquals(room.size.getWidth(), roomNode.xSize(RoomRotation.ROTATION_180));
	}
	
	/**
	 * Test xSize, with Rotation of 270 degrees.
	 */
	@Test 
	public void testXSize270() {
		assertEquals(room.size.getHeight(), roomNode.xSize(RoomRotation.ROTATION_270));
	}
	
	/**
	 * Test xSize, with Rotation of wrong rotation.
	 * @throws IllegalArgumentException
	 * 			should be thrown here
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testXSizeWrong() throws IllegalArgumentException {
		assertEquals(room.size.getWidth(), roomNode.xSize(RoomRotation.ROTATION_EXCEPTION));
	}
	
	/**
	 * Test xSize, with Rotation of 0 degrees.
	 */
	@Test 
	public void testySize0() {
		assertEquals(room.size.getHeight(), roomNode.ySize(RoomRotation.ROTATION_0));
	}
	
	/**
	 * Test ySize, with Rotation of 90 degrees.
	 */
	@Test 
	public void testySize90() {
		assertEquals(room.size.getWidth(), roomNode.ySize(RoomRotation.ROTATION_90));
	}
	
	/**
	 * Test ySize, with Rotation of 180 degrees.
	 */
	@Test 
	public void testySize180() {
		assertEquals(room.size.getHeight(), roomNode.ySize(RoomRotation.ROTATION_180));
	}
	
	/**
	 * Test ySize, with Rotation of 270 degrees.
	 */
	@Test 
	public void testySize270() {
		assertEquals(room.size.getWidth(), roomNode.ySize(RoomRotation.ROTATION_270));
	}
	
	/**
	 * Test ySize, with Rotation of wrong rotation.
	 * @throws IllegalArgumentException
	 * 			should be thrown here
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testySizeWrong() throws IllegalArgumentException {
		assertEquals(room.size.getWidth(), roomNode.ySize(RoomRotation.ROTATION_EXCEPTION));
	}

}
