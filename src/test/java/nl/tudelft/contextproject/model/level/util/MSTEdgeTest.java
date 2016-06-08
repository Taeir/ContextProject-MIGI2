package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * MSTEdge test class.
 */
public class MSTEdgeTest {

	private MSTNode startNode;
	private MSTNode endNode;
	private double weight;
	private int corridorID;
	private MSTEdge edge;
	
	/**
	 * Set up test edge.
	 */
	@Before
	public void setupBeforeTest() {
		startNode = new MSTNode(MSTNodeType.EXIT_NODE, null, 1);
		endNode = new MSTNode(MSTNodeType.ENTRANCE_NODE, null, 2);
		weight = 42.0;
		corridorID = 1;
		edge = new MSTEdge(startNode, endNode, weight, corridorID);
	}
	
	/**
	 * Test constructor, startNode.
	 */
	@Test
	public void testMSTEdgeStartNode() {
		assertEquals(startNode, edge.startNode);
	}

	/**
	 * Test constructor, endNode.
	 */
	@Test
	public void testMSTEdgeEndNode() {
		assertEquals(endNode, edge.endNode);
	}
	
	/**
	 * Test constructor, weight.
	 */
	@Test
	public void testMSTEdgeWeight() {
		assertEquals(weight, edge.weight, 1E-4);
	}
	
	/**
	 * Test constructor, corridorEdgeID.
	 */
	@Test
	public void testMSTEdgeCorridorID() {
		assertEquals(corridorID, edge.corridorID);
	}
	
	/**
	 * Test constructor, endNode.
	 */
	@Test
	public void testToString() {
		assertEquals("MSTEdge [startNode=MSTNode [edges=0, roomNodeID=1, nodeType=EXIT_NODE], "
				+ "endNode=MSTNode [edges=0, roomNodeID=2, nodeType=ENTRANCE_NODE], "
				+ "weight=42.0, "
				+ "corridorID=1]", edge.toString());
	}

}
