package nl.tudelft.contextproject.model;

import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

/**
 * Test class for {@link Observable}.
 */
public class ObservableTest extends TestBase {
	
	private Observable observable;

	/**
	 * Create a fresh instance of {@link Observable} for each test.
	 */
	@Before
	public void setUp() {
		observable = new Observable() {
			private Set<Observer> observers = new HashSet<>();
			
			@Override
			public Set<Observer> getObservers() {
				return observers;
			}
		};
	}
	
	/**
	 * Test calling the simple update method.
	 */
	@Test
	public void testSimpleUpdate() {
		Observer tickListener = mock(Observer.class);
		observable.registerObserver(tickListener);
		observable.updateObservers();
		verify(tickListener, times(1)).update(0f);
	}
	
	/**
	 * Test calling the update method using a tpf value.
	 */
	@Test
	public void testUpdate() {
		Observer tickListener = mock(Observer.class);
		observable.registerObserver(tickListener);
		observable.updateObservers(2f);
		verify(tickListener, times(1)).update(2f);
	}

	/**
	 * Test if removing a {@link Observer} does not keep updating it.
	 */
	@Test
	public void testRemove() {
		Observer tickListener = mock(Observer.class);
		observable.registerObserver(tickListener);
		observable.updateObservers();
		verify(tickListener, times(1)).update(0f);
		
		observable.removeObserver(tickListener);
		observable.updateObservers();
		verifyNoMoreInteractions(tickListener);	
	}
}
