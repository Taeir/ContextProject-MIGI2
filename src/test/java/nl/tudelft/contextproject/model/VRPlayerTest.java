package nl.tudelft.contextproject.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.scene.Geometry;

import nl.tudelft.contextproject.Main;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the VRPlayer class.
 */
public class VRPlayerTest extends EntityTest {
	
	private static final double EPSILON = 1e-5;
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

	//TODO This method should be tested after VR support is added as it will change!
	/**
	 * Test if updating the player moves it.
	 * NOTE: moving by 0 is also moving.
	 */
//	@Test
//	public void testSimpleUpdate() {
//		Geometry mockedGeometry = mock(Geometry.class);
//		CharacterControl mockedCharacterControl = mock(CharacterControl.class);
//		player.setSpatial(mockedGeometry);
//		player.setCharacterControl(mockedCharacterControl);
//		player.update(0.f);
//		verify(mockedGeometry, times(1)).move(anyFloat(), anyFloat(), anyFloat());
//	}

	/**
	 * Test getGeometry().
	 */
	@Test
	public void testGetGeometryNotNull() {
		Geometry mockedGeometry = mock(Geometry.class);
		player.setSpatial(mockedGeometry);
		assertEquals(player.getSpatial(), mockedGeometry);
	}

	/**
	 * Test if calling getGeometry on an unset geometry creates one.
	 */
	@Test
	public void testGetGeometryNull() {
		setupGeometryMock();
		player.getSpatial();
		verify(Main.getInstance(), times(1)).getAssetManager();
	}

	/**
	 * Test if the spatial is an instance of CharacterControl.
	 */
	@Test
	public void testGetSpatielInstance() {
		setupGeometryMock();
		assertTrue(player.getPhysicsObject() instanceof CharacterControl);
	}
	
	/**
	 * Test if the spatial is an instance of fall speed is set correctly.
	 */
	@Test
	public void testGetSpatialCheckFallspeed() {
		setupGeometryMock();
		Object ob = player.getPhysicsObject();
		if (ob instanceof CharacterControl) {
			CharacterControl playerControl = (CharacterControl) ob;
			assertEquals(playerControl.getFallSpeed(), VRPlayer.FALL_SPEED, EPSILON);
		} else {
			fail();
		}
	}

	/**
	 * Test if the spatial is an instance of jump speed is set correctly.
	 */
	@Test
	public void testGetSpatialCheckJumpSpeed() {
		setupGeometryMock();
		Object ob = player.getPhysicsObject();
		if (ob instanceof CharacterControl) {
			CharacterControl playerControl = (CharacterControl) ob;
			assertEquals(playerControl.getJumpSpeed(), VRPlayer.JUMP_SPEED, EPSILON);
		} else {
			fail();
		}
	}
	
	/**
	 * Test if the spatial is an instance of jump speed is set correctly.
	 */
	@Test
	public void testGetSpatialCheckGravity() {
		setupGeometryMock();
		Object ob = player.getPhysicsObject();
		if (ob instanceof CharacterControl) {
			CharacterControl playerControl = (CharacterControl) ob;
			assertEquals(playerControl.getGravity(), VRPlayer.PLAYER_GRAVITY, EPSILON);
		} else {
			fail();
		}
	}
}
