package nl.tudelft.contextproject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the Key class.
 */
public class KeyTest extends EntityTest {
	private Key key;

	@Override
	public Entity getEntity() {
		return new Key();
	}
	
	/**
	 * Setup method.
	 * Creates a fresh key for every test.
	 */
	@Before
	public void setUp() {
		setupGeometryMock();
		key = new Key();
	}

	/**
	 * Test if updating the key doesn't make it move.
	 */
	@Test
	public void testSimpleUpdate() {
		Geometry mockedGeometry = mock(Geometry.class);
		key.setGeometry(mockedGeometry);
		key.simpleUpdate(0.f);
		verify(mockedGeometry, times(0)).move(0, 0, 0);
	}

	/**
	 * Test getGeometry().
	 */
	@Test
	public void testGetGeometryNotNull() {
		Geometry mockedGeometry = mock(Geometry.class);
		key.setGeometry(mockedGeometry);
		assertEquals(key.getGeometry(), mockedGeometry);
	}
	/**
	 * Test getGeometry().
	 */
	@Test
	public void testGetGeometry() {
		Box cube1Mesh = new Box(1f, 1f, 1f);
		Geometry geometry = new Geometry("dink", cube1Mesh); 
		key.setGeometry(geometry);
		assertEquals(key.getGeometry(), geometry);
	}
	/**
	 * Test getSpatial().
	 */
	@Test
	public void testGetSpatialNotNull() {
		Spatial mockedSpatial = mock(Spatial.class);
		key.setSpatial(mockedSpatial);
		assertEquals(key.getSpatial(), mockedSpatial);
	}


}
