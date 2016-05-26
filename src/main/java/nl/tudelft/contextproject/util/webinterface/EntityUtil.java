package nl.tudelft.contextproject.util.webinterface;

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
	private static final int PITFALL = 6;

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
			case "Pitfall":
				return PITFALL;
			default:
				return DEFAULT;
		}
	}
}
