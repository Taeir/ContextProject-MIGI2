package nl.tudelft.contextproject.level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Entity;

/**
 * An implementation of a LevelFactory that creates tiles randomly.
 * NOTE: This factory becomes deprecated after a proper factory is introduced!
 */
public class RandomLevelFactory implements LevelFactory {
	private Random rand;
	private int width;
	private int height;
	private int[][] preCreate;
	private MazeTile[][] mazeTiles;
	private Room[] rooms;
	
	/**
	 * Constructor for the random level factory.
	 * @param width The width of the maze.
	 * @param height The height of the maze.
	 */
	public RandomLevelFactory(int width, int height) {
		this.height = height;
		this.width = width;
	}

	public void smoothMap(int minNeighbours) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int countNeighbours = getNeighbourCount(x, y);
				if (countNeighbours > minNeighbours) {
					preCreate[x][y] = 1;
				} else if (countNeighbours < minNeighbours) {
					preCreate[x][y] = 0;
				}
			}
		}
	}

	public int getNeighbourCount(int x, int y) {
		int result = 0;
		for (int movX = x - 1; movX <= x + 1; movX++) {
			for (int movY = y - 1; movY <= y + 1; movY++) {
				if (movY >= 0 && movY < height && movX >= 0 && movX < width) {
					if (movY != y && movX != x) {
						result += preCreate[movX][movY];
					}
				} else {
					result++;
				}
			}
		}
		return result;
	}
	
	@Override
	public Level generateSeeded(long seed) {
		rand = new Random(seed);
		
		mazeTiles = new MazeTile[width][height];
		preCreate = new int[width][height];
		Set<Entity> entities = new HashSet<Entity>();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
					preCreate[x][y] = 1;
				} else if (rand.nextFloat() < 0.45f) {
					preCreate[x][y] = 1;
				} else {
					preCreate[x][y] = 0;
				}
			}
		}
		for (int i = 0; i < 2; i++) {
			smoothMap(2);
		}
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (preCreate[x][y] == 1) {
					mazeTiles[x][y] = new MazeTile(x, y, Math.abs(rand.nextInt()) % MazeTile.MAX_HEIGHT);
				}
			}
		}
		ArrayList<Light> lights = new ArrayList<>(1);
		PointLight p = new PointLight();
		p.setPosition(new Vector3f(1, 1, 4));
		p.setColor(ColorRGBA.randomColor());
		p.setRadius(20);
		lights.add(p);
		lights.clear();
		entities.clear();
		rooms = new Room[1];
		rooms[0] = new Room(mazeTiles, lights);
		return new Level(rooms);
	}

}
