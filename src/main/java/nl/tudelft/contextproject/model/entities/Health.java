package nl.tudelft.contextproject.model.entities;

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
	public void setHealth(float newHealth);
	/**
	 * Make the object take damage.
	 * 
	 * @param damage
	 * 		the damage dealt to the object
	 */
	public void takeDamage(float damage);
	
	/**
	 * @return
	 * 		the health of this objects
	 */
	public float getHealth();
}
