package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.Room;

/**
 * Test class for RoomNode.
 */
public class RoomNodeTest extends TestBase {

	private static final int SAFE_TEST_SIZE = RoomNode.MIN_DIST + 3;
	private Room room;
	private RoomNode roomNode;
	
	/**
	 * Set up RoomNode.
	 */
	@Before
	public void roomNodeSetUp() {
		room = new Room("/maps/correctWithName/");
		roomNode = new RoomNode(room, 0);
	}
	
	/**
	 * Test constructor.
	 */
	@Test
	public void testRoomNode() {
		assertEquals(room, roomNode.room);
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
	 * Test checkBoundaryCollision with improper placement.
	 * Room is placed too close to top boundary
	 */
	@Test
	public void testCheckBoundaryCollisionTop() {
		MazeTile[][] testTiles = new MazeTile[RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE][RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE];
		assertTrue(roomNode.checkBoundaryCollision(testTiles, 
			new Vec2I(0, RoomNode.MIN_DIST + 1)));
	}
	
	/**
	 * Test checkBoundaryCollision with improper placement.
	 * Room is placed too close to left boundary
	 */
	@Test
	public void testCheckBoundaryCollisionLeft() {
		MazeTile[][] testTiles = new MazeTile[RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE][RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE];
		assertTrue(roomNode.checkBoundaryCollision(testTiles, 
				new Vec2I(RoomNode.MIN_DIST + 1, 0)));
	}
	
	/**
	 * Test checkBoundaryCollision with improper placement.
	 * Room is placed too close to right boundary
	 */
	@Test
	public void testCheckBoundaryCollisionRight() {
		MazeTile[][] testTiles = new MazeTile[RoomNode.MIN_DIST 
		                                      + room.size.getWidth()][RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE];
		assertTrue(roomNode.checkBoundaryCollision(testTiles, 
				new Vec2I(RoomNode.MIN_DIST + 1,
				RoomNode.MIN_DIST + 1)));
	}

	/**
	 * Test checkBoundaryCollision with improper placement.
	 * Room is placed too close to bottom boundary
	 */
	@Test
	public void testCheckBoundaryCollisionBottom() {
		MazeTile[][] testTiles = new MazeTile[RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE][RoomNode.MIN_DIST 
		                                      + room.size.getWidth()];
		assertTrue(roomNode.checkBoundaryCollision(testTiles, 
				new Vec2I(RoomNode.MIN_DIST + 1,
				RoomNode.MIN_DIST + 1)));
	}
	
	/**
	 * Test checkBoundaryCollision with proper placement.
	 */
	@Test
	public void testCheckBoundaryCollisionProper() {
		MazeTile[][] testTiles = new MazeTile[RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE][RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE];
		assertFalse(roomNode.checkBoundaryCollision(testTiles, 
				new Vec2I(RoomNode.MIN_DIST + 1,
				RoomNode.MIN_DIST + 1)));
	}
}
