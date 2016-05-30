package nl.tudelft.contextproject.model.level.util;

/**
 * Corridor edge class.
 * Used for graph construction.
 */
public class CorridorEdge {

	public RoomExitPoint start;
	
	public RoomEntrancePoint end;
	
	public int id;

	/**
	 * Constructor Corridor edge class.
	 * Creates a graph connection between two room nodes.
	 * @param startLocation
	 * 				start location of corridor, used as identifier for finding actual location in the maze
	 * @param endLocation
	 * 				end location of corridor, used as identifier for finding actual location in the maze
	 * @param id
	 * 				id of CorridorEdge
	 */
	public CorridorEdge(RoomExitPoint startLocation, RoomEntrancePoint endLocation, int id) {
		this.start = startLocation;
		this.end = endLocation;
		this.id = id;
	}
}
