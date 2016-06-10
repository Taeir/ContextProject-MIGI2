package nl.tudelft.contextproject.model.level;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jme3.light.Light;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.level.roomIO.EntityParser;
import nl.tudelft.contextproject.model.level.roomIO.RoomParser;

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
		
		return new Level(tiles, lights, entities, playerSpawn);
	}

}
