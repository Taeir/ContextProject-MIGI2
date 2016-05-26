package nl.tudelft.contextproject.util;

/**
 * Utility class for the webinterface.
 */
public final class WebUtil {

	/**
	 * Private constructor to avoid initialisation.
	 */
	private WebUtil() {}

	/**
	 * Decode an action integer back to its action.
	 *
	 * @param action
	 * 		the action to decode
	 * @return
	 * 		the decoded action
	 */
	public static String decodeAction(int action) {
		switch (action) {
			case 0:
				return "placebomb";
			case 1:
				return "placepitfall";
			case 2:
				return "placemine";
			case 3:
				return "spawnenemy";
			case 4:
				return "dropbait";
			default:
				return "notanaction";
		}
	}

	/**
	 * Check if an action is valid.
	 *
	 * @param action
	 * 		the action to check for
	 * @param team
	 * 		the team to check for
	 * @return
	 * 		true if the action is valid, false otherwise
	 */
	public static boolean checkValidAction(String action, String team) {
		switch (team) {
			case "Elves":
				return checkValidElves(action);
			case "Dwarfs":
				return checkValidDwarfs(action);
			default:
				return false;
		}
	}

	/**
	 * Check if an action is valid for elves.
	 *
	 * @param action
	 * 		the action to check for
	 * @return
	 * 		true if the action is valid, false otherwise
	 */
	protected static boolean checkValidElves(String action) {
		switch (action) {
			case "dropbait":
				return true;
			default:
				return false;
		}
	}

	/**
	 * Check if an action is valid for dwarfs.
	 *
	 * @param action
	 * 		the action to check for
	 * @return
	 * 		true if the action is valid, false otherwise
	 */
	protected static boolean checkValidDwarfs(String action) {
		switch (action) {
			case "placebomb":
				return true;
			case "placepitfall":
				return true;
			case "placemine":
				return true;
			case "spawnenemy":
				return true;
			default:
				return false;
		}
	}
}
