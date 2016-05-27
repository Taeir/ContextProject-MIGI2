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
import com.jme3.font.BitmapText;
import com.jme3.asset.AssetManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.ui.Picture;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Drawable;
import nl.tudelft.contextproject.model.entities.Bunny;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.EntityState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.model.level.roomIO.RoomParser;

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
	public GameController(SimpleApplication app, Level level) {
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
	public GameController(SimpleApplication app, String folder) {
		super(app, "GameController");

		Set<Entity> entities = ConcurrentHashMap.newKeySet();
		List<Light> lights = new ArrayList<>();

		try {
			File file = RoomParser.getMapFile(folder);
			String[] tmp = file.getName().split("_")[0].split("x");
			MazeTile[][] tiles = new MazeTile[Integer.parseInt(tmp[0])][Integer.parseInt(tmp[1])];
			RoomParser.importFile(folder, tiles, entities, lights, 0, 0);
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
		if (Main.getInstance().getAssetManager() != null) {
			attachHud();
		}
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
		addInputListener(game.getPlayer(), "Left", "Right", "Up", "Down", "Jump", "Bomb", "Pickup");
	}
	
	/**
	 * Attaches the Hud to the renderer.
	 */
	protected void attachHud() {
		Float height = 0f;
		Float width = 0f;
		if (Main.getInstance().getSettings() != null) {
		height = (float) Main.getInstance().getSettings().getHeight();
		width = (float) Main.getInstance().getSettings().getWidth();
		}
		//attach the keycounter
		Picture keypicyellow = new Picture("key Picture");
		keypicyellow.setImage(Main.getInstance().getAssetManager(), "Textures/emptykeyicon.png", true);
		keypicyellow.setWidth(width / 30);
		keypicyellow.setHeight(height / 12);
		keypicyellow.setPosition(width * 0.5f, 0);
		Picture keypicred = new Picture("key2Picture");
		keypicred.setImage(Main.getInstance().getAssetManager(), "Textures/emptykeyicon.png", true);
		keypicred.setWidth(width / 30);
		keypicred.setHeight(height / 12);
		keypicred.setPosition(width * 0.55f, 0);
		Picture keypicblue = new Picture("key3Picture");
		keypicblue.setImage(Main.getInstance().getAssetManager(), "Textures/emptykeyicon.png", true);
		keypicblue.setWidth(width / 30);
		keypicblue.setHeight(height / 12);
		keypicblue.setPosition(width * 0.6f, 0);
		addGuiElement(keypicyellow);
		addGuiElement(keypicred);
		addGuiElement(keypicblue);
		//attach the bombcounter
		BitmapText hudTextb = new BitmapText(Main.getInstance().getGuiFont(), false);
		hudTextb.setSize(height / 30);
		hudTextb.setColor(ColorRGBA.White);
		hudTextb.setText("" + Main.getInstance().getCurrentGame().getPlayer().getInventory().numberOfBombs());
		hudTextb.setLocalTranslation(width / 3f, hudTextb.getLineHeight() + height / 40, 0);
		addGuiElement(hudTextb);
		//attach bombicon
		Picture pic = new Picture("Bomb Picture");
		pic.setImage(Main.getInstance().getAssetManager(), "Textures/bombicon.png", true);
		pic.setWidth(width / 15);
		pic.setHeight(height / 10);
		pic.setPosition(width / 4f, 0);
		addGuiElement(pic);
		
		//attach your healthcounter        
		Picture heart1 = healthContainer(1, height, width);
		addGuiElement(heart1);
		Picture heart2 = healthContainer(2, height, width);
		addGuiElement(heart2);
		Picture heart3 = healthContainer(3, height, width);
		addGuiElement(heart3);
		//update the gui
		Main.getInstance().attachTickListener(new TickListener() {
			
			@Override
			public void update(float tpf) {
				//bomb update
				hudTextb.setText("" + Main.getInstance().getCurrentGame().getPlayer().getInventory().numberOfBombs()); 
				
				//health update
				if (game.getPlayer().getHealth() <= 2.5) {
					heart3.setImage(Main.getInstance().getAssetManager(), "Textures/emptyheart.png", true);
				}
				if (game.getPlayer().getHealth() <= 1.5) {
					heart2.setImage(Main.getInstance().getAssetManager(), "Textures/emptyheart.png", true);
				}
				if (game.getPlayer().getHealth() <= 0.5) {
					heart1.setImage(Main.getInstance().getAssetManager(), "Textures/emptyheart.png", true);
				}
				//keys update
				if ((game.getPlayer().getInventory().containsColorKey(ColorRGBA.Yellow.set(1.0f, 1.0f, 0.0f, 0.0f)))) {
					keypicyellow.setImage(Main.getInstance().getAssetManager(), "Textures/yellowkeyicon.png", true);
				} else {
					keypicyellow.setImage(Main.getInstance().getAssetManager(), "Textures/emptykeyicon.png", true);
				}
				if ((game.getPlayer().getInventory().containsColorKey(ColorRGBA.Blue.set(0.0f, 0.0f, 1.0f, 0.0f)))) {
					keypicblue.setImage(Main.getInstance().getAssetManager(), "Textures/bluekeyicon.png", true);
				} else {
					keypicblue.setImage(Main.getInstance().getAssetManager(), "Textures/emptykeyicon.png", true);
				}
				if ((game.getPlayer().getInventory().containsColorKey(ColorRGBA.Red.set(1.0f, 0.0f, 0.0f, 0.0f)))) {
					keypicred.setImage(Main.getInstance().getAssetManager(), "Textures/redkeyicon.png", true);
				} else {
					keypicred.setImage(Main.getInstance().getAssetManager(), "Textures/emptykeyicon.png", true);
				}
			}
		});
	}
	/**
	 * returns a picture of a healthContainer.
	 * @param pos
	 * 		Position of the healthContainer
	 * @param height
	 * 		Height of the screen
	 * @param width
	 * 		Width of the screen
	 * @return
	 * 		Picture of the healthcontainer
	 */
	public Picture healthContainer(int pos, float height, float width) {
		Picture heart = new Picture("heartcontainer" + pos);
		heart.setImage(Main.getInstance().getAssetManager(), "Textures/fullheart.png", true);
		heart.setWidth(width / 20);
		heart.setHeight(height / 20);
		if (pos > 0) {
				heart.setPosition((width / 3f), height / 1.1f);
		}
		if (pos > 1) {
				heart.setPosition((width / 2.6f), height / 1.1f);
		}
		if (pos > 2) {
				heart.setPosition((width / 2.3f), height / 1.1f);
		}
		return heart;
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
		if (!(Main.getInstance().getAssetManager() == null)) {
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
	protected void setGame(Game game) {
		this.game = game;
	}
}
