package nl.tudelft.contextproject.model.level;

/**
 * Room tuple, data container for passing around 2 rooms at the same time.
 * 
 * Should be used for starter and treasure rooms.
 */
public class RoomTuple {
	
	protected Room starterRoom;
	
	protected Room treasureRoom;
	
	/**
	 * Constructor.
	 * 
	 * @param starterRoom
	 * 		starter Room
	 * @param treasureRoom
	 * 		treasure Room
	 */
	public RoomTuple(Room starterRoom, Room treasureRoom) {
		this.starterRoom = starterRoom;
		this.treasureRoom = treasureRoom;
	}

	/**
	 * Get starter room.
	 * 
	 * @return
	 * 		starter Room.
	 */
	public Room getStarterRoom() {
		return starterRoom;
	}

	/**
	 * Set starter Room.
	 * 
	 * @param starterRoom
	 *		Room to set to starterRoom
	 */
	public void setStarterRoom(Room starterRoom) {
		this.starterRoom = starterRoom;
	}

	/**
	 * Get treasure Room.
	 * 
	 * @return treasureRoom
	 * 		treasure Room
	 */
	public Room getTreasureRoom() {
		return treasureRoom;
	}

	/**
	 * Set treasure Room.
	 * 
	 * @param treasureRoom
	 * 		treasureRoom to set
	 */
	public void setTreasureRoom(Room treasureRoom) {
		this.treasureRoom = treasureRoom;
	}
	
	

}
