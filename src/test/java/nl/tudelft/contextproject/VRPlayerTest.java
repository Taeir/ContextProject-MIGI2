package nl.tudelft.contextproject;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.material.MatParam;
import com.jme3.material.MaterialDef;
import com.jme3.scene.Geometry;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VRPlayerTest extends EntityTest{

	private Entity entity;
	private VRPlayer player;

	@Override
	public Entity getEntity() {
		return new VRPlayer();
	}
	
	@Before
	public void setUp() {
		entity = getEntity();
		player = new VRPlayer();
	}

	@Test
	public void testSimpleUpdate() {
		Geometry mockedGeometry = mock(Geometry.class);
		player.setGeometry(mockedGeometry);
		player.simpleUpdate(0.f);
		verify(mockedGeometry, times(1)).move(anyFloat(), anyFloat(), anyFloat());
	}

	@Test
	public void testGetGeometryNotNull() {
		Geometry mockedGeometry = mock(Geometry.class);
		player.setGeometry(mockedGeometry);
		assertEquals(player.getGeometry(), mockedGeometry);
	}

	@Test @SuppressWarnings("unchecked")
	public void testGetGeometryNull() {
		Main mockedMain = mock(Main.class);
		AssetManager mockedAssetManager = mock(AssetManager.class);
		MaterialDef mockedMaterialDef = mock(MaterialDef.class);
		MatParam mockedMatParam = mock(MatParam.class);
		Main.setInstance(mockedMain);
		when(mockedMain.getAssetManager()).thenReturn(mockedAssetManager);
		when(mockedAssetManager.loadAsset(any(AssetKey.class))).thenReturn(mockedMaterialDef);
		when(mockedMaterialDef.getMaterialParams()).thenReturn(new ArrayList<>());
		when(mockedMaterialDef.getMaterialParam(anyString())).thenReturn(mockedMatParam);
		player.getGeometry();
		verify(mockedMain, times(1)).getAssetManager();
	}



}
