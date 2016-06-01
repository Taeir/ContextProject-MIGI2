package nl.tudelft.contextproject.controller;

import com.jme3.app.Application;

import nl.tudelft.contextproject.model.Game;

/**
 * GameController for the ending state.
 */
public class EndingController extends GameController {
	
	private static final String WIN_LEVEL = "WinLevel";
	private static final String LOSE_LEVEL = "LoseLevel";

	/**
	 * Constructor for the endingController.
	 * 
	 * @param app
	 * 		the application this controller is attached to
	 * @param elvesWin
	 * 		true when the elves won, false when the dwarfs won
	 */
	public EndingController(Application app, boolean elvesWin) {
		super(app, "/maps/" + (elvesWin ? WIN_LEVEL : LOSE_LEVEL) + "/", Float.MAX_VALUE);
	}
	
	@Override
	public GameState getGameState() {
		return GameState.ENDED;
	}

	@Override
	protected void placeTreasure(Game game) { };
}
