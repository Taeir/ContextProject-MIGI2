package nl.tudelft.contextproject.webinterface;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

/**
 * Test class for the {@link WebInventory}.
 */
public class WebInventoryTest extends TestBase {
	private WebInventory inventory;
	
	/**
	 * Creates a new WebInventory before every test.
	 */
	@Before
	public void setUp() {
		inventory = spy(new WebInventory());
	}
	
	/**
	 * Tests if getActionCount works properly.
	 */
	@Test
	public void testGetActionCount() {
		assertEquals(Action.PLACEBOMB.getGlobalMaxAmount(), inventory.getActionCount(Team.DWARFS, Action.PLACEBOMB));
		assertEquals(Action.DROPCRATE.getGlobalMaxAmount(), inventory.getActionCount(Team.ELVES, Action.DROPCRATE));
	}
	
	/**
	 * Tests if getActionCount works properly when the action is not valid for the team.
	 */
	@Test
	public void testGetActionCount_wrongTeam() {
		assertEquals(-1, inventory.getActionCount(Team.ELVES, Action.PLACEPITFALL));
	}

	/**
	 * Test if the inventory properly limits the amount of times an action can be performed.
	 */
	@Test
	public void testPerformAction() {
		//Action should be performable for the max amount of times.
		for (int i = 0; i < Action.SPAWNENEMY.getGlobalMaxAmount(); i++) {
			assertTrue(inventory.performAction(Team.DWARFS, Action.SPAWNENEMY));
			assertEquals(Action.SPAWNENEMY.getGlobalMaxAmount() - i, inventory.getActionCount(Team.DWARFS, Action.SPAWNENEMY));
		}
		
		//Action should no longer be performable
		assertFalse(inventory.performAction(Team.DWARFS, Action.SPAWNENEMY));
	}
	
	/**
	 * Test if the inventory properly allows actions that can be done unlimited times.
	 */
	@Test
	public void testPerformAction_unlimited() {
		//Action should always be performable
		assertEquals(-1, inventory.getActionCount(Team.ELVES, Action.PLACETILE));
		assertTrue(inventory.performAction(Team.ELVES, Action.PLACETILE));
		assertEquals(-1, inventory.getActionCount(Team.ELVES, Action.PLACETILE));
	}

	/**
	 * Tests if the inventory is properly reset.
	 */
	@Test
	public void testReset() {
		for (int i = 0; i < Action.SPAWNENEMY.getGlobalMaxAmount(); i++) {
			inventory.performAction(Team.DWARFS, Action.SPAWNENEMY);
		}
		
		assertEquals(0, inventory.getActionCount(Team.DWARFS, Action.SPAWNENEMY));
		
		inventory.reset();
		assertEquals(Action.SPAWNENEMY.getGlobalMaxAmount(), inventory.getActionCount(Team.DWARFS, Action.SPAWNENEMY));
	}

	/**
	 * Tests if the toWebJson method properly adds limited actions.
	 */
	@Test
	public void testToWebJson() {
		JSONObject json = inventory.toWebJson(Team.DWARFS);
		assertEquals(Action.SPAWNENEMY.getGlobalMaxAmount(), json.get("" + Action.SPAWNENEMY.ordinal()));
	}

}
