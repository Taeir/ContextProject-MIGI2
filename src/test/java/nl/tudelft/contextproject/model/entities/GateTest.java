package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for the Gate class.
 */
public class GateTest extends EntityTest {
	private Gate gate;

	@Override
	public Entity getEntity() {
		return new Gate();
	}

	@Override
	public EntityType getType() {
		return EntityType.GATE;
	}

	/**
	 * Setup method.
	 * Creates a fresh gate for every test.
	 */
	@Before
	public void setUp() {
		gate = new Gate();
		gate.move(1, 1, 1);
	}

	/**
	 * Test if updating a closed gate that hasn't been opened doesn't make it move.
	 */
	@Test
	public void testUpdate() {
		Spatial mock = mock(Spatial.class);
		gate.setSpatial(mock);
		when(mock.getLocalTranslation()).thenReturn(new Vector3f(1, 1, 1));
		gate.update(0.f);
		verify(mock, never()).move(anyFloat(), anyFloat(), anyFloat());
	}

	/**
	 * Tests if activating the gate sets it to active.
	 */
	@Test
	public void testOpenGate() {
		gate.openGate();
		assertTrue(gate.getOpen());

	}
	
	/**
	 * Tests if loading gates works properly.
	 */
	@Test
	public void testLoadEntity() {
		Gate gate = Gate.loadEntity(loadPosition, new String[]{"1", "1", "1", EntityType.GATE.getName()});
		
		assertEquals(loadPosition, gate.getLocation());
	}
	
	/**
	 * Tests if loading gates with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		Gate.loadEntity(loadPosition, new String[3]);
	}

	/**
	 * Tests if the gate moves when its opened.
	 */
	@Test
	public void testPickedup() {
		Spatial mock = mock(Spatial.class);
		TestUtil.mockGame();
		gate.setSpatial(mock);
		gate.openGate();
		when(mock.getLocalTranslation()).thenReturn(new Vector3f(1, 1, 1));
		gate.update(0.f);
		Mockito.verify(mock, atLeastOnce()).move(anyFloat(), anyFloat(), anyFloat());
	}
	
	/**
	 * Tests if the gate closes after being opened for a few seconds. 
	 */
	@Test
	public void testCloses() {
		TestUtil.mockGame();
		gate.openGate();
		gate.update(3);
		gate.update(0.f);
		assertFalse(gate.getOpen()); 
	}
	
	/**
	 * Tests wether the gate comes down again after reaching it's highest point.
	 */
	@Test
	public void testGateComesDown() {
		TestUtil.mockGame();
		gate.openGate();
		gate.update(3);
		gate.update(0.f);
		gate.update(3);
		assertTrue(gate.getSpatial().getLocalTranslation().y < 6); 
	}
}
