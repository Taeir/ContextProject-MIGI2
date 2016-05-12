package nl.tudelft.contextproject.controller;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for the GameState enum.
 */
public class GameStateTest {

	/**
	 * Check that {@link GameState#ENDED} does not count as started.
	 */
	@Test
	public void testIsStartedENDED() {
		assertFalse(GameState.ENDED.isStarted());
	}
	
	/**
	 * Check that {@link GameState#RUNNING} does count as started.
	 */
	@Test
	public void testIsStartedRUNNING() {
		assertTrue(GameState.RUNNING.isStarted());
	}
	
	/**
	 * Check that {@link GameState#PAUSED} does count as started.
	 */
	@Test
	public void testIsStartedPAUSED() {
		assertTrue(GameState.PAUSED.isStarted());
	}
	
	/**
	 * Check that {@link GameState#WAITING} does not count as started.
	 */
	@Test
	public void testIsStartedWAITING() {
		assertFalse(GameState.WAITING.isStarted());
	}

}
