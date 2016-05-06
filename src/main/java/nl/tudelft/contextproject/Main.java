package nl.tudelft.contextproject;

import java.util.Iterator;

import com.jme3.app.SimpleApplication;
import com.jme3.light.Light;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

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

	
	public static void setInstance(Main main) {
		instance = main;
	}
	
	public void setRootNode(Node rn) {
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
	}


	public void attachLevel() {
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

	public void setLevel(Level level) {
		this.level = level;
		System.out.println("!");
	}

	public void clearLevel() {
		rootNode.detachAllChildren();
		for (Light l : rootNode.getLocalLightList()) {
			rootNode.removeLight(l);
		}
	}

	@Override
	public void simpleUpdate(float tpf) {
		System.out.println(level.getPlayer());
		level.getPlayer().simpleUpdate(tpf);
		updateEntities(tpf);
	}

	/**
	 * Update all the entities in the level.
	 * Add all new entities to should be added to the rootNode and all dead ones should be removed.
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
	
	public Level getLevel() {
		return level;
	}
}