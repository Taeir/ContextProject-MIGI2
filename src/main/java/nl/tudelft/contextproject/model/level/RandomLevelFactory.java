package nl.tudelft.contextproject.model.level;

import java.util.ArrayList;
import java.util.Random;

import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;


/**
 * An implementation of a LevelFactory that creates tiles randomly.
 * NOTE: This factory becomes deprecated after a proper factory is introduced!
 */
public class RandomLevelFactory implements LevelFactory {
	private Random rand;
	private int width;
	private int height;

	/**
	 * Constructor for the random level factory.
	 * @param width The width of the maze.
	 * @param height The height of the maze.
	 */
	public RandomLevelFactory(int width, int height) {
		this.height = height;
		this.width = width;
	}

	@Override
	public Level generateSeeded(long seed) {
		rand = new Random(seed);

		MazeTile[][] mazeTiles = new MazeTile[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (rand.nextFloat() < .3f) {
					mazeTiles[x][y] = new MazeTile(x, y);
				}
			}
		}

		ArrayList<Light> lights = new ArrayList<>(1);
		PointLight p = new PointLight();
		p.setPosition(new Vector3f(1, 1, 4));
		p.setColor(ColorRGBA.randomColor());
		p.setRadius(20);
		lights.add(p);
		return new Level(mazeTiles, lights);
	}

}
