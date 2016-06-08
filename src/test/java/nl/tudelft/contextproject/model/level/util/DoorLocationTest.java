package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

/**
 * Abstract test class for door locations.
 */
public abstract class DoorLocationTest extends TestBase {

	protected DoorLocation doorLocation;
	
	/**
	 * Getter for a specific instance of DoorLocation.
	 *
	 * @return
	 * 		a DoorLocation to test with
	 */
	public abstract DoorLocation getDoorLocation();
	
	/**
	 * Getter for a specific instance of DoorLocation with other location.
	 *
	 * @return
	 * 		a DoorLocation to test with
	 */
	public abstract DoorLocation getDoorLocationOtherLocation();
	
	/**
	 * Getter for a specific instance of DoorLocation with other RoomNode.
	 *
	 * @return
	 * 		a DoorLocation to test with
	 */
	public abstract DoorLocation getDoorLocationOtherRoomNode();
	
	/**
	 * Test if hash code works.
	 */
	@Test
	public abstract void testHashCode();

	
	/**
	 * Set up a door Location for testing.
	 */
	@Before
	public void setupBeforeTest() {
		doorLocation = getDoorLocation();
	}
	
	/**
	 * Test if update location works.
	 */
	@Test
	public abstract void testUpdateDoorLocation();

	/**
	 * Test equals method, other object.
	 */
	@Test
	public void testEqualsOtherObject() {
		assertNotEquals(doorLocation, 5);
	}
	
	/**
	 * Test equals method, other location.
	 */
	@Test
	public void testEqualsOtherLocation() {
		DoorLocation otherLocation = getDoorLocationOtherLocation();
		assertNotEquals(doorLocation, otherLocation);
	}
	
	/**
	 * Test equals method, other roomNode.
	 */
	@Test
	public void testEqualsOtherRoomNode() {
		DoorLocation roomNode = getDoorLocationOtherRoomNode();
		assertNotEquals(doorLocation, roomNode);
	}
	
	/**
	 * Test equals method, other roomNode.
	 */
	@Test
	public void testEqualsSame() {
		assertEquals(doorLocation, doorLocation);
	}

}
