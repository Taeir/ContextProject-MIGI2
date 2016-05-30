package nl.tudelft.contextproject.webinterface;

/**
 * Enum representing the possible actions a player can perform.
 */
public enum Action {
	PLACEBOMB(10, 3),
	PLACEPITFALL(10, 2),
	PLACEMINE(10, 4),
	SPAWNENEMY(10, 1),
	DROPBAIT(10, 5);

	private int cooldown;
	private int maxAmount;

	/**
	 * @param cooldown
	 * 		the cooldown in seconds
	 * @param maxAmount
	 * 		the max amount of times you can perform the action within a cooldown
	 */
	Action(int cooldown, int maxAmount) {
		this.cooldown = cooldown;
		this.maxAmount = maxAmount;
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
}
