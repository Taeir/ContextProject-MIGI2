package nl.tudelft.contextproject.webinterface;

/**
 * Enum representing the possible actions a player can perform.
 */
public enum Action {
	//Cooldowns are set in seconds	
	PLACEBOMB(10, 		2, 					3, false),
	PLACEPITFALL(10, 	1, 					3, false),
	PLACEMINE(10, 		1, 					3, false),
	SPAWNENEMY(15, 		1, 					3, false),
	DROPBAIT(10, 		1, 					0, false),
	PLACETILE(0, 		Integer.MAX_VALUE,  0, true),
	OPENGATE(5, 		1, 					0, false),
	DROPCRATE(10, 		1, 					0, false),
	INVALID(0, 			0, 					0, false);

	private int cooldown;
	private int maxAmount;
	private int radius;
	private boolean allowedVoid;

	/**
	 * @param cooldown
	 * 		the cooldown in seconds
	 * @param maxAmount
	 * 		the max amount of times you can perform the action within a cooldown
	 * @param radius
	 * 		the radius round the player this action can not occur on
	 * @param allowedVoid
	 * 		true if the action can be performed in the void, false if the action can be performed on tiles
	 */
	Action(int cooldown, int maxAmount, int radius, boolean allowedVoid) {
		this.cooldown = cooldown * 1000;
		this.maxAmount = maxAmount;
		this.radius = radius;
		this.allowedVoid = allowedVoid;
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
		if (this == INVALID) return false;
		return allowedVoid;
	}

	/**
	 * @return
	 * 		true if the action can be performed on tiles, false if not
	 */
	public boolean isAllowedTiles() {
		if (this == INVALID) return false;
		
		return !allowedVoid;
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