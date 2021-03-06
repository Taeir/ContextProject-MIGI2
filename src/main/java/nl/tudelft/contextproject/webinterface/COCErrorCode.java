package nl.tudelft.contextproject.webinterface;

/**
 * Enum for the different error codes that the webserver can respond with.
 */
public enum COCErrorCode {
	/** Authentication failed because the game was in progress. */
	AUTHENTICATE_FAIL_IN_PROGRESS(100),
	/** Authentication failed because the game was full. */
	AUTHENTICATE_FAIL_FULL(101),
	
	/** Client tried to set it's team after the game started. */
	SETTEAM_STARTED(110),
	/** Client tried to set it's team to an invalid value (cheating). */
	SETTEAM_INVALID_TEAM(111),
	/** Client tried to set it's team to a team that was full. */
	SETTEAM_TEAM_FULL(112),

	/** The client attempted to perform an action at an illegal location. */
	ACTION_ILLEGAL_LOCATION(200),
	/** The action the client attempted was on cooldown. */
	ACTION_COOLDOWN(201),
	/** The action is illegal for the current team / action is not known. */
	ACTION_ILLEGAL(202),
	/** The action cannot be performed so close to the player. */
	ACTION_RADIUS(203),
	
	/** The client is not authorized. */
	UNAUTHORIZED(300),

	/** The server encountered an exception while handling a client request. */
	SERVER_ERROR(500);

	private final int errorCodeNumber;

	/**
	 * Creates a new COCErrorCode with the given number.
	 * 
	 * @param errorCodeNumber
	 * 		the error code number
	 */
	COCErrorCode(int errorCodeNumber) {
		this.errorCodeNumber = errorCodeNumber;
	}

	/**
	 * @return
	 * 		a json string for this error message
	 */
	public String toJSON() {
		return "{error: " + errorCodeNumber + "}";
	}
	
	@Override
	public String toString() {
		return "" + errorCodeNumber;
	}
}
