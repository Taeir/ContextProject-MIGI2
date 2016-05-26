package nl.tudelft.contextproject.model.level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.jme3.light.Light;

import nl.tudelft.contextproject.model.level.roomIO.MapReader;

/**
 * Generates a level based on a graph structure.
 *
 */
public class GraphBasedLevelFactory implements LevelFactory {
	
	private Random rand;
	
	private ArrayList<Room> rooms;
	
	private RoomTuple startAndEndRooms;
	
	private ArrayList<Light> lights;
	
	private MazeTile[][] mazeTile;
	
	
	/**
	 * Constructor.
	 * @param mapFolder
	 * 		location and name of mapFolder
	 */
	public GraphBasedLevelFactory(String mapFolder) {
		rooms = new ArrayList<Room>();
		lights = new ArrayList<Light>();
		initializeBuilder(mapFolder);
	}
	
	@Override
	public Level generateSeeded(long seed) {
		createRNG(seed);
		createLevelGraph();

		return new Level(null, null);
	}
	
	/**
	 * Ask the GraphLevel class to create a GraphLevel
	 */
	protected void createLevelGraph() {
		
		
	}

	/**
	 * Initialize all the data needed for building.
	 * @param mapFolder
	 * 			location of map folder
	 */
	protected void initializeBuilder(String mapFolder) {
		try {
			startAndEndRooms = MapReader.readMap(null, rooms);
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
