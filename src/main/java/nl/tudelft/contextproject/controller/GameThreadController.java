package nl.tudelft.contextproject.controller;

import java.util.Iterator;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.hud.HUD;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.EntityState;
import nl.tudelft.contextproject.model.entities.control.PlayerControl;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.LevelFactory;
import nl.tudelft.contextproject.model.level.MSTBasedLevelFactory;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.RoomLevelFactory;
import nl.tudelft.contextproject.model.level.TileType;

import jme3tools.optimize.GeometryBatchFactory;
import jmevr.app.VRApplication;

/**
 * Controller for a game thread.
 */
public abstract class GameThreadController extends Controller {
	/**
	 * The highest tpf that will be passed to all entities.
	 * This ensures that a big lag-spike wont allow entities to glitch through walls.
	 */
	public static final float MAX_TPF = .033f;
	
	private Game game;
	private HUD hud;

	/**
	 * Constructor for a game thread.
	 *
	 * @param app
	 * 		The Main instance of this game
	 * @param level
	 * 		The level for this game
	 * @param timeLimit
	 * 		the time limit for this game
	 */
	public GameThreadController(Application app, Level level, float timeLimit) {
		super(app, "GameController");
		
		game = new Game(level, this, timeLimit);
	}

	/**
	 * Create a game thread with a level loaded from a file.
	 *
	 * @param app
	 * 		the main app that this controller is attached to
	 * @param folder
	 * 		the folder where to load the level from
	 * @param timeLimit
	 * 		the time limit for this game
	 * @param isMap
	 * 		if the level is a map, otherwise the map is a single room file
	 */
	public GameThreadController(Application app, String folder, float timeLimit, boolean isMap) {
		super(app, "GameController");
		
		LevelFactory factory;
		if (!isMap) {
			factory = new RoomLevelFactory(folder);
		} else {
			factory = new MSTBasedLevelFactory("/maps/testGridMap/"); 	
		}
		
		Level level = factory.generateRandom();
		game = new Game(level, this, timeLimit);
	}

	@Override
	public void cleanup() {
		super.cleanup();

		for (Entity entity : game.getEntities()) {
			entity.setState(EntityState.NEW);
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

		//Listener for stop the game
		addInputListener((ActionListener) (name, ip, tpf) -> Main.getInstance().stop(), "Exit");

		//Listener to pause the game
		addInputListener((ActionListener) this::onPause, "pause");

		addInputListener((PlayerControl) game.getPlayer().getControl(), "Left", "Right", "Up", "Down", "Jump", "Drop", "Pickup");
	}

	/**
	 * Attaches the current level to the renderer.
	 */
	protected void attachLevel() {
		Level level = game.getLevel();
		if (level == null) throw new IllegalStateException("No level set!");

		attachMazeTiles(level);
		
		//Optimize the roof, walls and floors
		GeometryBatchFactory.optimize(roofNode, false, true);
		GeometryBatchFactory.optimize(wallsNode, false, true);
		GeometryBatchFactory.optimize(floorsNode, false, true);
		
		addDrawable(game.getPlayer());
		for (Light light : level.getLights()) {
			addLight(light);
		}
		
		AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(ColorRGBA.White.mult(.9f));
		addLight(ambientLight);
		
		Spatial sky = SkyFactory.createSky(
                Main.getInstance().getAssetManager(), "Textures/lavatexture.jpg", SkyFactory.EnvMapType.EquirectMap);
		getRootNode().attachChild(sky);

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
				attachRoofTile(x, y);
				if (level.isTileAtPosition(x, y)) {
					TileType type = level.getTile(x, y).getTileType();
					
					if ((type == TileType.FLOOR || type == TileType.CORRIDOR) && start.x == 0 && start.y == 0) {
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
		for (Iterator<Entity> it = game.getEntities().iterator(); it.hasNext();) {
			Entity entity = it.next();
			EntityState state = entity.getState();

			switch (state) {
				case DEAD:
					removeDrawable(entity);
					it.remove();
					break;
				case NEW:
					addDrawable(entity);
					entity.setState(EntityState.ALIVE);
					entity.update(tpf);
					break;
				default:
					entity.update(tpf);
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
	
	/**
	 * @return
	 * 		the hud
	 */
	public HUD getHUD() {
		return hud;
	}
	
	/**
	 * @param name
	 * 		the name of the event
	 * @param isPressed
	 * 		if the key was pressed (true) or released (false)
	 * @param tpf
	 * 		the time per frame
	 */
	public void onPause(String name, boolean isPressed, float tpf) {
		if (isPressed) return;
		
		Main main = Main.getInstance();
		main.togglePause();
		
		if (main.isPaused()) {
			getHUD().showPopupText("Paused", ColorRGBA.White, 2);
		} else {
			getHUD().showPopupText("Unpaused", ColorRGBA.White, 2);
		}
	}
}
