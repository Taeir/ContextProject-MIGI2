package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.ColorRGBA;
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
	public void testGetSpatialInstance() {
		setupGeometryMock();
		assertNotNull(player.getPhysicsObject());
	}

	/**
	 * Test if the spatial is an instance of fall speed is set correctly.
	 */
	@Test
	public void testGetSpatialCheckFallspeed() {
		setupGeometryMock();
		Object ob = player.getPhysicsObject();
		CharacterControl playerControl = (CharacterControl) ob;
		assertEquals(playerControl.getFallSpeed(), VRPlayer.FALL_SPEED, EPSILON);
	}

	/**
	 * Test if the spatial is an instance of jump speed is set correctly.
	 */
	@Test
	public void testGetSpatialCheckJumpSpeed() {
		setupGeometryMock();
		Object ob = player.getPhysicsObject();
		CharacterControl playerControl = (CharacterControl) ob;
		assertEquals(playerControl.getJumpSpeed(), VRPlayer.JUMP_SPEED, EPSILON);
	}

	/**
	 * Test if the spatial is an instance of jump speed is set correctly.
	 */
	@Test
	public void testGetSpatialCheckGravity() {
		setupGeometryMock();
		Object ob = player.getPhysicsObject();
		CharacterControl playerControl = (CharacterControl) ob;
		assertEquals(playerControl.getGravity(), VRPlayer.PLAYER_GRAVITY, EPSILON);
	}

	/**
	 * Tests that the dropbomb method removes a bomb from your inventory.
	 */
	@Test 
	public void testDropBomb() {
		setupGeometryMock();
		player.getInventory().add(new Bomb());
		player.dropBomb();
		assertSame(player.getInventory().size(), 0);
	}

	/**
	 * tests that the dropbomb method doesn't remove a bomb when there is none.
	 */
	@Test
	public void testDropNoBomb() {
		setupGeometryMock();
		player.getInventory().add(new Key(ColorRGBA.Yellow));
		player.dropBomb();
		assertSame(player.getInventory().size(), 1);
	}
}
