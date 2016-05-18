package nl.tudelft.contextproject.controller;

import java.util.Iterator;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Direction;
import nl.tudelft.contextproject.model.Entity;
import nl.tudelft.contextproject.model.EntityState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.PlayerTrigger;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.model.WallFrame;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.util.ScriptLoader;
import nl.tudelft.contextproject.util.ScriptLoaderException;

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

		PointLight pl = new PointLight(new Vector3f(25, 100, 25));
		 pl.setColor(ColorRGBA.White);
		 pl.setRadius(500);
		addLight(pl);
		 
		AmbientLight al = new AmbientLight();
		 al.setColor(ColorRGBA.White.mult(.5f));
		addLight(al);
//		addTestEntities(game, xStart, yStart);
	}

	/**
	 * Add some testing entities to the game.
	 * Made into a separate method for easy test fixing by removing the call to this method.
	 * @param game The game to add them to.
	 * @param xStart The x position of the player.
	 * @param yStart The y position of the player.
	 */
	private void addTestEntities(Game game, int xStart, int yStart) {
		game.addEntity(new WallFrame(new Vector3f(xStart, 1f, yStart), "logo.png", Direction.NORTH));
		
		try {			
			TickListener tl = ScriptLoader.getInstanceFrom("scripts/", "TestListener", TickListener.class);
			PlayerTrigger e = new PlayerTrigger(.4f, 1f, tl, new Vector3f(xStart, 0, yStart));

			game.getEntities().add(e);
		} catch (ScriptLoaderException e1) {
			e1.printStackTrace();
		}
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