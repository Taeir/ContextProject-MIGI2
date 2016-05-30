package nl.tudelft.contextproject.model;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import nl.tudelft.contextproject.model.level.Level;

/**
 * Class representing a game.
 */
public class Game {
	private Level level;
	private VRPlayer player;
	private Set<Entity> entities;
	private GameController controller;
	
	/**
	 * Advanced constructor for the game.
	 *
	 * @param level
	 * 		the level for this game
	 * @param player
	 * 		the VRPlayer in this game
	 * @param entities
	 * 		a list containing all entities in the game
	 * @param controller
	 * 		the controller instantiating this game
	 */
	public Game(Level level, VRPlayer player, Set<Entity> entities, GameController controller) {
		this.level = level;
		this.player = player;
		this.entities = entities;
		this.controller = controller;
	}
	
	/**
	 * Simple constructor for the game.
	 *
	 * @param level
	 * 		the level for this game
	 */
	public Game(Level level) {
		this.level = level;
		this.player = new VRPlayer();
		this.entities = ConcurrentHashMap.newKeySet();
	}
	
	/**
	 * Add an entity to the game.
	 *
	 * @param entity
	 * 		the entity to add
	 * @return
	 * 		true if the entity was added, false otherwise
	 */
	public boolean addEntity(Entity entity) {
		return entities.add(entity);
	}

	/**
	 * @return
	 * 		the player
	 */
	public VRPlayer getPlayer() {
		return player;
	}

	/**
	 * @return
	 * 		a set with all the entities
	 */
	public Set<Entity> getEntities() {
		return entities;
	}

	/**
	 * @return
	 * 		the current level.
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Method used in testing.
	 * Sets the current level.
	 *
	 * @param level
	 * 		the new level
	 */
	public void setLevel(Level level) {
		this.level = level;
	}
	
	/**
	 * End this game.
	 * 
	 * @param didElvesWin
	 * 		true when the elves won, false when the dwarfs did
	 */
	public void endGame(boolean didElvesWin) {
		controller.gameEnded(didElvesWin);
	}
}
