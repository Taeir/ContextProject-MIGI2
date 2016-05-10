package nl.tudelft.contextproject;

/**
 * Enum representing the state of the game.
 */
public enum GameState {
	/**
	 * Waiting for the game to start.
	 */
	WAITING,
	
	/**
	 * The main game is running.
	 */
	RUNNING,
	
	/**
	 * The main game is suspended.
	 */
	PAUSED,
	
	/**
	 * The main game is finished.
	 */
	ENDED;
	
	/**
	 * Check if the state represents a started game.
	 * @return True if the state represents a started game, false otherwise.
	 */
	public boolean isStarted() {
		return this == RUNNING || this == PAUSED;
	}
}


