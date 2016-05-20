package nl.tudelft.contextproject.model.level;

import java.util.List;

import com.jme3.light.Light;

import nl.tudelft.contextproject.model.Entity;
import nl.tudelft.contextproject.util.Size;

/**
 * Class that represent a room used in level creation.
 * 
 * This class is a wrapper for the level reader so that it can be used with the
 * level creation class.
 */
public class Room {

	//Room size 
	public Size size;
	
	//List of entities in the room
	public List<Entity> entities;
	
	//List of lights in the room
	public List<Light> lights;
	
	//List of tiles in the room
	public List<MazeTile> tiles;
	
	
	
	
}
