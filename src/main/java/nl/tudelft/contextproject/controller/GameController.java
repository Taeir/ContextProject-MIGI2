package nl.tudelft.contextproject.controller;

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
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.ui.Picture;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Entity;
import nl.tudelft.contextproject.model.EntityState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.TickListener;
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
		attachHud();
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
	 * Attaches the Hud to the renderer.
	 */
	public void attachHud() {
		float onethird = (float) (Main.getInstance().getSettings().getWidth() / 3);
		float twothird = onethird * 2;
		//attach the keycounter
		BitmapText hudText = new BitmapText(Main.getInstance().getGuiFont(), false);          
		hudText.setSize(Main.getInstance().getGuiFont().getCharSet().getRenderedSize());
		hudText.setColor(ColorRGBA.White);
		hudText.setText("Keys: " + Main.getInstance().getCurrentGame().getPlayer().getInventory().numberOfKeys());
		hudText.setLocalTranslation(twothird, hudText.getLineHeight(), 0);
		addGuiElement(hudText);
		
		//attach the bombcounter
		BitmapText hudTextb = new BitmapText(Main.getInstance().getGuiFont(), false);          
		hudTextb.setSize(Main.getInstance().getSettings().getHeight() / 30);
		hudTextb.setColor(ColorRGBA.White);
		hudTextb.setText("" + Main.getInstance().getCurrentGame().getPlayer().getInventory().numberOfBombs());
		hudTextb.setLocalTranslation(onethird + 30, hudTextb.getLineHeight() + Main.getInstance().getSettings().getHeight() / 40, 0);
		addGuiElement(hudTextb);
		
		//attach bombicon
		Picture pic = new Picture("Bomb Picture");
		pic.setImage(Main.getInstance().getAssetManager(), "Textures/bombicon.png", true);
		pic.setWidth(Main.getInstance().getSettings().getWidth() / 15);
		pic.setHeight(Main.getInstance().getSettings().getHeight() / 10);
		pic.setPosition(200, 0);
		addGuiElement(pic);
		
		//attach your healthcounter
		BitmapText hudTexth = new BitmapText(Main.getInstance().getGuiFont(), false);          
		hudTexth.setSize(Main.getInstance().getGuiFont().getCharSet().getRenderedSize());
		hudTexth.setColor(ColorRGBA.White);
		hudTexth.setText("Health: " + Main.getInstance().getCurrentGame().getPlayer().getHealth());             
		hudTexth.setLocalTranslation((float) (Main.getInstance().getSettings().getWidth() / 3), Main.getInstance().getSettings().getHeight(), 0); 
		addGuiElement(hudTexth);
		//update the gui
		Main.getInstance().attachTickListener(new TickListener() {
			
			@Override
			public void update(float tpf) {
				hudText.setText("Keys: " + Main.getInstance().getCurrentGame().getPlayer().getInventory().numberOfKeys());
				hudTextb.setText("" + Main.getInstance().getCurrentGame().getPlayer().getInventory().numberOfBombs()); 
				hudTexth.setText("Health: " + Main.getInstance().getCurrentGame().getPlayer().getHealth());
			}
		});
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