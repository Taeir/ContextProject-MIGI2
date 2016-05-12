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
	 * Creates a fresh Bomb for every test.
	 */
	@Before
	public void setUp() {
		setupGeometryMock();
		bomb = new Bomb();
	}

	/**
	 * Test getGeometry().
	 */
	@Test
	public void testGetGeometryNotNull() {
		Geometry mockedGeometry = mock(Geometry.class);
		bomb.setGeometry(mockedGeometry);
		assertEquals(bomb.getGeometry(), mockedGeometry);
	}
	/**
	 * Test getGeometry().
	 */
	@Test
	public void testGetGeometry() {
		Box cube1Mesh = new Box( 1f,1f,1f);
		Geometry geometry = new Geometry("dink", cube1Mesh); 
		bomb.setGeometry(geometry);
		assertEquals(bomb.getGeometry(), geometry);
	}
	/**
	 * Test getSpatial().
	 */
	@Test
	public void testGetSpatialNotNull(){
		Spatial mockedSpatial = mock(Spatial.class);
		bomb.setSpatial(mockedSpatial);
		assertEquals(bomb.getSpatial(), mockedSpatial);
	}

	@Override
	public Drawable getDrawable() {
		// TODO Auto-generated method stub
		return null;
	}


}
