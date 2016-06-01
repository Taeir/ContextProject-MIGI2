package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class fot the {@link KillerBunny}.
 */
public class KillerBunnyTest extends MovingEnemyTest {

	private KillerBunny bunny;

	/**
	 * Create a fresh instance for every test.
	 */
	@Before
	public void setUp() {
		TestUtil.mockGame();
		bunny = new KillerBunny(new Vector3f());
	}
	
	@Override
	public MovingEntity getEnemy() {
		return bunny;
	}

	/**
	 * Tests if loading killer bunnies works properly.
	 */
	@Test
	public void testLoadEntity() {
		KillerBunny bunny = KillerBunny.loadEntity(loadPosition, new String[] {"1", "1", "1", EntityType.KILLER_BUNNY.getName()});
		
		assertEquals(loadPosition, bunny.getLocation());
	}

	/**
	 * Tests if loading killer bunnies with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		KillerBunny.loadEntity(loadPosition, new String[3]);
	}
}
