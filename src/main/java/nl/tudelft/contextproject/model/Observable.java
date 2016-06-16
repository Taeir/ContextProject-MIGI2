package nl.tudelft.contextproject.model;

import java.util.Set;

/**
 * Interface for classes that can be observed by others.
 * 
 * @see Observer
 */
public interface Observable {

	/**
	 * @return
	 * 		a set with all the attached {@link Observer}s
	 */
	Set<Observer> getObservers();

	/**
	 * Update all attached {@link Observer}s.
	 */
	default void updateObservers() {
		updateObservers(0);
	}
	
	/**
	 * Update all attached {@link Observer}s with the given tpf value.
	 * 
	 * @param tpf
	 * 		the tpf value for the update
	 */
	default void updateObservers(float tpf) {
		for (Observer observer : getObservers()) {
			observer.update(tpf);
		}
	}

	/**
	 * Registers an {@link Observer}.
	 * 
	 * @param observer
	 * 		the {@link Observer} to register
	 */
	default void registerObserver(Observer observer) {
		getObservers().add(observer);		
	}
	
	/**
	 * Remove a registered {@link Observer}.
	 *
	 * @param observer
	 * 		the {@link Observer} to remove
	 */
	default void removeObserver(Observer observer) {
		getObservers().remove(observer);
	}
}
