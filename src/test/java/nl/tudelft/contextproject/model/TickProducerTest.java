package nl.tudelft.contextproject.model;

import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

/**
 * Test class for {@link TickProducer}.
 */
public class TickProducerTest extends TestBase {
	
	private TickProducer tp;

	/**
	 * Create a fresh instance of {@link TickProducer} for each test.
	 */
	@Before
	public void setUp() {
		tp = new TickProducer() {
			private Set<TickListener> listeners = new HashSet<>();
			
			@Override
			public Set<TickListener> getTickListeners() {
				return listeners;
			}
		};
	}
	
	/**
	 * Test calling the simple update method.
	 */
	@Test
	public void testSimpleUpdate() {
		TickListener tl = mock(TickListener.class);
		tp.attachTickListener(tl);
		tp.updateTickListeners();
		verify(tl, times(1)).update(0f);
	}
	
	/**
	 * Test calling the update method using a tpf value.
	 */
	@Test
	public void testUpdate() {
		TickListener tl = mock(TickListener.class);
		tp.attachTickListener(tl);
		tp.updateTickListeners(2f);
		verify(tl, times(1)).update(2f);
	}

	/**
	 * Test if removing a {@link TickListener} does not keep updating it.
	 */
	@Test
	public void testRemove() {
		TickListener tl = mock(TickListener.class);
		tp.attachTickListener(tl);
		tp.updateTickListeners();
		verify(tl, times(1)).update(0f);
		
		tp.removeTickListener(tl);
		tp.updateTickListeners();
		verifyNoMoreInteractions(tl);	
	}
}
