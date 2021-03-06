package nl.tudelft.contextproject.model.entities.util;

/**
 * Interface for entities with health.
 */
public interface Health {
	
	/**
	 * Set the health of the object.
	 * 
	 * @param newHealth
	 * 		the new health
	 */
	void setHealth(float newHealth);
	
	/**
	 * Make the object take damage.
	 * 
	 * @param damage
	 * 		the damage dealt to the object
	 */
	void takeDamage(float damage);
	
	/**
	 * @return
	 * 		the health of this objects
	 */
	float getHealth();
}
