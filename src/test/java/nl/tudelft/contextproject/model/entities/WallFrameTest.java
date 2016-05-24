package nl.tudelft.contextproject.model.entities;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyFloat;

import org.junit.Before;
import org.junit.Test;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Test class for {@link WallFrame}.
 */
public class WallFrameTest extends EntityTest {
	private WallFrame wf;
	
	@Override
	public Entity getEntity() {
		return new WallFrame(new Vector3f(), "logo.png", Direction.SOUTH, 1, 3);
	}
	
	/**
	 * Create a fresh wallFrame for each test.
	 */
	@Before
	public void setUp() {
		wf = new WallFrame(new Vector3f(), "logo.png", Direction.NORTH, 1, 3);		
	}
	
	/**
	 * Test if snapping North moves the spatial.
	 */
	@Test
	public void testSnapToWallNorth() {
		Spatial sp = mock(Spatial.class);
		wf.setSpatial(sp);
		wf.snapToWall(Direction.NORTH);

		verify(sp, times(1)).rotate(anyFloat(), anyFloat(), anyFloat());
		verify(sp, times(1)).move(anyFloat(), anyFloat(), anyFloat());
	}
	
	/**
	 * Test if snapping West moves the spatial.
	 */
	@Test
	public void testSnapToWallWest() {
		Spatial sp = mock(Spatial.class);
		wf.setSpatial(sp);
		wf.snapToWall(Direction.WEST);

		verify(sp, times(1)).rotate(anyFloat(), anyFloat(), anyFloat());
		verify(sp, times(1)).move(anyFloat(), anyFloat(), anyFloat());
	}
	
	/**
	 * Test if snapping South moves the spatial.
	 */
	@Test
	public void testSnapToWallSouth() {
		Spatial sp = mock(Spatial.class);
		wf.setSpatial(sp);
		wf.snapToWall(Direction.SOUTH);

		verify(sp, times(1)).move(anyFloat(), anyFloat(), anyFloat());
	}
	
	/**
	 * Test if snapping East moves the spatial.
	 */
	@Test
	public void testSnapToWallEast() {
		Spatial sp = mock(Spatial.class);
		wf.setSpatial(sp);
		wf.snapToWall(Direction.EAST);

		verify(sp, times(1)).rotate(anyFloat(), anyFloat(), anyFloat());
		verify(sp, times(1)).move(anyFloat(), anyFloat(), anyFloat());
	}
}
