package nl.tudelft.contextproject;

public enum GameState {
	WAITING,
	RUNNING,
	PAUSED,
	ENDED;
	
	public boolean isStarted() {
		return this == RUNNING || this == PAUSED;
	}
}


