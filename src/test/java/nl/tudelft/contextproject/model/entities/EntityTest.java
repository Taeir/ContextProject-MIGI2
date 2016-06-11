package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import nl.tudelft.contextproject.model.Drawable;
import org.junit.Test;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Abstract test class for Entity.
 */
public abstract class EntityTest extends DrawableTest {
	
	protected final Vector3f loadPosition = new Vector3f(1, 1, 1);
	protected final Vector3f expectedPosition = loadPosition.add(0, .5f, 0);
	private Entity entity;
	
	@Override
	public Drawable getDrawable() {
		return getEntity();
	}
	
	/**
	 * Getter for a specific instance of Entity.
	 *
	 * @return
	 * 		an Entity to test with
	 */
	public abstract Entity getEntity();
	
	/**
	 * @return
	 * 		the type of the Entity under test
	 */
	public abstract EntityType getType();

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
	public void testInitialState_entity() {
		setupEntity();
		assertEquals(entity.getState(), EntityState.NEW);
	}
	
	/**
	 * Test if setting a new state changes the state.
	 */
	@Test
	public void testSetState_entity() {
		setupEntity();
		entity.setState(EntityState.ALIVE);
		assertEquals(entity.getState(), EntityState.ALIVE);
	}
	
	/**
	 * Test if moving an entity moves the entity.
	 */
	@Test
	public void testMove_entity() {
		setupEntity();
		Vector3f before = entity.getSpatial().getLocalTranslation();
		Vector3f expected = before.clone().add(1.23f, 2.34f, 3.45f);
		entity.move(1.23f, 2.34f, 3.45f);
		Vector3f after = entity.getSpatial().getLocalTranslation();
		assertEquals(expected.x, after.x, 10e-10);
		assertEquals(expected.y, after.y, 10e-10);
		assertEquals(expected.z, after.z, 10e-10);			
	}
	
	/**
	 * Test if collision detection returns true when it should.
	 */
	@Test
	public void testPlayerCollisionTrue_entity() {
		setupEntity();
		TestUtil.mockGame();
		
		Spatial entitySpatial = mock(Spatial.class);
		Spatial playerSpatial = mock(Spatial.class);
		entity.setSpatial(entitySpatial);
		Main.getInstance().getCurrentGame().getPlayer().setSpatial(playerSpatial);
		
		when(entitySpatial.getLocalTranslation()).thenReturn(new Vector3f(0, 0, 0));
		when(playerSpatial.getLocalTranslation()).thenReturn(new Vector3f(0, 0, .199f));
		
		assertTrue(entity.collidesWithPlayer(.2f));
	}
	
	/**
	 * Test if collision detection returns false when it should.
	 */
	@Test
	public void testPlayerCollisionFalse_entity() {
		TestUtil.mockGame();
		setupEntity();
		
		Spatial entitySpatial = mock(Spatial.class);
		Spatial playerSpatial = mock(Spatial.class);
		entity.setSpatial(entitySpatial);		
		Main.getInstance().getCurrentGame().getPlayer().setSpatial(playerSpatial);
		
		when(entitySpatial.getLocalTranslation()).thenReturn(new Vector3f(0, 0, 0));
		when(playerSpatial.getLocalTranslation()).thenReturn(new Vector3f(0, 0, .201f));
		
		assertFalse(entity.collidesWithPlayer(.2f));
	}

	/**
	 * Test if the getLocation method works.
	 */
	@Test
	public void testGetLocation_entity() {
		TestUtil.mockGame();
		setupEntity();
		Spatial mockedSpatial = mock(Spatial.class);
		entity.setSpatial(mockedSpatial);
		entity.getLocation();
		verify(mockedSpatial, times(1)).getLocalTranslation();
	}
	
	/**
	 * Tests if the type of the entity matches with what we expect.
	 */
	@Test
	public void testGetType() {
		assertEquals(getType(), getEntity().getType());
	}
}
