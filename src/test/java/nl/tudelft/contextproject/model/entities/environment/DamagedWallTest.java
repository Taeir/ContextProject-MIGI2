package nl.tudelft.contextproject.model.entities.environment;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.EntityTest;
import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;

/**
 * Test class for {@link DamagedWall}.
 */
public class DamagedWallTest extends EntityTest {

	private DamagedWall instance;

	/**
	 * Create a fresh instance for every test.
	 */
	@Before
	public void setUp() {
		instance = new DamagedWall();
	}

	@Override
	public Entity getEntity() {
		return instance;
	}
	
	/**
	 * Test if taking damage reduces the health correctly.
	 */
	@Test
	public void testTakeDamage() {
		instance.getSpatial();
		instance.setHealth(1);
		instance.takeDamage(.6f);
		assertEquals(.4f, instance.getHealth(), 1e-6);
	}
	
	/**
	 * Test if killing the entity kills it.
	 */
	@Test
	public void testKill() {
		instance.setHealth(1);
		instance.takeDamage(2f);
		assertEquals(EntityState.DEAD, instance.getState());
	}

	@Override
	public EntityType getType() {
		return EntityType.DAMAGED_WALL;
	}
}
