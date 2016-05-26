package nl.tudelft.contextproject.model.entities;

/**
 * Interface for entities that can be eaten.
 */
public interface FoodItem {
	/**
	 * Eat a piece of this {@link FoodItem}.
	 * 
	 * @param eatenBy
	 * 		the entity eating this
	 * @param amount
	 * 		the amount that was eaten
	 */
	public void eat(Entity eatenBy, float amount);
}
