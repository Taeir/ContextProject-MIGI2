package nl.tudelft.contextproject.util.webinterface;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Pitfall;
import nl.tudelft.contextproject.model.entities.Carrot;
import nl.tudelft.contextproject.model.entities.Crate;
import nl.tudelft.contextproject.model.entities.Gate;
import nl.tudelft.contextproject.model.entities.LandMine;
import nl.tudelft.contextproject.model.entities.KillerBunny;
import nl.tudelft.contextproject.model.entities.VoidPlatform;
import nl.tudelft.contextproject.test.TestUtil;
import nl.tudelft.contextproject.webinterface.Action;

/**
 * Test for the ActionUtil class.
 */
public class ActionUtilTest extends TestBase {
	private Game mockedGame;

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
		ActionUtil.perform(Action.PLACEBOMB, 0, 0);
		verify(Main.getInstance(), times(1)).getCurrentGame();
		verify(mockedGame, times(1)).addEntity(any(Bomb.class));
	}

	/**
	 * Test the placepitfall action.
	 */
	@Test
	public void testPerformPlacePitfall() {
		ActionUtil.perform(Action.PLACEPITFALL, 0, 0);
		verify(Main.getInstance(), times(1)).getCurrentGame();
		verify(mockedGame, times(1)).addEntity(any(Pitfall.class));
	}

	/**
	 * Test the placemine action.
	 */
	@Test
	public void testPerformPlaceMine() {
		ActionUtil.perform(Action.PLACEMINE, 0, 0);
		verify(Main.getInstance(), times(1)).getCurrentGame();
		verify(mockedGame, times(1)).addEntity(any(LandMine.class));
	}

	/**
	 * Test the spawnenemy action.
	 */
	@Test
	public void testPerformSpawnEnemy() {
		ActionUtil.perform(Action.SPAWNENEMY, 0, 0);
		verify(mockedGame, times(1)).addEntity(any(KillerBunny.class));
	}

	/**
	 * Test the dropbait action.
	 */
	@Test
	public void testPerformDropBait() {
		ActionUtil.perform(Action.DROPBAIT, 0, 0);
		verify(Main.getInstance(), times(1)).getCurrentGame();
		verify(mockedGame, times(1)).addEntity(any(Carrot.class));
	}

	/**
	 * Test the PlaceTile action.
	 */
	@Test
	public void testPerformPlaceTile() {
		ActionUtil.perform(Action.PLACETILE, 0, 0);
		verify(Main.getInstance(), times(1)).getCurrentGame();
		verify(mockedGame, times(1)).addEntity(any(VoidPlatform.class));
	}
	
	/**
	 * Test the DropCrate action.
	 */
	@Test
	public void testPerformDropCrate() {
		ActionUtil.perform(Action.DROPCRATE, 0, 0);
		verify(Main.getInstance(), times(1)).getCurrentGame();
		verify(mockedGame, times(1)).addEntity(any(Crate.class));
	}
	
	/**
	 * Tests the OpenGate action.
	 */
	@Test
	public void testPerformOpenGate() {
		TestUtil.mockGame();
		
		Gate gate = mock(Gate.class);
		when(gate.getLocation()).thenReturn(new Vector3f());
		
		Main.getInstance().getCurrentGame().addEntity(gate);
		ActionUtil.perform(Action.OPENGATE, 0, 0);
		verify(gate, atLeastOnce()).openGate();
	}
}
