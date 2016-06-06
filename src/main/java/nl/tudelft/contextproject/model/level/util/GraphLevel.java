package nl.tudelft.contextproject.model.level.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.tudelft.contextproject.model.level.Room;
import nl.tudelft.contextproject.model.level.RoomTuple;

/**
 * Generate the graph of a level using RoomNodes and CorridorEdges.
 */
public class GraphLevel {

	public Random rand;
	public RoomNode startRoom;
	public RoomNode endRoom;
	public ArrayList<RoomNode> nodes;
	public ArrayList<CorridorEdge> edges;
	
	/**
	 * Create and initialize graph level.
	 * @param starterAndEndRooms
	 * 			start and end room tuple
	 * @param rooms
	 * 			rest of the rooms.
	 * @param rand
	 * 			random class to generate the graphLevel with.
	 */
	public GraphLevel(RoomTuple starterAndEndRooms, List<Room> rooms, Random rand) {
		this.rand = rand;
		this.startRoom = new RoomNode(starterAndEndRooms.getStarterRoom(), 0);
		this.endRoom = new RoomNode(starterAndEndRooms.getTreasureRoom(), 0);
		nodes = new ArrayList<RoomNode>(rooms.size());
		edges = new ArrayList<CorridorEdge>();
		for (Room room : rooms) {
			nodes.add(new RoomNode(room, 0));
		}
	}
	
	/**
	 * First step of graph level generator.
	 */
	public void createLevelGraph() {
		
	}
}
