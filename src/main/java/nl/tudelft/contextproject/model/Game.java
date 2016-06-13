package nl.tudelft.contextproject.model;

import java.util.Set;

import nl.tudelft.contextproject.controller.GameThreadController;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import nl.tudelft.contextproject.model.level.Level;

/**
 * Class representing a game.
 */
public class Game {
	private Level level;
	private VRPlayer player;
	private GameThreadController controller;
	private float timeLimit;
	
	/**
	 * Advanced constructor for the game.
	 *
	 * @param level
	 * 		the level for this game
	 * @param player
	 * 		the VRPlayer in this game
	 * @param controller
	 * 		the controller instantiating this game
	 * @param timeLimit
	 * 		the time limit for this game
	 */
	public Game(Level level, VRPlayer player, GameThreadController controller, float timeLimit) {
		this.level = level;
		this.player = player;
		this.controller = controller;
		this.timeLimit = timeLimit;
	}
	
	/**
	 * Simple constructor for the game.
	 *
	 * @param level
	 * 		the level for this game
	 * @param controller
	 * 		the controller instantiating this game
	 * @param timeLimit
	 * 		the time limit for this game
	 */
	public Game(Level level, GameThreadController controller, float timeLimit) {
		this.level = level;
		this.player = new VRPlayer();
		this.player.move(level.getPlayerSpawnPosition());
		this.controller = controller;
		this.timeLimit = timeLimit;
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
		return level.getEntities().add(entity);
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
	 * 		the set with all the entities
	 */
	public Set<Entity> getEntities() {
		return level.getEntities();
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

	/**
	 * Update the game timer.
	 * 
	 * @param tpf
	 * 		the time per frame of this update
	 */
	public void update(float tpf) {
		timeLimit -= tpf;
		if (timeLimit < 0) {
			endGame(false);
		}
	}
	
	/**
	 * @return
	 * 		the remaining time for the player to find the treasure
	 */
	public float getTimeRemaining() {
		return timeLimit;
	}
}
