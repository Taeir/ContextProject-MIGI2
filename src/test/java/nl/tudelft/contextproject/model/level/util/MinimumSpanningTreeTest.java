package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.level.MSTBasedLevelFactory;

/**
 * Test class for minimum spanning tree.
 */
public class MinimumSpanningTreeTest extends TestBase {

	private MinimumSpanningTree mst;
	private MSTBasedLevelFactory factoryMST;
	
	/**
	 * Set up a minimum spanning tree class, using a MSTBasedLevelFactory.
	 */
	@Before
	public void setupBeforeTest() {
		factoryMST = new MSTBasedLevelFactory("/maps/testMap2/");
		factoryMST.createRNG(0L);
		factoryMST.placeStartAndTreasureRoom();
		factoryMST.placeOtherRooms();
		factoryMST.createEdges();
		mst = new MinimumSpanningTree(factoryMST.usedNodes, factoryMST.edges);
	}
	
	/**
	 * Test constructor, used nodes.
	 */
	@Test
	public void testMinimumSpanningTreeUsedNodes() {
		assertEquals(factoryMST.usedNodes, mst.roomNodes);
	}
	
	/**
	 * Test constructor, edges.
	 */
	@Test
	public void testMinimumSpanningTreeEdges() {
		assertEquals(factoryMST.edges, mst.corridorEdges);
	}
	
	/**
	 * Test constructor, created graphNodes.
	 */
	@Test
	public void testMinimumSpanningTreeNotNullgraphNodes() {
		assertNotNull(mst.graphNodes);
	}
	
	/**
	 * Test createTransformedGraph, test for a EXIT_NODE MSTNode in graphNodes.
	 */
	@Test
	public void testCreateTransformedGraphExitNodeInGraph() {
		mst.createTransformedGraph();
		assertEquals(7, mst.graphNodes.size());
	}
	
	/**
	 * Check if check connection of easy graph still works after removing 1 edge.
	 */
	@Test
	public void testCheckConnectionAfterRemovalTrue() {
		HashSet<MSTNode> graphNodesSimple = new HashSet<MSTNode>();
		MSTNode node1 = new MSTNode(MSTNodeType.CONNECTOR_NODE, null, 1);
		MSTNode node2 = new MSTNode(MSTNodeType.ENTRANCE_NODE, null, 2);
		MSTNode node3 = new MSTNode(MSTNodeType.EXIT_NODE, null, 3);
		MSTEdge edge1 = new MSTEdge(node1, node2, 0, 1);
		MSTEdge edge2 = new MSTEdge(node2, node3, 0, 1);
		MSTEdge edge3 = new MSTEdge(node3, node1, 0, 1);
		node1.addOutGoingEdge(edge1);
		node1.addInComingEdge(edge3);
		node2.addOutGoingEdge(edge2);
		node2.addInComingEdge(edge1);
		node3.addOutGoingEdge(edge3);
		node3.addInComingEdge(edge2);
		
		graphNodesSimple.add(node1);
		graphNodesSimple.add(node2);
		graphNodesSimple.add(node3);
		mst.graphNodes = graphNodesSimple;
		assertTrue(mst.checkConnectionAfterRemoval(edge1));
	}
	
	/**
	 * Check if check connection of easy graph doesn't work after removing 1 edge.
	 */
	@Test
	public void testCheckConnectionAfterRemovalFalse() {
		HashSet<MSTNode> graphNodesSimple = new HashSet<MSTNode>();
		MSTNode node1 = new MSTNode(MSTNodeType.CONNECTOR_NODE, null, 1);
		MSTNode node2 = new MSTNode(MSTNodeType.ENTRANCE_NODE, null, 2);
		MSTEdge edge1 = new MSTEdge(node1, node2, 0, 1);
		node1.addOutGoingEdge(edge1);
		node2.addInComingEdge(edge1);
		
		graphNodesSimple.add(node1);
		graphNodesSimple.add(node2);
		mst.graphNodes = graphNodesSimple;
		assertFalse(mst.checkConnectionAfterRemoval(edge1));
	}
	
	/**
	 * Test runReverseDeleteAlgorithm.
	 * Runs the algorithm. Due to the nature of the graph certain conditions can be checked easily.
	 * Tests if number of nodes does not change (tree should still be connected).
	 */
	@Test
	public void testRunReverseDeleteAlgorithmSameNumberOfNodes() {
		mst.runReverseDeleteAlgorithm();
		assertEquals(7, mst.graphNodes.size());
	}
	
	/**
	 * Test runReverseDeleteAlgorithm.
	 * Runs the algorithm. Due to the nature of the graph certain conditions can be checked easily.
	 * Test if there are 2 edges selected in the end.
	 */
	@Test
	public void testRunReverseDeleteAlgorithmNumberOfSelectedCorridors() {
		mst.runReverseDeleteAlgorithm();
		assertEquals(2, mst.getCorridorIDsReverseAlgorithm().size());
	}
}
