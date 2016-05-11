package nl.tudelft.contextproject.model;

/**
 * Abstract parent class for all entities.
 * All entities have a state, are Drawable and are Collidable.
 */
public abstract class Entity implements Drawable, TickListener, Collidable {
	private EntityState state = EntityState.NEW;

	/**
	 * Get the current state of this entity.
	 * @return The current state.
	 */
	public EntityState getState() {
		return state;
	}

	/**
	 * Set the state of this entity to a new state.
	 * @param newState The new state of this entity.
	 */
	public void setState(EntityState newState) {
		this.state = newState;
	}
}
