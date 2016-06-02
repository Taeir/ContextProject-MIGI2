package nl.tudelft.contextproject.model.level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

import com.jme3.light.Light;

import nl.tudelft.contextproject.model.level.roomIO.MapReader;
import nl.tudelft.contextproject.model.level.util.CorridorBreadthFirstSearch;
import nl.tudelft.contextproject.model.level.util.CorridorEdge;
import nl.tudelft.contextproject.model.level.util.MinimumSpanningTree;
import nl.tudelft.contextproject.model.level.util.RoomEntrancePoint;
import nl.tudelft.contextproject.model.level.util.RoomExitPoint;
import nl.tudelft.contextproject.model.level.util.RoomNode;
import nl.tudelft.contextproject.model.level.util.Vec2I;
import nl.tudelft.contextproject.util.RandomUtil;

/**
 * Minimum Spanning Tree Based Level Factory.
 *
 */
public class MSTBasedLevelFactory implements LevelFactory {

	public static final int START_ROOM_ID = -1;
	//Max width of level 
	protected static final int MAX_WIDTH = 40;
	//Max height of level 
	protected static final int MAX_HEIGHT = 40;

	/**
	 * Number of tries when placing rooms randomly.
	 * Increasing this number increase the density of the maze that is generated.
	 * The density is also dependent on the MAX_WIDTH and MAX_HEIGHT, because a larger
	 * size will require more attempts to fill to the same density.
	 */
	private static final int MAX_ATTEMPTS = 100;

	/**
	 * Allow duplicates rooms in a single level.
	 * Should be set to initialize to true if duplicates are needed.
	 */
	public boolean duplicates;

	private Random rand;

	private ArrayList<RoomNode> baseNodes;

	private ArrayList<RoomNode> usedNodes;

	private int idCounter;

	private HashMap<Integer, CorridorEdge> edges;

	private ArrayList<Integer> chosenEdges;

	private ArrayList<RoomEntrancePoint> usedEntrancePoints;

	private ArrayList<RoomExitPoint> usedExitPoints;

	private RoomTuple startAndEndRooms;

	private ArrayList<Light> lights;

	private MazeTile[][] mazeTiles;


	/**
	 * Constructor.
	 * @param mapFolder
	 * 		location and name of mapFolder
	 */
	public MSTBasedLevelFactory(String mapFolder) {
		lights = new ArrayList<Light>();
		mazeTiles = new MazeTile[MAX_WIDTH][MAX_HEIGHT];
		initializeBuilder(mapFolder);
	}

	/**
	 * Generate a level in 6 steps.
	 *<ol>
	 *	<li>Place start and treasure room.</li>
	 *	<li>Place randomly rooms for some amount of attempts.</li>
	 *	<li>Create an edge from every entrance door to every exit door of other rooms.</li>
	 *	<li>Create MST. </li>
	 *	<li>For every edge still existing use breadth first search to find corridors.</li>
	 *	<li>Beautify corridors.</li>
	 *</ol>
	 */
	@Override
	public Level generateSeeded(long seed) {
		createRNG(seed);
		placeStartAndTreasureRoom();
		placeOtherRooms();
		createEdges();
		createMST();
		placeCorridors();

		return new Level(mazeTiles, lights);
	}



	/**
	 * Place start and treasure room on semi-random locations.
	 * Start room is placed in left-most quarter, treasure-room in right most quarter.
	 */
	protected void placeStartAndTreasureRoom() {
		int endLeftMostQuarter = (int) Math.round((double) MAX_WIDTH / 4.0);
		int beginningRightMostQuarter = (int) Math.round(3.0 * (double) MAX_WIDTH / 4.0);

		//Place start room
		Vec2I startLocation = new Vec2I(RandomUtil.getRandomIntegerFromInterval(rand, 
				RoomNode.MIN_DIST, endLeftMostQuarter), 
				RandomUtil.getRandomIntegerFromInterval(rand, 
						RoomNode.MIN_DIST, MAX_HEIGHT - (startAndEndRooms.getStarterRoom().size.getWidth() + RoomNode.MIN_DIST + 1)));
		RoomNode startNode = new RoomNode(startAndEndRooms.getStarterRoom(), START_ROOM_ID);
		addRoomNode(startNode, startLocation);

		//Place treasure room
		Vec2I treasureLocation = new Vec2I(RandomUtil.getRandomIntegerFromInterval(rand, 
				beginningRightMostQuarter, MAX_WIDTH - (startAndEndRooms.getTreasureRoom().size.getWidth() + RoomNode.MIN_DIST + 1)), 
				RandomUtil.getRandomIntegerFromInterval(rand, 
						RoomNode.MIN_DIST, MAX_HEIGHT - (startAndEndRooms.getTreasureRoom().size.getHeight() + RoomNode.MIN_DIST + 1)));
		RoomNode treasureNode = new RoomNode(startAndEndRooms.getTreasureRoom(), -2);
		addRoomNode(treasureNode, treasureLocation);
	}

	/**
	 * Place the other rooms.
	 * Will try to place random rooms until max attempts has been reached or there are no rooms left to place.
	 */
	protected void placeOtherRooms() {
		int attempts = 0;
		int randomIndex;
		RoomNode currentNode;
		while (attempts <= MAX_ATTEMPTS && !baseNodes.isEmpty()) {

			//Get random room
			randomIndex = rand.nextInt(baseNodes.size());
			currentNode = baseNodes.get(randomIndex);

			//Get random coordinates
			Vec2I coordinates = new Vec2I(RandomUtil.getRandomIntegerFromInterval(rand, 
					RoomNode.MIN_DIST, MAX_WIDTH - (currentNode.room.size.getWidth() + RoomNode.MIN_DIST + 1)), 
					RandomUtil.getRandomIntegerFromInterval(rand, 
							RoomNode.MIN_DIST, MAX_HEIGHT - (currentNode.room.size.getHeight() + RoomNode.MIN_DIST + 1)));

			//Check placement and place if possible
			if (currentNode.scanPossiblePlacement(mazeTiles, coordinates)) {
				addRoomNode(currentNode, coordinates);
			}
			attempts++;
		}
	}

	/**
	 * Create edges between all RoomNodes.
	 * Creates edges from exits from one room to entrances to another room.
	 * It creates an edge from every exit to every entrance.
	 */
	protected void createEdges() {
		int corridorIdcounter = 0;
		for (RoomEntrancePoint entrance : usedEntrancePoints) {
			for (RoomExitPoint exit : usedExitPoints) {
				if (!entrance.node.equals(exit.node)) {
					CorridorEdge edge = new CorridorEdge(exit, entrance, corridorIdcounter++);
					edges.put(edge.id, edge);
					exit.node.addOutgoingEdge(edge);
				}
			}
		}
	}

	/**
	 * Create a MST, connecting all the rooms with the least amount of path.
	 * Create the MST object.
	 * Run the MST algorithm, selecting which edges are chosen in map generation.
	 * 
	 */
	protected void createMST() {
		MinimumSpanningTree mst = new MinimumSpanningTree(usedNodes, edges);
		mst.runKruskalAlgorithm();
		chosenEdges = mst.getCorridorIDs();
	}


	/**
	 * Place the corridors from the chosenEdges list.
	 * For each corridor a breadth first search is done from
	 * the corridor starting point to the corridor end point.
	 * This progress creates a list of stacks on that contain locations that should
	 * be turned into a corridor tile.
	 */
	protected void placeCorridors() {
		ArrayList<Stack<Vec2I>> corridorList = getCorridorLocations();
		Vec2I carveLocation;
		int x, y;
		for (Stack<Vec2I> stack : corridorList) {
			while (!stack.empty()) {
				carveLocation = stack.pop();
				x = carveLocation.x;
				y = carveLocation.y;
				if (mazeTiles[x][y] == null) {
					mazeTiles[x][y] = new MazeTile(x, y, TileType.CORRIDOR);
				}
			}
		}
	}	
	
	/**
	 * Get all corridor locations on the map.
	 * @return
	 * 		list of stacks of corridor locations
	 */
	protected ArrayList<Stack<Vec2I>> getCorridorLocations() {
		ArrayList<Stack<Vec2I>> corridorList = new ArrayList<Stack<Vec2I>>();
		CorridorEdge currentCorridor;
		for (Integer corridorID: chosenEdges) {
			currentCorridor = edges.get(corridorID);
			
			//TODO DEBUG
			if (currentCorridor.start.location.distance(currentCorridor.end.location) < 2) {
				System.out.println("Too close, give me some space please!");
			}
			corridorList.add(CorridorBreadthFirstSearch.creatCorridor(mazeTiles, currentCorridor.start.location, currentCorridor.end.location));
		}
		return corridorList;
	}

	/**
	 * Add RoomNode to graph and map.
	 * Will remove node from the base list of nodes if duplicates are turned off.
	 * Adds node to usedNodes list.
	 * Adds entrances to usedEntrancePoints list.
	 * Adds exits to usedExitsPoints list.
	 * Adds lights to the lights list.
	 * 
	 * Copies the RoomNode if duplicates are allowed.
	 * @param	node
	 * 				Node to add
	 * @param position
	 * 				position of room
	 */
	protected void addRoomNode(RoomNode node, Vec2I position) {
		if (!duplicates) {
			baseNodes.remove(node);
		} else {
			node = node.copy(idCounter++);
		}

		node.carveRoomNode(mazeTiles, position);
		usedNodes.add(node);
		usedExitPoints.addAll(node.geOutGoingConnections());
		usedEntrancePoints.addAll(node.getIncomingConnections());
		lights.addAll(node.getLights());
	}


	/**
	 * Initialize all the data containers needed for building.
	 * @param mapFolder
	 * 			location of map folder
	 */
	protected void initializeBuilder(String mapFolder) {
		ArrayList<Room> rooms = new ArrayList<Room>();
		baseNodes = new ArrayList<RoomNode>();
		usedNodes = new ArrayList<RoomNode>();
		edges = new HashMap<Integer, CorridorEdge>();
		usedEntrancePoints = new ArrayList<RoomEntrancePoint>();
		usedExitPoints = new ArrayList<RoomExitPoint>();
		try {
			startAndEndRooms = MapReader.readMap(mapFolder, rooms);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Room room : rooms) {
			baseNodes.add(new RoomNode(room, idCounter++));
		}
	}

	/**
	 * Create the random number generator.
	 *
	 * @param seed
	 *		the seed to use for the random number generator
	 */
	protected void createRNG(long seed) {
		rand = new Random(seed);
	}
}
