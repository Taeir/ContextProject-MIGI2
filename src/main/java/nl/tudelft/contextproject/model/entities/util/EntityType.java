package nl.tudelft.contextproject.model.entities.util;

import java.util.function.BiFunction;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.model.entities.Carrot;
import nl.tudelft.contextproject.model.entities.Crate;
import nl.tudelft.contextproject.model.entities.Door;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.Gate;
import nl.tudelft.contextproject.model.entities.Key;
import nl.tudelft.contextproject.model.entities.Pitfall;
import nl.tudelft.contextproject.model.entities.VoidPlatform;
import nl.tudelft.contextproject.model.entities.environment.DamagedWall;
import nl.tudelft.contextproject.model.entities.environment.InvisibleWall;
import nl.tudelft.contextproject.model.entities.environment.PlayerTrigger;
import nl.tudelft.contextproject.model.entities.environment.Torch;
import nl.tudelft.contextproject.model.entities.environment.Treasure;
import nl.tudelft.contextproject.model.entities.environment.WallFrame;
import nl.tudelft.contextproject.model.entities.exploding.Bomb;
import nl.tudelft.contextproject.model.entities.exploding.LandMine;
import nl.tudelft.contextproject.model.entities.moving.KillerBunny;
import nl.tudelft.contextproject.model.entities.moving.VRPlayer;

/**
 * Enum for the different entity types.
 */
public enum EntityType {
	BOMB("Bomb", 					1,  Bomb::loadEntity),
	LANDMINE("LandMine", 			2,  LandMine::loadEntity),
	PITFALL("Pitfall", 				3,  Pitfall::loadEntity),
	
	DOOR("Door", 					4,  Door::loadEntity),
	KEY("Key", 						5,  Key::loadEntity),
	GATE("Gate",					6,  Gate::loadEntity),
	CRATE("Crate",					7,  Crate::loadEntity),
	
	CARROT("Carrot", 				8, Carrot::loadEntity),
	KILLER_BUNNY("KillerBunny", 	9, KillerBunny::loadEntity),
	
	VOID_PLATFORM("VoidPlatform", 	10, VoidPlatform::loadEntity),
	INVISIBLE_WALL("InvisibleWall",	11,	InvisibleWall::loadEntity),
	DAMAGED_WALL("DamagedWall",		12,	DamagedWall::loadEntity),
	
	PLAYER("Player", 				13, VRPlayer::loadEntity),
	PLAYER_TRIGGER("PlayerTrigger", 14, PlayerTrigger::loadEntity),
	
	TREASURE("Treasure", 			15, Treasure::loadEntity),
	
	EXPLOSION("Explosion",			0,  null),
	WALLFRAME("WallFrame", 			0,  WallFrame::loadEntity),
	TORCH("Torch", 					0,  Torch::loadEntity);
	
	private final String name;
	private final int webId;
	private final transient BiFunction<Vector3f, String[], Entity> loader;
	
	/**
	 * Creates a new EntityType.
	 * 
	 * @param name
	 * 		the name of the entity
	 * @param webId
	 * 		the id of the entity in the web interface
	 * @param loader
	 * 		the function for loading an entity of this type
	 */
	EntityType(String name, int webId, BiFunction<Vector3f, String[], Entity> loader) {
		this.name = name;
		this.webId = webId;
		this.loader = loader;
	}
	
	/**
	 * @return
	 * 		the name of the entity, as used by the map format
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return
	 * 		the ID of the entity in the web interface, 0 if it has no ID
	 */
	public int getWebId() {
		return webId;
	}
	
	/**
	 * Loads an entity of this type from the given data.
	 * 
	 * @param position
	 * 		the position of the entity
	 * @param data
	 * 		the string with data of the entity
	 * @return
	 * 		the entity loaded from the given data
	 * @throws IllegalArgumentException
	 * 		if the the length of the data does not match with what the entity requests
	 */
	public Entity loadEntity(Vector3f position, String[] data) {
		return loader.apply(position, data);
	}
	
	/**
	 * Gets the EntityType of the entity with the given name.
	 * 
	 * @param name
	 * 		the name of the EntityType to get
	 * @return
	 * 		the EntityType of the entity with the given name
	 */
	public static EntityType getType(String name) {
		if (name == null) throw new IllegalArgumentException("Name cannot be null!");
		
		for (EntityType type : values()) {
			if (type.name.equals(name)) return type;
		}
		
		throw new IllegalArgumentException(name + " is not a known Entity type!");
	}
}
