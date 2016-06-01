package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link VoidPlatform}.
 */
public class VoidPlatformTest extends EntityTest {

	private VoidPlatform instance;

	/**
	 * Create a fresh instance for each test.
	 */
	@Before
	public void setUp() {
		instance = new VoidPlatform();
	}

	@Override
	public Entity getEntity() {
		return instance;
	}

	/**
	 * Tests if loading void platforms works properly.
	 */
	@Test
	public void testLoadEntity() {
		VoidPlatform platform = VoidPlatform.loadEntity(loadPosition, new String[] {"1", "1", "1", EntityType.VOID_PLATFORM.getName()});
		
		//Void platforms are spawned slightly higher than indicated
		assertEquals(loadPosition.add(0f, 0.5f, 0f), platform.getLocation());
	}

	/**
	 * Tests if loading void platforms with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		VoidPlatform.loadEntity(loadPosition, new String[3]);
	}
}
