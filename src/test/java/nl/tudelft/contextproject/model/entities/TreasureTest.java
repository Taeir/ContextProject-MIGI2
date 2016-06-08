package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link Treasure}.
 */
public class TreasureTest extends EntityTest {

	private Treasure instance;

	@Override
	public Entity getEntity() {
		return instance;
	}

	@Override
	public EntityType getType() {
		return EntityType.TREASURE;
	}
	
	/**
	 * Create a fresh instance for each test.
	 */
	@Before
	public void setUp() {
		instance = new Treasure();
	}
	
	/**
	 * Test if triggering ends the game.
	 */
	@Test
	public void testOnTrigger() {
		TestUtil.mockGame();
		Game g = Main.getInstance().getCurrentGame();
		instance.onTrigger();
		verify(g, times(1)).endGame(true);
	}

	/**
	 * Tests if loading treasure works properly.
	 */
	@Test
	public void testLoadEntity() {
		Treasure treasure = Treasure.loadEntity(loadPosition, new String[] {"1", "1", "1", EntityType.TREASURE.getName()});
		assertEquals(loadPosition.add(0, 1, 0), treasure.getLocation());
	}

	/**
	 * Tests if loading treasure with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		Treasure.loadEntity(loadPosition, new String[3]);
	}

}
