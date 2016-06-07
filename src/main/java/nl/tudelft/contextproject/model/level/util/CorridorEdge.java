package nl.tudelft.contextproject.model.level.util;

/**
 * Corridor edge class.
 * Saves connections between DoorLocations.
 * Connects from a RoomExitPoint to a RoomEntrancePoint.
 */
public class CorridorEdge {

	public RoomExitPoint start;
	
	public RoomEntrancePoint end;
	
	public int id;
	
	public double weight;

	/**
	 * Constructor Corridor edge class.
	 * Creates a graph connection between two room nodes.
	 * 
	 * @param startLocation
	 * 		start location of corridor, used as identifier for finding actual location in the maze
	 * @param endLocation
	 * 		end location of corridor, used as identifier for finding actual location in the maze
	 * @param id
	 * 		id of CorridorEdge, used for easy referencing after another class handles the
	 * 		required CorridorEdges.
	 */
	public CorridorEdge(RoomExitPoint startLocation, RoomEntrancePoint endLocation, int id) {
		this.start = startLocation;
		this.end = endLocation;
		this.id = id;
		this.weight = calculateWeight();
	}
	
	/**
	 * Calculate the weight of the edge. 
	 * 
	 * @return
	 * 			distance between start and end location of edge
	 */
	protected double calculateWeight() {
		return start.location.distance(end.location);
	}
}
