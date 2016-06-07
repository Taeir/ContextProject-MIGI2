package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.level.Room;

/**
 * Test class for Corridor edge.
 */
public class CorridorEdgeTest extends TestBase {

	private CorridorEdge corridorEdge;
	private int id;
	private double weight;
	private RoomEntrancePoint endOfCorridor;
	private RoomExitPoint startOfCorridor;
	
	/**
	 * Set up corridor edge to be tested.
	 */
	@Before
	public void setUpBeforeTest() {
		startOfCorridor = new RoomNode(new Room("/maps/testMap2/startroom/"), 1).exits.get(0);
		endOfCorridor = new RoomNode(new Room("/maps/testMap2/endroom/"), 2).entrances.get(0);
		weight = startOfCorridor.location.distance(endOfCorridor.location);
		id = 5;
		corridorEdge = new CorridorEdge(startOfCorridor, endOfCorridor, id);
	}
	
	/**
	 * Test constructor, test start location.
	 */
	@Test
	public void testCorridorEdgeStartLocation() {
		assertEquals(startOfCorridor, corridorEdge.start);
	}
	
	/**
	 * Test constructor, test end location.
	 */
	@Test
	public void testCorridorEdgeEndLocation() {
		assertEquals(endOfCorridor, corridorEdge.end);
	}
	
	/**
	 * Test constructor, test ID.
	 */
	@Test
	public void testCorridorEdgeID() {
		assertEquals(id, corridorEdge.id);
	}

	/**
	 * Test constructor, weight assignment.
	 */
	@Test
	public void testCorridorWeight() {
		assertEquals(weight, corridorEdge.weight, 1E-4);
	}
	
	/**
	 * Test if calculate weight works.
	 */
	@Test
	public void testCalculateWeight() {
		assertEquals(weight, corridorEdge.calculateWeight(), 1E-4);
	}
}
