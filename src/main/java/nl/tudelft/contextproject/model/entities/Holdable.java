package nl.tudelft.contextproject.model.entities;

/**
 * Interface for holdable entities.
 */
public interface Holdable {
	/**
	 * @return
	 * 		true when picked up, false otherwise
	 */
	boolean isPickedUp();
	
	/**
	 * Method called when the entity is picked up.
	 */
	void pickUp();
	
	/**
	 * Method called when the entity is dropped.
	 */
	void drop();
	
	/**
	 * Called every frame.
	 * 
	 * @param tpf
	 * 		the time per frame of this frame
	 */
	default void update(float tpf) { }
}
