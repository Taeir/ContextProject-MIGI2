package nl.tudelft.contextproject.model.level;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jme3.light.Light;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.util.Size;

/**
 * Class that represent a room used in level creation.
 * 
 * This class is a wrapper for the level reader so that it can be used with the
 * level creation class.
 */
public class Room {

	private static final Pattern PATTERN = Pattern.compile("(?<width>\\d+)x(?<height>\\d+)_.*");

	//Room size 
	public Size size;

	//List of entities in the room
	public List<Entity> entities;

	//List of lights in the room
	public List<Light> lights;

	//2d representation of tiles in the room
	public TileType[][] tiles;

	/**
	 * Constructor will load room from files using RoomIO.
	 * @param fileName
	 * 		fileName of the room
	 */
	public Room(String fileName) {
		entities = new ArrayList<Entity>();
		lights = new ArrayList<Light>();
		setSizeFromFileName(fileName);


	}

	/**
	 * Get size of room from file name.
	 * @param fileName
	 * 		fileName of the Room
	 */
	protected void setSizeFromFileName(String fileName) {
		int width = 0;
		int height = 0;
		Matcher m = PATTERN.matcher(fileName);
		if (m.matches()) {
			width = Integer.parseInt(m.group("width"));
			height = Integer.parseInt(m.group("height"));
			size = new Size(width, height);
		} 
	}




}
