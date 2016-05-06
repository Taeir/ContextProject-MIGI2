package nl.tudelft.contextproject;

import com.jme3.scene.Geometry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VRPlayerTest extends EntityTest{
	private VRPlayer player;

	@Override
	public Entity getEntity() {
		return new VRPlayer();
	}
	
	@Before
	public void setUp() {
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

	@Test
	public void testGetGeometryNull() {
		setupGeometryMock();
		player.getGeometry();
		verify(Main.getInstance(), times(1)).getAssetManager();
	}



}
