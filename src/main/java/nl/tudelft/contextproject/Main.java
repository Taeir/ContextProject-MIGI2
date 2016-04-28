package nl.tudelft.contextproject;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

/**
 * Main class of the game 'The Cave of Caerbannog'.
 */
public class Main extends SimpleApplication {
	private static Main instance;
	private Level level;
 
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
		this.level = new Level(12, 10);
		
		for (int x = 0; x < level.getWidth(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				if (level.isTileAtPosition(x, y)) {
					Geometry g = level.getTile(x, y).getGeometry();
					g.move(new Vector3f(x, y, 0));
					rootNode.attachChild(g);
				}
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