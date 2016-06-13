package nl.tudelft.contextproject.model.entities;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.Assert.assertEquals;
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

	private PlayerTrigger playerTrigger;
	private TickListener action;
	
	@Override
	public Entity getEntity() {
		setUp();
		return playerTrigger;
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER_TRIGGER;
	}
	
	/**
	 * Create a fresh playerTrigger for each test.
	 */
	@Before
	public void setUp() {
		TestUtil.mockGame();
		
		action = mock(TickListener.class);
		playerTrigger = new PlayerTrigger(0.2f, 1, action);	
	}
	
	/**
	 * Test calling update while cooling down.
	 */
	@Test
	public void testUpdateCoolingDown() {
		Spatial playerSpatial = Main.getInstance().getCurrentGame().getPlayer().getSpatial();
		playerSpatial.setLocalTranslation(playerTrigger.getSpatial().getLocalTranslation());
		playerTrigger.update(.001f);

		playerTrigger.update(.5f);
		verify(action, times(1)).update(anyFloat());
		playerSpatial.setLocalTranslation(0, 10, 0);
	}

	/**
	 * Test update when there is no collision.
	 */
	@Test
	public void testUpdateNoCollision() {
		playerTrigger.update(2f);
		verify(action, times(0)).update(anyFloat());		
	}

	/**
	 * Test triggering the action when there is a collision.
	 */
	@Test
	public void testUpdateTrigger() {
		Spatial playerSpatial = Main.getInstance().getCurrentGame().getPlayer().getSpatial();
		playerSpatial.setLocalTranslation(playerTrigger.getSpatial().getLocalTranslation());
		playerTrigger.update(1.2f);

		verify(action, times(1)).update(0f);
		playerSpatial.setLocalTranslation(0, 10, 0);
	}
	
	/**
	 * Tests if loading player triggers works properly.
	 */
	@Test
	public void testLoadEntity() {
		//Player Triggers expect the path to be the fourth entry (in contrast to most other entities)
		PlayerTrigger trigger = PlayerTrigger.loadEntity(loadPosition, new String[] {"1", "1", "1", "/maps/MenuLevel/", "2", "2", "StartGame"});
		
		//Player triggers are moved upwards slightly, so we need to keep that in mind
		assertEquals(loadPosition.add(0f, 0.505f, 0f), trigger.getLocation());
	}

	/**
	 * Tests if loading player triggers with invalid data throws an exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testLoadEntityInvalidData() {
		PlayerTrigger.loadEntity(loadPosition, new String[6]);
	}

}
