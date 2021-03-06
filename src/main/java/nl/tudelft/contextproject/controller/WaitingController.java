package nl.tudelft.contextproject.controller;

import com.jme3.app.Application;

/**
 * A controller for the waiting state of the game.
 * In this state players can connect to the web interface and the player is placed in a tutorial level.
 */
public class WaitingController extends GameThreadController {
	
	private static final String MENU_LEVEL = "MenuLevel";

	/**
	 * Create a waitingController.
	 *
	 * @param app
	 * 		the app this Controller is created by
	 */
	public WaitingController(Application app) {
		super(app, "/maps/" + MENU_LEVEL + "/", Float.MAX_VALUE, false);
	}
	
	@Override
	public GameState getGameState() {
		return GameState.WAITING;
	}
}