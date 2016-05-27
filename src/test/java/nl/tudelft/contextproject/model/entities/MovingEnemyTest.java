package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.Test;

import nl.tudelft.contextproject.model.entities.control.EntityControl;

/**
 * Abstract test class for testing {@link MovingEntity} classes.
 */
public abstract class MovingEnemyTest extends EntityTest {

	@Override
	public Entity getEntity() {
		return getEnemy();
	}

	/**
	 * Getter for an {@link MovingEntity}.
	 * 
	 * @return
	 * 		a fresh instance of {@link MovingEntity}.
	 */
	public abstract MovingEntity getEnemy();
	
	/**
	 * Test if setting the control sets the owner.
	 */
	@Test
	public void testSetAI() {
		EntityControl ai = mock(EntityControl.class);
		MovingEntity e = getEnemy();
		e.setAI(ai);
		verify(ai, times(1)).setOwner(e);
	}
	
	/**
	 * Test if updating the entity invokes the control.
	 */
	@Test
	public void testUpdate() {
		EntityControl ai = mock(EntityControl.class);
		MovingEntity e = getEnemy();
		e.setAI(ai);
		e.update(.2f);
		verify(ai, times(1)).move(.2f);
	}
	
	/**
	 * Test if the default control is not null.
	 */
	@Test
	public void testIsAINotNull() {
		try {
			MovingEntity e = getEnemy();
			e.update(.2f);
		} catch (NullPointerException e) {
			fail();
		}
	}

}
