package nl.tudelft.contextproject.model.entities.moving;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.model.Inventory;
import nl.tudelft.contextproject.model.entities.Key;
import nl.tudelft.contextproject.model.entities.exploding.Bomb;
import nl.tudelft.contextproject.model.entities.util.EntityType;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Test class for the VRPlayer class.
 */
public class VRPlayerTest extends MovingEntityTest {

	private static final double EPSILON = 1e-5;
	private VRPlayer player;
	
	/**
	 * Constructor that ensures that the game is mocked before the tests.
	 */
	public VRPlayerTest() {
		setMockGame(true);
	}

	@Override
	public MovingEntity getEnemy() {
		return new VRPlayer();
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
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
		Object obj = player.getPhysicsObject();
		CharacterControl playerControl = (CharacterControl) obj;
		assertEquals(playerControl.getFallSpeed(), VRPlayer.FALL_SPEED, EPSILON);
	}

	/**
	 * Test if the spatial is an instance of jump speed is set correctly.
	 */
	@Test
	public void testGetSpatialCheckJumpSpeed() {
		Object obj = player.getPhysicsObject();
		CharacterControl playerControl = (CharacterControl) obj;
		assertEquals(playerControl.getJumpSpeed(), VRPlayer.JUMP_SPEED, EPSILON);
	}

	/**
	 * Test if the spatial is an instance of jump speed is set correctly.
	 */
	@Test
	public void testGetSpatialCheckGravity() {
		Object obj = player.getPhysicsObject();
		CharacterControl playerControl = (CharacterControl) obj;
		assertEquals(playerControl.getGravity(), VRPlayer.PLAYER_GRAVITY, EPSILON);
	}

	/**
	 * Tests that the dropbomb method removes a bomb from your inventory.
	 */
	@Test 
	public void testDropBomb() {
		player.getInventory().pickUp(new Bomb());
		player.drop();
		assertSame(player.getInventory().size(), 0);
	}

	/**
	 * tests that the dropbomb method doesn't remove a bomb when there is none.
	 */
	@Test
	public void testDropNoBomb() {
		player.getInventory().add(new Key(ColorRGBA.Yellow));
		player.drop();
		assertSame(player.getInventory().size(), 1);
	}
	
	/**
	 * Test if the fallingTimer respawns the player at the correct position.
	 */
	@Test
	public void testUpdateFallingTimer() {
		player.setSpatial(new Geometry(""));
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
		Spatial playerSpatial = mock(Spatial.class);
		when(playerSpatial.getLocalTranslation()).thenReturn(new Vector3f(0, 2, 0));
		player.setSpatial(playerSpatial);
		
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
		when(spatial.getLocalTranslation()).thenReturn(new Vector3f(VRPlayer.EXPLORATION_RADIUS, VRPlayer.SPAWN_HEIGHT, VRPlayer.EXPLORATION_RADIUS));
		player.setSpatial(spatial);
		
		//When we now call updateExploration
		player.updateExploration(VRPlayer.EXPLORATION_INTERVAL + 0.1f);
		
		//Then the tile should have been marked explored
		assertTrue(tile.isExplored());
	}
	
	/**
	 * Tests if a bomb gets picked up if pickup is pressed.
	 */
	@Test
	public void testPickUpBomb() {
		Bomb bomb = new Bomb();
		Vector3f location = player.getSpatial().getLocalTranslation();
		bomb.move(location.x + 1, location.y, location.z);
		Main.getInstance().getCurrentGame().getEntities().add(bomb);
		player.pickUp();
		assertTrue(bomb.isPickedUp());
	}
	
	/**
	 * Tests wether you pick up a nearby key when pickup is pressed.
	 */
	@Test
	public void testPickUpKey() {
		Key key = new Key(ColorRGBA.Yellow);
		Vector3f location = player.getSpatial().getLocalTranslation();
		key.move(location.x + 1, location.y, location.z);
		Main.getInstance().getCurrentGame().getEntities().add(key);
		player.pickUp();
		player.getInventory().containsKey();
	}
	
	/**
	 * Tests if setting your health does not go over your maxhealth.
	 */
	@Test
	public void testsetHealth() {
		player.setHealth(1 + VRPlayer.PLAYER_MAX_HEALTH);
		assertEquals(VRPlayer.PLAYER_MAX_HEALTH, player.getHealth(), 1e-8);
	}
	
	/**
	 * Tests if changing the inventory changes the inventory.
	 */
	@Test
	public void testsetInventory() {
		Inventory inventory = new Inventory();
		player.setInventory(inventory);
		assertEquals(player.getInventory(), inventory);
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
		when(spatial.getLocalTranslation()).thenReturn(new Vector3f(VRPlayer.EXPLORATION_RADIUS + 1, VRPlayer.SPAWN_HEIGHT, VRPlayer.EXPLORATION_RADIUS + 1));
		player.setSpatial(spatial);
		
		//When we now call updateExploration
		player.updateExploration(VRPlayer.EXPLORATION_INTERVAL + 0.1f);
		
		//Then the tile should not have been marked explored
		assertFalse(tile.isExplored());
	}
	
	/**
	 * Test if damaging the player makes the player take damage.
	 */
	@Test
	public void testTakeDamage() {
		float expectedHeath = player.getHealth() - .2f;
		player.takeDamage(.2f);
		assertEquals(expectedHeath, player.getHealth(), 1e-8);
	}
	
	/**
	 * Test if killing the player ends the game.
	 */
	@Test
	public void testKill() {
		float damage = player.getHealth() + .2f;
		player.takeDamage(damage);
		assertEquals(GameState.ENDED, Main.getInstance().getGameState());
	}
	
	/**
	 * Tests if loading players works properly.
	 */
	@Test
	public void testLoadEntity() {
		VRPlayer player = VRPlayer.loadEntity(loadPosition, new String[] {"1", "1", "1", EntityType.PLAYER.getName()});
		
		//Players are spawned a certain amount higher than where they are spawned in.
		assertEquals(loadPosition.add(0f, VRPlayer.SPAWN_HEIGHT, 0f), player.getLocation());
	}

	/**
	 * Tests if loading players with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		VRPlayer.loadEntity(loadPosition, new String[3]);
	}
	
	/**
	 * Creates a 1x1 mocked level with a single floor tile.
	 * 
	 * @return
	 * 		the one and only MazeTile in the level
	 */
	private MazeTile mock1x1Level() {
		Level level = Main.getInstance().getCurrentGame().getLevel();
		
		MazeTile tile = new MazeTile(0, 0, TileType.FLOOR);
		when(level.getTile(anyInt(), anyInt())).thenReturn(tile);
		return tile;
	}	
}
