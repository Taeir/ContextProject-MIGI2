package nl.tudelft.contextproject.model.entities;

import org.junit.Before;

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

}
