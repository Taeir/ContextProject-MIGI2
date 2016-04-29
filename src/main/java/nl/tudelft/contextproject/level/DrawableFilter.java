package nl.tudelft.contextproject.level;

import java.util.HashSet;
import java.util.Set;

import nl.tudelft.contextproject.Entity;

/**
 * A class representing a filter for drawing a map.
 * An instance of this class is able to determine for a given entity if it should be drawn.
 */
public class DrawableFilter {
	private Set<String> entitySet;
	private boolean hideUnexplored;
	
	/**
	 * Simple constructor with an empty whitelist.
	 * @param hideUnexplored Boolean to indicate if explored tiles should be draw.
	 */
	public DrawableFilter(boolean hideUnexplored) {
		this(new HashSet<String>(), hideUnexplored);
	}
	
	/**
	 * Normal constructor with a specific whitelist.
	 * @param entitySet The whitelist to filter on.
	 * @param hideUnexplored Boolean to indicate if explored tiles should be draw.
	 */
	public DrawableFilter(Set<String> entitySet, boolean hideUnexplored) {
		this.entitySet = entitySet;
		this.hideUnexplored = hideUnexplored;
	}

	/**
	 * Add an entity to the whitelist.
	 * @param e The entity to add.
	 * @return true when te entity is added, false if it was already in the list.
	 */
	public boolean addEntity(Entity e) {
		return entitySet.add(e.getClass().getSimpleName());
	}
	
	/**
	 * Check if an entity is on the whitelist.
	 * @param e Entity to check for.
	 * @return true when the entity is on the whitelist, false otherwise.
	 */
	public boolean entity(Entity e) {
		return entitySet.contains(e.getClass().getSimpleName());
	}

	/**
	 * Check if unexplored tiles should be shown.
	 * @return true if it should be shown, else otherwise.
	 */
	public boolean hideUnexplored() {
		return hideUnexplored;
	}

}
