package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link LandMine}.
 */
public class LandMineTest extends EntityTest {

	private LandMine mine;

	/**
	 * Create a fresh instance for each test.
	 */
	@Before
	public void setUp() {
		mine = new LandMine();
	}

	@Override
	public Entity getEntity() {
		return mine;
	}
	
	/**
	 * Test if triggering removes the entity.
	 */
	@Test
	public void testExplosionState() {
		TestUtil.mockGame();
		mine.onTrigger();
		assertEquals(EntityState.DEAD, mine.getState());
	}

}
