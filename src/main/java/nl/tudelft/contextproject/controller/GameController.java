package nl.tudelft.contextproject.controller;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.level.Level;

/**
 * Controller class for the main game.
 */
public class GameController extends GameThreadController {

	/**
	 * Constructor for the game controller.
	 *
	 * @param app
	 * 		the Main instance of this game
	 * @param level
	 * 		the level for this game
	 * @param timeLimit
	 * 		the time limit for this game
	 */
	public GameController(Application app, Level level, float timeLimit) {
		super(app, level, timeLimit);
	}
	
	/**
	 * Create main game with a level loaded from a file.
	 *
	 * @param app
	 * 		the main app that this controller is attached to
	 * @param folder
	 * 		the folder where to load the level from
	 * @param timeLimit
	 * 		the time limit for this game
	 * @param isMap
	 * 		if the level is a map, otherwise the map is a single room file
	 */
	public GameController(Application app, String folder, float timeLimit, boolean isMap) {
		super(app, folder, timeLimit, isMap);
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		
		getHUD().showPopupText("Go!", ColorRGBA.White, 4);
	}

	@Override
	public GameState getGameState() {
		return GameState.RUNNING;
	}

}
