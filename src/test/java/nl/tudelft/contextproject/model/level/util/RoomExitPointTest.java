package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import nl.tudelft.contextproject.model.level.Room;

/**
 * Test class for Room exit points.
 */
public class RoomExitPointTest extends DoorLocationTest {

	@Override
	public DoorLocation getDoorLocation() {
		return new RoomNode(new Room("/maps/testMap2/startroom/"), 0).exits.get(0);
	}

	@Override
	public DoorLocation getDoorLocationOtherLocation() {
		RoomExitPoint exitPoint  = new RoomNode(new Room("/maps/testMap2/startroom/"), 0).exits.get(0);
		exitPoint.updateDoorLocation(new Vec2I(42, 42));
		return exitPoint;
	}

	@Override
	public DoorLocation getDoorLocationOtherRoomNode() {
		return new RoomNode(new Room("/maps/testMap2/startroom/"), 1).exits.get(0);
	}

	@Override
	public void testHashCode() {
		assertEquals(4064255, doorLocation.hashCode());
	}
	
	@Override
	public void testUpdateDoorLocation() {
		doorLocation.updateDoorLocation(new Vec2I(42, 42));
		assertEquals(new Vec2I(44, 43), doorLocation.location);
	}
}
