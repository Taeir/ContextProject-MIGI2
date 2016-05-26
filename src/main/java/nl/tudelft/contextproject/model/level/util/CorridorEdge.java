package nl.tudelft.contextproject.model.level.util;

/**
 * Corridor edge class.
 * Used for graph construction.
 */
public class CorridorEdge {

	public Vec2I startLocation;
	
	public Vec2I endLocation;
	
	public RoomNode startRoom;
	
	public RoomNode endRoom;

	/**
	 * Constructor Corridor edge class.
	 * Creates a graph connection between two room nodes.
	 * @param startLocation
	 * 				start location in room, used as identifier for finding actual location in the maze
	 * @param endLocation
	 * 				end location in room, used as identifier for finding actual location in the maze
	 * @param startRoom
	 * 				room from which the corridor comes
	 * @param endRoom
	 * 				room to which the corridor goes
	 */
	public CorridorEdge(Vec2I startLocation, Vec2I endLocation, RoomNode startRoom, RoomNode endRoom) {
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.startRoom = startRoom;
		this.endRoom = endRoom;
	}
}
