package nl.tudelft.contextproject.model;

import java.util.Set;

/**
 * Interface for classes that produce ticks that can be listened to.
 */
public interface TickProducer {

	/**
	 * @return
	 * 		a set with all the attached tickListeners
	 */
	Set<TickListener> getTickListeners();

	/**
	 * Update all attached {@link TickListener}s with 0 as tpf.
	 */
	default void updateTickListeners() {
		updateTickListeners(0);
	}
	
	/**
	 * Update all attached {@link TickListener}s.
	 * 
	 * @param tpf
	 * 		the tpf value for the update
	 */
	default void updateTickListeners(float tpf) {
		for (TickListener tickListener : getTickListeners()) {
			tickListener.update(tpf);
		}
	}

	/**
	 * Attach a {@link TickListener}.
	 * 
	 * @param tickListener
	 * 		the {@link TickListener} to attach
	 */
	default void attachTickListener(TickListener tickListener) {
		getTickListeners().add(tickListener);		
	}
	
	/**
	 * Remove a registered TickListener.
	 *
	 * @param tickListener
	 * 		the TickListener to remove
	 */
	default void removeTickListener(TickListener tickListener) {
		getTickListeners().remove(tickListener);
	}
}
