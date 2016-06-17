package nl.tudelft.contextproject.model.entities.environment;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyFloat;

import org.junit.Before;
import org.junit.Test;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.EntityTest;
import nl.tudelft.contextproject.model.entities.util.Direction;
import nl.tudelft.contextproject.model.entities.util.EntityType;

/**
 * Test class for {@link WallFrame}.
 */
public class WallFrameTest extends EntityTest {
	private WallFrame wallFrame;
	
	@Override
	public Entity getEntity() {
		return new WallFrame(new Vector3f(), "logo.png", Direction.SOUTH, new Vector2f(1, 3));
	}

	@Override
	public EntityType getType() {
		return EntityType.WALLFRAME;
	}
	
	/**
	 * Create a fresh wallFrame for each test.
	 */
	@Before
	public void setUp() {
		wallFrame = new WallFrame(new Vector3f(), "logo.png", Direction.NORTH, new Vector2f(1, 3));		
	}
	
	/**
	 * Test if snapping North moves the spatial.
	 */
	@Test
	public void testSnapToWallNorth() {
		Spatial spatial = mock(Spatial.class);
		wallFrame.setSpatial(spatial);
		wallFrame.snapToWall(Direction.NORTH);

		verify(spatial, times(1)).rotate(anyFloat(), anyFloat(), anyFloat());
		verify(spatial, times(1)).move(anyFloat(), anyFloat(), anyFloat());
	}
	
	/**
	 * Test if snapping West moves the spatial.
	 */
	@Test
	public void testSnapToWallWest() {
		Spatial spatial = mock(Spatial.class);
		wallFrame.setSpatial(spatial);
		wallFrame.snapToWall(Direction.WEST);

		verify(spatial, times(1)).rotate(anyFloat(), anyFloat(), anyFloat());
		verify(spatial, times(1)).move(anyFloat(), anyFloat(), anyFloat());
	}
	
	/**
	 * Test if snapping South moves the spatial.
	 */
	@Test
	public void testSnapToWallSouth() {
		Spatial spatial = mock(Spatial.class);
		wallFrame.setSpatial(spatial);
		wallFrame.snapToWall(Direction.SOUTH);

		verify(spatial, times(1)).move(anyFloat(), anyFloat(), anyFloat());
	}
	
	/**
	 * Test if snapping East moves the spatial.
	 */
	@Test
	public void testSnapToWallEast() {
		Spatial spatial = mock(Spatial.class);
		wallFrame.setSpatial(spatial);
		wallFrame.snapToWall(Direction.EAST);

		verify(spatial, times(1)).rotate(anyFloat(), anyFloat(), anyFloat());
		verify(spatial, times(1)).move(anyFloat(), anyFloat(), anyFloat());
	}
	
	/**
	 * Tests if loading wall frames works properly.
	 */
	@Test
	public void testLoadEntity() {
		//WallFrames expect the path to be the fourth entry (in contrast to most other entities)
		WallFrame frame = WallFrame.loadEntity(loadPosition, new String[] {"1", "1", "1", "/", "NORTH", "logo.png", "2", "2"});
		
		//The position of the frame is changed, because it has "snapped" to the wall
		assertEquals(expectedPosition.add(0.5f, 0f, 0.49f), frame.getLocation());
	}

	/**
	 * Tests if loading wall frames with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		WallFrame.loadEntity(loadPosition, new String[7]);
	}
}
