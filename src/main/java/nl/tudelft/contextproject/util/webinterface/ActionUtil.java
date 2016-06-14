package nl.tudelft.contextproject.util.webinterface;

import com.jme3.math.Vector3f;
import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.entities.Carrot;
import nl.tudelft.contextproject.model.entities.Crate;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.Gate;
import nl.tudelft.contextproject.model.entities.Pitfall;
import nl.tudelft.contextproject.model.entities.VoidPlatform;
import nl.tudelft.contextproject.model.entities.exploding.Bomb;
import nl.tudelft.contextproject.model.entities.exploding.LandMine;
import nl.tudelft.contextproject.model.entities.moving.KillerBunny;
import nl.tudelft.contextproject.webinterface.Action;

/**
 * Utility to perform actions generated by the web client.
 */
public final class ActionUtil {

	/**
	 * The radius in which gates can be opened.
	 */
	private static final float GATE_RADIUS = 2f;

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
	public static void perform(Action action, int xCoord, int yCoord) {
		switch (action) {
			case PLACEBOMB:
				placeBomb(xCoord, yCoord);
				break;
			case PLACEPITFALL:
				placePitfall(xCoord, yCoord);
				break;
			case PLACEMINE:
				placeMine(xCoord, yCoord);
				break;
			case SPAWNENEMY:
				spawnEnemy(xCoord, yCoord);
				break;
			case DROPBAIT:
				dropBait(xCoord, yCoord);
				break;
			case PLACETILE:
				placeTile(xCoord, yCoord);
				break;
			case DROPCRATE:
				dropCrate(xCoord, yCoord);
				break;
			case OPENGATE:
				openGate(xCoord, yCoord);
				break;
			default:
				throw new IllegalArgumentException("Your action is not valid.");
		}
	}

	/**
	 * Drop a crate.
	 * 
	 * @param xCoord
	 * 		the x coordinate to use
	 * @param yCoord
	 * 		the y coordinate to use
	 */
	private static void dropCrate(int xCoord, int yCoord) {
		Crate c = new Crate();
		c.move(xCoord, 5, yCoord);
		Main.getInstance().getCurrentGame().addEntity(c);
	}

	/**
	 * Place a bomb.
	 *
	 * @param xCoord
	 * 		the x coordinate to use
	 * @param yCoord
	 * 		the y coordinate to use
	 */
	private static void placeBomb(int xCoord, int yCoord) {
		Bomb bomb = new Bomb();
		bomb.move(xCoord, 0, yCoord);
		bomb.activate();
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
	private static void placePitfall(int xCoord, int yCoord) {
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
	private static void placeMine(int xCoord, int yCoord) {
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
	private static void spawnEnemy(int xCoord, int yCoord) {
		KillerBunny bunny = new KillerBunny(new Vector3f(xCoord, 0, yCoord));
		Main.getInstance().getCurrentGame().addEntity(bunny);
	}

	/**
	 * Drop bait.
	 *
	 * @param xCoord
	 * 		the x coordinate to use
	 * @param yCoord
	 * 		the y coordinate to use
	 */
	private static void dropBait(int xCoord, int yCoord) {
		Carrot carrot = new Carrot();
		carrot.move(xCoord, 0, yCoord);
		Main.getInstance().getCurrentGame().addEntity(carrot);
	}

	/**
	 * Place a voidPlatform.
	 *
	 * @param xCoord
	 * 		the x coordinate to use
	 * @param yCoord
	 * 		the y coordinate to use
	 */
	private static void placeTile(int xCoord, int yCoord) {
		VoidPlatform voidPlatform = new VoidPlatform();
		voidPlatform.move(xCoord, 0, yCoord);
		Main.getInstance().getCurrentGame().addEntity(voidPlatform);
	}
	
	/**
	 * Opens a gate.
	 * 
	 * @param xCoord
	 * 		the x coordinate to use
	 * @param yCoord
	 * 		the y coordinate to use
	 */
	private static void openGate(int xCoord, int yCoord) {
		for (Entity entity : Main.getInstance().getCurrentGame().getEntities()) {
			if (!(entity instanceof Gate)) continue;
			
			//The z is intentional, the y coordinates of the web interface map to the z coordinate in the maze.
			Vector3f location = entity.getLocation();
			if (location.x - xCoord > GATE_RADIUS || location.z - yCoord > GATE_RADIUS) continue;
			
			((Gate) entity).openGate();
		}
	}
}
