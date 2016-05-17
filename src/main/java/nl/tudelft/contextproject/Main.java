package nl.tudelft.contextproject;

import java.util.Arrays;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import nl.tudelft.contextproject.audio.AudioManager;
import nl.tudelft.contextproject.audio.BackgroundMusic;
import nl.tudelft.contextproject.controller.Controller;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.controller.PauseController;
import nl.tudelft.contextproject.logging.Log;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.model.level.RandomLevelFactory;
import nl.tudelft.contextproject.webinterface.WebServer;

/**
 * Main class of the game 'The Cave of Caerbannog'.
 */
public class Main extends SimpleApplication {
	
	/**
	 * Port number of server. //TODO this should be set in a setting class preferably.
	 */
	public static final int PORT_NUMBER = 8080;
	
	private static boolean debugHud;
	
	private static Main instance;
	private Controller controller;
	private WebServer webServer;
	private List<TickListener> tickListeners = new LinkedList<>();

	/**
	 * Main method that is called when the program is started.
	 * @param args run-specific arguments.
	 */
	public static void main(String[] args) {
		Main main = getInstance();
		List<String> a = Arrays.asList(args);
		debugHud = a.contains("--debugHud");
		main.start();
	}

	/**
	 * Method used for testing.
	 * Sets the instance of this singleton to the provided instance.
	 * @param main The new value of the instance.
	 */
	public static void setInstance(Main main) {
		instance = main;
	}
	
	/**
	 * Set the controller for the current scene.
	 * Cleans up the old scene before initializing the new one.
	 * @param c The new controller.
	 * @return true is the controller was changed, false otherwise.
	 */
	public boolean setController(Controller c) {
		if (c != controller) {
			if (controller != null) {
				stateManager.detach(controller);
			}
			controller = c;
			stateManager.attach(controller);
			return true;
		}
		return false;
	}
	
	/**
	 * Get the instance of the current game.
	 * @return the current instance of the game.
	 * @throws IllegalStateException when the current controller is not a game Controller.
	 */
	public Game getCurrentGame() throws IllegalStateException {
		if (controller instanceof GameController) {
			return ((GameController) controller).getGame();				
		}
		if (controller instanceof PauseController) {
			return ((PauseController) controller).getPausedController().getGame();				
		}
		throw new IllegalStateException("The game is not running!");
	}
	
	/**
	 * Method used for testing.
	 * Sets the rootNode of Main to a new Node.
	 * @param rn The new node to replace the rootNode.
	 */
	public void setRootNode(Node rn) {
		rootNode = rn;
	}

	/**
	 * Method used for testing.
	 * Sets the guiNode of Main to a new Node.
	 * @param gn The new node to replace the guiNode.
	 */
	public void setGuiNode(Node gn) {
		guiNode = gn;
	}

	/**
	 * Method used for testing.
	 * Sets the list of tickListeners to the specified list.
	 * @param listeners The new List of ticklisteners.
	 */
	protected void setTickListeners(List<TickListener> listeners) {
		tickListeners = listeners;
	}
	
	/**
	 * Method used for testing.
	 * Sets the inputManager to the specified inputManager.
	 * @param im The new InputManager.
	 */
	protected void setInputManager(InputManager im) {
		inputManager = im;
	}

	@Override
	public void simpleInitApp() {
		setDisplayFps(debugHud);
		setDisplayStatView(debugHud);
		
		//TODO if VR support is implemented the flyby camera should be disabled
		getFlyByCamera().setMoveSpeed(100);
		getViewPort().setBackgroundColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 1f));
		getCamera().lookAtDirection(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
		
		setupControlMappings();
		setController(new GameController(this, (new RandomLevelFactory(5, false)).generateRandom()));
		setupWebServer();

		//Initialize the AudioManager.
		AudioManager.getInstance().init();

		//Start the background music
		BackgroundMusic.getInstance().start();
	}
	
	@Override
	public void stop(boolean waitFor) {
		//Stop the webServer before shutting down
		try {
			webServer.stop();
		} catch (Exception ex) {
			Log.getLog("WebInterface").warning("Exception while trying to stop webserver", ex);
		}

		BackgroundMusic.getInstance().stop();
		super.stop(waitFor);
	}

	/**
	 * Setup all the key mappings.
	 */
	protected void setupControlMappings() {
		getInputManager().addMapping("pause", new KeyTrigger(KeyInput.KEY_P));
		getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
	}
	
	//TODO this will be removed when camera type is changed
	/**
	 * Creates the web server and starts it.
	 */
	protected void setupWebServer() {
		webServer = new WebServer();
		
		try {
			webServer.start(PORT_NUMBER);
		} catch (Exception ex) {
			Log.getLog("WebInterface").severe("Exception while trying to start webserver", ex);
		}
	}
	
	/**
	 * Move the camera to a new location.
	 * @param newLoc
	 * 					the new location of the camera
	 */ 
	public void moveCameraTo(Vector3f newLoc) {
		getCamera().setLocation(newLoc);
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		for (TickListener tl : tickListeners) {
			tl.update(tpf);
		}

		//Update location for 3D audio
		getListener().setLocation(getCamera().getLocation());
		getListener().setRotation(getCamera().getRotation());

		//Update BackgroundMusic
		BackgroundMusic.getInstance().update(tpf);
	}
	
	/**
	 * Add a Ticklistener.
	 * @param tl The ticklistener to add.
	 */
	public void attachTickListener(TickListener tl) {
		tickListeners.add(tl);
	}
	
	/**
	 * Remove a registered TickListener.
	 * @param tl The ticklistener to remove.
	 */
	public void removeTickListener(TickListener tl) {
		tickListeners.remove(tl);
	}

	/**
	 * Return the singleton instance of the game.
	 * @return the running instance of the game.
	 */
	public static Main getInstance() {
		if (instance == null) {
			instance = new Main();
		}
		return instance;
	}
	
	/**
	 * Get the current game state.
	 * @return The current game state.
	 */
	public GameState getGameState() {
		if (controller == null) return null;
		return controller.getGameState();
	}

	/**
	 * Check if the debug Hud is shown.
	 * @return True when shown, false otherwise.
	 */
	public static boolean isDebugHudShown() {
		return debugHud;
	}
}