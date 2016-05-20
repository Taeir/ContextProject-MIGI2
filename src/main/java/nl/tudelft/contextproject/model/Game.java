package nl.tudelft.contextproject.model;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nl.tudelft.contextproject.model.level.Level;

/**
 * Class representing a game.
 */
public class Game {
	private Level level;
	private VRPlayer player;
	private Set<Entity> entities;
	
	/**
	 * Advanced constructor for the game.
	 * @param level The level for this game.
	 * @param player The VRPlayer in this game.
	 * @param entities A list containing all entities in the game.
	 */
	public Game(Level level, VRPlayer player, Set<Entity> entities) {
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
		this.entities = ConcurrentHashMap.newKeySet();
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
	 * @return a set with all the entities
	 */
	public Set<Entity> getEntities() {
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
	public void setLevel(Level level) {
		this.level = level;
	}
}
