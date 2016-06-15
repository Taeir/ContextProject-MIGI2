package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link Crate}.
 */
public class CrateTest extends EntityTest {

	private Crate instance;

	/**
	 * Create a fresh instance for each test.
	 */
	@Before
	public void setUp() {
		instance = new Crate();
	}

	@Override
	public Entity getEntity() {
		return instance;
	}

	@Override
	public EntityType getType() {
		return EntityType.CRATE;
	}

	/**
	 * Test if taking damage reduces the health correctly.
	 */
	@Test
	public void testTakeDamage() {
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
	
	/**
	 * Tests if loading crates works properly.
	 */
	@Test
	public void testLoadEntity() {
		Crate crate = Crate.loadEntity(loadPosition, new String[] {"1", "1", "1", EntityType.CRATE.getName(), "12.3"});
		
		assertEquals(loadPosition.add(0, 0.5f, 0), crate.getLocation());
		assertEquals(12.3f, crate.getHealth(), 1E-8);
	}

	/**
	 * Tests if loading void platforms with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		Crate.loadEntity(loadPosition, new String[3]);
	}
	
	/**
	 * Test the pickUp method.
	 */
	@Test
	public void testPickUp() {
		instance.pickUp();
		
		assertTrue(instance.isPickedUp());		
	}
	
	/**
	 * Test if dropping also throws the crate.
	 */
	@Test
	public void testDrop() {
		instance.pickUp();
		instance.drop();
		
		assertFalse(instance.isPickedUp());	
		RigidBodyControl rigidBody = ((RigidBodyControl) instance.getPhysicsObject());
		assertEquals(new Vector3f(0, 6, 4), rigidBody.getLinearVelocity());
	}
	
	/**
	 * Check if the object location is correct when holding.
	 */
	@Test
	public void testHoldLocation() {
		TestUtil.mockGame();
		instance.pickUp();
		instance.getPhysicsObject();
		instance.update(0);
		
		assertEquals(new Vector3f(0, .5f, 0), instance.getLocation());
	}
}
