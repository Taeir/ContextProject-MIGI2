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
		Spatial spatialMock = mock(Spatial.class);
		explosion.setSpatial(spatialMock);
		when(spatialMock.getLocalScale()).thenReturn(new Vector3f(11, 11, 11));
		explosion.update(1);
		assertEquals(EntityState.DEAD, explosion.getState());
	}
	
	/**
	 * Test if updating increases the size of the explosion.
	 */
	@Test
	public void testUpdateScale() {
		Spatial spatialMock = mock(Spatial.class);
		explosion.setSpatial(spatialMock);
		when(spatialMock.getLocalScale()).thenReturn(new Vector3f(1, 1, 1));
		when(spatialMock.getLocalTranslation()).thenReturn(new Vector3f(1, 1, 1));
		explosion.update(1);
		verify(spatialMock, times(1)).setLocalScale(AdditionalMatchers.gt(1f));
	}
	
	/**
	 * Test if the player gets correctly damaged.
	 */
	@Test
	public void testDamageEntitiesPlayer() {
		VRPlayer vrPlayer = Main.getInstance().getCurrentGame().getPlayer();
		explosion.damageEntities(100, 2);
		assertEquals(VRPlayer.PLAYER_MAX_HEALTH - 2, vrPlayer.getHealth(), 1e-6);
	}
	
	/**
	 * Test if other entities are correctly damaged.
	 */
	@Test
	public void testDamageEntitiesOther() {
		Game game = Main.getInstance().getCurrentGame();
		InvisibleWall invisibleWall = new InvisibleWall();
		invisibleWall.getSpatial();	// prevent nullpointer
		invisibleWall.setHealth(6);
		game.getEntities().add(invisibleWall);
		
		explosion.damageEntities(10, 5);
		assertEquals(1, invisibleWall.getHealth(), 1e-6);
	}

}
