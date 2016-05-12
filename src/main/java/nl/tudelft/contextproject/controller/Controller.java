package nl.tudelft.contextproject.controller;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.InputListener;
import com.jme3.light.Light;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Drawable;

/**
 * Abstract class for controllers.
 */
public abstract class Controller extends AbstractAppState {
	private Node rootNode = new Node();
	private Node guiNode = new Node();
	private InputManager inputManager;

	/**
	 * Protected constructor for the controller class.
	 * @param app The application initializing this controller.
	 * @param name The controller name.
	 */
	protected Controller(SimpleApplication app, String name) {
		this.rootNode = new Node(name + "RootNode");
		this.guiNode = new Node(name + "GuiNode"); 
		this.inputManager = app.getInputManager();
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);

		Main main = Main.getInstance();
		main.getRootNode().attachChild(rootNode);
		main.getGuiNode().attachChild(guiNode);
	}
	
	@Override
	public abstract void update(float tpf);
	
	/**
	 * Get the gamestate of this controller.
	 * @return The gamestate of this controller.
	 */
	public abstract GameState getGameState();
	
	/**
	 * Add an element to the GUI.
	 * @param s A Spatial to add to the GUI.
	 */
	public void addGuiElement(Spatial s) {
		guiNode.attachChild(s);
	}
	
	/**
	 * Removes an element from the GUI.
	 * @param s The Spatial to remove.
	 * @return True when the element was removed, false otherwise.
	 */
	public boolean removeGuiElement(Spatial s) {
		return guiNode.detachChild(s) != -1;
	}
	
	/**
	 * Add a Drawable to the renderer.
	 * @param d The drawable to add.
	 */
	public void addDrawable(Drawable d) {
		rootNode.attachChild(d.getSpatial());
	}
	
	/**
	 * Removes a Drawable from the renderer.
	 * @param d The Drawable to remove.
	 * @return True when the Drawable was removed, false otherwise.
	 */
	public boolean removeDrawable(Drawable d) {
		return rootNode.detachChild(d.getSpatial()) != -1;
	}
	
	/**
	 * Add a light to the scene.
	 * @param l The light to add.
	 */
	public void addLight(Light l) {
		rootNode.addLight(l);
	}
	
	/**
	 * Removes the specified light from the scene.
	 * @param l The light to remove.
	 */
	public void removeLight(Light l) {
		rootNode.removeLight(l);
	}
	
	/**
	 * Remove the specified listener form the input manager.
	 * @param listener The listener to remove.
	 */
	public void removeInputListener(InputListener listener) {
		inputManager.removeListener(listener);
	}
	
	/**
	 * Add a listener to the specified mappings.
	 * @param listener The listener to add.
	 * @param mappingNames One ore more mappingNames to map the listener to.
	 */
	public void addInputListener(InputListener listener, String... mappingNames) {
		inputManager.addListener(listener, mappingNames);
	}
	
	/**
	 * Method used for testing.
	 * Sets the rootNode of Main to a new Node.
	 * @param rn The new node to replace the rootNode.
	 */
	protected void setRootNode(Node rn) {
		rootNode = rn;
	}

	/**
	 * Method used for testing.
	 * Sets the guiNode of Main to a new Node.
	 * @param gn The new node to replace the guiNode.
	 */
	protected void setGuiNode(Node gn) {
		guiNode = gn;
	}
	
	/**
	 * Method used for testing.
	 * Sets the inputMainager a specified InputManager.
	 * @param inputManager The new InputManager.
	 */
	protected void setInputManager(InputManager inputManager) {
		this.inputManager = inputManager;
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		Main main = Main.getInstance();
		main.getRootNode().detachChild(rootNode);
		main.getGuiNode().detachChild(guiNode);
		for (Light l: rootNode.getLocalLightList()) {
			rootNode.removeLight(l);
		}
	}
}
