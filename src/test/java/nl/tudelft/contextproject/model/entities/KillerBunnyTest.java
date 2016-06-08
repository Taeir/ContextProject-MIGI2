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
	 * Create a fresh bunny for every test.
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
	 * Test if taking damage reduces the health correctly.
	 */
	@Test
	public void testTakeDamage() {
		bunny.getSpatial();
		bunny.setHealth(3);
		bunny.takeDamage(1f);
		assertEquals(2f, bunny.getHealth(), 1e-6);
	}
	
	/**
	 * Tests if loading killer bunnies works properly.
	 */
	@Test
	public void testLoadEntity() {
		KillerBunny bunny = KillerBunny.loadEntity(loadPosition, new String[] {"1", "1", "1", EntityType.KILLER_BUNNY.getName()});

		assertEquals(expectedPosition.add(0, 0.5f, 0), bunny.getLocation());
	}
	
	/**
	 * Tests if loading killer bunnies with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		KillerBunny.loadEntity(loadPosition, new String[3]);
	}
	 
	/**
	 * Test if killing the entity kills it.
	 */
	@Test
	public void testKill() {
		bunny.setHealth(1);
		bunny.takeDamage(2f);
		assertEquals(EntityState.DEAD, bunny.getState());
	}

	@Override
	public EntityType getType() {
		return EntityType.KILLER_BUNNY;
	}
}
