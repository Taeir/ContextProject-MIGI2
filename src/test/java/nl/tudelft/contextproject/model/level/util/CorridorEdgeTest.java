package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.model.level.Room;

/**
 * Test class for Corridor edge.
 */
public class CorridorEdgeTest {

	private CorridorEdge corridorEdge;
	private Vec2I startVector;
	private Vec2I endVector;
	private RoomNode roomNodeStart;
	private RoomNode roomNodeEnd;
	
	/**
	 * Set up a corridor edge.
	 */
	@Before
	public void setUp() {
		startVector = new Vec2I(1, 0);
		endVector = new Vec2I(0, 1);
		roomNodeStart = new RoomNode(new Room("/maps/correctWithName/"));
		roomNodeEnd = new RoomNode(new Room("/maps/correctWithNameOther/"));
		corridorEdge = new CorridorEdge(startVector, endVector, roomNodeStart, roomNodeEnd);
	}
	
	/**
	 * Test constructor start Vector.
	 */
	@Test
	public void testCorridorEdgeConstructorStartVector() {
		assertTrue(startVector.equals(corridorEdge.startLocation));
	}
	
	/**
	 * Test constructor end Vector.
	 */
	@Test
	public void testCorridorEdgeConstructorEndVector() {
		assertTrue(endVector.equals(corridorEdge.endLocation));
	}
	
	/**
	 * Test constructor room node start.
	 */
	@Test
	public void testCorridorEdgeConstructorStartRoomNode() {
		assertTrue(roomNodeStart.equals(corridorEdge.startRoom));
	}
	
	/**
	 * Test constructor room node end.
	 */
	@Test
	public void testCorridorEdgeConstructorEndRoomNode() {
		assertTrue(roomNodeEnd.equals(corridorEdge.endRoom));
	}
	
	/**
	 * Test constructor not same RoomNode.
	 */
	@Test
	public void testCorridorEdgeConstructorFalse() {
		assertFalse(roomNodeStart.equals(corridorEdge.endRoom));
	}

}
