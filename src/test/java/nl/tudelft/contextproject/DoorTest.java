package nl.tudelft.contextproject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the Door class.
 */
public class DoorTest extends DrawableTest {
	private Door door;

	@Override
	public Drawable getDrawable() {
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
	 * Test if updating the Door makes it move by 0.
	 */
	@Test
	public void testSimpleUpdate() {
		Geometry mockedGeometry = mock(Geometry.class);
		door.setGeometry(mockedGeometry);
		door.simpleUpdate(0.f);
		verify(mockedGeometry, times(1)).move(0, 0, 0);
	}

	/**
	 * Test getGeometry().
	 */
	@Test
	public void testGetGeometryNotNull() {
		Geometry mockedGeometry = mock(Geometry.class);
		door.setGeometry(mockedGeometry);
		assertEquals(door.getGeometry(), mockedGeometry);
	}
	@Test
	public void testGetGeometry() {
		Box cube1Mesh = new Box( 1f,1f,1f);
		Geometry geometry = new Geometry("dink", cube1Mesh); 
		door.setGeometry(geometry);
		assertEquals(door.getGeometry(), geometry);
	}
	/**
	 * Test getSpatial().
	 */
	@Test
	public void testGetSpatialNotNull(){
		Spatial mockedSpatial = mock(Spatial.class);
		door.setSpatial(mockedSpatial);
		assertEquals(door.getSpatial(), mockedSpatial);
	}


}
