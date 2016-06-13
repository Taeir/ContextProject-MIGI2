package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.Drawable;

/**
 * Abstract test class for the drawable interface.
 */
public abstract class DrawableTest extends TestBase {

	private Drawable drawable;
	
	/**
	 * Getter for a specific instance of Drawable.
	 *
	 * @return
	 * 		a Drawable to test with
	 */
	public abstract Drawable getDrawable();
	
	/**
	 * Method to setup the drawables for a test.
	 */
	private void setupDrawable() {
		drawable = getDrawable();
		Geometry geometry = mock(Geometry.class);
		drawable.setSpatial(geometry);
		when(geometry.getLocalTranslation()).thenReturn(new Vector3f(0, 0, 0));
	}
	
	/**
	 * Ensure that a geometry is generated.
	 */
	@Test
	public void testGetGeometryNotNull_drawable() {
		setupDrawable();
		assertNotNull(drawable.getSpatial());
	}
	
	/**
	 * Test that get geometry always returns the same geometry.
	 */
	@Test
	public void testPersistentGeometry_drawable() {
		setupDrawable();
		Spatial spatial = drawable.getSpatial();
		assertEquals(spatial, drawable.getSpatial());
	}
}
