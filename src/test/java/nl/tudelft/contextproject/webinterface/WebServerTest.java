package nl.tudelft.contextproject.webinterface;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.webinterface.websockets.COCSocket;

/**
 * Test class for {@link WebServer}.
 */
public class WebServerTest extends WebTestBase {
	private static final String ID = "TESTID";
	
	public WebServer webServer;

	/**
	 * Creates a webServer before every test.
	 */
	@Before
	public void setUp() {
		webServer = new WebServer();
	}

	/**
	 * Stops the server if it is running.
	 * 
	 * @throws Exception
	 * 		if stopping the server results in an exception
	 */
	@After
	public void tearDown() throws Exception {
		webServer.stop();
	}
	
	/**
	 * Tests if {@link WebServer#getUniqueClientCount()} works properly.
	 */
	@Test
	public void testGetUniqueClientCount() {
		assertEquals(0, webServer.getUniqueClientCount());
		
		webServer.getClients().put("A", new WebClient());
		assertEquals(1, webServer.getUniqueClientCount());
	}
	
	/**
	 * Tests if {@link WebServer#getDwarfsCount()} works properly.
	 */
	@Test
	public void testGetDwarfsCount() {
		WebClient clientA = new WebClient();
		clientA.setTeam(Team.DWARFS);
		
		WebClient clientB = new WebClient();
		clientB.setTeam(Team.ELVES);
		
		WebClient clientC = new WebClient();
		clientC.setTeam(Team.ELVES);
		
		webServer.getClients().put("A", clientA);
		webServer.getClients().put("B", clientB);
		webServer.getClients().put("C", clientC);
		
		assertEquals(1, webServer.getDwarfsCount());
	}
	
	/**
	 * Tests if {@link WebServer#getElvesCount()} works properly.
	 */
	@Test
	public void testGetElvesCount() {
		WebClient clientA = new WebClient();
		clientA.setTeam(Team.ELVES);
		
		WebClient clientB = new WebClient();
		clientB.setTeam(Team.DWARFS);
		
		WebClient clientC = new WebClient();
		clientC.setTeam(Team.ELVES);
		
		webServer.getClients().put("A", clientA);
		webServer.getClients().put("B", clientB);
		webServer.getClients().put("C", clientC);
		
		assertEquals(2, webServer.getElvesCount());
	}
	
	/**
	 * Test method for {@link WebServer#start(int)}, when the server is not running.
	 * 
	 * This should throw an IllegalStateException.
	 * 
	 * @throws Exception
	 * 		if starting the server causes an Exception
	 */
	@Test
	public void testStart_notRunning() throws Exception {
		//Start the webserver and confirm it is running
		webServer.start(8080);
		assertTrue(webServer.isRunning());
	}

	/**
	 * Test method for {@link WebServer#start(int)}, when the server is already running.
	 * 
	 * This should throw an IllegalStateException.
	 * 
	 * @throws Exception
	 * 		if starting the server causes an Exception
	 */
	@Test(expected = IllegalStateException.class)
	public void testStart_running() throws Exception {
		//Start the webserver and confirm it is running
		webServer.start(8080);
		assertTrue(webServer.isRunning());

		//Starting again should throw an exception
		webServer.start(8080);
	}

	/**
	 * Test method for {@link WebServer#stop()}, when the server is running.
	 * 
	 * @throws Exception
	 * 		if starting or stopping the webserver causes an exception
	 */
	@Test
	public void testStop_running() throws Exception {
		//Start the webserver and confirm it is running
		webServer.start(8080);
		assertTrue(webServer.isRunning());

		//Stop the webserver again and assert it has stopped running
		webServer.stop();
		assertFalse(webServer.isRunning());
	}
	
	/**
	 * Test method for {@link WebServer#stop()}, when the server is not running.
	 * 
	 * @throws Exception
	 * 		if starting or stopping the webserver causes an exception
	 */
	@Test
	public void testStop_notRunning() throws Exception {
		//Stopping the webServer if it is not running should not have any effect
		webServer.stop();
		assertFalse(webServer.isRunning());
	}
	
	/**
	 * Test method for {@link WebServer#getUser(HttpServletRequest)}, when a user is requested,
	 * without a cookie.
	 */
	@Test
	public void testGetUser_unknown() {
		HttpServletRequest request = createMockedRequest(null);
		assertNull(webServer.getUser(request));
	}
	
	/**
	 * Test method for {@link WebServer#getUser(HttpServletRequest)}, when a user is requested,
	 * with an unknown session id.
	 */
	@Test
	public void testGetUser_unknown_noSession() {
		HttpServletRequest request = createMockedRequest(ID);
		assertNull(webServer.getUser(request));
	}
	
	/**
	 * Test method for {@link WebServer#getUser(HttpServletRequest)}, when a known user is
	 * requested.
	 */
	@Test
	public void testGetUser_known() {
		//Add the client with their session1 id
		WebClient client = new WebClient();
		webServer.getClients().put(ID, client);

		//Create the request
		HttpServletRequest request = createMockedRequest(ID);
		
		assertSame(client, webServer.getUser(request));
	}

	/**
	 * Test method for {@link WebServer#findCookie(String, Cookie[])}, when the cookies array is
	 * null.
	 */
	@Test
	public void testFindCookie_null() {
		assertNull(WebServer.findCookie("COOKIE", null));
	}
	
	/**
	 * Test method for {@link WebServer#findCookie(String, Cookie[])}, when the cookies array is not
	 * null.
	 */
	@Test
	public void testFindCookie_notNull() {
		Cookie[] cookies = new Cookie[] {
				new Cookie("COOKIE-0", "The"),
				new Cookie("COOKIE-1", "way"),
				new Cookie("COOKIE-2", "the"),
				new Cookie("COOKIE-3", "cookie"),
				new Cookie("COOKIE-4", "crumbles")};

		//Try to find nr 4
		assertSame(cookies[4], WebServer.findCookie("COOKIE-4", cookies));

		//Check that non present cookies return null
		assertNull(WebServer.findCookie("COOKIE-5", cookies));
	}

	/**
	 * Test method for {@link WebServer#createCookie()}.
	 */
	@Test
	public void testCreateCookie() {
		Cookie cookie = webServer.createCookie();

		//Check if correct information was set on the cookie
		assertEquals(WebServer.COOKIE_NAME, cookie.getName());
		assertEquals(WebServer.COOKIE_MAX_AGE, cookie.getMaxAge());
	}

	/**
	 * Tests if {@link WebServer#clearCooldowns()} properly resets all the cooldowns.
	 */
	@Test
	public void testClearCooldowns() {
		WebClient clientA = mock(WebClient.class);
		WebClient clientB = mock(WebClient.class);
		webServer.getClients().put("A", clientA);
		webServer.getClients().put("B", clientB);
		
		webServer.clearCooldowns();
		verify(clientA).resetPerformed();
		verify(clientB).resetPerformed();
	}
	
	/**
	 * Tests if {@link WebServer#disconnect} properly disconnects a client.
	 */
	@Test
	public void testDisconnect() {
		COCSocket socket = mock(COCSocket.class);
		when(socket.getSession()).thenReturn(mock(Session.class));
		when(socket.isConnected()).thenReturn(true);
		
		WebClient client = new WebClient();
		client.setWebSocket(socket);
		
		webServer.getClients().put("A", client);
		webServer.disconnect(client, 10);
		
		verify(socket.getSession()).close(10, null);
		assertFalse(webServer.getClients().containsValue(client));
	}
	
	/**
	 * Tests if {@link WebServer#disconnectAll} properly disconnects all clients.
	 */
	@Test
	public void testDisconnectAll() {
		COCSocket socket = mock(COCSocket.class);
		when(socket.getSession()).thenReturn(mock(Session.class));
		
		WebClient client1 = new WebClient();
		WebClient client2 = new WebClient();
		client1.setWebSocket(socket);
		client2.setWebSocket(socket);
		
		webServer.getClients().put("A", client1);
		webServer.getClients().put("B", client2);
		
		verify(socket.getSession(), times(2)).close(StatusCode.NORMAL, null);
		assertTrue(webServer.getClients().isEmpty());
	}
}
