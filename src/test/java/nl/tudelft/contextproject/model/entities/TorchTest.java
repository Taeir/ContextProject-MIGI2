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
		assertEquals(new Vector3f(0.95f, 1.0f, 0.95f), torch.getLocation());
	}
	
	/**
	 * Tests if loading lamp works properly.
	 */
	@Test
	public void testLoadEntityLamp() {
		Torch torch = Torch.loadEntity(loadPosition, new String[] {"1", "1", "1", EntityType.TORCH.getName(), "false"});
		assertEquals(new Vector3f(0.95f, 1.0f, 0.95f), torch.getLocation());
	}
	
	/**
	 * Tests if creating a lamp spawns a lamp at the correct altitude.
	 */
	@Test
	public void testCeilingLamp() {
		Torch lamp = new Torch(false);
		assertEquals(new Vector3f(-0.05f, 0, -0.05f), lamp.getSpatial().getLocalTranslation());	
	}
	
	/**
	 * Test if rotating south repositions the torch accordingly.
	 */
	@Test
	public void testRotateSouth() {
		instance.rotateSouth();
		assertEquals(new Vector3f(-0.09f, 0.27f, -0.003f), instance.getFire().getLocalTranslation());	
	}

	/**
	 * Test if rotating east repositions the torch accordingly.
	 */
	@Test
	public void testRotateEast() {
		instance.rotateEast();
		assertEquals(new Vector3f(-0.09f, 0.27f, -0.003f), instance.getFire().getLocalTranslation());	
	}
	
	/**
	 * Test if rotating North repositions the torch accordingly.
	 */
	@Test
	public void testRotateNorth() {
		instance.rotateNorth();
		assertEquals(new Vector3f(-0.09f, 0.27f, -0.003f), instance.getFire().getLocalTranslation());	
	}
	
	/**
	 * Test if rotating south repositions the torch accordingly.
	 */
	@Test
	public void testRotateWest() {
		instance.rotateWest();
		assertEquals(new Vector3f(-0.09f, 0.27f, -0.003f), instance.getFire().getLocalTranslation());	
	}
	
	/**
	 * Tests that rotating a lamp does not move it from its start position.
	 */
	@Test
	public void testrotateLamp() {
		Torch lamp = new Torch(false);
		lamp.rotateEast();
		lamp.rotateNorth();
		lamp.rotateSouth();
		lamp.rotateWest();
		assertEquals(new Vector3f(-0.05f, 0.0f, -0.05f), lamp.getSpatial().getLocalTranslation());
		
	}
	/**
	 * Tests if loading torch with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		Torch.loadEntity(loadPosition, new String[4]);
	}

}
