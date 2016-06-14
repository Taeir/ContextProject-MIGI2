package nl.tudelft.contextproject.util.webinterface;

import nl.tudelft.contextproject.Main;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.moving.VRPlayer;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.webinterface.Action;
import nl.tudelft.contextproject.webinterface.Team;
import nl.tudelft.contextproject.webinterface.WebClient;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.jme3.math.Vector3f;

/**
 * Utility class for the webinterface.
 */
public final class WebUtil {

	/**
	 * Private constructor to avoid initialisation.
	 */
	private WebUtil() {}

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
	public static boolean checkValidAction(Action action, Team team) {
		return team.getActions().contains(action);
	}

	/**
	 * Check if a location is a valid location to perform an action on.
	 *
	 * @param xCoord
	 * 		the x coordinate of the location
	 * @param yCoord
	 * 		the y coordinate of the location
	 * @param action
	 * 		the action to check for
	 * @return
	 * 		true if the location is valid, false otherwise
	 */
	public static boolean checkValidLocation(int xCoord, int yCoord, Action action) {
		MazeTile tile = Main.getInstance().getCurrentGame().getLevel().getTile(xCoord, yCoord);
		if (tile == null && action.isAllowedVoid()) {
			return checkValidLocationEntities(xCoord, yCoord);
		} else if (tile == null || tile.getTileType() == TileType.WALL) {
			return false;
		}

		if (action.isAllowedTiles()) {
			return checkValidLocationEntities(xCoord, yCoord);
		}

		return false;
	}

	/**
	 * Check if an action can be performed according to the entities in the map.
	 *
	 * @param xCoord
	 * 		the x coordinate of the location
	 * @param yCoord
	 * 		the y coordinate of the location
	 * @return
	 * 		true if the location is valid, false otherwise
	 */
	private static boolean checkValidLocationEntities(int xCoord, int yCoord) {
		Set<Entity> entities = Main.getInstance().getCurrentGame().getEntities();
		for (Entity entity : entities) {
			if (Math.round(entity.getLocation().getX()) == xCoord &&
					Math.round(entity.getLocation().getZ()) == yCoord) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Check if an action can or cannot be performed because of a cooldown.
	 *
	 * @param action
	 * 		the action the client wants to perform
	 * @param client
	 * 		the client who wants to perform the action
	 * @return
	 * 		true if the action is valid and can be performed, false if it is not
	 */
	public static boolean checkWithinCooldown(Action action, WebClient client) {
		Long currTime = System.currentTimeMillis();
		List<Long> timestamps = client.getPerformedActions().get(action);

		shrinkTimestampsList(timestamps, action.getCooldown(), currTime);
		if (timestamps.size() < action.getMaxAmount()) {
			timestamps.add(currTime);
			return true;
		}

		return false;
	}

	/**
	 * Remove all actions which are outside of the cooldown from a list of actions.
	 *
	 * @param timestamps
	 * 		the list of timestamps to shrink
	 * @param cooldown
	 * 		the cooldown for the current action
	 * @param currTime
	 * 		the current time
	 */
	private static void shrinkTimestampsList(List<Long> timestamps, int cooldown, long currTime) {
		Iterator<Long> itr = timestamps.iterator();
		while (itr.hasNext()) {
			Long selectedLong = itr.next();
			if (currTime - selectedLong >= cooldown) {
				itr.remove();
			}
		}
	}

	/**
	 * Returns true if the player is null or the action is outside of the player radius.
	 * Returns false if the action is within the player radius.
	 * 
	 * @param xCoord
	 * 		the x coordinate of the location
	 * @param yCoord
	 * 		the y coordinate of the location
	 * @param action
	 * 		the action to check for 
	 * @return
	 * 		true if the action is outside of the player radius
	 */
	public static boolean checkOutsideRadius(int xCoord, int yCoord, Action action) {
		VRPlayer player = Main.getInstance().getCurrentGame().getPlayer();
		if (player == null) return false;
		
		Vector3f location = player.getLocation();
		return Math.abs((Math.abs(location.x) + Math.abs(location.z) - Math.abs(xCoord) - Math.abs(yCoord))) >= action.getRadius();
	}
}
