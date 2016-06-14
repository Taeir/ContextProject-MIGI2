package nl.tudelft.contextproject.controller;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;

/**
 * GameController for the ending state.
 */
public class EndingController extends GameThreadController {
	
	private static final String WIN_LEVEL = "WinLevel";
	private static final String LOSE_LEVEL = "LoseLevel";
	private boolean elvesWin;

	/**
	 * Constructor for the endingController.
	 * 
	 * @param app
	 * 		the application this controller is attached to
	 * @param elvesWin
	 * 		true when the elves won, false when the dwarfs won
	 */
	public EndingController(Application app, boolean elvesWin) {
		//We use a ternary if here because a call to the super class must be the first line, so
		//we cannot use a proper if statement.
		super(app, "/maps/" + (elvesWin ? WIN_LEVEL : LOSE_LEVEL) + "/", Float.MAX_VALUE, false);
		this.elvesWin = elvesWin;
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		if (elvesWin) {
			getHUD().showPopupText("You've won!", ColorRGBA.Green, 4);
		} else {
			getHUD().showPopupText("You've lost!", ColorRGBA.Red, 4);
		}
	}
	
	@Override
	public GameState getGameState() {
		return GameState.ENDED;
	}

	/**
	 * @return
	 * 		true if the elves won, false if the dwarfs won
	 */
	public boolean didElvesWin() {
		return elvesWin;
	}
}
