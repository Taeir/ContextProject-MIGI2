package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.*;
import org.junit.Test;

import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link Pitfall}.
 */
public class PitfallTest extends EntityTest {

	/**
	 * Test if the entity is removed after triggering.
	 */
	@Test
	public void testOnTrigger() {
		TestUtil.mockGame();
		Pitfall p = new Pitfall(1);
		p.onTrigger();
		assertEquals(EntityState.DEAD, p.getState());
	}
	
	@Override
	public Entity getEntity() {
		return new Pitfall(1);
	}
	
	/**
	 * Tests if pitfalls properly remember their width.
	 */
	@Test
	public void testGetWidth() {
		Pitfall pit = new Pitfall(2);
		assertEquals(2f, pit.getWidth(), 1E-5);
	}

	/**
	 * Tests if loading pitfalls works properly.
	 */
	@Test
	public void testLoadEntity() {
		Pitfall pit = Pitfall.loadEntity(loadPosition, new String[] {"1", "1", "1", EntityType.PITFALL.getName(), "1.5"});
		
		//Pitfalls are placed slightly lower than the provided height
		assertEquals(loadPosition.subtract(0f, 0.2f, 0f), pit.getLocation());
		assertEquals(1.5f, pit.getWidth(), 1E-5);
	}
	
	/**
	 * Tests if loading pitfalls with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		Pitfall.loadEntity(loadPosition, new String[6]);
	}
}
