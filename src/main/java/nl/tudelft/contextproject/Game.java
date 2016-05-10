package nl.tudelft.contextproject;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nl.tudelft.contextproject.level.Level;

public class Game {
	private Level level;
	private VRPlayer player;
	private List<Dwarf> dwarfs;
	private List<Elf> elves;
	private List<Entity> entities;
	
	
	public Game(Level level, VRPlayer player, List<Dwarf> dwarfs, List<Elf> elves, List<Entity> entities) {
		this.level = level;
		this.player = player;
		this.dwarfs = dwarfs;
		this.elves = elves;
		this.entities = entities;
	}
	
	public Game(Level level) {
		this.level = level;
		this.player = new VRPlayer();
		this.dwarfs = new LinkedList<>();
		this.elves = new LinkedList<>();
		this.entities = new LinkedList<>();
	}


	/**
	 * Getter for the player.
	 * @return The player.
	 */
	public VRPlayer getPlayer() {
		return player;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public Level getLevel() {
		return level;
	}
	
	/**
	 * Add an entity to the level.
	 * @param entity the entity to add.
	 * @return true if the entity was added, false otherwise.
	 */
	public boolean addEntity(Entity entity) {
		return entities.add(entity);
	}
}
