package nl.tudelft.contextproject.webinterface;

/**
 * Enum representing the possible actions a player can perform.
 */
public enum Action {
	//Cooldowns are set in seconds
	PLACEBOMB(10, 3, false, true),
	PLACEPITFALL(10, 2, false, true),
	PLACEMINE(10, 4, false, true),
	SPAWNENEMY(10, 1, false, true),
	DROPBAIT(10, 5, false, true),
	PLACETILE(0, Integer.MAX_VALUE, true, false),
	INVALID(0, 0, false, false);

	private int cooldown;
	private int maxAmount;
	private boolean allowedVoid;
	private boolean allowedTiles;

	/**
	 * @param cooldown
	 * 		the cooldown in seconds
	 * @param maxAmount
	 * 		the max amount of times you can perform the action within a cooldown
	 * @param allowedVoid
	 * 		if the action can be performed in the void
	 * @param allowedTiles
	 * 		if the action can be performed on tiles
	 */
	Action(int cooldown, int maxAmount, boolean allowedVoid, boolean allowedTiles) {
		this.cooldown = cooldown * 1000;
		this.maxAmount = maxAmount;
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
	 * If the ordinal is not valid, this method returns null.
	 * 
	 * @param ordinal
	 * 		the ordinal of the action to get
	 * @return
	 * 		the Action with the given ordinal
	 */
	public static Action getAction(int ordinal) {
		if (ordinal < 0) return null;
		
		Action[] actions = values();
		if (ordinal >= actions.length) return null;
		return actions[ordinal];
	}
}