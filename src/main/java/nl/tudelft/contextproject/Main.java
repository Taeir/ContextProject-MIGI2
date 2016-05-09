package nl.tudelft.contextproject;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;

import nl.tudelft.contextproject.level.RandomLevelFactory;

/**
 * Main class of the game 'The Cave of Caerbannog'.
 */
public class Main extends SimpleApplication {
	private static Main instance;
	private Controller controller;
	
	/**
	 * Method used for testing.
	 * Sets the instance of this singleton to the provided instance.
	 * @param main The new value of the instance.
	 */
	static void setInstance(Main main) {
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
				controller.cleanup();
				stateManager.detach(controller);
			}
			controller = c;
			stateManager.attach(controller);
			c.initialize(stateManager, this);
			return true;
		}
		return false;
	}
	
	/**
	 * Method used for testing.
	 * Sets the rootNode of Main to a new Node.
	 * @param rn The new node to replace the rootNode.
	 */
	void setRootNode(Node rn) {
		rootNode = rn;
	}

	/**
	 * Main method that is called when the program is started.
	 * @param args run-specific arguments.
	 */
	public static void main(String[] args) {
		getInstance().start();
	}

	@Override
	public void simpleInitApp() {
		setController(new GameController(this, new RandomLevelFactory(10, 10)));
	}

	/**
	 * Return the singleton instance of the game.
	 * @return the running instance of the game.
	 */
	public static Main getInstance() {
		if (instance == null) instance = new Main();
		return instance;
	}
	
	
}