package nl.tudelft.contextproject.controller;

/**
 * Enum representing the state of the game.
 */
public enum GameState {
	WAITING,
	RUNNING,
	PAUSED,
	ENDED;
	
	/**
	 * Check if the state represents a started game.
	 *
	 * @return
	 * 		true if the state represents a started game, false otherwise
	 */
	public boolean isStarted() {
		return this == RUNNING || this == PAUSED;
	}
}


