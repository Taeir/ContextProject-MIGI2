package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jme3.math.Vector3f;

/**
 * Test class for {@link Torch}.
 */
public class TorchTest extends EntityTest {

	private Torch instance;

	@Override
	public Entity getEntity() {
		return instance;
	}

	@Override
	public EntityType getType() {
		return EntityType.TORCH;
	}
	
	/**
	 * Create a fresh instance for each test.
	 */
	@Before
	public void setUp() {
		instance = new Torch(true);
	}

	/**
	 * Tests if loading torch works properly.
	 */
	@Test
	public void testLoadEntity() {
		Torch torch = Torch.loadEntity(loadPosition, new String[] {"1", "1", "1", EntityType.TORCH.getName(), "true"});
		assertEquals(loadPosition, torch.getLocation());
	}
	
	/**
	 * Tests if creating a lamp spawns a lamp at the correct altitude.
	 */
	@Test
	public void testCeilingLamp() {
		Torch lamp = new Torch(false);
		assertEquals(new Vector3f(0, 5.32f, 0), lamp.getSpatial().getLocalTranslation());	
	}

	/**
	 * Tests if loading torch with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		Torch.loadEntity(loadPosition, new String[4]);
	}

}
