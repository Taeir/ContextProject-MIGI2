package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;

import nl.tudelft.contextproject.model.level.Room;
import nl.tudelft.contextproject.util.Vec2I;

/**
 * Room entrance point test class.
 */
public class RoomEntrancePointTest extends DoorLocationTest {

	@Override
	public DoorLocation getDoorLocation() {
		return new RoomNode(new Room("/maps/testMap2/endroom/"), 0).entrances.get(0);
	}

	@Override
	public DoorLocation getDoorLocationOtherLocation() {
		RoomEntrancePoint exitPoint  = new RoomNode(new Room("/maps/testMap2/endroom/"), 0).entrances.get(0);
		exitPoint.updateDoorLocation(new Vec2I(42, 42));
		return exitPoint;
	}

	@Override
	public DoorLocation getDoorLocationOtherRoomNode() {
		return new RoomNode(new Room("/maps/testMap2/endroom/"), 1).entrances.get(0);
	}
	
	@Override
	public void testHashCode() {
		assertEquals(992, doorLocation.hashCode());
	}
	
	@Override
	public void testUpdateDoorLocation() {
		doorLocation.updateDoorLocation(new Vec2I(42, 42));
		assertEquals(new Vec2I(42, 43), doorLocation.location);
	}
}
