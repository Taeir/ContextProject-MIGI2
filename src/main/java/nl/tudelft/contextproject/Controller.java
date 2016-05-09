package nl.tudelft.contextproject;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.Light;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Abstract class for controllers.
 */
public abstract class Controller extends AbstractAppState {
	private Node rootNode = new Node();
	private Node guiNode = new Node();

	/**
	 * protected constructor for the controller class.
	 * @param app The main app that is the parent of this controller.
	 */
	protected Controller(SimpleApplication app) {
		this.rootNode = app.getRootNode();
		this.guiNode = app.getGuiNode(); 
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);

		Main.getInstance().getRootNode().attachChild(rootNode);
		Main.getInstance().getGuiNode().attachChild(guiNode);  
	}
	
	@Override
	public abstract void update(float tpf);
	
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
		rootNode.attachChild(d.getGeometry());
	}
	
	/**
	 * Removes a Drawable from the renderer.
	 * @param d The Drawable to remove.
	 * @return True when the Drawable was removed, false otherwise.
	 */
	public boolean removeDrawable(Drawable d) {
		return rootNode.detachChild(d.getGeometry()) != -1;
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
	

	@Override
	public void cleanup() {
		Main main = Main.getInstance();
		main.getRootNode().detachAllChildren();
		main.getRootNode().detachChild(rootNode);
		main.getGuiNode().detachChild(guiNode);

		super.cleanup();
	}
}
