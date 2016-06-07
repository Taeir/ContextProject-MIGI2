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
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.hud.HUD;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.EntityState;
import nl.tudelft.contextproject.model.entities.Gate;
import nl.tudelft.contextproject.model.entities.Treasure;
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
	/**
	 * The highest tpf that will be passed to all entities.
	 * This ensures that a big lag-spike wont allow entities to glitch through walls.
	 */
	public static final float MAX_TPF = .033f;
	private Game game;
	private HUD hud;

	/**
	 * Constructor for the game controller.
	 *
	 * @param app
	 * 		The Main instance of this game
	 * @param level
	 * 		The level for this game
	 * @param timeLimit
	 * 		the time limit for this game
	 */
	public GameController(Application app, Level level, float timeLimit) {
		super(app, "GameController");

		game = new Game(level, this, timeLimit);
	}

	/**
	 * Create a game with a level loaded from a file.
	 *
	 * @param app
	 * 		the main app that this controller is attached to
	 * @param folder
	 * 		the folder where to load the level from
	 * @param timeLimit
	 * 		the time limit for this game
	 */
	public GameController(Application app, String folder, float timeLimit) {
		super(app, "GameController");

		Set<Entity> entities = ConcurrentHashMap.newKeySet();
		List<Light> lights = new ArrayList<>();

		try {
			File file = RoomParser.getMapFile(folder);
			String[] tmp = file.getName().split("_")[0].split("x");
			MazeTile[][] tiles = new MazeTile[Integer.parseInt(tmp[0])][Integer.parseInt(tmp[1])];
			RoomParser.importFile(folder, tiles, entities, lights, 0, 0);
			Level level = new Level(tiles, lights);
			game = new Game(level, new VRPlayer(), entities, this, timeLimit);
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
			hud = new HUD(this);
			hud.attachHud();
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
		addDrawable(game.getPlayer());
		game.getPlayer().move(start.x, 0, start.y);
		for (Light l : level.getLights()) {
			addLight(l);
		}
		
		placeTreasure(game);
		
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(.5f));
		addLight(al);
	}

	/**
	 * Place a treasure in the level.
	 * 
	 * @param game
	 * 		the game that contains the level
	 */
	protected void placeTreasure(Game game) {
		Level level = game.getLevel();

		for (int x = level.getWidth() - 1; x >= 0; x--) {
			for (int y = level.getHeight() - 1; y >= 0; y--) {
				if (level.isTileAtPosition(x, y) && level.getTile(x, y).getTileType() == TileType.FLOOR) {
					Treasure e = new Treasure();
					e.move(x, 0, y);
					game.getEntities().add(e);
					return;
				}
			}
		}
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
					TileType t = level.getTile(x, y).getTileType();
					if (t == TileType.FLOOR || t == TileType.CORRIDOR) {
						attachRoofTile(x, y);
						if (start.x == 0 && start.y == 0) {
							start.x = x;
							start.y = y;
						}
					}
					addDrawable(level.getTile(x, y));
				}
			}
		}
		return start;
	}

	@Override
	public void update(float tpf) {
		tpf = Math.min(tpf, MAX_TPF);
		hud.setGameTimer(Math.round(game.getTimeRemaining()));
		game.update(tpf);
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

	/**
	 * Method used for testing.
	 * 
	 * @param hud
	 * 		the new hud
	 */
	protected void setHUD(HUD hud) {
		this.hud = hud;
	}
}
