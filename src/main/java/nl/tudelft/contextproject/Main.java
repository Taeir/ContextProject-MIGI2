package nl.tudelft.contextproject;

import java.util.Iterator;

import com.jme3.app.SimpleApplication;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;

import nl.tudelft.contextproject.Level.Level;
import nl.tudelft.contextproject.Level.LevelFactory;
import nl.tudelft.contextproject.Level.RandomLevelFactory;

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
		app.start();
		instance = app;
	}

	@Override
	public void simpleInitApp() {
		levelFactory = new RandomLevelFactory(10, 10);
		this.level = levelFactory.generateRandom();

		for (int x = 0; x < level.getWidth(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				if (level.isTileAtPosition(x, y)) {
					Geometry g = level.getTile(x, y).getGeometry();
					g.move(x, y, 0);
					rootNode.attachChild(g);
				}
			}
		}
		
		PointLight p = new PointLight();
		p.setPosition(new Vector3f(1, 1, -4));
		p.setColor(ColorRGBA.randomColor());
		p.setRadius(20);
		rootNode.addLight(p);
	}


	@Override
	public void simpleUpdate(float tpf) {
		level.getPlayer().simpleUpdate(tpf);
		updateEntities(tpf);
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