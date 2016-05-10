package nl.tudelft.contextproject.controller;

import java.util.Iterator;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Entity;
import nl.tudelft.contextproject.model.EntityState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.LevelFactory;
import nl.tudelft.contextproject.model.level.Room;

/**
 * Controller for the main game.
 */
public class GameController extends Controller {
	private Game game;
	private LevelFactory levelFactory;

	/**
	 * Constructor for the game controller.
	 * @param app The Main instance of this game.
	 * @param levelFactory The factory that creates levels for this game.
	 */
	public GameController(SimpleApplication app, LevelFactory levelFactory) {
		super(app, "GameController");
		this.levelFactory = levelFactory;
		game = new Game(levelFactory.generateRandom());
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

		Controller t = this;
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

		//		/* Temp code*/
		//		MapBuilder.setLevel(game.getLevel());
		//		DrawableFilter filter = new DrawableFilter(false);
		//		filter.addEntity(game.getPlayer());
		//		filter.addEntity(new Entity() {
		//			@Override
		//			public Geometry getGeometry() {
		//				return null;
		//			}
		//			@Override
		//			public void update(float tpf) { }
		//
		//			@Override
		//			public void setGeometry(Geometry geometry) { }
		//		});
		//		MapBuilder.export("hello.png", filter, 16);
	}

	/**
	 * Attaches the current level to the renderer.
	 * Note: this method does not clear the previous level, use {@link #clearLevel()} for that.
	 */
	public void attachLevel() {
		Level level = game.getLevel();
		if (level == null) throw new IllegalStateException("No level set!");
		for (int i = 0; i < level.getRooms().length; i++) {
			Room room = level.getRooms()[i];
			for (int x = 0; x < room.getWidth(); x++) {
				for (int y = 0; y < room.getHeight(); y++) {
					if (room.isTileAtPosition(x, y)) {
						addDrawable(room.getTile(x, y));
					}
				}
			}
		}
		addDrawable(game.getPlayer());

		for (Light l : level.getLights()) {
			addLight(l);
		}

		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(.9f));
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

	public void setLevel(Level level) {
		game.setLevel(level);
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		cleanup();
		this.game = game;
		attachLevel();
	}
}