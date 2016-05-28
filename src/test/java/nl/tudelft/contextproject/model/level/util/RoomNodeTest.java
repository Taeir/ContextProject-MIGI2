package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.level.Room;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Test class for RoomNode.
 */
public class RoomNodeTest extends TestBase {

	private static final int SAFE_TEST_SIZE = RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES + 3;
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
	
	/**
	 * Test checkBoundaryCollision with improper placement.
	 * Room is placed too close to top boundary
	 */
	@Test
	public void testCheckBoundaryCollisionTop() {
		TileType[][] testTiles = new TileType[RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE][RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE];
		assertTrue(roomNode.checkBoundaryCollision(testTiles, 
				RoomRotation.ROTATION_0, 0, RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES + 1));
	}
	
	/**
	 * Test checkBoundaryCollision with improper placement.
	 * Room is placed too close to left boundary
	 */
	@Test
	public void testCheckBoundaryCollisionLeft() {
		TileType[][] testTiles = new TileType[RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE][RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE];
		assertTrue(roomNode.checkBoundaryCollision(testTiles, 
				RoomRotation.ROTATION_0, RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES + 1, 0));
	}
	
	/**
	 * Test checkBoundaryCollision with improper placement.
	 * Room is placed too close to right boundary
	 */
	@Test
	public void testCheckBoundaryCollisionRight() {
		TileType[][] testTiles = new TileType[RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES 
		                                      + room.size.getWidth()][RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE];
		assertTrue(roomNode.checkBoundaryCollision(testTiles, 
				RoomRotation.ROTATION_0, 
				RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES + 1,
				RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES + 1));
	}

	/**
	 * Test checkBoundaryCollision with improper placement.
	 * Room is placed too close to bottom boundary
	 */
	@Test
	public void testCheckBoundaryCollisionBottom() {
		TileType[][] testTiles = new TileType[RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE][RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES 
		                                      + room.size.getWidth()];
		assertTrue(roomNode.checkBoundaryCollision(testTiles, 
				RoomRotation.ROTATION_0, 
				RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES + 1,
				RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES + 1));
	}
	
	/**
	 * Test checkBoundaryCollision with proper placement.
	 */
	@Test
	public void testCheckBoundaryCollisionProper() {
		TileType[][] testTiles = new TileType[RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE][RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE];
		assertFalse(roomNode.checkBoundaryCollision(testTiles, 
				RoomRotation.ROTATION_0, 
				RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES + 1,
				RoomNode.MINIMUM_DISTANCE_BETWEEN_ROOMNODES + 1));
	}
}
