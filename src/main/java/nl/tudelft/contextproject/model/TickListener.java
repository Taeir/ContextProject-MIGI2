package nl.tudelft.contextproject.model;

/**
 * Interface for objects that listen to the game ticks.
 */
public interface TickListener {
	
	/**
	 * Update method called once per frame.
	 * @param tpf The time between this and the previous frame.
	 */
	public void update(float tpf);
}
