package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link Pitfall}.
 */
public class PitfallTest extends EntityTest {

	/**
	 * Test if the entity is removed after triggering.
	 */
	@Test
	public void testOnTrigger() {
		TestUtil.mockGame();
		VRPlayer player = Main.getInstance().getCurrentGame().getPlayer();
		when(player.getSpatial()).thenReturn(mock(Spatial.class));
		Pitfall p = new Pitfall(1);
		p.onTrigger();
		assertEquals(EntityState.DEAD, p.getState());
	}
	
	@Override
	public Entity getEntity() {
		return new Pitfall(1);
	}

}
