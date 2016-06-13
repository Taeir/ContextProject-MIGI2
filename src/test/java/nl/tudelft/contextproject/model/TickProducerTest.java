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
	
	private TickProducer tickProducer;

	/**
	 * Create a fresh instance of {@link TickProducer} for each test.
	 */
	@Before
	public void setUp() {
		tickProducer = new TickProducer() {
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
		TickListener tickListener = mock(TickListener.class);
		tickProducer.attachTickListener(tickListener);
		tickProducer.updateTickListeners();
		verify(tickListener, times(1)).update(0f);
	}
	
	/**
	 * Test calling the update method using a tpf value.
	 */
	@Test
	public void testUpdate() {
		TickListener tickListener = mock(TickListener.class);
		tickProducer.attachTickListener(tickListener);
		tickProducer.updateTickListeners(2f);
		verify(tickListener, times(1)).update(2f);
	}

	/**
	 * Test if removing a {@link TickListener} does not keep updating it.
	 */
	@Test
	public void testRemove() {
		TickListener tickListener = mock(TickListener.class);
		tickProducer.attachTickListener(tickListener);
		tickProducer.updateTickListeners();
		verify(tickListener, times(1)).update(0f);
		
		tickProducer.removeTickListener(tickListener);
		tickProducer.updateTickListeners();
		verifyNoMoreInteractions(tickListener);	
	}
}
