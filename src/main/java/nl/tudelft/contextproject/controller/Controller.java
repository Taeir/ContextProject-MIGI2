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
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Abstract class for controllers.
 */
public abstract class Controller extends AbstractAppState {
	protected Node wallsNode;
	protected Node floorsNode;
	protected Node roofNode;
	protected BulletAppState physicsEnvironment = new BulletAppState();
	
	private Node rootNode;
	private Node guiNode;
	
	
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
		
		this.roofNode = new Node(name + "RoofNode");
		this.wallsNode = new Node(name + "WallsNode");
		this.floorsNode = new Node(name + "FloorsNode");
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		
		rootNode.attachChild(wallsNode);
		rootNode.attachChild(floorsNode);
		rootNode.attachChild(roofNode);
		
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
	 * @param spatial
	 * 		a Spatial to add to the GUI
	 */
	public void addGuiElement(Spatial spatial) {
		guiNode.attachChild(spatial);
	}

	/**
	 * Removes an element from the GUI.
	 *
	 * @param spatial
	 * 		the Spatial to remove
	 * @return
	 * 		true when the element was removed, false otherwise
	 */
	public boolean removeGuiElement(Spatial spatial) {
		return guiNode.detachChild(spatial) != -1;
	}
	
	/**
	 * Add a Drawable to the renderer.
	 * Drawables should also have a collision.
	 *
	 * @param drawable 
	 * 		The drawable to add
	 */
	public void addDrawable(Drawable drawable) {
		if (drawable instanceof PhysicsObject) {
			physicsEnvironment.getPhysicsSpace().add(((PhysicsObject) drawable).getPhysicsObject());
		}
		
		if (drawable instanceof MazeTile) {
			TileType type = ((MazeTile) drawable).getTileType();
			if (type == TileType.FLOOR) {
				floorsNode.attachChild(drawable.getSpatial());
				return;
			} else if (type == TileType.WALL) {
				wallsNode.attachChild(drawable.getSpatial());
				return;
			}
		}

		rootNode.attachChild(drawable.getSpatial());
	}
	
	/**
	 * Removes a Drawable from the renderer.
	 *
	 * @param drawable
	 * 		the Drawable to remove
	 * @return
	 * 		true when the Drawable was removed, false otherwise
	 */
	public boolean removeDrawable(Drawable drawable) {
		if (drawable instanceof PhysicsObject) {
			physicsEnvironment.getPhysicsSpace().remove(((PhysicsObject) drawable).getPhysicsObject());
		}
		
		if (drawable instanceof MazeTile) {
			MazeTile tile = (MazeTile) drawable;
			switch (tile.getTileType()) {
				case FLOOR:
					return floorsNode.detachChild(drawable.getSpatial()) != -1;
				case WALL:
					return wallsNode.detachChild(drawable.getSpatial()) != -1;
				default:
					break;
			}
		}

		return rootNode.detachChild(drawable.getSpatial()) != -1;
	}

	/**
	 * Add a light to the scene.
	 *
	 * @param light
	 * 		the light to add
	 */
	public void addLight(Light light) {
		rootNode.addLight(light);
	}

	/**
	 * Removes the specified light from the scene.
	 *
	 * @param light
	 * 		the light to remove
	 */
	public void removeLight(Light light) {
		rootNode.removeLight(light);
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
	 * @param rootNode
	 * 		the new node to replace the rootNode
	 */
	protected void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	/**
	 * Method used for testing.
	 * Sets the guiNode of Main to a new Node.
	 *
	 * @param guiNode
	 * 		the new node to replace the guiNode
	 */
	protected void setGuiNode(Node guiNode) {
		this.guiNode = guiNode;
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

		for (Light light: rootNode.getLocalLightList()) {
			rootNode.removeLight(light);
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
		Quad quad = new Quad(1, 1);
		Geometry roofTile = new Geometry("roofTile", quad);
		Material material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md"); 
		material.setTexture("LightMap", Main.getInstance().getAssetManager().loadTexture("Textures/rocktexture.png"));
		TangentBinormalGenerator.generate(quad);
		material.setBoolean("UseMaterialColors", true);    
		material.setColor("Diffuse", ColorRGBA.Gray);
		material.setColor("Specular", ColorRGBA.White);
		material.setFloat("Shininess", 64f);
		material.setColor("Ambient", ColorRGBA.Gray);
		material.setTexture("NormalMap", Main.getInstance().getAssetManager().loadTexture("Textures/rocknormalmap.jpg"));
		material.setBoolean("UseMaterialColors", true);
		roofTile.setMaterial(material); 

		roofTile.rotate((float) Math.toRadians(90), 0, 0);
		roofTile.move(x - .5f, TileType.WALL.getHeight() * 2, y - .5f);
		roofNode.attachChild(roofTile);
	}
}
