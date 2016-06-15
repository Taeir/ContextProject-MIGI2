package nl.tudelft.contextproject.webinterface;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import nl.tudelft.contextproject.webinterface.websockets.COCSocket;

/**
 * Test class for {@link WebClient}.
 */
public class WebClientTest extends WebTestBase {

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
	 * Tests {@link WebClient#sendMessage(String, HttpServletResponse)}, when sending a message
	 * normally.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testSendMessage_normal() throws IOException {
		WebClient client = new WebClient();
		HttpServletResponse response = createMockedResponse();
		
		client.sendMessage("MESSAGE", response);
		
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("MESSAGE");
	}
	
	/**
	 * Tests {@link WebClient#sendMessage(String, HttpServletResponse)}, when sending a message
	 * via a websocket.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testSendMessage_socket() throws IOException {
		COCSocket socket = mock(COCSocket.class);
		
		WebClient client = new WebClient();
		client.setWebSocket(socket);
		
		client.sendMessage("A", null);
		
		verify(socket).sendMessage("A");
	}
	
	/**
	 * Tests {@link WebClient#sendMessage(JSONObject, HttpServletResponse)}, when sending a JSON
	 * message normally.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testSendMessage_json_normal() throws IOException {
		WebClient client = new WebClient();
		HttpServletResponse response = createMockedResponse();
		
		JSONObject json = new JSONObject();
		
		client.sendMessage(json, response);
		
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response).setContentType("text/json");
		verify(response.getWriter()).write(json.toString());
	}
	
	/**
	 * Tests {@link WebClient#sendMessage(JSONObject, HttpServletResponse)}, when sending a JSON
	 * message via a websocket.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testSendMessage_json_socket() throws IOException {
		COCSocket socket = mock(COCSocket.class);
		
		WebClient client = new WebClient();
		client.setWebSocket(socket);
		
		JSONObject json = new JSONObject();
		
		client.sendMessage(json, null);
		
		verify(socket).sendMessage(json.toString());
	}
	
	/**
	 * Tests {@link WebClient#confirmMessage}.
	 */
	@Test
	public void testConfirmMessage() {
		WebClient client = new WebClient();
		HttpServletResponse response = createMockedResponse();
		
		client.confirmMessage(response);
		
		verify(response).setStatus(HttpStatus.NO_CONTENT_204);
	}
	
	/**
	 * Tests {@link WebClient#getWebSocket} and {@link WebClient#setWebSocket}.
	 */
	@Test
	public void testGetSetWebSocket() {
		COCSocket socket = mock(COCSocket.class);
		
		WebClient client = new WebClient();
		client.setWebSocket(socket);
		
		assertSame(socket, client.getWebSocket());
	}
	
	/**
	 * Tests {@link WebClient#removeWebSocket}, when the socket being removed is the current socket.
	 */
	@Test
	public void testRemoveWebSocket_same() {
		COCSocket socket = mock(COCSocket.class);
		
		WebClient client = new WebClient();
		client.setWebSocket(socket);
		
		//Remove same
		client.removeWebSocket(socket);
		
		//Should have been removed
		assertNull(client.getWebSocket());
	}
	
	/**
	 * Tests {@link WebClient#removeWebSocket}, when the socket being removed is not the current
	 * socket.
	 */
	@Test
	public void testRemoveWebSocket_different() {
		COCSocket socket = mock(COCSocket.class);
		
		WebClient client = new WebClient();
		client.setWebSocket(socket);
		
		//Remove different
		client.removeWebSocket(mock(COCSocket.class));
		
		//Socket should not have been removed
		assertSame(socket, client.getWebSocket());
	}
	
	/**
	 * Tests {@link WebClient#getPerformedActions()}.
	 */
	@Test
	public void testGetPerformedActions() {
		WebClient client = new WebClient();
		client.setTeam(Team.ELVES);
		
		//Elves should have 4 actions
		assertEquals(4, client.getPerformedActions().size());
	}

	/**
	 * Tests {@link WebClient#toString}.
	 */
	@Test
	public void testToString() {
		//Create a new WebClient with team Elves
		WebClient client = new WebClient();
		client.setTeam(Team.ELVES);
		
		assertEquals("WebClient<team=" + client.getTeam() + ">", client.toString());
	}

}
