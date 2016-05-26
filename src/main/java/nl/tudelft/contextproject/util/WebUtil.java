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
				return  action.equals("dropbait");
			case "Dwarfs":
				return  action.equals("placebomb") ||
						action.equals("placepitfall") ||
						action.equals("placemine") ||
						action.equals("spawnenemy");
			default:
				return false;
		}
	}
}
