package nl.tudelft.contextproject.model.entities;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyFloat;

import nl.tudelft.contextproject.model.TickListener;
import org.junit.Before;
import org.junit.Test;

import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link PlayerTrigger}.
 */
public class PlayerTriggerTest extends EntityTest {

	private PlayerTrigger pt;
	private TickListener action;
	
	@Override
	public Entity getEntity() {
		setUp();
		return pt;
	}
	
	/**
	 * Create a fresh playerTrigger for each test.
	 */
	@Before
	public void setUp() {
		TestUtil.mockGame();
		
		action = mock(TickListener.class);
		pt = new PlayerTrigger(0.2f, 1, action);	
	}
	
	/**
	 * Test calling update while cooling down.
	 */
	@Test
	public void testUpdateCoolingDown() {
		Spatial pSpat = Main.getInstance().getCurrentGame().getPlayer().getSpatial();
		pSpat.setLocalTranslation(pt.getSpatial().getLocalTranslation());
		pt.update(.001f);

		pt.update(.5f);
		verify(action, times(1)).update(anyFloat());
		pSpat.setLocalTranslation(0, 10, 0);
	}

	/**
	 * Test update when there is no collision.
	 */
	@Test
	public void testUpdateNoCollision() {
		pt.update(2f);
		verify(action, times(0)).update(anyFloat());		
	}

	/**
	 * Test triggering the action when there is a collision.
	 */
	@Test
	public void testUpdateTrigger() {
		Spatial pSpat = Main.getInstance().getCurrentGame().getPlayer().getSpatial();
		pSpat.setLocalTranslation(pt.getSpatial().getLocalTranslation());
		pt.update(1.2f);

		verify(action, times(1)).update(0f);
		pSpat.setLocalTranslation(0, 10, 0);
	}

}
