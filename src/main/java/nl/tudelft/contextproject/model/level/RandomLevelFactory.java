package nl.tudelft.contextproject.model.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.files.FileUtil;
import nl.tudelft.contextproject.util.Size;

/**
 * An implementation of a LevelFactory that creates tiles randomly.
 * NOTE: This factory becomes deprecated after a proper factory is introduced!
 */
public class RandomLevelFactory implements LevelFactory {
	protected static final int MAX_WIDTH = 50;
	protected static final int MAX_HEIGHT = 50;
	private static final int MAX_ATTEMPTS = 10;
	private static final Pattern PATTERN = Pattern.compile("(?<width>\\d+)x(?<height>\\d+)_.*");

	private Random rand;
	private int amount;
	private boolean allowDuplicates;

	/**
	 * Constructor for the random level factory.
	 *
	 * @param amount
	 * 			the amount of rooms to generate
	 * @param allowDuplicates
	 * 			if duplicates are allowed
	 */
	public RandomLevelFactory(int amount, boolean allowDuplicates) {
		if (amount < 1) {
			throw new IllegalArgumentException("You must spawn at least 1 room.");
		}

		this.amount = amount;
		this.allowDuplicates = allowDuplicates;
	}

	@Override
	public Level generateSeeded(long seed) {
		createRNG(seed);

		List<GeneratorRoom> generated = create();
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
		PointLight p = new PointLight();
		p.setPosition(new Vector3f(MAX_HEIGHT / 2, 100, MAX_WIDTH / 2));
		p.setColor(ColorRGBA.randomColor());
		p.setRadius(200);
		lights.add(p);
		return new Level(mazeTiles, lights);
	}

	/**
	 * Create the random number generator.
	 *
	 * @param seed
	 * 			the seed to use for the random number generator.
     */
	protected void createRNG(long seed) {
		rand = new Random(seed);
	}

	/**
	 * Return a random number between two values.
	 * @param min
	 *          The minimum value the random number can be.
	 * @param max
	 *          The maximum value the random number can be.
	 * @return
	 *          The random number.
	 */
	protected int getRandom(int min, int max) {
		return (rand.nextInt((max - min)) + min);
	}

	/**
	 * Method to load all rooms from file and return the sizes.
	 *
	 * @return
	 *          An ArrayList of sizes.
	 */
	protected List<Size> loadRooms() {
		String[] names = FileUtil.getFileNames("/rooms/");

		/**
		 * If the folder does not exist, we throw an IllegalStateException.
		 */
		if (names == null) {
			throw new IllegalStateException("The rooms folder does not exist.");
		}

		List<Size> sizes = new ArrayList<>();
		for (String name : names) {
			Matcher m = PATTERN.matcher(name);
			if (m.matches()) {
				int width = Integer.parseInt(m.group("width"));
				int height = Integer.parseInt(m.group("height"));
				sizes.add(new Size(width, height));
			}
		}
		return sizes;
	}

	/**
	 * Method to create a list of {@link GeneratorRoom}.
	 *
	 * @return
	 * 			the created generatorrooms
     */
	protected List<GeneratorRoom> create() {
		List<GeneratorRoom> rooms = new ArrayList<>();
		List<Size> sizes = loadRooms();
		if (!allowDuplicates && sizes.size() < amount) {
			throw new IllegalArgumentException("You are requesting " + amount + " rooms, while there only are " + sizes.size() + " available.");
		}

		/**
		 * Try to place the requested amount of rooms.
		 */
		for (int i = 0; i < amount; i++) {
			int attempts = 0;
			boolean success = false;
			Size rSize = getRandomSize(sizes, allowDuplicates);
			GeneratorRoom newRoom = null;
			if (rSize.getWidth() >= MAX_WIDTH || rSize.getHeight() >= MAX_HEIGHT) {
				throw new IllegalArgumentException("It is impossible for this level to fit in your map size.");
			}

			/**
			 * Try MAX_ATTEMPTS times to create the room, if all of these
			 * fail restart the entire algorithm.
			 */
			while (!success && attempts < MAX_ATTEMPTS) {
				success = true;
				int xCoord = getRandom(0, MAX_WIDTH - rSize.getWidth());
				int yCoord = getRandom(0, MAX_HEIGHT - rSize.getHeight());
				newRoom = new GeneratorRoom(xCoord, yCoord, rSize);
				for (GeneratorRoom r : rooms) {
					if (newRoom.intersects(r)) {
						success = false;
						attempts++;
					}
				}
			}

			/**
			 * If the room has been places, add it to the list of places rooms.
			 * Otherwise, break out of the while loop as there is no need to continue.
			 */
			if (success) {
				rooms.add(newRoom);
			} else {
				break;
			}
		}

		/**
		 * If all rooms have been placed, return the list of all the rooms.
		 * Else call the algorithm again and start all over.
		 */
		if (rooms.size() == amount) {
			return rooms;
		} else {
			return create();
		}
	}

	/**
	 * Create a matrix of TileTypes to represent the map which will be generated.
	 * This method creates the rooms.
	 *
	 * @param rooms
	 * 			the generated rooms
	 * @return
	 * 			the generated matrix of TileTypes
     */
	protected TileType[][] carveRooms(List<GeneratorRoom> rooms) {
		return carveRooms(rooms, MAX_WIDTH, MAX_HEIGHT);
	}

	/**
	 * Create a matrix of TileTypes to represent the map which will be generated.
	 * This method creates the rooms.
	 *
	 * @param rooms
	 * 			the generated rooms
	 * @param maxWidth
	 * 			the maximum width for the matrix
	 * @param maxHeight
	 * 			the maximum height for the matrix
	 * @return
	 * 			the generated matrix of TileTypes
     */
	protected TileType[][] carveRooms(List<GeneratorRoom> rooms, int maxWidth, int maxHeight) {
		TileType[][] carved = new TileType[maxWidth][maxHeight];
		for (GeneratorRoom r : rooms) {
			for (int x = r.getxLeft(); x < r.getxRight(); x++) {
				for (int y = r.getyLeft(); y < r.getyRight(); y++) {
					if (x == r.getxLeft() || x == r.getxRight() - 1 || y == r.getyLeft() || y == r.getyRight() - 1) {
						carved[x][y] = TileType.WALL;
					} else {
						carved[x][y] = TileType.FLOOR;
					}
				}
			}
		}
		return carved;
	}

	/**
	 * Create a matrix of TileTypes to represent the map which will be generated.
	 * This method creates the corridors.
	 *
	 * @param map
	 * 		the map in which to place the corridors
	 * @param rooms
	 * 		the generated rooms
     * @return
	 * 		the generated matrix of TileTypes
     */
	protected TileType[][] carveCorridors(TileType[][] map, List<GeneratorRoom> rooms) {
		if (rooms.size() == 1) {
			return map;
		}

		for (int i = 1; i < rooms.size(); i++) {
			Vector2f prevCenter = rooms.get(i - 1).getCenter();
			Vector2f currCenter = rooms.get(i).getCenter();
			int rn = getRandom(0, 2);
			if (rn == 1) {
				map = hCorridor(map, prevCenter.getX(), currCenter.getX(), prevCenter.getY());
				map = vCorridor(map, prevCenter.getY(), currCenter.getY(), currCenter.getX());
			} else {
				map = vCorridor(map, prevCenter.getY(), currCenter.getY(), prevCenter.getX());
				map = hCorridor(map, prevCenter.getX(), currCenter.getX(), currCenter.getY());
			}
		}
		
		carveCorridorWalls(map);
		return map;
	}

	/**
	 * This method adds walls to corridors on the map.
	 * Checks if a Tile is a corridor, if true add a wall to all Null TileTypes.
	 * @param map
	 * 				the map in which to place the corridor walls
	 */
	protected static void carveCorridorWalls(TileType[][] map) {
		int width = map.length;
		int heigth = map[0].length;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < heigth; j++) {
				if (map[i][j] == TileType.CORRIDOR) {
					//Check North
					if (j != 0 && map[i][j - 1] == null) {
						map[i][j - 1] = TileType.WALL;
					}
					
					//Check South
					if (j != heigth - 1 && map[i][j + 1] == null) {
						map[i][j + 1] = TileType.WALL;
					}
					
					//Check West
					if (i != 0 && map[i - 1][j] == null) {
						map[i - 1][j] = TileType.WALL;
					}
					
					//Check East
					if (i != width - 1 && map[i + 1][j] == null) {
						map[i + 1][j] = TileType.WALL;
					}
				}
			}
		}
	}

	/**
	 * Get a random size from a list of sizes.
	 *
	 * @param sizes
	 * 			the list of sizes from which to randomly select one
	 * @param allowDuplicates
	 * 			if the size should be deleted from the list after selection
     * @return
	 * 			the selected size
     */
	protected Size getRandomSize(List<Size> sizes, boolean allowDuplicates) {
		Size selected = sizes.get(getRandom(0, sizes.size()));
		if (!allowDuplicates) {
			sizes.remove(selected);
		}
		return selected;
	}

	/**
	 * Method for generating a horizontal corridor.
	 *
	 * @param map
	 * 			the map in which to generate the corridor
	 * @param x1
	 * 			the x coordinate of a room
	 * @param x2
	 * 			the x coordinate of a room
	 * @param yF
	 * 			the y coordinate of a room
	 * @return
	 * 			the map with the corridor cut out
	 */
	protected TileType[][] hCorridor(TileType[][] map, float x1, float x2, float yF) {
		int min = (int) Math.floor(Math.min(x1, x2));
		int max = (int) Math.floor(Math.max(x1, x2));
		int y = (int) Math.floor(yF);
		for (int x = min; x < max + 1; x++) {
			if (map[x][y] == null || map[x][y] == TileType.WALL) {
				map[x][y] = TileType.CORRIDOR;
			}
		}
		return map;
	}

	/**
	 * Method for generating a vertical corridor.
	 *
	 * @param map
	 * 			the map in which to generate the corridor
	 * @param y1
	 * 			the y coordinate of a room
	 * @param y2
	 * 			the y coordinate of a room
	 * @param xF
	 * 			the x coordinate of a room
	 * @return
	 * 			the map with the corridor cut out
	 */
	protected TileType[][] vCorridor(TileType[][] map, float y1, float y2, float xF) {
		int min = (int) Math.floor(Math.min(y1, y2));
		int max = (int) Math.floor(Math.max(y1, y2));
		int x = (int) Math.floor(xF);
		for (int y = min; y < max + 1; y++) {
			if (map[x][y] == null || map[x][y] == TileType.WALL) {
				map[x][y] = TileType.CORRIDOR;
			}
		}
		return map;
	}

}
