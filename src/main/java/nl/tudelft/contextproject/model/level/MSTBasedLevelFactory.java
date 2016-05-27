package nl.tudelft.contextproject.model.level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.jme3.light.Light;

import nl.tudelft.contextproject.model.level.roomIO.MapReader;

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
	//Minimum distance from Edges
	private static final int MINIMUM_EDGE_DISTANCE = 10;
	
	private Random rand;
	
	private ArrayList<Room> rooms;
	
	private RoomTuple startAndEndRooms;
	
	private ArrayList<Light> lights;
	
	private TileType[][] tiles;
	
	
	/**
	 * Constructor.
	 * @param mapFolder
	 * 		location and name of mapFolder
	 */
	public MSTBasedLevelFactory(String mapFolder) {
		rooms = new ArrayList<Room>();
		lights = new ArrayList<Light>();
		tiles = new TileType[MAX_WIDTH][MAX_HEIGHT];
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
		
		
	}


	/**
	 * Initialize all the data needed for building.
	 * @param mapFolder
	 * 			location of map folder
	 */
	protected void initializeBuilder(String mapFolder) {
		try {
			startAndEndRooms = MapReader.readMap(mapFolder, rooms);
		} catch (IOException e) {
			e.printStackTrace();
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
