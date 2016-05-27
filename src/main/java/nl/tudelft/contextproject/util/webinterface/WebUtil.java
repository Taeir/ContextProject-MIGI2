package nl.tudelft.contextproject.util.webinterface;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

import java.util.Set;

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

	/**
	 * Check if a location is a valid location to perform an action on.
	 *
	 * @param xCoord
	 * 		the x coordinate of the location
	 * @param yCoord
	 * 		the y coordinate of the location
	 * @return
	 * 		true if the location is valid, false otherwise
	 */
	public static boolean checkValidLocation(int xCoord, int yCoord) {
		MazeTile tile = Main.getInstance().getCurrentGame().getLevel().getTile(xCoord, yCoord);
		if (tile == null || tile.getTileType() == TileType.WALL) {
			return false;
		}

		Set<Entity> entities = Main.getInstance().getCurrentGame().getEntities();
		for (Entity e : entities) {
			if (Math.round(e.getLocation().getX()) == xCoord &&
					Math.round(e.getLocation().getZ()) == yCoord) {
				return false;
			}
		}

		VRPlayer player = Main.getInstance().getCurrentGame().getPlayer();
		if (Math.round(player.getLocation().getX()) == xCoord && Math.round(player.getLocation().getZ()) == yCoord) {
			return false;
		}

		return true;
	}
}