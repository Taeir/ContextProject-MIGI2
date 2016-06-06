package nl.tudelft.contextproject.webinterface;

import static org.junit.Assert.*;

import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

/**
 * Test class for {@link WebClient}.
 */
public class WebClientTest extends TestBase {

	/**
	 * Tests {@link WebClient#isElf}.
	 */
	@Test
	public void testIsElf() {
		WebClient client = new WebClient();
		
		client.setTeam(Team.ELVES);
		assertTrue(client.isElf());
		
		client.setTeam(Team.DWARFS);
		assertFalse(client.isElf());
	}

	/**
	 * Tests {@link WebClient#isDwarf}.
	 */
	@Test
	public void testIsDwarf() {
		WebClient client = new WebClient();
		client.setTeam(Team.DWARFS);
		
		assertTrue(client.isDwarf());
		
		client.setTeam(Team.ELVES);
		assertFalse(client.isDwarf());
	}

	/**
	 * Tests {@link WebClient#getTeam}.
	 */
	@Test
	public void testGetTeam() {
		WebClient client = new WebClient();
		client.setTeam(Team.DWARFS);
		
		assertEquals(Team.DWARFS, client.getTeam());
	}

	/**
	 * Tests {@link WebClient#setTeam}.
	 */
	@Test
	public void testSetTeam() {
		WebClient client = new WebClient();
		client.setTeam(Team.DWARFS);
		
		assertTrue(client.isDwarf());
		
		client.setTeam(Team.NONE);
		assertFalse(client.isDwarf());
		assertFalse(client.isElf());
		assertEquals(Team.NONE, client.getTeam());
	}

	/**
	 * Tests {@link WebClient#toString}.
	 */
	@Test
	public void testToString() {
		//Create a new WebClient with team Elves
		WebClient client = new WebClient();
		client.setTeam(Team.ELVES);
		
		assertEquals("WebClient<team=ELVES>", client.toString());
	}

}
