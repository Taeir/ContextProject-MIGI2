package nl.tudelft.contextproject.model;

import static org.mockito.Mockito.*;

import com.jme3.scene.Geometry;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the Bomb class.
 */
public class BombTest extends EntityTest {
	private Bomb bomb;

	@Override
	public Entity getEntity() {
		return new Bomb(0, 0, 0);
	}
	
	/**
	 * Setup method.
	 * Creates a fresh bomb for every test.
	 */
	@Before
	public void setUp() {
		setupGeometryMock();
		bomb = new Bomb(0, 0, 0);
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
}
