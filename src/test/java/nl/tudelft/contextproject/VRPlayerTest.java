package nl.tudelft.contextproject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.jme3.scene.Geometry;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the VRPlayer class.
 */
public class VRPlayerTest extends EntityTest {
	private VRPlayer player;

	@Override
	public Entity getEntity() {
		return new VRPlayer();
	}
	
	/**
	 * Setup method.
	 * Creates a fresh player for every test.
	 */
	@Before
	public void setUp() {
		player = new VRPlayer();
	}

	/**
	 * Test if updating the player moves it.
	 * NOTE: moving by 0 is also moving.
	 */
	@Test
	public void testSimpleUpdate() {
		Geometry mockedGeometry = mock(Geometry.class);
		player.setGeometry(mockedGeometry);
		player.simpleUpdate(0.f);
		verify(mockedGeometry, times(1)).move(anyFloat(), anyFloat(), anyFloat());
	}

	/**
	 * Test getGeometry().
	 */
	@Test
	public void testGetGeometryNotNull() {
		Geometry mockedGeometry = mock(Geometry.class);
		player.setGeometry(mockedGeometry);
		assertEquals(player.getGeometry(), mockedGeometry);
	}

	/**
	 * Test if calling getGeometry on an unset geometry creates one.
	 */
	@Test
	public void testGetGeometryNull() {
		setupGeometryMock();
		player.getGeometry();
		verify(Main.getInstance(), times(1)).getAssetManager();
	}



}
