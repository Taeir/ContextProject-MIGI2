package nl.tudelft.contextproject.model.level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import com.jme3.light.Light;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.level.roomIO.MapParser;
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

	/**
	 * Start roomID.
	 */
	public static final int START_ROOM_ID = -1;

	/**
	 * Treasure roomID.
	 */
	public static final int TREASURE_ROOM_ID = 2;

	/**
	 * Max width of the level.
	 */
	protected static final int MAX_WIDTH = 50;
	/**
	 * Max width of the level.
	 */
	protected static final int MAX_HEIGHT = 50;

	/**
	 * Number of tries when placing rooms randomly.
	 * Increasing this number increase the density of the maze that is generated.
	 * The density is also dependent on the MAX_WIDTH and MAX_HEIGHT, because a larger
	 * size will require more attempts to fill to the same density.
	 */
	private static final int MAX_ATTEMPTS = 10000;

	/**
	 * Current Entity storage.
	 */
	public Set<Entity> entities = ConcurrentHashMap.newKeySet();

	/**
	 * Allow duplicates rooms in a single level.
	 * Should be set to initialize to true if duplicates are needed.
	 */
	public boolean duplicates;

	public Random rand;
	public ArrayList<RoomNode> baseNodes;
	public ArrayList<RoomNode> usedNodes;
	public HashMap<Integer, CorridorEdge> edges;
	public ArrayList<Integer> chosenEdges;
	public ArrayList<RoomEntrancePoint> usedEntrancePoints;
	public ArrayList<RoomExitPoint> usedExitPoints;
	public int idCounter;
	public RoomTuple startAndEndRooms;
	public ArrayList<Light> lights;
	public MazeTile[][] mazeTiles;


	/**
	 * Constructor.
	 * 
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
		createReverseMST();
		//createMST();
		placeCorridors();
		MSTBasedLevelFactory.carveCorridorWalls(mazeTiles);
		for (RoomNode roomNode: usedNodes) {
			Room room = roomNode.room;
			entities.addAll(room.entities);
		}
		return new Level(mazeTiles, lights);
	}

	/**
	 * Place start and treasure room on semi-random locations.
	 * Start room is placed in left-most quarter, treasure-room in right most quarter.
	 */
	public void placeStartAndTreasureRoom() {
		int endLeftMostQuarter = (int) Math.round(MAX_WIDTH / 4.0);
		int beginningRightMostQuarter = (int) Math.round(3.0 * MAX_WIDTH / 4.0);

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
		RoomNode treasureNode = new RoomNode(startAndEndRooms.getTreasureRoom(), TREASURE_ROOM_ID);
		addRoomNode(treasureNode, treasureLocation);
	}

	/**
	 * Place the other rooms.
	 * Will try to place random rooms until max attempts has been reached or there are no rooms left to place.
	 */
	public void placeOtherRooms() {
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
	public void createEdges() {
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
	protected void createReverseMST() {
		MinimumSpanningTree mst = new MinimumSpanningTree(usedNodes, edges);
		mst.runReverseDeleteAlgorithm();
		chosenEdges = mst.getCorridorIDsReverseAlgorithm();
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
			corridorList.add(CorridorBreadthFirstSearch.breadthFirstSearch(mazeTiles, currentCorridor.start.location, currentCorridor.end.location));
		}
		return corridorList;
	}

	/**
	 * This method adds walls to corridors on the map.
	 * Checks if a Tile is a corridor, if true add a wall to all Null TileTypes.
	 *
	 * @param map
	 *		the map in which to place the corridor walls
	 */
	protected static void carveCorridorWalls(MazeTile[][] map) {
		int width = map.length;
		int heigth = map[0].length;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < heigth; j++) {
				System.out.println("i,j: " + i + "," + j);
				if (map[i][j] != null && map[i][j].getTileType() == TileType.CORRIDOR) {
					//Check North
					if (j != 0 && map[i][j - 1] == null) {
						map[i][j - 1] = new MazeTile(i, j - 1, TileType.WALL);
					}

					//Check South
					if (j != heigth - 1 && map[i][j + 1] == null) {
						map[i][j + 1] = new MazeTile(i, j + 1, TileType.WALL);
					}

					//Check West
					if (i != 0 && map[i - 1][j] == null) {
						map[i - 1][j] = new MazeTile(i - 1, j, TileType.WALL);
					}

					//Check East
					if (i != width - 1 && map[i + 1][j] == null) {
						map[i + 1][j] = new MazeTile(i + 1, j, TileType.WALL);
					}
				}
			}
		}
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
	 * 
	 * @param	node
	 * 				Node to add
	 * @param position
	 * 				position of room
	 */
	protected void addRoomNode(RoomNode node, Vec2I position) {
		if (!duplicates) {
			baseNodes.remove(node);
		} else if (node.id != START_ROOM_ID) {
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
			startAndEndRooms = MapParser.readMap(mapFolder, rooms);
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
	public void createRNG(long seed) {
		rand = new Random(seed);
	}
}
