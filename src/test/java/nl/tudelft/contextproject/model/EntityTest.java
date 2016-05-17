package nl.tudelft.contextproject.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Abstract test class for Entity.
 */
public abstract class EntityTest extends DrawableTest {
	
	private Entity entity;
	
	@Override
	public Drawable getDrawable() {
		return getEntity();
	}
	
	/**
	 * Getter for a specific instance of Entity.
	 * @return an Entity to test with.
	 */
	public abstract Entity getEntity();

	/**
	 * create a new (clean) entity to test with.
	 */
	public void setupEntity() {
		entity = getEntity();
	}
	
	/**
	 * Test if the initial state of an entity is NEW.
	 */
	@Test
	public void testInitialState() {
		setupEntity();
		assertEquals(entity.getState(), EntityState.NEW);
	}
	
	/**
	 * Test if setting a new state changes the state.
	 */
	@Test
	public void testSetState() {
		setupEntity();
		entity.setState(EntityState.ALIVE);
		assertEquals(entity.getState(), EntityState.ALIVE);
	}
	
	/**
	 * Test if collision detection returns true when it should.
	 */
	@Test
	public void testPlayerCollisionTrue() {
		TestUtil.mockGame();
		setupEntity();
		
		Spatial eSpat = mock(Spatial.class);
		Spatial pSpat = mock(Spatial.class);
		entity.setSpatial(eSpat);		
		Main.getInstance().getCurrentGame().getPlayer().setSpatial(pSpat);
		
		when(eSpat.getLocalTranslation()).thenReturn(new Vector3f(0, 0, 0));
		when(pSpat.getLocalTranslation()).thenReturn(new Vector3f(0, 0, .199f));
		
		assertTrue(entity.collidesWithPlayer(.2f));
	}
	
	/**
	 * Test if collision detection returns flase when it should.
	 */
	@Test
	public void testPlayerCollisionFalse() {
		TestUtil.mockGame();
		setupEntity();
		
		Spatial eSpat = mock(Spatial.class);
		Spatial pSpat = mock(Spatial.class);
		entity.setSpatial(eSpat);		
		Main.getInstance().getCurrentGame().getPlayer().setSpatial(pSpat);
		
		when(eSpat.getLocalTranslation()).thenReturn(new Vector3f(0, 0, 0));
		when(pSpat.getLocalTranslation()).thenReturn(new Vector3f(0, 0, .201f));
		
		assertFalse(entity.collidesWithPlayer(.2f));
	}
}
