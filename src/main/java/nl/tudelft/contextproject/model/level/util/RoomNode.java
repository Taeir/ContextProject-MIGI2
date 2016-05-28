package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;

import nl.tudelft.contextproject.model.level.Room;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Room Node for Graph creation.
 * Contains methods useful for graphs.
 */
public class RoomNode {
	
	//Minimum distance between RoomNodes
	public static final int MINIMUM_DISTANCE_BETWEEN_ROOMNODES = 2;
	
	public Boolean used;
	public int xCoordinate;
	public int yCoordinate;
	public RoomRotation roomRotation;
	public Room room;
	public ArrayList<RoomEntrancePoint> entrances;
	public ArrayList<RoomExitPoint> exits;
	
	/**
	 * Constructor.
	 * @param room
	 * 			room to set.
	 */
	public RoomNode(Room room) {
		this.room = room;
		this.used = false;
		entrances = new ArrayList<RoomEntrancePoint>();
		exits = new ArrayList<RoomExitPoint>();
		for (Vec2I door : room.entranceDoorsLocations) {
			entrances.add(new RoomEntrancePoint(door));
		}
		for (Vec2I door : room.exitDoorLocations) {
			exits.add(new RoomExitPoint(door));
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
	
	/**
	 * Scan Tile type and room to see if it possible to place the room.
	 * @param tiles
	 * @param rotation
	 * @param xCoordinate
	 * @param yCoordinate
	 * @return
	 */
	public boolean scanPossiblePlacement(TileType[][] tiles, RoomRotation rotation, int xCoordinate,
			int yCoordinate) {
	
		if (checkBoundaryCollision(tiles, rotation, xCoordinate, yCoordinate)) {
			return false;
		}
		return false;
	}

	/**
	 * Check boundaries of the tiles map.
	 * @param tiles
	 * @param rotation
	 * @param xCoordinate2
	 * @param yCoordinate2
	 * @return
	 */
	public boolean checkBoundaryCollision(TileType[][] tiles, RoomRotation rotation, int xCoordinate,
			int yCoordinate) {
		//Check up collisions with top boundary
		//if ()

		return false;
	}
	
	/**
	 * Calculates the size from the start xCoordinate with rotation taken into account.
	 * @param rotation
	 * 				rotation of RoomNode
	 * @return
	 * 				difference in coordinates
	 * @throws IllegalArgumentException
	 *				if rotation is not defined
	 */
	public int xSize(RoomRotation rotation) throws IllegalArgumentException {
		switch (rotation) {
			case ROTATION_0: case ROTATION_180:
				return room.size.getWidth();
			case ROTATION_90: case ROTATION_270:
				return room.size.getHeight();
			default:
				throw new IllegalArgumentException("Need a rotation!");
		}
	}
	
	/**
	 * Calculates the size from the start yCoordinate with rotation taken into account.
	 * @param rotation
	 * 				rotation of RoomNode
	 * @return
	 * 				difference in coordinates
	 * @throws IllegalArgumentException
	 *				if rotation is not defined
	 */
	public int ySize(RoomRotation rotation) throws IllegalArgumentException {
		switch (rotation) {
			case ROTATION_0: case ROTATION_180:
				return room.size.getHeight();
			case ROTATION_90: case ROTATION_270:
				return room.size.getWidth();
			default:
				throw new IllegalArgumentException("Need a rotation!");
		}
	}

}
