package nl.tudelft.contextproject.model;

import static org.mockito.Mockito.*;

import com.jme3.scene.Geometry;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the Door class.
 */
public class DoorTest extends EntityTest {
	private Door door;

	@Override
	public Entity getEntity() {
		return new Door();
	}
	
	/**
	 * Setup method.
	 * Creates a fresh Door for every test.
	 */
	@Before
	public void setUp() {
		setupGeometryMock();
		door = new Door();
	}

	/**
	 * Test if updating the Door doesnt make it move.
	 */
	@Test
	public void testUpdate() {
		Geometry mockedGeometry = mock(Geometry.class);
		door.setSpatial(mockedGeometry);
		door.update(0.f);
		verifyZeroInteractions(mockedGeometry);
	}
}
