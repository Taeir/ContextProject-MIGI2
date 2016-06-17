package nl.tudelft.contextproject.model.level;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.level.roomIO.EntityParser;
import nl.tudelft.contextproject.model.level.roomIO.RoomParser;
import nl.tudelft.contextproject.model.level.util.RoomNode;
import nl.tudelft.contextproject.model.level.util.TorchType;
import nl.tudelft.contextproject.util.Vec2I;

import lombok.SneakyThrows;

/**
 * Implementation of Level Factory, for creating levels from single rooms.
 */
public class RoomLevelFactory implements LevelFactory {
	private String fileLocation;
	
	/**
	 * Creates a new RoomLevelFactory to provide levels of the room specified in the file at the
	 * given file location.
	 * 
	 * @param fileLocation
	 * 		the location of the room file
	 */
	public RoomLevelFactory(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
	@SneakyThrows(IOException.class)
	@Override
	public Level generateSeeded(long seed) {
		Set<Entity> entities = ConcurrentHashMap.newKeySet();
		List<Light> lights = new ArrayList<>();

		File file = RoomParser.getMapFile(this.fileLocation);
		String[] dimensions = file.getName().split("_")[0].split("x");
		
		int width = Integer.parseInt(dimensions[0]);
		int height = Integer.parseInt(dimensions[1]);
		
		MazeTile[][] tiles = new MazeTile[width][height];
		RoomParser.importFile(this.fileLocation, tiles, entities, lights, 0, 0);
		
		Vector3f playerSpawn = EntityParser.getPlayerSpawnLocation(entities, new Random(seed));
		
		addLightModels(lights, entities, tiles);
		
		return new Level(tiles, lights, entities, playerSpawn);
	}
	
	/**
	 * Add a light models as torches to level.
	 * 
	 * @param lights
	 * 		the list of lights
	 * @param entities
	 * 		the list of entities which will contain the torches entities.
	 * @param tiles
	 * 		the map that should be checked.
	 */
	protected void addLightModels(List<Light> lights, Set<Entity> entities, MazeTile[][] tiles) {
		for (Light light : lights) {
			if (light instanceof PointLight) {
				PointLight pointLight = ((PointLight) light);
				Vector3f position = pointLight.getPosition();
				if (RoomNode.renderTorches) {
					position = pointLight.getPosition();
					Vec2I newLightPosition = new Vec2I(Math.round(position.x), Math.round(position.z));
					entities.add(TorchType.createTorchOfTorchType(TorchType.getTorchType(tiles, newLightPosition), 
							new Vector3f(newLightPosition.x, 4.5f, newLightPosition.y)));
				}
			}
		}
	}

}
