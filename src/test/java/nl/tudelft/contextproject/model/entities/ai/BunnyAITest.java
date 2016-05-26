package nl.tudelft.contextproject.model.entities.ai;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.entities.Carrot;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.KillerBunny;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link BunnyAI}.
 */
public class BunnyAITest {

	private BunnyAI ai;
	private Entity owner;

	/**
	 * Setup all the mocks for each test.
	 */
	@Before
	public void setUp() {
		TestUtil.mockGame();
		ai = new BunnyAI();
		owner = mock(KillerBunny.class);
		ai.setOwner(owner);

		when(owner.getLocation()).thenReturn(new Vector3f(0, 0, 0));
	}
	
	/**
	 * Reset the entity list after each test.
	 */
	@After
	public void tearDown() {
		Main.getInstance().getCurrentGame().getEntities().clear();
	}
	
	/**
	 * Set the owner to an entity that is not a bunny.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void setOwnerNotABunny() {
		ai.setOwner(mock(Entity.class));
	}
	
	/**
	 * Find the closest non existent carrot.
	 */
	@Test
	public void testFindClosestCarrotNull() {
		Carrot c = ai.findClosestCarrot();
		assertNull(c);
	}
	
	/**
	 * Find the closest carrot in the list of entities.
	 */
	@Test
	public void testFindClosestCarrot() {
		Set<Entity> entities = Main.getInstance().getCurrentGame().getEntities();
		Carrot c = mock(Carrot.class);
		when(c.getLocation()).thenReturn(new Vector3f(1, 1, 1));
		entities.add(mock(Entity.class));
		entities.add(c);
		
		assertEquals(c, ai.findClosestCarrot());
	}
	
	/**
	 * Test if calling move moves the owner.
	 */
	@Test
	public void testMove() {
		ai.move(.2f);
		verify(owner, times(1)).move(anyFloat(), anyFloat(), anyFloat());
	}
	
	/**
	 * Find the target when the player is closer than the carrot.
	 */
	@Test
	public void testFindTargetPlayerCloser() {
		Set<Entity> entities = Main.getInstance().getCurrentGame().getEntities();
		Carrot c = mock(Carrot.class);
		when(c.getLocation()).thenReturn(new Vector3f(1, 1, 1));
		entities.add(c);
		
		VRPlayer player = Main.getInstance().getCurrentGame().getPlayer();
		assertEquals(player, ai.findTarget(.2f, .3f));
	}
	
	/**
	 * Test find the target when a carrot is closer than the player.
	 */
	@Test
	public void testFindTargetCarrotCloser() {
		Set<Entity> entities = Main.getInstance().getCurrentGame().getEntities();
		Carrot c = mock(Carrot.class);
		when(c.getLocation()).thenReturn(new Vector3f(1, 1, 1));
		entities.add(c);
		
		assertEquals(c, ai.findTarget(6f, .3f));
	}
	
	/**
	 * Test eating a carrot that is really close.
	 */
	@Test
	public void testFindTargetEatCarrot() {
		Set<Entity> entities = Main.getInstance().getCurrentGame().getEntities();
		Carrot c = mock(Carrot.class);
		when(c.getLocation()).thenReturn(new Vector3f(0, 0, 0));
		entities.add(c);
		
		assertEquals(c, ai.findTarget(6f, .3f));
		verify(c, times(1)).eat(.3f);
	}

}
