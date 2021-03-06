package nl.tudelft.contextproject.controller;

/**
 * Enum representing the state of the game.
 */
public enum GameState {
	/**
	 * VRPlayer is playing the tutorial.
	 */
	TUTORIAL,

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
	 *
	 * @return
	 * 		true if the state represents a started game, false otherwise
	 */
	public boolean isStarted() {
		return this == RUNNING || this == PAUSED || this == ENDED;
	}
}


