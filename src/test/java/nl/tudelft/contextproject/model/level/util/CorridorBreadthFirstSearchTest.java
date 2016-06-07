package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Stack;

import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Corridor breadth first search test class.
 */
public class CorridorBreadthFirstSearchTest extends TestBase {
	
	/**
	 * Test if correct Corridor is created.
	 */
	@Test
	public void testCreatCorridor() {
		MazeTile[][] mazeTiles = new MazeTile[3][3];
		mazeTiles[0][0] = new MazeTile(0, 0, TileType.DOOR_EXIT);
		mazeTiles[0][2] = new MazeTile(0, 2, TileType.DOOR_ENTRANCE);
		Stack<Vec2I> testStack = CorridorBreadthFirstSearch.breadthFirstSearch(mazeTiles, new Vec2I(0, 0), new Vec2I(0, 2));
		assertEquals(2, testStack.size());
	}

	/**
	 * Get smaller distance: North.
	 */
	@Test
	public void testGetSmallerDistanceNodeNorth() {
		HashMap<Vec2I, Integer> distanceMap = new HashMap<Vec2I, Integer>();
		Vec2I previousLocation = new Vec2I(0, 1);
		Vec2I smallerDistanceLocation = new Vec2I(0, 0);
		distanceMap.put(previousLocation, 1);
		distanceMap.put(smallerDistanceLocation, 0);
		assertEquals(smallerDistanceLocation, CorridorBreadthFirstSearch.getSmallerDistanceNode(distanceMap, previousLocation));
	}
	
	/**
	 * Get smaller distance: South.
	 */
	@Test
	public void testGetSmallerDistanceNodeSouth() {
		HashMap<Vec2I, Integer> distanceMap = new HashMap<Vec2I, Integer>();
		Vec2I previousLocation = new Vec2I(0, 0);
		Vec2I smallerDistanceLocation = new Vec2I(0, 1);
		distanceMap.put(previousLocation, 1);
		distanceMap.put(smallerDistanceLocation, 0);
		assertEquals(smallerDistanceLocation, CorridorBreadthFirstSearch.getSmallerDistanceNode(distanceMap, previousLocation));
	}
	
	/**
	 * Get smaller distance: West.
	 */
	@Test
	public void testGetSmallerDistanceNodeWest() {
		HashMap<Vec2I, Integer> distanceMap = new HashMap<Vec2I, Integer>();
		Vec2I previousLocation = new Vec2I(1, 0);
		Vec2I smallerDistanceLocation = new Vec2I(0, 0);
		distanceMap.put(previousLocation, 1);
		distanceMap.put(smallerDistanceLocation, 0);
		assertEquals(smallerDistanceLocation, CorridorBreadthFirstSearch.getSmallerDistanceNode(distanceMap, previousLocation));
	}
	
	/**
	 * Get smaller distance: East.
	 */
	@Test
	public void testGetSmallerDistanceNodeEast() {
		HashMap<Vec2I, Integer> distanceMap = new HashMap<Vec2I, Integer>();
		Vec2I previousLocation = new Vec2I(0, 0);
		Vec2I smallerDistanceLocation = new Vec2I(1, 0);
		distanceMap.put(previousLocation, 1);
		distanceMap.put(smallerDistanceLocation, 0);
		assertEquals(smallerDistanceLocation, CorridorBreadthFirstSearch.getSmallerDistanceNode(distanceMap, previousLocation));
	}

	/**
	 * Test get North Neighbor: no neighbor.
	 * Top of the map.
	 */
	@Test
	public void testGetNeighBorsNorthNoNeighbor() {
		MazeTile[][] mazeTiles = new MazeTile[1][1];
		Vec2I currentNode = new Vec2I(0, 0);
		Vec2I endNode = new Vec2I(2, 2);
		assertTrue(CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).isEmpty());
	}
	
	/**
	 * Test get North Neighbor: not null and not endNode.
	 */
	@Test
	public void testGetNeighBorsNorthNotNullNotEndNode() {
		MazeTile[][] mazeTiles = new MazeTile[2][2];
		mazeTiles[0][0] = new MazeTile(0, 0, TileType.FLOOR);
		mazeTiles[1][1] = new MazeTile(0, 0, TileType.FLOOR);
		Vec2I currentNode = new Vec2I(0, 1);
		Vec2I endNode = new Vec2I(2, 2);
		assertTrue(CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).isEmpty());
	}
	
	/**
	 * Test get North Neighbor: endNode.
	 */
	@Test
	public void testGetNeighBorsNorthEndNode() {
		MazeTile[][] mazeTiles = new MazeTile[2][2];
		mazeTiles[0][0] = new MazeTile(0, 0, TileType.FLOOR);
		mazeTiles[1][1] = new MazeTile(0, 0, TileType.FLOOR);
		Vec2I currentNode = new Vec2I(0, 1);
		Vec2I endNode = new Vec2I(0, 0);
		assertEquals(1, CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).size());
	}
	
	/**
	 * Test get North Neighbor: null.
	 */
	@Test
	public void testGetNeighBorsNorthNull() {
		MazeTile[][] mazeTiles = new MazeTile[2][2];
		mazeTiles[1][1] = new MazeTile(0, 0, TileType.FLOOR);
		Vec2I currentNode = new Vec2I(0, 1);
		Vec2I endNode = new Vec2I(2, 2);
		assertEquals(1, CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).size());
	}
	
	/**
	 * Test get South Neighbor: no neighbor.
	 * Bottom of the map.
	 */
	@Test
	public void testGetNeighBorsSouthNoNeighbor() {
		MazeTile[][] mazeTiles = new MazeTile[1][1];
		Vec2I currentNode = new Vec2I(0, 0);
		Vec2I endNode = new Vec2I(2, 2);
		assertTrue(CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).isEmpty());
	}
	
	/**
	 * Test get South Neighbor: not null and not endNode.
	 */
	@Test
	public void testGetNeighBorsSouthNotNullNotEndNode() {
		MazeTile[][] mazeTiles = new MazeTile[2][2];
		mazeTiles[0][1] = new MazeTile(0, 0, TileType.FLOOR);
		mazeTiles[1][0] = new MazeTile(0, 0, TileType.FLOOR);
		Vec2I currentNode = new Vec2I(0, 0);
		Vec2I endNode = new Vec2I(2, 2);
		assertTrue(CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).isEmpty());
	}
	
	/**
	 * Test get South Neighbor: endNode.
	 */
	@Test
	public void testGetNeighBorsSouthEndNode() {
		MazeTile[][] mazeTiles = new MazeTile[2][2];
		mazeTiles[0][1] = new MazeTile(0, 0, TileType.FLOOR);
		mazeTiles[1][1] = new MazeTile(0, 0, TileType.FLOOR);
		Vec2I currentNode = new Vec2I(0, 1);
		Vec2I endNode = new Vec2I(0, 1);
		assertEquals(1, CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).size());
	}
	
	/**
	 * Test get South Neighbor: null.
	 */
	@Test
	public void testGetNeighBorsSouthNull() {
		MazeTile[][] mazeTiles = new MazeTile[2][2];
		mazeTiles[1][1] = new MazeTile(0, 0, TileType.FLOOR);
		Vec2I currentNode = new Vec2I(0, 1);
		Vec2I endNode = new Vec2I(2, 2);
		assertEquals(1, CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).size());
	}

	/**
	 * Test get West Neighbor: no neighbor.
	 * left of the map.
	 */
	@Test
	public void testGetNeighBorsWestNoNeighbor() {
		MazeTile[][] mazeTiles = new MazeTile[1][1];
		Vec2I currentNode = new Vec2I(0, 0);
		Vec2I endNode = new Vec2I(2, 2);
		assertTrue(CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).isEmpty());
	}
	
	/**
	 * Test get West Neighbor: not null and not endNode.
	 */
	@Test
	public void testGetNeighBorsWestNotNullNotEndNode() {
		MazeTile[][] mazeTiles = new MazeTile[2][2];
		mazeTiles[0][0] = new MazeTile(0, 0, TileType.FLOOR);
		mazeTiles[1][1] = new MazeTile(0, 0, TileType.FLOOR);
		Vec2I currentNode = new Vec2I(1, 0);
		Vec2I endNode = new Vec2I(2, 2);
		assertTrue(CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).isEmpty());
	}
	
	/**
	 * Test get West Neighbor: endNode.
	 */
	@Test
	public void testGetNeighBorsWestEndNode() {
		MazeTile[][] mazeTiles = new MazeTile[2][2];
		mazeTiles[1][1] = new MazeTile(0, 0, TileType.FLOOR);
		mazeTiles[1][0] = new MazeTile(0, 0, TileType.FLOOR);
		Vec2I currentNode = new Vec2I(1, 0);
		Vec2I endNode = new Vec2I(1, 0);
		assertEquals(1, CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).size());
	}
	
	/**
	 * Test get West Neighbor: null.
	 */
	@Test
	public void testGetNeighBorsWestNull() {
		MazeTile[][] mazeTiles = new MazeTile[2][2];
		mazeTiles[1][1] = new MazeTile(0, 0, TileType.FLOOR);
		Vec2I currentNode = new Vec2I(1, 0);
		Vec2I endNode = new Vec2I(2, 2);
		assertEquals(1, CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).size());
	}
	
	/**
	 * Test get East Neighbor: no neighbor.
	 * left of the map.
	 */
	@Test
	public void testGetNeighBorsEastNoNeighbor() {
		MazeTile[][] mazeTiles = new MazeTile[1][1];
		Vec2I currentNode = new Vec2I(0, 0);
		Vec2I endNode = new Vec2I(2, 2);
		assertTrue(CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).isEmpty());
	}
	
	/**
	 * Test get East Neighbor: not null and not endNode.
	 */
	@Test
	public void testGetNeighBorsEastNotNullNotEndNode() {
		MazeTile[][] mazeTiles = new MazeTile[2][2];
		mazeTiles[1][0] = new MazeTile(0, 0, TileType.FLOOR);
		mazeTiles[0][1] = new MazeTile(0, 0, TileType.FLOOR);
		Vec2I currentNode = new Vec2I(0, 0);
		Vec2I endNode = new Vec2I(2, 2);
		assertTrue(CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).isEmpty());
	}
	
	/**
	 * Test get East Neighbor: endNode.
	 */
	@Test
	public void testGetNeighBorsEastEndNode() {
		MazeTile[][] mazeTiles = new MazeTile[2][2];
		mazeTiles[1][0] = new MazeTile(0, 0, TileType.FLOOR);
		mazeTiles[0][1] = new MazeTile(0, 0, TileType.FLOOR);
		Vec2I currentNode = new Vec2I(0, 0);
		Vec2I endNode = new Vec2I(1, 0);
		assertEquals(1, CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).size());
	}
	
	/**
	 * Test get East Neighbor: null.
	 */
	@Test
	public void testGetNeighBorsEastNull() {
		MazeTile[][] mazeTiles = new MazeTile[2][2];
		mazeTiles[0][1] = new MazeTile(0, 0, TileType.FLOOR);
		Vec2I currentNode = new Vec2I(0, 0);
		Vec2I endNode = new Vec2I(2, 2);
		assertEquals(1, CorridorBreadthFirstSearch.getNeighBors(mazeTiles, currentNode, endNode).size());
	}
}
