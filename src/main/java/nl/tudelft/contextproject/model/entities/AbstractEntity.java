package nl.tudelft.contextproject.model.entities;

import nl.tudelft.contextproject.model.entities.util.EntityState;

/**
 * Abstract implementation of an Entity.
 */
public abstract class AbstractEntity implements Entity {
	private EntityState state = EntityState.NEW;

	@Override
	public EntityState getState() {
		return state;
	}

	@Override
	public void setState(EntityState newState) {
		this.state = newState;
	}
}
