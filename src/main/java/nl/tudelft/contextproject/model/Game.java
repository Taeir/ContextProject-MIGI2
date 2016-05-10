package nl.tudelft.contextproject.model;

import java.util.LinkedList;
import java.util.List;

import nl.tudelft.contextproject.model.level.Level;

/**
 * Class representing a game.
 */
public class Game {
	private Level level;
	private VRPlayer player;
	private List<Entity> entities;
	
	/**
	 * Advanced constructor for the game.
	 * @param level The level for this game.
	 * @param player The VRPlayer in this game.
	 * @param entities A list containing all entities in the game.
	 */
	public Game(Level level, VRPlayer player, List<Entity> entities) {
		this.level = level;
		this.player = player;
		this.entities = entities;
	}
	
	/**
	 * Simple constructor for the game.
	 * @param level The level for this game.
	 */
	public Game(Level level) {
		this.level = level;
		this.player = new VRPlayer();
		this.entities = new LinkedList<>();
	}
	
	/**
	 * Add an entity to the game.
	 * @param entity The entity to add.
	 * @return true if the entity was added, false otherwise.
	 */
	public boolean addEntity(Entity entity) {
		return entities.add(entity);
	}

	/**
	 * Getter for the player.
	 * @return The player.
	 */
	public VRPlayer getPlayer() {
		return player;
	}

	/**
	 * Get all the entities from the game.
	 * @return A list with all the entities.
	 */
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * Get the level of this game.
	 * @return The current level.
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Method used in testing.
	 * Sets the current level.
	 * @param level The new level.
	 */
	protected void setLevel(Level level) {
		this.level = level;
	}
}
