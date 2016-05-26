package nl.tudelft.contextproject.util.webinterface;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.LandMine;
import nl.tudelft.contextproject.model.entities.Pitfall;

/**
 * Utility to perform actions generated by the web client.
 */
public final class ActionUtil {

	/**
	 * Private constructor to avoid initialization.
	 */
	private ActionUtil() {}

	/**
	 * Perform an action.
	 *
	 * @param action
	 * 		the action to perform
	 * @param xCoord
	 * 		the x coordinate to perform the action on
	 * @param yCoord
	 * 		the y coordinate to perform the action on
	 */
	public static void perform(String action, int xCoord, int yCoord) {
		switch (action) {
			case "placebomb":
				placeBomb(xCoord, yCoord);
				break;
			case "placepitfall":
				placePitfall(xCoord, yCoord);
				break;
			case "placemine":
				placeMine(xCoord, yCoord);
				break;
			case "spawnenemy":
				spawnEnemy(xCoord, yCoord);
				break;
			case "dropbait":
				dropBait(xCoord, yCoord);
				break;
			default:
				throw new IllegalArgumentException("Your action \"" + action + "\" is not a valid.");
		}
	}

	/**
	 * Place a bomb.
	 *
	 * @param xCoord
	 * 		the x coordinate to use
	 * @param yCoord
	 * 		the y coordinate to use
	 */
	protected static void placeBomb(int xCoord, int yCoord) {
		Bomb bomb = new Bomb();
		bomb.move(xCoord, 1, yCoord);
		Main.getInstance().getCurrentGame().addEntity(bomb);
	}

	/**
	 * Place a pitfall.
	 *
	 * @param xCoord
	 * 		the x coordinate to use
	 * @param yCoord
	 * 		the y coordinate to use
	 */
	protected static void placePitfall(int xCoord, int yCoord) {
		Pitfall pitfall = new Pitfall(1);
		pitfall.move(xCoord, 0, yCoord);
		Main.getInstance().getCurrentGame().addEntity(pitfall);
	}

	/**
	 * Place a mine.
	 *
	 * @param xCoord
	 * 		the x coordinate to use
	 * @param yCoord
	 * 		the y coordinate to use
	 */
	protected static void placeMine(int xCoord, int yCoord) {
		LandMine landmine = new LandMine();
		landmine.move(xCoord, 0, yCoord);
		Main.getInstance().getCurrentGame().addEntity(landmine);
	}

	/**
	 * Spawn an enemy.
	 *
	 * @param xCoord
	 * 		the x coordinate to use
	 * @param yCoord
	 * 		the y coordinate to use
	 */
	protected static void spawnEnemy(int xCoord, int yCoord) {
		//TODO Implement action once enemies are present
	}

	/**
	 * Drop bait.
	 *
	 * @param xCoord
	 * 		the x coordinate to use
	 * @param yCoord
	 * 		the y coordinate to use
	 */
	protected static void dropBait(int xCoord, int yCoord) {
		//TODO Implement action once enemies are present
	}
}
