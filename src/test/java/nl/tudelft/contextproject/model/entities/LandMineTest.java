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

	@Override
	public EntityType getType() {
		return EntityType.LANDMINE;
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

	/**
	 * Tests if loading landmines works properly.
	 */
	@Test
	public void testLoadEntity() {
		LandMine mine = LandMine.loadEntity(loadPosition, new String[] {"1", "1", "1", EntityType.LANDMINE.getName()});
		
		//Landmines are placed a bit higher than indicated when loaded
		assertEquals(loadPosition.add(0f, 0.505f, 0f), mine.getLocation());
	}
	
	/**
	 * Tests if loading landmines with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		LandMine.loadEntity(loadPosition, new String[3]);
	}
}
