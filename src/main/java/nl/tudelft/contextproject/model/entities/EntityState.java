package nl.tudelft.contextproject.model.entities;

/**
 * Enum representing the state of an Entity.
 */
public enum EntityState {
	/**
	 * The entity is dead and should be removed from the renderer.
	 */
	DEAD,

	/**
	 * The entity is alive and should receive updates.
	 */
	ALIVE,

	/**
	 * The entity is new and should be added to the renderer.
	 */
	NEW
}
