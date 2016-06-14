package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

import nl.tudelft.contextproject.model.entities.util.EntityType;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the Key class.
 */
public class KeyTest extends EntityTest {
	private Key key;

	@Override
	public Entity getEntity() {
		ColorRGBA color = new ColorRGBA();
		color.set(ColorRGBA.Yellow);
		return new Key(color);
	}

	@Override
	public EntityType getType() {
		return EntityType.KEY;
	}
	
	/**
	 * Setup method.
	 * Creates a fresh key for every test.
	 */
	@Before
	public void setUp() {
		ColorRGBA color = new ColorRGBA();
		color.set(ColorRGBA.Yellow);
		key = new Key(color);
		key.move(1, 1, 1);
	}

	/**
	 * Test if updating the key doesn't make it move.
	 */
	@Test
	public void testUpdate() {
		Geometry mockedGeometry = mock(Geometry.class);
		key.setSpatial(mockedGeometry);
		key.update(0.f);
		verifyZeroInteractions(mockedGeometry);
	}
	
	/**
	 * Tests the setcolor method.
	 */
	@Test
	public void testSetColor() {
		key.setColor(ColorRGBA.Red);
		assertEquals(key.getColor(), ColorRGBA.Red);
	}
	
	/**
	 * Tests if loading keys works properly.
	 */
	@Test
	public void testLoadEntity() {
		Key key = Key.loadEntity(loadPosition, new String[] {"1", "1", "1", EntityType.KEY.getName(), "0/1/0/1"});
		
		assertEquals(expectedPosition.add(0, .5f, 0), key.getLocation());
		assertEquals(ColorRGBA.Green, key.getColor());
	}
	
	/**
	 * Tests if loading keys with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		Key.loadEntity(loadPosition, new String[4]);
	}
}
