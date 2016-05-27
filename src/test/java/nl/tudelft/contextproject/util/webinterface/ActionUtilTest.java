package nl.tudelft.contextproject.util.webinterface;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Pitfall;
import nl.tudelft.contextproject.model.entities.Carrot;
import nl.tudelft.contextproject.model.entities.LandMine;
import nl.tudelft.contextproject.model.entities.KillerBunny;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for the ActionUtil class.
 */
public class ActionUtilTest extends TestBase {
	Game mockedGame;

	/**
	 * Set up all need mocks for testing.
	 */
	@Before
	public void setUp() {
		mockedGame = mock(Game.class);
		when(Main.getInstance().getCurrentGame()).thenReturn(mockedGame);
	}

	/**
	 * Test the placebomb action.
	 */
	@Test
	public void testPerformPlaceBomb() {
		ActionUtil.perform("placebomb", 0, 0);
		verify(Main.getInstance(), times(1)).getCurrentGame();
		verify(mockedGame, times(1)).addEntity(any(Bomb.class));
	}

	/**
	 * Test the placepitfall action.
	 */
	@Test
	public void testPerformPlacePitfall() {
		ActionUtil.perform("placepitfall", 0, 0);
		verify(Main.getInstance(), times(1)).getCurrentGame();
		verify(mockedGame, times(1)).addEntity(any(Pitfall.class));
	}

	/**
	 * Test the placemine action.
	 */
	@Test
	public void testPerformPlaceMine() {
		ActionUtil.perform("placemine", 0, 0);
		verify(Main.getInstance(), times(1)).getCurrentGame();
		verify(mockedGame, times(1)).addEntity(any(LandMine.class));
	}

	/**
	 * Test the spawnenemy action.
	 */
	@Test
	public void testPerformSpawnEnemy() {
		ActionUtil.perform("spawnenemy", 0, 0);
		verify(mockedGame, times(1)).addEntity(any(KillerBunny.class));
	}

	/**
	 * Test the dropbait action.
	 */
	@Test
	public void testPerformDropBait() {
		ActionUtil.perform("dropbait", 0, 0);
		verify(Main.getInstance(), times(1)).getCurrentGame();
		verify(mockedGame, times(1)).addEntity(any(Carrot.class));
	}

	/**
	 * Make sure we thrown an exception if an illegal action occurs.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testDefault() {
		ActionUtil.perform("HackGameAndWinInstantly", 0, 0);
	}
}
