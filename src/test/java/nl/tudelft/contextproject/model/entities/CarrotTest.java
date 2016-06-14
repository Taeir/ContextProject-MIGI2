package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;

/**
 * Test class for the {@link Carrot} class.
 */
public class CarrotTest extends EntityTest {

	private Carrot carrot;

	/**
	 * Create a fresh carrot for each test.
	 */
	@Before
	public void setUp() {
		carrot = new Carrot();
	}

	@Override
	public Entity getEntity() {
		return carrot;
	}

	@Override
	public EntityType getType() {
		return EntityType.CARROT;
	}
	
	/**
	 * Test eating from the carrot.
	 */
	@Test
	public void testEat() {
		float total = carrot.getAmount();
		carrot.eat(.2f);
		assertEquals(total - .2f, carrot.getAmount(), 1e-8);
	}
	
	/**
	 * Test eating the whole carrot.
	 */
	@Test
	public void testEatAll() {
		float total = carrot.getAmount();
		carrot.eat(total + 1f);
		assertEquals(EntityState.DEAD, carrot.getState());
	}

	/**
	 * Tests if loading carrots works properly.
	 */
	@Test
	public void testLoadEntity() {
		Carrot carrot = Carrot.loadEntity(loadPosition, new String[]{"1", "1", "1", EntityType.CARROT.getName()});
		
		assertEquals(expectedPosition.add(0, 0.5f, 0), carrot.getLocation());
	}
	
	/**
	 * Tests if loading carrots with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		Carrot.loadEntity(loadPosition, new String[3]);
	}
}
