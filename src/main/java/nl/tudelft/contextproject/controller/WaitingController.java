package nl.tudelft.contextproject.controller;

import java.io.File;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.files.FileUtil;
import nl.tudelft.contextproject.model.level.RandomLevelFactory;

/**
 * A controller for the waiting state of the game.
 * In this state players can connect to the web interface and the player is placed in a tutorial level.
 */
public class WaitingController extends GameController {
	
	private static final String MENU_LEVEL = "50x50_MenuLevel.crf";

	/**
	 * Create a waitingController.
	 * @param app The app this Controller is created by (probably).
	 */
	public WaitingController(SimpleApplication app) {
		super(app, getFile());
	}
	
	/**
	 * Temporary method to enable the player to start the main game by pressing the pause button.
	 */
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		Main main = Main.getInstance();
		main.getInputManager().addListener(new ActionListener() {			
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				main.setController(new GameController(main, (new RandomLevelFactory(5, false)).generateRandom()));
			}
		}, "pause");
	}

	/**
	 * Get the file to load.
	 * @return The file to load.
	 */
	protected static File getFile() {
		return FileUtil.getFile("/maps/" + MENU_LEVEL);
	}

	@Override
	public GameState getGameState() {
		return GameState.WAITING;
	}
}
