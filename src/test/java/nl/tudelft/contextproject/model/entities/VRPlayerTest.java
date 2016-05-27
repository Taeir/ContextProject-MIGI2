package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

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
//	/**
//	 * Test if updating the player moves it.
//	 * NOTE: moving by 0 is also moving.
//	 */
//		@Test
//		public void testSimpleUpdate() {
//			Geometry mockedGeometry = mock(Geometry.class);
//			CharacterControl mockedCharacterControl = mock(CharacterControl.class);
//			player.setSpatial(mockedGeometry);
//			player.setCharacterControl(mockedCharacterControl);
//			player.update(0.f);
//			verify(mockedGeometry, times(1)).move(anyFloat(), anyFloat(), anyFloat());
//		}

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
	 * Test if the spatial is an instance of CharacterControl.
	 */
	@Test
	public void testGetSpatialInstance() {
		assertNotNull(player.getPhysicsObject());
	}

	/**
	 * Test if the spatial is an instance of fall speed is set correctly.
	 */
	@Test
	public void testGetSpatialCheckFallspeed() {
		Object ob = player.getPhysicsObject();
		CharacterControl playerControl = (CharacterControl) ob;
		assertEquals(playerControl.getFallSpeed(), VRPlayer.FALL_SPEED, EPSILON);
	}

	/**
	 * Test if the spatial is an instance of jump speed is set correctly.
	 */
	@Test
	public void testGetSpatialCheckJumpSpeed() {
		Object ob = player.getPhysicsObject();
		CharacterControl playerControl = (CharacterControl) ob;
		assertEquals(playerControl.getJumpSpeed(), VRPlayer.JUMP_SPEED, EPSILON);
	}

	/**
	 * Test if the spatial is an instance of jump speed is set correctly.
	 */
	@Test
	public void testGetSpatialCheckGravity() {
		Object ob = player.getPhysicsObject();
		CharacterControl playerControl = (CharacterControl) ob;
		assertEquals(playerControl.getGravity(), VRPlayer.PLAYER_GRAVITY, EPSILON);
	}

	/**
	 * Tests that the dropbomb method removes a bomb from your inventory.
	 */
	@Test 
	public void testDropBomb() {
		player.getInventory().add(new Bomb());
		player.dropBomb();
		assertSame(player.getInventory().size(), 0);
	}

	/**
	 * tests that the dropbomb method doesn't remove a bomb when there is none.
	 */
	@Test
	public void testDropNoBomb() {
		player.getInventory().add(new Key(ColorRGBA.Yellow));
		player.dropBomb();
		assertSame(player.getInventory().size(), 1);
	}
	
	/**
	 * Test if the fallingTimer respawns the player at the correct position.
	 */
	@Test
	public void testUpdateFallingTimer() {
		when(player.getSpatial()).thenReturn(mock(Spatial.class));
		player.move(0, -20, 0);
		player.updateFallingTimer(4);
		player.updateFallingTimer(.5f);
		
		assertEquals(5f, player.getLocation().y, 1e-4);
	}
	
	/**
	 * Tests if the interval updating for exploration works correctly.
	 * 
	 * <p>This means that exploration should only be updated when a certain interval has passed,
	 * instead of every update.
	 */
	@Test
	public void testUpdateExploration_interval() {
		//Create a 1x1 mocked level with a single floor tile
		MazeTile tile = mock1x1Level();
		
		//Mock the player's spatial to the appropriate location
		Spatial spatial = mock(Spatial.class);
		when(spatial.getLocalTranslation()).thenReturn(new Vector3f(0, 2, 0));
		player.setSpatial(spatial);
		
		//When we now call updateExploration with a too low TPF
		player.updateExploration(VRPlayer.EXPLORATION_INTERVAL - 0.1f);
		
		//Then nothing should have happened
		assertFalse(tile.isExplored());
	}
	
	/**
	 * Tests if the exploration works correctly.
	 * 
	 * <p>This means that exploration should update tiles that are within exploration range of the
	 * player.
	 */
	@Test
	public void testUpdateExploration_range() {
		MazeTile tile = mock1x1Level();
		
		//Mock the player's spatial to the appropriate location
		Spatial spatial = mock(Spatial.class);
		when(spatial.getLocalTranslation()).thenReturn(new Vector3f(VRPlayer.EXPLORATION_RADIUS, 2, VRPlayer.EXPLORATION_RADIUS));
		player.setSpatial(spatial);
		
		//When we now call updateExploration
		player.updateExploration(VRPlayer.EXPLORATION_INTERVAL + 0.1f);
		
		//Then the tile should have been marked explored
		assertTrue(tile.isExplored());
	}
	
	/**
	 * Tests if the exploration works correctly.
	 * 
	 * <p>This means that exploration should NOT update tiles that are outside the exploration
	 * range of the player.
	 */
	@Test
	public void testUpdateExploration_outOfRange() {
		MazeTile tile = mock1x1Level();
		
		//Mock the player's spatial to the appropriate location
		Spatial spatial = mock(Spatial.class);
		when(spatial.getLocalTranslation()).thenReturn(new Vector3f(VRPlayer.EXPLORATION_RADIUS + 1, 2, VRPlayer.EXPLORATION_RADIUS + 1));
		player.setSpatial(spatial);
		
		//When we now call updateExploration
		player.updateExploration(VRPlayer.EXPLORATION_INTERVAL + 0.1f);
		
		//Then the tile should not have been marked explored
		assertFalse(tile.isExplored());
	}
	
	/**
	 * Creates a 1x1 mocked level with a single floor tile.
	 * 
	 * @return
	 * 		the one and only MazeTile in the level
	 */
	private MazeTile mock1x1Level() {
		Level level = mock(Level.class);
		when(level.getHeight()).thenReturn(1);
		when(level.getWidth()).thenReturn(1);
		
		MazeTile tile = new MazeTile(0, 0, TileType.FLOOR);
		when(level.getTile(anyInt(), anyInt())).thenReturn(tile);
		
		Game game = new Game(level);
		when(Main.getInstance().getCurrentGame()).thenReturn(game);
		return tile;
	}
}
