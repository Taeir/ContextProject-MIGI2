package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;
import java.util.Collection;

import com.jme3.light.Light;

import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.Room;

/**
 * Room Node for Graph creation.
 * Contains methods useful for graphs.
 */
public class RoomNode {
	
	/**
	 * Minimum distance between rooms and boundaries.
	 */
	public static final int MIN_DIST = 2;
	
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
		entrances = new ArrayList<RoomEntrancePoint>();
		exits = new ArrayList<RoomExitPoint>();
		for (Vec2I door : room.entranceDoorsLocations) {
			entrances.add(new RoomEntrancePoint(room, door));
		}
		for (Vec2I door : room.exitDoorLocations) {
			exits.add(new RoomExitPoint(room, door));
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
	 * @param coordinates
	 * 				location
	 * @return
	 * 				true if placement of RoomNode at xCoordinate, yCoordinate with Rotation of rotation is possible 
	 */
	public boolean scanPossiblePlacement(MazeTile[][] tiles, Vec2I coordinates) {
		//First check boundaries 
		if (checkBoundaryCollision(tiles, coordinates)) {
			return false;
		}
		
		//Check room overlap
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
	public boolean checkRoomOverlap(MazeTile[][] tiles, Vec2I coordinates) {
		int xSize = room.size.getWidth();
		int ySize = room.size.getHeight();
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
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
	 * 				location on the map
	 * @return
	 * 				true if room overlaps with existing tile. 
	 */
	public boolean checkTileOverlap(MazeTile[][] tiles, Vec2I coordinates) {
		for (int i = coordinates.x - MIN_DIST; i < coordinates.x + MIN_DIST; i++) {
			for (int j = coordinates.y - MIN_DIST; j < coordinates.y + MIN_DIST; j++) {
				if (tiles[i][j] != null) {
					return true;
				}
			}
		}
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
	public boolean checkBoundaryCollision(MazeTile[][] tiles, Vec2I coordinates) {
		//Check collisions with top boundary
		if (coordinates.y <= (MIN_DIST)) {
			return true;
		}
		//Check collisions with left boundary
		if (coordinates.x  <= (MIN_DIST)) {
			return true;
		}
		//Check collisions with right boundary
		if ((coordinates.x +  MIN_DIST) >= tiles[0].length) {
			return true;
		}
		//Check collisions bottom boundary
		if ((coordinates.y + MIN_DIST) >= tiles.length) {
			return true;
		}
		//No collisions
		return false;
	}
	
	/**
	 * Carve room into tiles.
	 * @param tiles
	 * 				map to carve room on
	 * @param coordinates
	 * 				location of room
	 */
	public void carveRoomNode(MazeTile[][] tiles, Vec2I coordinates) {
		int xSize = room.size.getWidth();
		int ySize = room.size.getHeight();
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				tiles[coordinates.x + x][coordinates.y + y] = room.mazeTiles[x][y];
			}
		}
	}

	/**
	 * Get all outgoing connection points.
	 * @return
	 * 				all outgoing connection points
	 */
	public Collection<? extends RoomExitPoint> geOutGoingConnections() {
		return exits;
	}

	/**
	 * get all incoming connection points.
	 * @return
	 * 				all incoming connection points
	 */
	public Collection<? extends RoomEntrancePoint> getIncomingConnections() {
		return entrances;
	}

	/**
	 * Get lights.
	 * @return
	 * 				lights of the room
	 */
	public Collection<? extends Light> getLights() {
		return room.lights;
	}
}
