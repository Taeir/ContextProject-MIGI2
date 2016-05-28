package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the Door class.
 */
public class DoorTest extends EntityTest {
	private Door door;

	@Override
	public Entity getEntity() {
		ColorRGBA color = new ColorRGBA();
		color.set(ColorRGBA.Yellow);
		return new Door(color);
	}
	
	/**
	 * Setup method.
	 * Creates a fresh Door for every test.
	 */
	@Before
	public void setUp() {
		ColorRGBA color = new ColorRGBA();
		color.set(ColorRGBA.Yellow);
		door = new Door(color);
	}

	/**
	 * Test if updating the Door doesnt make it move.
	 */
	@Test
	public void testUpdate() {
		Geometry mockedGeometry = mock(Geometry.class);
		door.setSpatial(mockedGeometry);
		door.update(0.f);
		verifyZeroInteractions(mockedGeometry);
	}
	
	/**
	 * Tests the setcolor method.
	 */
	@Test
	public void testSetColor() {
		door.setColor(ColorRGBA.Red);
		assertEquals(door.getColor(), ColorRGBA.Red);
	}
}
