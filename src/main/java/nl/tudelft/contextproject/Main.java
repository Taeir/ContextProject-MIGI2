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

import nl.tudelft.contextproject.controller.Controller;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.controller.PauseController;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.model.level.RandomLevelFactory;

/**
 * Main class of the game 'The Cave of Caerbannog'.
 */
public class Main extends SimpleApplication {
	private static boolean debugHud;
	
	private static Main instance;
	private Controller controller;
	private List<TickListener> tickListeners;
	
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

	@Override
	public void simpleInitApp() {
		tickListeners = new LinkedList<>();
		setDisplayFps(debugHud);
		setDisplayStatView(debugHud);
		
		flyCam.setMoveSpeed(100);
		viewPort.setBackgroundColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 1f));
		getCamera().lookAtDirection(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
		
		setupControlMappings();
		setController(new GameController(this, (new RandomLevelFactory(10, 10)).generateRandom()));
	}

	/**
	 * Setup all the key mappings.
	 */
	protected void setupControlMappings() {
		inputManager.addMapping("pause", new KeyTrigger(KeyInput.KEY_P));
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
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