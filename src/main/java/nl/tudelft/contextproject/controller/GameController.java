package nl.tudelft.contextproject.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.hud.HUD;
import nl.tudelft.contextproject.model.Drawable;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.EntityState;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import nl.tudelft.contextproject.model.entities.control.PlayerControl;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.model.level.roomIO.RoomParser;

import jmevr.app.VRApplication;

/**
 * Controller for the main game.
 */
public class GameController extends Controller {
	private Game game;

	/**
	 * Constructor for the game controller.
	 *
	 * @param app
	 * 		The Main instance of this game
	 * @param level
	 * 		The level for this game
	 */
	public GameController(Application app, Level level) {
		super(app, "GameController");

		game = new Game(level);
	}

	/**
	 * Create a game with a level loaded from a file.
	 *
	 * @param app
	 * 		the main app that this controller is attached to
	 * @param folder
	 * 		the folder where to load the level from
	 */
	public GameController(Application app, String folder) {
		super(app, "GameController");

		Set<Entity> entities = ConcurrentHashMap.newKeySet();
		List<Light> lights = new ArrayList<>();

		try {
			File file = RoomParser.getMapFile(folder);
			String[] tmp = file.getName().split("_")[0].split("x");
			MazeTile[][] tiles = new MazeTile[Integer.parseInt(tmp[0])][Integer.parseInt(tmp[1])];
			RoomParser.importFile(folder, tiles, entities, lights, 0, 0);
			Level level = new Level(tiles, lights);
			game = new Game(level, new VRPlayer(), entities, this);
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
		
		//Check if we are running in tests or not
		if (VRApplication.getMainVRApp().getContext() != null) {
			new HUD(this).attachHud();
		}
		
		GameController t = this;

		//Listener for stop the game
		addInputListener((ActionListener) (n, ip, tpf) -> Main.getInstance().stop(), "Exit");

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

		addInputListener((PlayerControl) game.getPlayer().getControl(), "Left", "Right", "Up", "Down", "Jump", "Bomb", "Pickup");
	}

	/**
	 * Attaches the current level to the renderer.
	 */
	protected void attachLevel() {
		Level level = game.getLevel();
		if (level == null) throw new IllegalStateException("No level set!");

		Vector2f start = attachMazeTiles(level);
		attachRoof(level);
		addDrawable(game.getPlayer());		
		game.getPlayer().move(start.x, 6, start.y);

		for (Light l : level.getLights()) {
			addLight(l);
		}

		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(.5f));
		addLight(al);
	}

	private void attachRoof(Level level) {
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
				mat.setFloat("Shininess", 64f);
				mat.setColor("Ambient", color);
				mat.setTexture("LightMap", am.loadTexture("Textures/rocktexture.png"));
				geom.setMaterial(mat); 

				geom.rotate((float) Math.toRadians(90), 0, 0);
				geom.move(0, 6, 0);
				return geom;
			}

			@Override
			public void setSpatial(Spatial spatial) { }
		});
	}

	/**
	 * Attach all {@link MazeTile}s in the level to the renderer.
	 * 
	 * @param 
	 * 		level the level that contains all the mazetiles
	 * @return 
	 * 		the starting position of the player
	 */
	private Vector2f attachMazeTiles(Level level) {
		Vector2f start = new Vector2f();
		for (int x = 0; x < level.getWidth(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				if (level.isTileAtPosition(x, y)) {
					//TODO add starting room with starting location
					if ((start.x == 0 && start.y == 0) && level.getTile(x, y).getTileType() == TileType.FLOOR) {
						start.x = x;
						start.y = y;
					}
					addDrawable(level.getTile(x, y));
				}
			}
		}
		return start;
	}

	@Override
	public void update(float tpf) {
		game.getPlayer().update(tpf);
		updateEntities(tpf);
	}

	/**
	 * Update all the entities in the level.
	 * Adds all new entities and removes all dead ones.
	 *
	 * @param tpf
	 * 		the time per frame for this update
	 */
	void updateEntities(float tpf) {
		for (Iterator<Entity> i = game.getEntities().iterator(); i.hasNext();) {
			Entity e = i.next();
			EntityState state = e.getState();

			switch (state) {
				case DEAD:
					removeDrawable(e);
					i.remove();
					break;
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
	 * @return
	 * 		the current level
	 */
	public Level getLevel() {
		return game.getLevel();
	}

	@Override
	public GameState getGameState() {
		return GameState.RUNNING;
	}

	/**
	 * @return
	 * 		the current game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Method used for testing.
	 * Set the instance of the game.
	 *
	 * @param game
	 * 		the new game instance
	 */
	public void setGame(Game game) {
		this.game = game;
	}
	
	/**
	 * Callback called when the game ends.
	 * 
	 * @param didElvesWin
	 * 		true when the elves won, false when the dwarfs did
	 */
	public void gameEnded(boolean didElvesWin) {
		Main main = Main.getInstance();
		main.setController(new EndingController(main, didElvesWin));
	}
}
