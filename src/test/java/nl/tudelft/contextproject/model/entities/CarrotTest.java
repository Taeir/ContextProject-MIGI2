package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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

}
