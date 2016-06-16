package nl.tudelft.contextproject.webinterface;

/**
 * Enum representing the possible actions a player can perform.
 */
public enum Action {
	//Cooldowns are set in seconds	
	PLACEBOMB(10, 		2, 					-1, 3, false, true),
	PLACEPITFALL(10, 	1, 					-1, 3, false, true),
	PLACEMINE(10, 		1, 					-1, 3, false, true),
	SPAWNENEMY(15, 		1, 					8, 	3, false, true),
	DROPBAIT(10, 		1, 					-1, 0, false, true),
	PLACETILE(0, 		Integer.MAX_VALUE,  -1, 0, true,  false),
	OPENGATE(5, 		1, 					-1, 0, false, true),
	DROPCRATE(10, 		1, 					-1,	0, false, true),
	INVALID(0, 			0, 					0, 	0, false, false);

	private int cooldown;
	private int maxAmount;
	private int radius;
	private int globalMaxAmount;
	private boolean allowedVoid;
	private boolean allowedTiles;

	/**
	 * @param cooldown
	 * 		the cooldown in seconds
	 * @param maxAmount
	 * 		the max amount of times you can perform the action within a cooldown
	 * @param globalMaxAmount
	 * 		the max amount of times you can perform the action in a single game
	 * @param radius
	 * 		the radius round the player this action can not occur on
	 * @param allowedVoid
	 * 		if the action can be performed in the void
	 * @param allowedTiles
	 * 		if the action can be performed on tiles
	 */
	Action(int cooldown, int maxAmount, int globalMaxAmount, int radius, boolean allowedVoid, boolean allowedTiles) {
		this.cooldown = cooldown * 1000;
		this.maxAmount = maxAmount;
		this.globalMaxAmount = globalMaxAmount;
		this.radius = radius;
		this.allowedVoid = allowedVoid;
		this.allowedTiles = allowedTiles;
	}

	/**
	 * @return
	 * 		the cooldown in seconds
	 */
	public int getCooldown() {
		return cooldown;
	}

	/**
	 * @return
	 * 		the max amount of times you can perform the action within a cooldown
	 */
	public int getMaxAmount() {
		return maxAmount;
	}
	
	/**
	 * @return
	 * 		the max amount of times you can perform the action in a single game
	 */
	public int getGlobalMaxAmount() {
		return globalMaxAmount;
	}
	
	/**
	 * @return
	 * 		the radius around the player on which this action can not be performed
	 */
	public int getRadius() {
		return radius;
	}
	
	/**
	 * @return
	 * 		true if the action can be performed in the void, false if not
	 */
	public boolean isAllowedVoid() {
		return allowedVoid;
	}

	/**
	 * @return
	 * 		true if the action can be performed on tiles, false if not
	 */
	public boolean isAllowedTiles() {
		return allowedTiles;
	}
	
	/**
	 * Gets the action with the given ordinal.
	 * If the ordinal is not valid, this method returns {@link #INVALID}.
	 * 
	 * @param ordinal
	 * 		the ordinal of the action to get
	 * @return
	 * 		the Action with the given ordinal
	 */
	public static Action getAction(int ordinal) {
		if (ordinal < 0) return INVALID;
		
		Action[] actions = values();
		if (ordinal >= actions.length) return INVALID;
		return actions[ordinal];
	}
}