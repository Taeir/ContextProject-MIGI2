package nl.tudelft.contextproject.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

/**
 * Test class for the GameState enum.
 */
public class GameStateTest extends TestBase {

	/**
	 * Check that {@link GameState#ENDED} does count as started.
	 */
	@Test
	public void testIsStartedENDED() {
		assertTrue(GameState.ENDED.isStarted());
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
