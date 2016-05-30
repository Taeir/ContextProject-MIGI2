package nl.tudelft.contextproject.util.webinterface;

import com.jme3.math.Vector3f;
import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.webinterface.Action;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
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
	 * Test for decoding actions.
	 */
	@Test
	public void testDecode() {
		assertEquals(WebUtil.decodeAction(0), Action.PLACEBOMB);
		assertEquals(WebUtil.decodeAction(1), Action.PLACEPITFALL);
		assertEquals(WebUtil.decodeAction(2), Action.PLACEMINE);
		assertEquals(WebUtil.decodeAction(3), Action.SPAWNENEMY);
		assertEquals(WebUtil.decodeAction(4), Action.DROPBAIT);
	}

	/**
	 * Test for checking if an action is valid.
	 */
	@Test
	public void testCheckValidAction() {
		assertTrue(WebUtil.checkValidAction(Action.DROPBAIT, "Elves"));
		assertTrue(WebUtil.checkValidAction(Action.PLACEBOMB, "Dwarfs"));
		assertFalse(WebUtil.checkValidAction(Action.PLACEMINE, "hax0r"));
	}

	/**
	 * Test for checking if an action is valid as an elf.
	 */
	@Test
	public void testCheckValidElves() {
		assertTrue(WebUtil.checkValidElves(Action.DROPBAIT));
		assertFalse(WebUtil.checkValidElves(Action.PLACEBOMB));
	}

	/**
	 * Test for checking if an action is valid as a dwarf.
	 */
	@Test
	public void testCheckValidDwarfs() {
		assertTrue(WebUtil.checkValidDwarfs(Action.PLACEBOMB));
		assertTrue(WebUtil.checkValidDwarfs(Action.PLACEPITFALL));
		assertTrue(WebUtil.checkValidDwarfs(Action.PLACEMINE));
		assertTrue(WebUtil.checkValidDwarfs(Action.SPAWNENEMY));
		assertFalse(WebUtil.checkValidDwarfs(Action.DROPBAIT));
	}

	/**
	 * Test for checking if placing in the void is not a valid location.
	 */
	@Test
	public void testCheckValidLocationNull() {
		when(mockedLevel.getTile(0, 0)).thenReturn(null);
		assertFalse(WebUtil.checkValidLocation(0, 0));
	}

	/**
	 * Test for checking if placing in a wall is not a valid location.
	 */
	@Test
	public void testCheckValidLocationWall() {
		MazeTile tile = new MazeTile(0, 0, TileType.WALL);
		when(mockedLevel.getTile(0, 0)).thenReturn(tile);
		assertFalse(WebUtil.checkValidLocation(0, 0));
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

		assertFalse(WebUtil.checkValidLocation(0, 0));
	}

	/**
	 * Test for checking if placing in the player is not a valid location.
	 */
	@Test
	public void testCheckValidLocationPlayer() {
		MazeTile tile = new MazeTile(0, 0, TileType.FLOOR);
		when(mockedLevel.getTile(0, 0)).thenReturn(tile);
		Set<Entity> entities = new HashSet<>();
		when(mockedGame.getEntities()).thenReturn(entities);

		when(mockedGame.getPlayer()).thenReturn(mockedPlayer);
		when(mockedPlayer.getLocation()).thenReturn(zeroVector);

		assertFalse(WebUtil.checkValidLocation(0, 0));
	}

	/**
	 * Check if placing on a valid location is indeed a valid location.
	 */
	@Test
	public void testCheckValidLocationValid() {
		MazeTile tile = new MazeTile(0, 0, TileType.FLOOR);
		when(mockedLevel.getTile(0, 0)).thenReturn(tile);
		Set<Entity> entities = new HashSet<>();
		when(mockedGame.getEntities()).thenReturn(entities);


		when(mockedGame.getPlayer()).thenReturn(mockedPlayer);
		Vector3f oneVector = new Vector3f(1, 1, 1);
		when(mockedPlayer.getLocation()).thenReturn(oneVector);

		assertTrue(WebUtil.checkValidLocation(0, 0));
	}
}
