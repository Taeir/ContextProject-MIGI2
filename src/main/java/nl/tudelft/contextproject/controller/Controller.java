package nl.tudelft.contextproject.controller;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.input.controls.InputListener;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.util.TangentBinormalGenerator;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Drawable;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Abstract class for controllers.
 */
public abstract class Controller extends AbstractAppState {
	private Node rootNode = new Node();
	private Node guiNode = new Node();
	private BulletAppState physicsEnvironment = new BulletAppState();
	private InputManager inputManager;

	/**
	 * Protected constructor for the controller class.
	 *
	 * @param app
	 * 		the application initializing this controller
	 * @param name
	 * 		the controller name
	 */
	protected Controller(Application app, String name) {
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
		main.getStateManager().attach(physicsEnvironment);
	}

	@Override
	public abstract void update(float tpf);

	/**
	 * @return
	 * 		the GameState of this controller
	 */
	public abstract GameState getGameState();

	/**
	 * Add an element to the GUI.
	 *
	 * @param s
	 * 		a Spatial to add to the GUI
	 */
	public void addGuiElement(Spatial s) {
		guiNode.attachChild(s);
	}

	/**
	 * Removes an element from the GUI.
	 *
	 * @param s
	 * 		the Spatial to remove
	 * @return
	 * 		true when the element was removed, false otherwise
	 */
	public boolean removeGuiElement(Spatial s) {
		return guiNode.detachChild(s) != -1;
	}
	
	/**
	 * Add a Drawable to the renderer.
	 * Drawables should also have a collision.
	 *
	 * @param d 
	 * 		The drawable to add
	 */
	public void addDrawable(Drawable d) {
		if (d instanceof PhysicsObject) {
			physicsEnvironment.getPhysicsSpace().add(((PhysicsObject) d).getPhysicsObject());
		}

		rootNode.attachChild(d.getSpatial());
	}
	
	/**
	 * Removes a Drawable from the renderer.
	 *
	 * @param d
	 * 		the Drawable to remove
	 * @return
	 * 		true when the Drawable was removed, false otherwise
	 */
	public boolean removeDrawable(Drawable d) {
		if (d instanceof PhysicsObject) {
			physicsEnvironment.getPhysicsSpace().remove(((PhysicsObject) d).getPhysicsObject());
		}

		return rootNode.detachChild(d.getSpatial()) != -1;
	}

	/**
	 * Add a light to the scene.
	 *
	 * @param l
	 * 		the light to add
	 */
	public void addLight(Light l) {
		rootNode.addLight(l);
	}

	/**
	 * Removes the specified light from the scene.
	 *
	 * @param l
	 * 		the light to remove
	 */
	public void removeLight(Light l) {
		rootNode.removeLight(l);
	}

	/**
	 * Remove the specified listener form the input manager.
	 *
	 * @param listener
	 *		the listener to remove
	 */
	public void removeInputListener(InputListener listener) {
		inputManager.removeListener(listener);
	}

	/**
	 * Add a listener to the specified mappings.
	 *
	 * @param listener
	 * 		the listener to add
	 * @param mappingNames
	 * 		one ore more mappingNames to map the listener to
	 */
	public void addInputListener(InputListener listener, String... mappingNames) {
		inputManager.addListener(listener, mappingNames);
	}

	/**
	 * Method used for testing.
	 * Sets the rootNode of Main to a new Node.
	 *
	 * @param rn
	 * 		the new node to replace the rootNode
	 */
	protected void setRootNode(Node rn) {
		rootNode = rn;
	}

	/**
	 * Method used for testing.
	 * Sets the guiNode of Main to a new Node.
	 *
	 * @param gn
	 * 		the new node to replace the guiNode
	 */
	protected void setGuiNode(Node gn) {
		guiNode = gn;
	}

	/**
	 * Set the physic environment.
	 *
	 * @param phe
	 * 		A bullet app state
	 */
	protected void setPhysicsEnvironmentNode(BulletAppState phe) {
		physicsEnvironment = phe;
	}
	
	/**
	 * Method used for testing.
	 * Sets the inputManager a specified InputManager.
	 *
	 * @param inputManager
	 * 		the new InputManager
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
	
	/**
	 * Attach a roof tile to the rootNode.
	 * 
	 * @param x
	 * 		the x location of the tile
	 * @param y
	 * 		the y location of the tile
	 */
	public void attachRoofTile(int x, int y) {	
		Quad q = new Quad(1, 1);
		Geometry roofTile = new Geometry("roofTile", q);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"); 
		mat.setTexture("LightMap", Main.getInstance().getAssetManager().loadTexture("Textures/rocktexture.png"));
		TangentBinormalGenerator.generate(q);
		mat.setBoolean("UseMaterialColors", true);    
		mat.setColor("Diffuse", ColorRGBA.Gray);
		mat.setColor("Specular", ColorRGBA.White);
		mat.setFloat("Shininess", 64f);
		mat.setColor("Ambient", ColorRGBA.Gray);
		mat.setTexture("NormalMap", Main.getInstance().getAssetManager().loadTexture("Textures/rocknormalmap.jpg"));
		mat.setBoolean("UseMaterialColors", true);
		roofTile.setMaterial(mat); 

		roofTile.rotate((float) Math.toRadians(90), 0, 0);
		roofTile.move(x - .5f, TileType.WALL.getHeight() * 2, y - .5f);
		rootNode.attachChild(roofTile);
	}
}
