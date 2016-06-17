package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.Room;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.util.Vec2I;

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
	
	/**
	 * Test possible placements with a bad placement due to obstruction.
	 */
	@Test
	public void testScanPossiblePlacementObstruction() {
		MazeTile[][] testTiles = new MazeTile[RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE][RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE];
		testTiles[SAFE_TEST_SIZE][SAFE_TEST_SIZE] = new MazeTile(SAFE_TEST_SIZE, SAFE_TEST_SIZE, TileType.WALL);
		assertFalse(roomNode.scanPossiblePlacement(testTiles, new Vec2I(RoomNode.MIN_DIST + 1, RoomNode.MIN_DIST + 1)));
	}
	
	/**
	 * Test possible placements with a bad placement due to being to near to the edges of the maze.
	 */
	@Test
	public void testScanPossiblePlacementTooCloseToMazeEnd() {
		MazeTile[][] testTiles = new MazeTile[RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE][RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE];
		assertFalse(roomNode.scanPossiblePlacement(testTiles, new Vec2I(0, 0)));
	}
	
	/**
	 * Test possible placements with good placement.
	 */
	@Test
	public void testScanPossiblePlacementGoodPlacement() {
		MazeTile[][] testTiles = new MazeTile[RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE][RoomNode.MIN_DIST 
		                                      + room.size.getWidth() + SAFE_TEST_SIZE];
		assertTrue(roomNode.scanPossiblePlacement(testTiles, new Vec2I(RoomNode.MIN_DIST + 1, RoomNode.MIN_DIST + 1)));
	}
	
	/**
	 * Test equals, with other object.
	 */
	@Test
	public void testEqualsOtherObject() {
		assertNotEquals(roomNode, 5);
	}
	
	/**
	 * Test equals, with other ID.
	 */
	@Test
	public void testEqualsOtherID() {
		Room otherRoom = new Room("/maps/correctWithName/");
		RoomNode otherRoomNode = new RoomNode(otherRoom, 1);
		assertNotEquals(roomNode, otherRoomNode);
	}
	
	/**
	 * Test equals, with same ID. 
	 * Should be equal even when room is another room.
	 */
	@Test
	public void testEqualsSameID() {
		Room otherRoom = new Room("/maps/testMap2/endroom/");
		RoomNode otherRoomNode = new RoomNode(otherRoom, 0);
		assertEquals(roomNode, otherRoomNode);
	}
	
	/**
	 * Test equals, with same ID. 
	 * Should be equal even when room is another room.
	 */
	@Test
	public void testEqualsSame() {
		assertEquals(roomNode, roomNode);
	}
	
	/**
	 * Test equals, with copy, that has same ID. 
	 */
	@Test
	public void testCopy() {
		RoomNode copy = roomNode.copy(0);
		assertEquals(roomNode, copy);
	}
}
