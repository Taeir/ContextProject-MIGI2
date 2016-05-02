package nl.tudelft.contextproject;

import java.util.Iterator;

import com.jme3.app.SimpleApplication;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

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
	 * Main method that is called when the program is started.
	 * @param args run-specific arguments.
	 */
	public static void main(String[] args) {
		Main app = new Main();
		
		instance = app;
		app.start();
	}

	@Override
	public void simpleInitApp() {
		levelFactory = new RandomLevelFactory(10, 10);
		this.level = levelFactory.generateRandom();

		for (int x = 0; x < level.getWidth(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				if (level.isTileAtPosition(x, y)) {
					Geometry g = level.getTile(x, y).getGeometry();
					rootNode.attachChild(g);
				}
			}
		}
		rootNode.attachChild(level.getPlayer().getGeometry());
		
		PointLight p = new PointLight();
		p.setPosition(new Vector3f(1, 1, 4));
		p.setColor(ColorRGBA.randomColor());
		p.setRadius(20);
		rootNode.addLight(p);
		
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

	@Override
	public void simpleUpdate(float tpf) {
		level.getPlayer().simpleUpdate(tpf);
		updateEntities(tpf);
		
		//Update location for 3D audio
		listener.setLocation(getCamera().getLocation());
		listener.setRotation(getCamera().getRotation());
		
		BackgroundMusic.getInstance().update(tpf);
	}

	/**
	 * Update all the entities in the level.
	 * Add all new entities to should be added to the rootNode and all dead ones should be removed.
	 */
	private void updateEntities(float tpf) {
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
		return instance;
	}
}