package nl.tudelft.contextproject.webinterface;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for the {@link Team} enum.
 */
public class TeamTest {

	/**
	 * Tests if the ELVES team has the correct actions.
	 */
	@Test
	public void testGetActionsElves() {
		assertTrue(Team.ELVES.getActions().contains(Action.DROPBAIT));
		assertTrue(Team.ELVES.getActions().contains(Action.PLACETILE));
		assertTrue(Team.ELVES.getActions().contains(Action.OPENGATE));
		assertTrue(Team.ELVES.getActions().contains(Action.DROPCRATE));
		assertFalse(Team.ELVES.getActions().contains(Action.PLACEBOMB));
	}
	
	/**
	 * Tests if the DWARFS team has the correct actions.
	 */
	@Test
	public void testGetActionsDwarfs() {
		assertTrue(Team.DWARFS.getActions().contains(Action.PLACEBOMB));
		assertTrue(Team.DWARFS.getActions().contains(Action.PLACEPITFALL));
		assertTrue(Team.DWARFS.getActions().contains(Action.PLACEMINE));
		assertTrue(Team.DWARFS.getActions().contains(Action.SPAWNENEMY));
		assertFalse(Team.DWARFS.getActions().contains(Action.DROPBAIT));
	}

	/**
	 * Tests if the NONE team has no valid actions.
	 */
	public void testGetActionsNone() {
		assertTrue(Team.NONE.getActions().isEmpty());
	}
}
