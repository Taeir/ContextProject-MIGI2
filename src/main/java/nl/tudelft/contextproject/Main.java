package nl.tudelft.contextproject;

import java.util.Iterator;

import com.jme3.app.SimpleApplication;
import com.jme3.light.Light;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import nl.tudelft.contextproject.audio.AudioManager;
import nl.tudelft.contextproject.audio.BackgroundMusic;
import nl.tudelft.contextproject.level.DrawableFilter;
import nl.tudelft.contextproject.level.Level;
import nl.tudelft.contextproject.level.LevelFactory;
import nl.tudelft.contextproject.level.MapBuilder;
import nl.tudelft.contextproject.level.RandomLevelFactory;

/**
 * Main class of the game 'The Cave of Caerbannog'.
 */
public class Main extends SimpleApplication {
	private static Main instance;
	private Level level;
	private LevelFactory levelFactory;

	/**
	 * Method used for testing.
	 * Sets the instance of this singleton to the provided instance.
	 * @param main The new value of the instance.
	 */
	static void setInstance(Main main) {
		instance = main;
	}
	
	/**
	 * Method used for testing.
	 * Sets the rootNode of Main to a new Node.
	 * @param rn The new node to replace the rootNode.
	 */
	void setRootNode(Node rn) {
		rootNode = rn;
	}

	/**
	 * Main method that is called when the program is started.
	 * @param args run-specific arguments.
	 */
	public static void main(String[] args) {
		getInstance().start();
	}

	@Override
	public void simpleInitApp() {
		levelFactory = new RandomLevelFactory(10, 10);
		setLevel(levelFactory.generateRandom());
		attachLevel();
		
		/* Temp code*/
		MapBuilder.setLevel(level);
		DrawableFilter filter = new DrawableFilter(false);
		filter.addEntity(level.getPlayer());
		filter.addEntity(new Entity() {
			@Override
			public Geometry getGeometry() {
				 return null;
			}
			@Override
			public void simpleUpdate(float tpf) { }

			@Override
			public void setGeometry(Geometry geometry) { }
		});
		MapBuilder.export("hello.png", filter, 16);
		
		//Initialize the AudioManager.
		AudioManager.getInstance().init();
		
		//Start the background music
		BackgroundMusic.getInstance().start();
	}
	
	@Override
	public void stop() {
		//Stop the background music
		BackgroundMusic.getInstance().stop();
		
		super.stop();
	}

	/**
	 * Attaches the current level to the renderer.
	 * Note: this method does not clear the previous level, use {@link #clearLevel()} for that.
	 */
	public void attachLevel() {
		if (level == null) throw new IllegalStateException("No level set!");
		for (int x = 0; x < level.getWidth(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				if (level.isTileAtPosition(x, y)) {
					Geometry g = level.getTile(x, y).getGeometry();
					rootNode.attachChild(g);
				}
			}
		}
		rootNode.attachChild(level.getPlayer().getGeometry());
		
		for (Light l : level.getLights()) {
			rootNode.addLight(l);
		}
	}

	/**
	 * Setter for the level.
	 * @param level The new level.
	 */
	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * Removes the current level from the renderer.
	 */
	public void clearLevel() {
		rootNode.detachAllChildren();
		for (Light l : rootNode.getLocalLightList()) {
			rootNode.removeLight(l);
		}
	}

	@Override
	public void simpleUpdate(float tpf) {
		level.getPlayer().simpleUpdate(tpf);
		updateEntities(tpf);
		
		//Update location for 3D audio
		getListener().setLocation(getCamera().getLocation());
		getListener().setRotation(getCamera().getRotation());
		
		//Update BackgroundMusic
		BackgroundMusic.getInstance().update(tpf);
	}

	/**
	 * Update all the entities in the level.
	 * Add all new entities to should be added to the rootNode and all dead ones should be removed.
	 * @param tpf The time per frame for this update.
	 */
	void updateEntities(float tpf) {
		for (Iterator<Entity> i = level.getEntities().iterator(); i.hasNext();) {
			Entity e = i.next();
		    EntityState state = e.getState();
			switch (state) {
			case DEAD:
				rootNode.detachChild(e.getGeometry());
				i.remove();
				continue;
			case NEW:
				rootNode.attachChild(e.getGeometry());
				e.setState(EntityState.ALIVE);
				e.simpleUpdate(tpf);
				break;
			default:
				e.simpleUpdate(tpf);
				break;
			}
		}
	}

	/**
	 * Return the singleton instance of the game.
	 * @return the running instance of the game.
	 */
	public static Main getInstance() {
		if (instance == null) instance = new Main();
		return instance;
	}
	
	/**
	 * Getter for the current level.
	 * @return The current level.
	 */
	public Level getLevel() {
		return level;
	}
}
