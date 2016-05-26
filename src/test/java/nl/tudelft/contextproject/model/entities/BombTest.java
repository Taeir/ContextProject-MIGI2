package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.test.TestUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;

/**
 * Test class for the Bomb class.
 */
public class BombTest extends EntityTest {
	private Bomb bomb;

	@Override
	public Entity getEntity() {
		return new Bomb();
	}

	/**
	 * Setup method.
	 * Creates a fresh bomb for every test.
	 */
	@Before
	public void setUp() {
		bomb = new Bomb();
		bomb.move(1, 1, 1);
	}

	/**
	 * Test if updating the bomb doesn't make it move.
	 */
	@Test
	public void testUpdate() {
		Geometry mockedGeometry = mock(Geometry.class);
		bomb.setSpatial(mockedGeometry);
		bomb.update(0.f);
		verifyZeroInteractions(mockedGeometry);
	}

	/**
	 * Tests if activating the bomb sets it to active.
	 */
	@Test
	public void testactive() {
		bomb.activate();
		assertTrue(bomb.getActive());

	}

	/**
	 * Tests if updating an active bomb increases the timer.
	 */
	@Test
	public void testUpdateactive() {
		bomb.activate();
		Spatial mock = mock(Spatial.class);
		bomb.setSpatial(mock);
		when(mock.getLocalScale()).thenReturn(new Vector3f(1, 1, 1));
		when(mock.getLocalTranslation()).thenReturn(new Vector3f(1, 1, 1));
		bomb.update(1);
		assertTrue(bomb.getTimer() == 1);
	}
	/**
	 * Tests if an active bomb disappears after 5 seconds.
	 */
	@Test
	public void testUpdateActiveFiveSeconds() {
		TestUtil.mockGame();
		bomb.activate();
		bomb.update(5.1f);
		assertEquals(EntityState.DEAD, bomb.getState());
	}
}
