package nl.tudelft.contextproject.model;

/**
 * Interface for observing objects.
 * 
 * @see Observable
 */
public interface Observer {
	
	/**
	 * Update method that is called whenever that which this Observer is observing, has changed.
	 *
	 * @param tpf
	 * 		the time between this and the previous update. Can be 0 if not applicable
	 */
	void update(float tpf);
}
