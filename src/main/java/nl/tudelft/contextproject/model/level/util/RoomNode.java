package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;

import nl.tudelft.contextproject.model.level.Room;

/**
 * Room Node for Graph creation.
 * Contains methods useful for graphs.
 */
public class RoomNode {
	
	
	public Boolean used;
	public Room room;
	public ArrayList<DoorConnectionPoint> entrances;
	public ArrayList<DoorConnectionPoint> exits;
	/**
	 * Constructor.
	 * @param room
	 * 			room to set.
	 */
	public RoomNode(Room room) {
		this.room = room;
		this.used = false;
		entrances = new ArrayList<DoorConnectionPoint>();
		exits = new ArrayList<DoorConnectionPoint>();
		for (Vec2I door : room.entranceDoorsLocations) {
			entrances.add(new DoorConnectionPoint(door));
		}
		for (Vec2I door : room.exitDoorLocations) {
			exits.add(new DoorConnectionPoint(door));
		}
	}
	
	/**
	 * Get number of outgoing connection points.
	 * @return
	 * 			number of outgoing connection points
	 */
	public int getNumberOfOutgoingConnections() {
		return exits.size();
	}
	
	/**
	 * Get number of incoming connection points.
	 * @return
	 * 			number of incoming connection points
	 */
	public int getNumberOfIncommingConnections() {
		return entrances.size();
	}

}
