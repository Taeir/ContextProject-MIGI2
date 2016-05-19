package nl.tudelft.contextproject.controller;

import com.jme3.app.SimpleApplication;

/**
 * A controller for the waiting state of the game.
 * In this state players can connect to the web interface and the player is placed in a tutorial level.
 */
public class WaitingController extends GameController {
	
	private static final String MENU_LEVEL = "MenuLevel";

	/**
	 * Create a waitingController.
	 * @param app The app this Controller is created by (probably).
	 */
	public WaitingController(SimpleApplication app) {
		super(app, "/maps/" + MENU_LEVEL + "/");
	}

	@Override
	public GameState getGameState() {
		return GameState.WAITING;
	}
}
