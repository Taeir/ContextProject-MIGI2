package nl.tudelft.contextproject.model.entities;

/**
 * Interface for AI creation.
 */
public interface EntityAI {

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
