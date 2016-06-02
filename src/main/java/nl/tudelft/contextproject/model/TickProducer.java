package nl.tudelft.contextproject.model;

import java.util.List;

/**
 * Interface for classes that produce ticks that can be listened to.
 */
public interface TickProducer {

	/**
	 * @return
	 * 		a list with all the attached tickListeners
	 */
	List<TickListener> getTickListeners();

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
		for (TickListener tl : getTickListeners()) {
			tl.update(tpf);
		}
	}

	/**
	 * Attach a {@link TickListener}.
	 * 
	 * @param tl
	 * 		the {@link TickListener} to attach
	 */
	default void attachTickListener(TickListener tl) {
		getTickListeners().add(tl);		
	}
	
	/**
	 * Remove a registered TickListener.
	 *
	 * @param tl
	 * 		the TickListener to remove
	 */
	default void removeTickListener(TickListener tl) {
		getTickListeners().remove(tl);
	}
}
