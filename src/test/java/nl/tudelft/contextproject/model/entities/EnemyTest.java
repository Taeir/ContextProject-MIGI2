package nl.tudelft.contextproject.model.entities;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.Test;

/**
 * Abstract test class for testing {@link Enemy} classes.
 */
public abstract class EnemyTest extends EntityTest {

	@Override
	public Entity getEntity() {
		return getEnemy();
	}

	/**
	 * Getter for an {@link Enemy}.
	 * 
	 * @return
	 * 		a fresh instance of {@link Enemy}.
	 */
	public abstract Enemy getEnemy();
	
	/**
	 * Test if setting the AI sets the owner.
	 */
	@Test
	public void testSetAI() {
		EntityAI ai = mock(EntityAI.class);
		Enemy e = getEnemy();
		e.setAI(ai);
		verify(ai, times(1)).setOwner(e);
	}
	
	/**
	 * Test if updating the entity invokes the ai.
	 */
	@Test
	public void testUpdate() {
		EntityAI ai = mock(EntityAI.class);
		Enemy e = getEnemy();
		e.setAI(ai);
		e.update(.2f);
		verify(ai, times(1)).move(.2f);
	}
	
	/**
	 * Test if the default AI is not null.
	 */
	@Test
	public void testIsAINotNull() {
		try {
			Enemy e = getEnemy();
			e.update(.2f);
		} catch (NullPointerException e) {
			fail();
		}
	}

}
