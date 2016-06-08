package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for the Bomb class.
 */
public class BombTest extends EntityTest {
	private Bomb bomb;

	@Override
	public Entity getEntity() {
		return new Bomb();
	}

	@Override
	public EntityType getType() {
		return EntityType.BOMB;
	}

	/**
	 * Setup method.
	 * Creates a fresh bomb for every test.
	 */
	@Before
	public void setUp() {
		bomb = new Bomb();
		bomb.move(1, 1, 1);
	}

	/**
	 * Test if updating the bomb doesn't make it move.
	 */
	@Test
	public void testUpdate() {
		Geometry mockedGeometry = mock(Geometry.class);
		bomb.setSpatial(mockedGeometry);
		bomb.update(0.f);
		verifyZeroInteractions(mockedGeometry);
	}

	/**
	 * Tests if activating the bomb sets it to active.
	 */
	@Test
	public void testactive() {
		bomb.activate();
		assertTrue(bomb.getActive());

	}

	/**
	 * Tests if updating an active bomb increases the timer.
	 */
	@Test
	public void testUpdateactive() {
		bomb.activate();
		Spatial mock = mock(Spatial.class);
		bomb.setSpatial(mock);
		when(mock.getLocalScale()).thenReturn(new Vector3f(1, 1, 1));
		when(mock.getLocalTranslation()).thenReturn(new Vector3f(1, 1, 1));
		bomb.update(1);
		assertEquals(9, bomb.getTimer(), 1E-5);
	}
	/**
	 * Tests if an active bomb disappears after 5 seconds.
	 */
	@Test
	public void testUpdateActiveFiveSeconds() {
		TestUtil.mockGame();
		bomb.activate();
		bomb.update(11.1f);
		assertEquals(EntityState.DEAD, bomb.getState());
	}
	
	/**
	 * Tests if loading bombs works properly.
	 */
	@Test
	public void testLoadEntity() {
		Bomb bomb = Bomb.loadEntity(loadPosition, new String[]{"1", "1", "1", EntityType.BOMB.getName()});
		
		assertEquals(expectedPosition.add(0, 0.5f, 0), bomb.getLocation());
	}
	
	/**
	 * Tests if loading bombs with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		Bomb.loadEntity(loadPosition, new String[3]);
	}

	/**
	 * Tests if the bomb stays in front of the player when it's picked up.
	 */
	@Test
	public void testPickedup() {
		TestUtil.mockGame();
		bomb.setPickedup(true);
		bomb.update(1);
		Vector3f vec = Main.getInstance().getCamera().getRotation().getRotationColumn(2).mult(1.5f);
		Vector3f vec2 = Main.getInstance().getCurrentGame().getPlayer().getSpatial().getLocalTranslation().add(vec.x, 1, vec.z);
		assertEquals(bomb.getSpatial().getLocalTranslation(), vec2);
	}
}
