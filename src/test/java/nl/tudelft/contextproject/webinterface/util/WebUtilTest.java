package nl.tudelft.contextproject.webinterface.util;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.moving.VRPlayer;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.util.Vec2I;
import nl.tudelft.contextproject.webinterface.Action;
import nl.tudelft.contextproject.webinterface.Team;
import nl.tudelft.contextproject.webinterface.WebClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for the WebUtil class.
 */
public class WebUtilTest extends TestBase {
	private Game mockedGame;
	private Level mockedLevel;
	private Vector3f zeroVector;
	private VRPlayer mockedPlayer;

	/**
	 * Set up all needed fields for testing.
	 */
	@Before
	public void setUp() {
		mockedGame = mock(Game.class);
		mockedLevel = mock(Level.class);
		mockedPlayer = mock(VRPlayer.class);

		zeroVector = new Vector3f(0.f, 0.f, 0.f);

		when(Main.getInstance().getCurrentGame()).thenReturn(mockedGame);
		when(mockedGame.getLevel()).thenReturn(mockedLevel);
	}

	/**
	 * Test for checking if an action is valid.
	 */
	@Test
	public void testCheckValidAction() {
		assertTrue(WebUtil.checkValidAction(Action.DROPBAIT, Team.ELVES));
		assertTrue(WebUtil.checkValidAction(Action.PLACEBOMB, Team.DWARFS));
		assertFalse(WebUtil.checkValidAction(Action.PLACEMINE, Team.NONE));
	}

	/**
	 * Test for checking if placing in the void is not a valid location.
	 */
	@Test
	public void testCheckValidLocationNull() {
		when(mockedLevel.getTile(0, 0)).thenReturn(null);
		assertFalse(WebUtil.checkValidLocation(new Vec2I(0, 0), Action.PLACEBOMB));
	}

	/**
	 * Test for checking if placing in a wall is not a valid location.
	 */
	@Test
	public void testCheckValidLocationWall() {
		MazeTile tile = new MazeTile(0, 0, TileType.WALL);
		when(mockedLevel.getTile(0, 0)).thenReturn(tile);
		assertFalse(WebUtil.checkValidLocation(new Vec2I(0, 0), Action.PLACEBOMB));
	}

	/**
	 * Test for checking if placing in an entity is not a valid location.
	 */
	@Test
	public void testCheckValidLocationEntity() {
		MazeTile tile = new MazeTile(0, 0, TileType.FLOOR);
		when(mockedLevel.getTile(0, 0)).thenReturn(tile);

		Entity mockedEntity = mock(Entity.class);
		when(mockedEntity.getLocation()).thenReturn(zeroVector);

		Set<Entity> entities = new HashSet<>();
		entities.add(mockedEntity);

		when(mockedGame.getEntities()).thenReturn(entities);

		assertFalse(WebUtil.checkValidLocation(new Vec2I(0, 0), Action.PLACEBOMB));
	}

	/**
	 * Test if placing inside of the player radius is invalid.
	 */
	@Test
	public void testCheckRadiusInvalid() {
		Set<Entity> entities = new HashSet<>();
		when(mockedGame.getEntities()).thenReturn(entities);

		when(mockedGame.getPlayer()).thenReturn(mockedPlayer);
		when(mockedPlayer.getLocation()).thenReturn(zeroVector);

		assertFalse(WebUtil.checkOutsideRadius(0, 0, Action.SPAWNENEMY));
	}

	/**
	 * Check if placing outside of the player radius is valid.
	 */
	@Test
	public void testCheckRadiusValid() {
		Set<Entity> entities = new HashSet<>();
		when(mockedGame.getEntities()).thenReturn(entities);

		when(mockedGame.getPlayer()).thenReturn(mockedPlayer);
		when(mockedPlayer.getLocation()).thenReturn(zeroVector);

		assertTrue(WebUtil.checkOutsideRadius(Action.SPAWNENEMY.getRadius() + 1, Action.SPAWNENEMY.getRadius() + 1, Action.SPAWNENEMY));
	}

	/**
	 * Check if placing on a valid location is indeed a valid location.
	 */
	@Test
	public void testCheckValidLocationValidVoid() {
		when(mockedLevel.getTile(0, 0)).thenReturn(null);
		Set<Entity> entities = new HashSet<>();
		when(mockedGame.getEntities()).thenReturn(entities);

		when(mockedGame.getPlayer()).thenReturn(mockedPlayer);
		Vector3f oneVector = new Vector3f(1, 1, 1);
		when(mockedPlayer.getLocation()).thenReturn(oneVector);

		assertTrue(WebUtil.checkValidLocation(new Vec2I(0, 0), Action.PLACETILE));
	}

	/**
	 * Check if we are allowed to place according to the cooldown.
	 */
	@Test
	public void testCheckWithinCooldownTrue() {
		WebClient mockedClient = mock(WebClient.class);

		Map<Action, List<Long>> performedActions = new HashMap<>();
		List<Long> timestamps = new ArrayList<>();
		performedActions.put(Action.PLACEBOMB, timestamps);

		when(mockedClient.getTeam()).thenReturn(Team.DWARFS);
		when(mockedClient.getPerformedActions()).thenReturn(performedActions);

		assertTrue(WebUtil.checkWithinCooldown(Action.PLACEBOMB, mockedClient));
		assertEquals(1, timestamps.size());
	}

	/**
	 * Check if we are not allowed to place according to the cooldown.
	 */
	@Test
	public void testCheckWithinCooldownFalse() {
		WebClient mockedClient = mock(WebClient.class);

		Map<Action, List<Long>> performedActions = new HashMap<>();
		List<Long> timestamps = new ArrayList<>();

		for (int i = 0; i < Action.PLACEBOMB.getMaxAmount(); i++) {
			timestamps.add(Long.MAX_VALUE);
		}

		performedActions.put(Action.PLACEBOMB, timestamps);

		when(mockedClient.getTeam()).thenReturn(Team.DWARFS);
		when(mockedClient.getPerformedActions()).thenReturn(performedActions);

		assertFalse(WebUtil.checkWithinCooldown(Action.PLACEBOMB, mockedClient));
		assertEquals(Action.PLACEBOMB.getMaxAmount(), timestamps.size());
	}

	/**
	 * Check if we are allowed to place according to the cooldown, while there is already an
	 * item in our list.
	 */
	@Test
	public void testCheckWithinCooldownTrueRemoved() {
		WebClient mockedClient = mock(WebClient.class);

		Map<Action, List<Long>> performedActions = new HashMap<>();
		List<Long> timestamps = new ArrayList<>();

		timestamps.add(0L);

		performedActions.put(Action.PLACEBOMB, timestamps);

		when(mockedClient.getTeam()).thenReturn(Team.DWARFS);
		when(mockedClient.getPerformedActions()).thenReturn(performedActions);

		assertTrue(WebUtil.checkWithinCooldown(Action.PLACEBOMB, mockedClient));
		assertEquals(1, timestamps.size());
	}
	
	/**
	 * Checks if we are allowed to place a bomb within the player radius.
	 */
	@Test
	public void testWithinplayerRadius() {
		MazeTile tile = new MazeTile(2, 2, TileType.FLOOR);
		Set<Entity> entities = new HashSet<>();
		Vector3f oneVector = new Vector3f(1, 1, 1);
		
		when(mockedLevel.getTile(2, 2)).thenReturn(tile);
		when(mockedGame.getEntities()).thenReturn(entities);
		when(mockedGame.getPlayer()).thenReturn(mockedPlayer);
		when(mockedPlayer.getLocation()).thenReturn(oneVector);
		
		assertFalse(WebUtil.checkValidLocation(new Vec2I(1 + Action.PLACEMINE.getRadius(), 1), Action.PLACEMINE));
	}
}
