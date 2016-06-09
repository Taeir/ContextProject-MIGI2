package nl.tudelft.contextproject.model.entities.control;

import nl.tudelft.contextproject.model.entities.Entity;

/**
 * Implementation of EntityControl that does nothing.
 */
public class NoControl implements EntityControl {

	@Override
	public void move(float tpf) { }

	@Override
	public void setOwner(Entity owner) { }

}
