package nl.tudelft.contextproject.LevelFactory;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

import nl.tudelft.contextproject.Entity;
import nl.tudelft.contextproject.EntityState;
import nl.tudelft.contextproject.Level;
import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.MazeTile;
import nl.tudelft.contextproject.VRPlayer;

/**
 * An implementation of a LevelFactory that creates tiles randomly.
 * NOTE: This factory becomes deprecated after a proper factory is introduced!
 */
public class RandomLevelFactory implements LevelFactory {
	private Random rand;
	private int width;
	private int height;
	
	/**
	 * Constructor for the random level factory.
	 * @param width The width of the maze.
	 * @param height The height of the maze.
	 */
	public RandomLevelFactory(int width, int height) {
		this.height = height;
		this.width = width;
	}
	
	@Override
	public Level generateSeeded(long seed) {
		rand = new Random(seed);
		
		AssetManager am = Main.getInstance().getAssetManager();
		MazeTile[][] mazeTiles = new MazeTile[width][height];
		Set<Entity> entities = new HashSet<Entity>();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (rand.nextFloat() < .3f) {
					mazeTiles[x][y] = new MazeTile(am);
					// spawn some randomly despawning entities.
					if (rand.nextFloat() < .5f) {
						entities.add(new Entity() {
							private Geometry geometry;
							{
								Sphere b = new Sphere(10, 10, .2f);
								geometry = new Geometry("entity", b);
								Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
								mat.setColor("Color", ColorRGBA.Red);
								geometry.setMaterial(mat);
								geometry.move((float) (Math.random() * 10), 1, 2);
							}

							@Override
							public Geometry getGeometry() {
								return geometry;
							}

							@Override
							public void simpleUpdate(float tpf) {
								if (rand.nextFloat() < .001) setState(EntityState.DEAD);
							}
						});
					}
				}
			}
		}
		return new Level(mazeTiles, new VRPlayer(), entities);
	}

}
