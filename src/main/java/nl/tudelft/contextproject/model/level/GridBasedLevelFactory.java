package nl.tudelft.contextproject.model.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jme3.light.Light;

public class GridBasedLevelFactory implements LevelFactory{

	private Random rand;
	
	private ArrayList<Room> rooms;
	
	@Override
	public Level generateSeeded(long seed) {
		createRNG(seed);

		initializeBuilder
		rooms = readRooms();
		TileType[][] carved = carveRooms(generated);
		carved = carveCorridors(carved, generated);

		MazeTile[][] mazeTiles = new MazeTile[MAX_WIDTH][MAX_HEIGHT];

		for (int x = 0; x < carved.length; x++) {
			for (int y = 0; y < carved[0].length; y++) {
				if (carved[x][y] != null) {
					mazeTiles[x][y] = new MazeTile(x, y, carved[x][y]);
				}
			}
		}

		ArrayList<Light> lights = new ArrayList<>(1);
		return new Level(mazeTiles, lights);
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
