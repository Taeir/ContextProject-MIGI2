package nl.tudelft.contextproject;

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

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.verification.NoInteractionsWanted;

/**
 * Abstract test class for the drawable interface.
 */
public abstract class DrawableTest {

	private Drawable dable;
	private Geometry geom;
	
	/**
	 * Getter for a specific instance of Drawable.
	 * @return a Drawable to test with.
	 */
	public abstract Drawable getDrawable();
	
	private void setupDrawable() {
		dable = getDrawable();
		Geometry geom = mock(Geometry.class);
		dable.setGeometry(geom);
		when(geom.getLocalTranslation()).thenReturn(new Vector3f(0, 0, 0));
	}
	
	/**
	 * Ensure that a geometry is generated.
	 */
	@Test
	public void testGetGeometryNotNull() {
		setupDrawable();
		assertNotNull(dable.getGeometry());
	}
	
	/**
	 * Test that get geometry always returns the same geometry.
	 */
	@Test
	public void testPersistentGeometry() {
		setupDrawable();
		Geometry geom = dable.getGeometry();
		assertEquals(geom, dable.getGeometry());
	}
	
	/**
	 * Verify that something is drawn on the Graphics2D when calling mapDraw().
	 */
	@Test
	public void testMapDraw() {	
		setupDrawable();
		Graphics2D g = mock(Graphics2D.class);
		dable.mapDraw(g, 16);
		try {
			verifyZeroInteractions(g);
		} catch (NoInteractionsWanted e) {
			return;
		}
		fail();
	}
	
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
