package nl.tudelft.contextproject.model.level;

import java.util.ArrayList;
import java.util.Random;

import com.jme3.light.Light;

/**
 * Generates a level based on a graph and grid structure.
 *
 */
public class GridBasedLevelFactory implements LevelFactory {

	private String baseFolder;
	
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
	public GridBasedLevelFactory(String mapFolder) {
		this.baseFolder = mapFolder;
		rooms = new ArrayList<Room>();
		
	}
	
	@Override
	public Level generateSeeded(long seed) {
		createRNG(seed);


		return new Level(null, null);
	}
	
	/**
	 * Initialize all the data needed for building.
	 */
	protected void initializeBuilder() {
		// TODO Auto-generated method stub
		
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
