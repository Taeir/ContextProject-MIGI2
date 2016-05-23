package nl.tudelft.contextproject.model;

/**
 * Interface for objects that listen to the game ticks.
 */
public interface TickListener {
	
	/**
	 * Update method called once per frame.
	 *
	 * @param tpf
	 * 		the time between this and the previous frame
	 */
	void update(float tpf);
}
