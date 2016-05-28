package nl.tudelft.contextproject.model.level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.jme3.light.Light;

import nl.tudelft.contextproject.model.level.roomIO.MapReader;
import nl.tudelft.contextproject.model.level.util.CorridorEdge;
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

	//Max width of level 
	protected static final int MAX_WIDTH = 200;
	//Max height of level 
	protected static final int MAX_HEIGHT = 200;
	//Number of attempts 
	private static final int MAX_ATTEMPTS = 1000;

	/**
	 * Allow duplicates rooms in a single level.
	 * Should be set to initialize to true if duplicates are needed.
	 */
	public boolean duplicates;

	private Random rand;

	private ArrayList<RoomNode> baseNodes;

	private ArrayList<RoomNode> usedNodes;

	private ArrayList<CorridorEdge> edges;

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

		return new Level(null, null);
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
		RoomNode startNode = new RoomNode(startAndEndRooms.getStarterRoom());
		addRoomNode(startNode, startLocation);

		//Place start room
		Vec2I treasureLocation = new Vec2I(RandomUtil.getRandomIntegerFromInterval(rand, 
				beginningRightMostQuarter, MAX_WIDTH - (startAndEndRooms.getTreasureRoom().size.getWidth() + RoomNode.MIN_DIST + 1)), 
				RandomUtil.getRandomIntegerFromInterval(rand, 
						RoomNode.MIN_DIST, MAX_HEIGHT - (startAndEndRooms.getTreasureRoom().size.getHeight() + RoomNode.MIN_DIST + 1)));
		RoomNode treasureNode = new RoomNode(startAndEndRooms.getTreasureRoom());
		addRoomNode(treasureNode, treasureLocation);
	}
	
	

	/**
	 * Add RoomNode to graph and map.
	 * Will remove node from the base list of nodes if duplicates are turned off.
	 * Adds node to usedNodes list.
	 * Adds entrances to usedEntrancePoints list.
	 * Adds exits to usedExitsPoints list.
	 * Adds lights to the lights list.
	 * @param	node
	 * 				Node to add
	 * @param position
	 * 				position of room
	 */
	protected void addRoomNode(RoomNode node, Vec2I position) {
		if (!duplicates) {
			baseNodes.remove(node);
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
		edges = new ArrayList<CorridorEdge>();
		usedEntrancePoints = new ArrayList<RoomEntrancePoint>();
		usedExitPoints = new ArrayList<RoomExitPoint>();
		try {
			startAndEndRooms = MapReader.readMap(mapFolder, rooms);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Room room : rooms) {
			baseNodes.add(new RoomNode(room));
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
