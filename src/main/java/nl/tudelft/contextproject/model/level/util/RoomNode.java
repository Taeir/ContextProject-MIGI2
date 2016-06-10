package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;
import java.util.Collection;

import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.Room;

/**
 * Room Node, contains placement maze generation related data and methods.
 * 
 * Contains methods useful for graphs and placement in MazeTile arrays.
 */
public class RoomNode {
	
	/**
	 * Minimum distance between rooms and boundaries.
	 */
	public static final int MIN_DIST = 3;
	
	public int id;
	public Vec2I coordinates;
	public Room room;
	public ArrayList<RoomEntrancePoint> entrances;
	public ArrayList<RoomExitPoint> exits;
	public ArrayList<CorridorEdge> outgoingEdges;
	
	/**
	 * Constructor.
	 * 
	 * @param room
	 * 		Room information, interior.
	 * @param id
	 * 		ID of RoomNode, used for references when switching data structures.
	 */
	public RoomNode(Room room, int id) {
		this.room = room;
		this.id = id;
		entrances = new ArrayList<RoomEntrancePoint>();
		exits = new ArrayList<RoomExitPoint>();
		outgoingEdges = new ArrayList<CorridorEdge>();
		for (Vec2I door : room.entranceDoorsLocations) {
			entrances.add(new RoomEntrancePoint(this, door));
		}
		for (Vec2I door : room.exitDoorLocations) {
			exits.add(new RoomExitPoint(this, door));
		}
	}
	
	/**
	 * Add an outgoing edge.
	 * 
	 * @param edge
	 * 		edge to add
	 */
	public void addOutgoingEdge(CorridorEdge edge) {
		outgoingEdges.add(edge);
	}
	
	/**
	 * Get number of outgoing connection points.
	 * 
	 * @return
	 * 		number of outgoing connection points
	 */
	public int getNumberOfOutgoingConnections() {
		return exits.size();
	}
	
	/**
	 * Get number of incoming connection points.
	 * 
	 * @return
	 * 		number of incoming connection points
	 */
	public int getNumberOfIncommingConnections() {
		return entrances.size();
	}
	
	/**
	 * Scan Tile type and room to see if it possible to place the room.
	 * First check the boundaries and then check room overlap.
	 * 
	 * @param tiles
	 * 		possible tiles
	 * @param coordinates
	 * 		location
	 * @return
	 * 		true if placement of RoomNode at xCoordinate, yCoordinate with Rotation of rotation is possible 
	 */
	public boolean scanPossiblePlacement(MazeTile[][] tiles, Vec2I coordinates) {
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
	 * 
	 * @param tiles
	 * 		tiles map
	 * @param coordinates
	 * 		location
	 * @return
	 * 		true if room overlaps with existing room
	 */
	public boolean checkRoomOverlap(MazeTile[][] tiles, Vec2I coordinates) {
		int xSize = room.size.getWidth();
		int ySize = room.size.getHeight();
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				if (checkTileOverlap(tiles, coordinates, x, y)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if tile intersects with existing tile.
	 * 
	 * @param tiles
	 * 		map of tiles
	 * @param coordinates
	 * 		room location on the map
	 * @param x
	 * 		current x location in the room interior 
	 * @param y 
	 * 		current y location in the room interior
	 * @return
	 * 		true if room overlaps with existing tile. 
	 */
	public boolean checkTileOverlap(MazeTile[][] tiles, Vec2I coordinates, int x, int y) {
		for (int i = x + coordinates.x - MIN_DIST; i < x + coordinates.x + MIN_DIST; i++) {
			for (int j = y + coordinates.y - MIN_DIST; j < y + coordinates.y + MIN_DIST; j++) {
				if (tiles[i][j] != null) {
					return true;
				}
			}
		}
		return false;
	}	

	/**
	 * Check boundaries of the tiles map.
	 * 
	 * @param tiles
	 * 		map to be checked
	 * @param coordinates
	 * 		location
	 * @return
	 * 		true if there is a collision, false if there is none
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
	 * Places the RoomNode in a location in the MazeTile maze.
	 * Also updates the doors location.
	 * Also updates entity locations.
	 * 
	 * @param tiles
	 * 		map to carve room on
	 * @param coordinates
	 * 		location of room
	 */
	public void carveRoomNode(MazeTile[][] tiles, Vec2I coordinates) {
		this.coordinates = coordinates;
		int xSize = room.size.getWidth();
		int ySize = room.size.getHeight();
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				tiles[coordinates.x + x][coordinates.y + y] = room.mazeTiles[x][y];
				if (tiles[coordinates.x + x][coordinates.y + y] == null) continue;
				tiles[coordinates.x + x][coordinates.y + y].replace(coordinates.x + x, coordinates.y + y);
			}
		}
		for (Entity e : room.entities) {
			e.move(coordinates.x, 0, coordinates.y);
		}
		for (Light l : room.lights) {
			if (l instanceof PointLight) {
				PointLight pl = ((PointLight) l);
				Vector3f position = pl.getPosition();
				pl.setPosition(position.add(coordinates.x, 0, coordinates.y));
				position = pl.getPosition();
				Vec2I newLightPosition = new Vec2I(Math.round(position.x), Math.round(position.z));
				room.entities.add(TorchType.createTorchOfTorchType(TorchType.getTorchType(tiles, newLightPosition), new Vector3f(position.x, 2, position.z)));
			}
			if (l instanceof SpotLight) {
				SpotLight sl = ((SpotLight) l);
				sl.setPosition(sl.getPosition().add(coordinates.x, 0, coordinates.y));
			}			
		}
		updateDoorLocations();
	}

	/**
	 * Update doors on new location.
	 */
	protected void updateDoorLocations() {
		for (RoomEntrancePoint entrance : entrances) {
			entrance.updateDoorLocation(coordinates);
		}
		for (RoomExitPoint exit : exits) {
			exit.updateDoorLocation(coordinates);
		}
	}

	/**
	 * Get all outgoing connection points.
	 * 
	 * @return
	 * 		all outgoing connection points
	 */
	public Collection<RoomExitPoint> geOutGoingConnections() {
		return exits;
	}

	/**
	 * Get all incoming connection points.
	 * 
	 * @return
	 * 		all incoming connection points
	 */
	public Collection<RoomEntrancePoint> getIncomingConnections() {
		return entrances;
	}

	/**
	 * Get lights.
	 * 
	 * @return
	 * 		lights of the room
	 */
	public Collection<? extends Light> getLights() {
		return room.lights;
	}

	/**
	 * Create a copy of the room.
	 * 
	 * @param idCounter
	 * 		id of new room
	 * @return
	 * 		copy of new room
	 */
	public RoomNode copy(int idCounter) {
		return new RoomNode(this.room.copy(), idCounter);
	}
	
	@Override
	public int hashCode() {
		return id;
	}

	/**
	 * Shallow equals that only checks id, for simple checking.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof RoomNode)) return false;
		
		return id == ((RoomNode) obj).id;
	}
		
}
