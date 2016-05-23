package nl.tudelft.contextproject.util;

/**
 * Class used to make entities usable for the web interface.
 */
public final class EntityUtil {
	private static final int DEFAULT = 0;
	private static final int BOMB = 1;
	private static final int DOOR = 2;
	private static final int KEY = 3;
	private static final int VRPLAYER = 4;
	private static final int PLAYERTRIGGER = 5;

	/**
	 * Private constructor to avoid initialization.
	 */
	private EntityUtil() {}

	/**
	 * Get the ID for a certain class.
	 *
	 * @param entity
	 * 		the classname of the entity
	 * @return
	 * 		an int representing the entity
	 */
	public static int getJSONCoded(String entity) {
		switch (entity) {
			case "Bomb":
				return BOMB;
			case "Door":
				return DOOR;
			case "Key":
				return KEY;
			case "VRPlayer":
				return VRPLAYER;
			case "PlayerTrigger":
				return PLAYERTRIGGER;
			default:
				return DEFAULT;
		}
	}
}
