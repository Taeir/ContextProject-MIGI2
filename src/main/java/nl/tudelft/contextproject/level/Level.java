package nl.tudelft.contextproject.level;

import java.util.List;
import java.util.Set;

import com.jme3.light.Light;

import nl.tudelft.contextproject.Entity;
import nl.tudelft.contextproject.VRPlayer;

/**
 * Class representing the entire level in the game.
 * An instance of this contains all the tiles in the maze and (in the future)
 * players and other entities.
 */
public class Level {
	private MazeTile[][] mazeTiles;
	private Set<Entity> entities;
	private VRPlayer player;
	private List<Light> lightList;
	
	/**
	 * Constructor to create a maze with specific mazeTiles.
	 * @param maze The set of tiles to include in the maze.
	 * @param p The player that is placed in the maze.
	 * @param entities The list of entities that is present in the maze.
	 * @param lights A list with all the lights in the level.
	 */
	public Level(MazeTile[][] maze, VRPlayer p, Set<Entity> entities, List<Light> lights) {
		this.mazeTiles = maze;
		this.player = p;
		this.entities = entities;
		this.lightList = lights;
	}
	
	/**
	 * Getter for the list of entities.
	 * @return A list of entities.
	 */
	public Set<Entity> getEntities() {
		return entities;
	}

	/**
	 * Getter for the player.
	 * @return The player.
	 */
	public VRPlayer getPlayer() {
		return player;
	}

	/**
	 * Getter for the height of the maze.
	 * @return the height of the maze.
	 */
	public int getHeight() {
		return mazeTiles[0].length;
	}
	
	/**
	 * Getter for the width of the maze.
	 * @return the width of the maze.
	 */
	public int getWidth() {
		return mazeTiles.length;	
	}
	
	/**
	 * Checks if there is a tile in the maze at the specified position.
	 * @param x the x-location in the maze
	 * @param y the y-location in the maze
	 * @return true when there is a tile at that position, false otherwise.
	 */
	public boolean isTileAtPosition(int x, int y) {
		return mazeTiles[x][y] != null;
	}
	
	/**
	 * Getter for a tile at a certain position.
	 * @param x the x-location in the maze
	 * @param y the y-location in the maze
	 * @return the tile at the specified location or null when no tile is present.
	 */
	public MazeTile getTile(int x, int y) {
		return mazeTiles[x][y];
	}

	/**
	 * Getter for the lights.
	 * @return A list with all lights in the scene.
	 */
	public List<Light> getLights() {
		return lightList;
	}

	/**
	 * Add an entity to the level.
	 * @param entity the entity to add.
	 * @return true if the entity was added, false otherwise.
	 */
	public boolean addEntity(Entity entity) {
		return entities.add(entity);
	}
}
