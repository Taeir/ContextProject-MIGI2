package nl.tudelft.contextproject.model.entities.moving;

import static org.mockito.Mockito.*;

import org.junit.Test;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.EntityTest;
import nl.tudelft.contextproject.model.entities.control.EntityControl;

/**
 * Abstract test class for testing {@link MovingEntity} classes.
 */
public abstract class MovingEntityTest extends EntityTest {

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
		EntityControl entityControl = mock(EntityControl.class);
		MovingEntity enemy = getEnemy();
		enemy.setAI(entityControl);
		verify(entityControl, times(1)).setOwner(enemy);
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
		MovingEntity e = getEnemy();
		e.update(.2f);
	}

}
