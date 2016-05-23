package nl.tudelft.contextproject.webinterface;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for {@link WebClient}.
 */
public class WebClientTest {

	/**
	 * Tests {@link WebClient#isElf}.
	 */
	@Test
	public void testIsElf() {
		WebClient client = new WebClient();
		
		client.setTeam(true);
		assertTrue(client.isElf());
		
		client.setTeam(false);
		assertFalse(client.isElf());
	}

	/**
	 * Tests {@link WebClient#isDwarf}.
	 */
	@Test
	public void testIsDwarf() {
		WebClient client = new WebClient();
		client.setTeam(false);
		
		assertTrue(client.isDwarf());
		
		client.setTeam(true);
		assertFalse(client.isDwarf());
	}

	/**
	 * Tests {@link WebClient#getTeam}.
	 */
	@Test
	public void testGetTeam() {
		WebClient client = new WebClient();
		client.setTeam(false);
		
		assertEquals("Dwarfs", client.getTeam());
	}

	/**
	 * Tests {@link WebClient#setTeam}.
	 */
	@Test
	public void testSetTeam() {
		WebClient client = new WebClient();
		client.setTeam(false);
		
		assertTrue(client.isDwarf());
		
		client.setTeam(null);
		assertFalse(client.isDwarf());
		assertFalse(client.isElf());
		assertEquals("None", client.getTeam());
	}

	/**
	 * Tests {@link WebClient#toString}.
	 */
	@Test
	public void testToString() {
		//Create a new WebClient with team Elves
		WebClient client = new WebClient();
		client.setTeam(true);
		
		assertEquals("WebClient<team=Elves>", client.toString());
	}

}
