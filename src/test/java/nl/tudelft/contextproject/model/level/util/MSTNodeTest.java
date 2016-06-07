package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.level.Room;

/**
 * MSTNode Test class.
 */
public class MSTNodeTest extends TestBase {

	private MSTNode node;
	//private MSTNode node2;
	private MSTNodeType nodeType;
	private RoomExitPoint doorLocation;
	private int roomNodeID;
	
	/**
	 * Set up a MSTNode for testing.
	 */
	@Before
	public void setupBeforeTest() {
		nodeType = MSTNodeType.EXIT_NODE;
		RoomNode roomNode = new RoomNode(new Room("/maps/testMap2/startroom/"), 1);
		doorLocation = roomNode.exits.get(0);
		roomNodeID = roomNode.id;
		node = new MSTNode(nodeType, doorLocation, roomNodeID);
	}
	
	/**
	 * Test constructor, setting Node type.
	 */
	@Test
	public void testMSTNodeNodeType() {
		assertEquals(nodeType, node.nodeType);
	}
	
	/**
	 * Test constructor, not null incoming edges list.
	 */
	@Test
	public void testMSTNodeNotNullIncomingEdges() {
		assertNotNull(node.incomingEdges);
	}
	
	/**
	 * Test constructor, not null outgoing edges list.
	 */
	@Test
	public void testMSTNodeNotNullOutGoingEdges() {
		assertNotNull(node.outgoingEdges);
	}
	
	/**
	 * Test constructor, setting door type.
	 */
	@Test
	public void testMSTNodeDoorType() {
		assertEquals(doorLocation, node.originalDoor);
	}
	
	/**
	 * Test constructor, setting door type.
	 */
	@Test
	public void testMSTNodeRoomID() {
		assertEquals(roomNodeID, node.roomNodeID);
	}
	
	/**
	 * Test hash code.
	 */
	@Test
	public void testHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeType == null) ? 0 : nodeType.hashCode());
		result = prime * result + ((doorLocation == null) ? 0 : doorLocation.hashCode());
		result = prime * result + roomNodeID;
		assertEquals(result, node.hashCode());
	}

	/**
	 * Test equals with other object.
	 */
	@Test
	public void testEqualsOtherObject() {
		assertNotEquals(node, 5);
	}
	
	/**
	 * Test equals with other MSTNode with other NodeType.
	 */
	@Test
	public void testEqualsOtherNodeType() {
		MSTNode otherNode = new MSTNode(MSTNodeType.CONNECTOR_NODE, doorLocation, roomNodeID);
		assertNotEquals(node, otherNode);
	}
	
	/**
	 * Test equals with other MSTNode with null originalDoor.
	 */
	@Test
	public void testEqualsNullOriginalDoor() {
		MSTNode otherNode = new MSTNode(nodeType, null, roomNodeID);
		assertNotEquals(node, otherNode);
	}
	
	/**
	 * Test equals with other MSTNode with self null originalDoor.
	 */
	@Test
	public void testEqualsNullOriginalDoorSelf() {
		MSTNode otherNode = new MSTNode(nodeType, null, roomNodeID);
		assertNotEquals(otherNode, node);
	}
	
	/**
	 * Test equals with other MSTNode with self null originalDoor.
	 */
	@Test
	public void testEqualsOtherDoorLocation() {
		MSTNode otherNode = new MSTNode(nodeType, new RoomNode(new Room("/maps/testMap2/endroom/"), 0).entrances.get(0), roomNodeID);
		assertNotEquals(node, otherNode);
	}
	
	/**
	 * Test equals with other MSTNode with other roomNodeID.
	 */
	@Test
	public void testEqualsOtherRoomID() {
		MSTNode otherNode = new MSTNode(nodeType, doorLocation, roomNodeID + 1);
		assertNotEquals(node, otherNode);
	}
	
	/**
	 * Test equals with other MSTNode with other roomNodeID.
	 */
	@Test
	public void testEquals() {
		assertEquals(node, node);
	}

	/**
	 * Test add an outgoing edge.
	 */
	@Test
	public void testAddOutGoingEdge() {
		MSTEdge newEdge = new MSTEdge(node, node, 0, 0);
		node.addOutGoingEdge(newEdge);
		assertEquals(1, node.outgoingEdges.size());
	}

	/**
	 * Test add an incoming edge.
	 */
	@Test
	public void testAddInComingEdge() {
		MSTEdge newEdge = new MSTEdge(node, node, 0, 0);
		node.addInComingEdge(newEdge);
		assertEquals(1, node.incomingEdges.size());
	}

	/**
	 * Test to string.
	 */
	@Test
	public void testToString() {
		assertEquals("MSTNode [edges=0, roomNodeID=1, nodeType=EXIT_NODE]", node.toString());
	}
}
