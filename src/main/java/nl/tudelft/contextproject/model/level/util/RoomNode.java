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
	public Vec2I coordinates;
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
	 * 				possible tiles
	 * @param rotation
	 * 				possible rotation
	 * @param coordinates
	 * 				location
	 * @return
	 * 				true if placement of RoomNode at xCoordinate, yCoordinate with Rotation of rotation is possible 
	 */
	public boolean scanPossiblePlacement(TileType[][] tiles, Vec2I coordinates) {
	
		if (checkBoundaryCollision(tiles, coordinates)) {
			return false;
		}
		
		if (checkRoomOverlap(tiles, coordinates)) {
			return false;
		}
		return true;
	}

	/**
	 * Check if RoomNode at location would overlap with existing rooms.
	 * Boundaries should have been checked before this is called.
	 * @param tiles
	 * 				tiles map
	 * @param coordinates
	 * 				location
	 * @return
	 * 				true if room overlaps with existing room
	 */
	public boolean checkRoomOverlap(TileType[][] tiles, Vec2I coordinates) {
		int xSize = room.size.getWidth();
		int ySize = room.size.getHeight();
		for (int x = coordinates.x; x < coordinates.x + xSize; x++) {
			for (int y = coordinates.y; y < coordinates.y + ySize; y++) {
				if (checkTileOverlap(tiles, coordinates)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if tile intersects with existing tile.
	 * @param tiles
	 * 				map of tiles
	 * @param coordinates
	 * 				location
	 * @return
	 * 				true if room overlaps with existing tile. 
	 */
	public boolean checkTileOverlap(TileType[][] tiles, Vec2I coordinates) {
		return false;
	}
	
		

	/**
	 * Check boundaries of the tiles map.
	 * @param tiles
	 * 				map to be checked
	 * @param coordinates
	 * 				location
	 * @return
	 * 				true if there is a collision, false if there is none
	 */
	public boolean checkBoundaryCollision(TileType[][] tiles, Vec2I coordinates) {
		//Check collisions with top boundary
		if (coordinates.y <= (MINIMUM_DISTANCE_BETWEEN_ROOMNODES)) {
			return true;
		}
		//Check collisions with left boundary
		if (coordinates.x  <= (MINIMUM_DISTANCE_BETWEEN_ROOMNODES)) {
			return true;
		}
		//Check collisions with right boundary
		if ((coordinates.x +  MINIMUM_DISTANCE_BETWEEN_ROOMNODES) >= tiles[0].length) {
			return true;
		}
		//Check collisions bottom boundary
		if ((coordinates.y + MINIMUM_DISTANCE_BETWEEN_ROOMNODES) >= tiles.length) {
			return true;
		}
		//No collisions
		return false;
	}
}
