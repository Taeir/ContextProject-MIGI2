package nl.tudelft.contextproject.controller;

import com.jme3.app.Application;

/**
 * Controller for the tutorial.
 */
public class TutorialController extends GameController {
	private static final String TUTORIAL_LEVEL = "TutorialLevel";

	/**
	 * Constructor for the TutorialController.
	 *
	 * @param app
	 * 		the application this controller is attached to
	 */
	public TutorialController(Application app) {
		super(app, "/maps/" + TUTORIAL_LEVEL + "/", Float.MAX_VALUE, false);
	}

	@Override
	public GameState getGameState() {
		return GameState.TUTORIAL;
	}
}
