package nl.tudelft.contextproject.model.level.util;

import nl.tudelft.contextproject.model.level.Room;

/**
 * Room Node for Graph creation.
 * Contains methods useful for graphs.
 */
public class RoomNode {
	
	public Room room;
	
	/**
	 * Constructor.
	 * @param room
	 * 		room to set.
	 */
	public RoomNode(Room room) {
		this.room = room;
	}
	
	/**
	 * Get number of outgoing connection points.
	 * @return
	 * 			number of outgoing connection points
	 */
	public int getNumberOfOutgoingConnections() {
		return room.exitDoorLocations.size();
	}
	
	/**
	 * Get number of incoming connection points.
	 * @return
	 * 			number of incoming connection points
	 */
	public int getNumberOfIncommingConnections() {
		return room.entranceDoorsLocations.size();
	}

}
