package nl.tudelft.contextproject.model.level;

import java.util.HashSet;
import java.util.Set;

import nl.tudelft.contextproject.model.entities.Entity;

/**
 * A class representing a filter for drawing a map.
 * An instance of this class is able to determine for a given entity if it should be drawn.
 */
public class DrawableFilter {
	private Set<String> entitySet;
	private boolean hideUnexplored;
	
	/**
	 * Simple constructor with an empty whitelist.
	 *
	 * @param hideUnexplored
	 * 		boolean to indicate if explored tiles should be drawn
	 */
	public DrawableFilter(boolean hideUnexplored) {
		this(new HashSet<String>(), hideUnexplored);
	}
	
	/**
	 * Normal constructor with a specific whitelist.
	 *
	 * @param entitySet
	 * 		the whitelist to filter on
	 * @param hideUnexplored
	 * 		boolean to indicate if explored tiles should be drawn
	 */
	public DrawableFilter(Set<String> entitySet, boolean hideUnexplored) {
		this.entitySet = entitySet;
		this.hideUnexplored = hideUnexplored;
	}

	/**
	 * Add an entity to the whitelist.
	 *
	 * @param e
	 * 		the entity to add
	 * @return
	 * 		true if the entity was added, false if it was already in the set
	 */
	public boolean addEntity(Entity e) {
		return entitySet.add(e.getClass().getSimpleName());
	}
	
	/**
	 * Check if an entity is on the whitelist.
	 *
	 * @param e
	 * 		entity to check for
	 * @return
	 * 		true when the entity is on the whitelist, false otherwise
	 */
	public boolean entity(Entity e) {
		return entitySet.contains(e.getClass().getSimpleName());
	}

	/**
	 * Check if unexplored tiles should be shown.
	 *
	 * @return
	 * 		true if it should be shown, false otherwise
	 */
	public boolean hideUnexplored() {
		return hideUnexplored;
	}

}
