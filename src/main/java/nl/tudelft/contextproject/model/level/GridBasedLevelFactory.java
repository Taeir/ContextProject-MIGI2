package nl.tudelft.contextproject.model.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jme3.light.Light;

public class GridBasedLevelFactory implements LevelFactory{

	private String baseFolder;
	
	private Random rand;
	
	private ArrayList<Room> rooms;
	
	private ArrayList<Light> lights;
	
	private MazeTile[][] MazeTile;
	
	public GridBasedLevelFactory(String baseFolder) {
		this.baseFolder = baseFolder;
		
	}
	
	@Override
	public Level generateSeeded(long seed) {
		createRNG(seed);


		return new Level(null, null);
	}
	
	/**
	 * Initialize all the data needed for building.
	 */
	private void initializeBuilder() {
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
