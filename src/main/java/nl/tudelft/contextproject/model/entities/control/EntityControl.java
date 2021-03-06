package nl.tudelft.contextproject.model.entities.control;

import nl.tudelft.contextproject.model.entities.Entity;

/**
 * Interface for AI creation.
 */
public interface EntityControl {

	/**
	 * Update call for the AI that moves the attached owner.
	 * 
	 * @param tpf
	 * 		the time per frame
	 */
	void move(float tpf);

	/**
	 * Set the owner of this ai.
	 * 
	 * @param owner
	 * 		the owner of this ai.
	 */
	void setOwner(Entity owner);

}
