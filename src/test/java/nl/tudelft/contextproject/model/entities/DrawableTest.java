package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.material.MatParam;
import com.jme3.material.MaterialDef;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.Drawable;
import org.junit.Test;
import org.mockito.exceptions.verification.NoInteractionsWanted;

/**
 * Abstract test class for the drawable interface.
 */
public abstract class DrawableTest extends TestBase {

	private Drawable dable;
	
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
		dable = getDrawable();
		Geometry geom = mock(Geometry.class);
		dable.setSpatial(geom);
		when(geom.getLocalTranslation()).thenReturn(new Vector3f(0, 0, 0));
	}
	
	/**
	 * Ensure that a geometry is generated.
	 */
	@Test
	public void testGetGeometryNotNull_drawable() {
		setupDrawable();
		assertNotNull(dable.getSpatial());
	}
	
	/**
	 * Test that get geometry always returns the same geometry.
	 */
	@Test
	public void testPersistentGeometry_drawable() {
		setupDrawable();
		Spatial s = dable.getSpatial();
		assertEquals(s, dable.getSpatial());
	}
	
	/**
	 * Verify that something is drawn on the Graphics2D when calling mapDraw().
	 */
	@Test
	public void testMapDraw_drawable() {	
		setupDrawable();
		Graphics2D g = mock(Graphics2D.class);
		dable.mapDraw(g, 16);
		
		//Verify that there were interactions
		try {
			verifyZeroInteractions(g);
		} catch (NoInteractionsWanted e) {
			return;
		}
		fail();
	}
	
	/**
	 * Setup the mocks in the Main class.
	 * This enables testing using {@link Drawable#getSpatial()}.
	 */
	@SuppressWarnings("unchecked")
	public void setupGeometryMock() {
		Main mockedMain = mock(Main.class);
		AssetManager mockedAssetManager = mock(AssetManager.class);
		MaterialDef mockedMaterialDef = mock(MaterialDef.class);
		MatParam mockedMatParam = mock(MatParam.class);
		Main.setInstance(mockedMain);
		when(mockedMain.getAssetManager()).thenReturn(mockedAssetManager);
		when(mockedAssetManager.loadAsset(any(AssetKey.class))).thenReturn(mockedMaterialDef);
		when(mockedMaterialDef.getMaterialParams()).thenReturn(new ArrayList<>());
		when(mockedMaterialDef.getMaterialParam(anyString())).thenReturn(mockedMatParam);
	}
}
