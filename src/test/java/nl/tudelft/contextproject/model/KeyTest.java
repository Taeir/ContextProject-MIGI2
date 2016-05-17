package nl.tudelft.contextproject.model;

import static org.mockito.Mockito.*;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the Key class.
 */
public class KeyTest extends EntityTest {
	private Key key;

	@Override
	public Entity getEntity() {
		ColorRGBA color = new ColorRGBA();
		color.set(ColorRGBA.Yellow);
		return new Key(color, 0, 0, 0);
	}
	
	/**
	 * Setup method.
	 * Creates a fresh key for every test.
	 */
	@Before
	public void setUp() {
		setupGeometryMock();
		ColorRGBA color = new ColorRGBA();
		color.set(ColorRGBA.Yellow);
		key = new Key(color, 0, 0, 0);
		key.move(1, 1, 1);
	}

	/**
	 * Test if updating the key doesn't make it move.
	 */
	@Test
	public void testUpdate() {
		Geometry mockedGeometry = mock(Geometry.class);
		key.setSpatial(mockedGeometry);
		key.update(0.f);
		verifyZeroInteractions(mockedGeometry);
	}
	/**
	 * Test if the move method works
	 */
}
