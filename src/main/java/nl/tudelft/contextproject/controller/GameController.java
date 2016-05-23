package nl.tudelft.contextproject.controller;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Drawable;
import nl.tudelft.contextproject.model.Entity;
import nl.tudelft.contextproject.model.EntityState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.VRPlayer;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.roomIO.RoomReader;

/**
 * Controller for the main game.
 */
public class GameController extends Controller {
	private Game game;

	/**
	 * Constructor for the game controller.
	 * @param app The Main instance of this game.
	 * @param level The level for this game.
	 */
	public GameController(SimpleApplication app, Level level) {
		super(app, "GameController");
		game = new Game(level);
	}
	
	/**
	 * Create a game with a level loaded from a file.
	 * @param app The main app that this controller is attached to.
	 * @param folder The folder where to load the level from.
	 */
	public GameController(SimpleApplication app, String folder) {
		super(app, "GameController");
		Set<Entity> entities = ConcurrentHashMap.newKeySet();
		List<Light> lights = new ArrayList<>();
		try {
			File file = RoomReader.getMapFile(folder);
			String[] tmp = file.getName().split("_")[0].split("x");
			MazeTile[][] tiles = new MazeTile[Integer.parseInt(tmp[0])][Integer.parseInt(tmp[1])];
			RoomReader.importFile(folder, tiles, entities, lights, 0, 0);
			Level level = new Level(tiles, lights);
			game = new Game(level, new VRPlayer(), entities);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void cleanup() {
		super.cleanup();
		for (Entity e : game.getEntities()) {
			e.setState(EntityState.NEW);
		}
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		attachLevel();

		GameController t = this;
		ActionListener al = new ActionListener() {
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				if (!isPressed) {
					removeInputListener(this);
					Main main = Main.getInstance();
					Main.getInstance().setController(new PauseController(t, main));
				}
			}
		};
		addInputListener(al, "pause");
		addInputListener(game.getPlayer(), "Left");
		addInputListener(game.getPlayer(), "Right");
		addInputListener(game.getPlayer(), "Up");
		addInputListener(game.getPlayer(), "Down");
		addInputListener(game.getPlayer(), "Jump");
		addInputListener(game.getPlayer(), "Bomb");
		addInputListener(game.getPlayer(), "Pickup");
	}

	/**
	 * Attaches the current level to the renderer.
	 * Note: this method does not clear the previous level, use {@link #clearLevel()} for that.
	 */
	public void attachLevel() {
		Level level = game.getLevel();
		if (level == null) throw new IllegalStateException("No level set!");
		int xStart = 0; 
		int yStart = 0;
		// add roof
		  addDrawable(new Drawable() {

		   @Override
		   public Spatial getSpatial() {
		    Quad roof = new Quad(level.getWidth(), level.getHeight());

		    Geometry geom = new Geometry("roof", roof);
		    
		    AssetManager am = Main.getInstance().getAssetManager();
		    Material mat = new Material(am, "Common/MatDefs/Light/Lighting.j3md");
		    mat.setBoolean("UseMaterialColors", true);
		    ColorRGBA color = ColorRGBA.Gray;
		    mat.setColor("Diffuse", color);
		    mat.setColor("Specular", color);
		    mat.setFloat("Shininess", 64f);  // [0,128]
		    mat.setColor("Ambient", color);
		    mat.setTexture("LightMap", am.loadTexture("Textures/rockwall.png"));
		    geom.setMaterial(mat); 
		    
		    geom.rotate((float) Math.toRadians(90), 0, 0);
		    geom.move(0, 6, 0);
		    return geom;
		   }

		   @Override
		   public void setSpatial(Spatial spatial) { }

		   @Override
		   public void mapDraw(Graphics2D g, int resolution) { }
		  });
		  
		for (int x = 0; x < level.getWidth(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				if (level.isTileAtPosition(x, y)) {
					//TODO add starting room with starting location
					if ((xStart == 0 && yStart == 0) && level.getTile(x, y).getTileType() == TileType.FLOOR) {
						xStart = x;
						yStart = y;
					}
					addDrawable(level.getTile(x, y));
				}
			}
		}
		//Add player
		addDrawable(game.getPlayer());
		
		if (game.getPlayer().getPhysicsObject() != null) {
			game.getPlayer().getPhysicsObject().setPhysicsLocation(new Vector3f(xStart, 6, yStart));
		}
		
		for (Light l : level.getLights()) {
			addLight(l);
		}
		 
		AmbientLight al = new AmbientLight();
		 al.setColor(ColorRGBA.White.mult(.5f));
		addLight(al);
	}

	@Override
	public void update(float tpf) {
		game.getPlayer().update(tpf);
		updateEntities(tpf);
	}

	/**
	 * Update all the entities in the level.
	 * Add all new entities to should be added to the rootNode and all dead ones should be removed.
	 * @param tpf The time per frame for this update.
	 */
	void updateEntities(float tpf) {
		for (Iterator<Entity> i = game.getEntities().iterator(); i.hasNext();) {
			Entity e = i.next();
			EntityState state = e.getState();
			switch (state) {
			case DEAD:
				removeDrawable(e);
				i.remove();
				continue;
			case NEW:
				addDrawable(e);
				e.setState(EntityState.ALIVE);
				e.update(tpf);
				break;
			default:
				e.update(tpf);
				break;
			}
		}
	}
	/**
	 * Getter for the current level.
	 * @return The current level.
	 */
	public Level getLevel() {
		return game.getLevel();
	}

	@Override
	public GameState getGameState() {
		return GameState.RUNNING;
	}

	/**
	 * Getter for the current game.
	 * @return The current game.
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Method used for testing.
	 * Set the instance of the game.
	 * @param game The new game instance.
	 */
	protected void setGame(Game game) {
		this.game = game;
	}
}