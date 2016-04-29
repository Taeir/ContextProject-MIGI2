package nl.tudelft.contextproject;

/**
 * Enum representing the state of an Entity.
 */
public enum EntityState {
	DEAD,	// The entity is dead and should be removed from the renderer
	ALIVE,	// The entity is alive and should receive updates
	NEW		// The entity is new and should be added to the renderer
}
