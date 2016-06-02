package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for explosions.
 */
public class ExplosionTest extends EntityTest {

	private Explosion explosion;

	/**
	 * Create a fresh instance for each test.
	 */
	@Before
	public void setUp() {
		TestUtil.mockGame();
		explosion = new Explosion(10);
	}

	@Override
	public Entity getEntity() {
		return explosion;
	}

	@Override
	public EntityType getType() {
		return EntityType.EXPLOSION;
	}
	
	/**
	 * Test if the entity is removed when the maximum radius is reached.
	 */
	@Test
	public void testUpdateMaxRadius() {
		Spatial mock = mock(Spatial.class);
		explosion.setSpatial(mock);
		when(mock.getLocalScale()).thenReturn(new Vector3f(11, 11, 11));
		explosion.update(1);
		assertEquals(EntityState.DEAD, explosion.getState());
	}
	
	/**
	 * Test if updating increases the size of the explosion.
	 */
	@Test
	public void testUpdateScale() {
		Spatial mock = mock(Spatial.class);
		explosion.setSpatial(mock);
		when(mock.getLocalScale()).thenReturn(new Vector3f(1, 1, 1));
		when(mock.getLocalTranslation()).thenReturn(new Vector3f(1, 1, 1));
		explosion.update(1);
		verify(mock, times(1)).setLocalScale(AdditionalMatchers.gt(1f));
	}
	
	/**
	 * Test if the player gets correctly damaged.
	 */
	@Test
	public void testDamageEntitiesPlayer() {
		VRPlayer p = Main.getInstance().getCurrentGame().getPlayer();
		explosion.damageEntities(100, 2);
		assertEquals(1, p.getHealth(), 1e-6);
	}
	
	/**
	 * Test if other entities are correctly damaged.
	 */
	@Test
	public void testDamageEntitiesOther() {
		Game g = Main.getInstance().getCurrentGame();
		InvisibleWall i = new InvisibleWall();
		i.getSpatial();	// prevent nullpointer
		i.setHealth(6);
		g.getEntities().add(i);
		
		explosion.damageEntities(10, 5);
		assertEquals(1, i.getHealth(), 1e-6);
	}

}
