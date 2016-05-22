package nl.tudelft.contextproject.webinterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link WebServer}.
 */
public class WebServerTest extends WebTestBase {
	private static final String ID1 = "TESTID1";
	private static final String ID2 = "TESTID2";
	
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
	 * Test method for {@link WebServer#getUser(HttpServletRequest)}, when an unknown user is
	 * requested, with a matching session1 and session2 id.
	 */
	@Test
	public void testGetUser_unknown() {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true);
		assertNull(webServer.getUser(request));
	}
	
	/**
	 * Test method for {@link WebServer#getUser(HttpServletRequest)}, when an unknown user is
	 * requested, with a matching session1 and session2 id.
	 */
	@Test
	public void testGetUser_unknown_noSession() {
		HttpServletRequest request = createMockedRequest(ID1, ID1, false);
		assertNull(webServer.getUser(request));
	}

	/**
	 * Test method for {@link WebServer#getUser(HttpServletRequest)}, when an unknown user is
	 * requested, with a session1 id and no session2 cookie.
	 */
	@Test
	public void testGetUser_unknown_session1() {
		HttpServletRequest request = createMockedRequest(ID1, null, true);
		assertNull(webServer.getUser(request));
	}
	
	/**
	 * Test method for {@link WebServer#getUser(HttpServletRequest)}, when an unknown user is
	 * requested, with mismatching session1 and session2 ids.
	 */
	@Test
	public void testGetUser_unknown_session2() {
		HttpServletRequest request = createMockedRequest(ID1, ID2, true);
		assertNull(webServer.getUser(request));
	}
	
	/**
	 * Test method for {@link WebServer#getUser(HttpServletRequest)}, when an unknown user is
	 * requested.
	 */
	@Test
	public void testGetUser_known_session1() {
		//Add the client with their session1 id
		WebClient client = new WebClient();
		webServer.getClients().put(ID1, client);

		//Create the request
		HttpServletRequest request = createMockedRequest(ID1, null, true);
		
		assertSame(client, webServer.getUser(request));
	}
	
	/**
	 * Test method for {@link WebServer#getUser(HttpServletRequest)}, when an unknown user is
	 * requested.
	 */
	@Test
	public void testGetUser_known_session2() {
		//Add the client with their session2 id
		WebClient client = new WebClient();
		webServer.getClients().put(ID2, client);

		//Create the request with an unknown session1 id and a known session2 id
		HttpServletRequest request = createMockedRequest(ID1, ID2, true);
		
		assertSame(client, webServer.getUser(request));
	}

	/**
	 * Test method for {@link WebServer#handleAuthentication(HttpServletRequest, HttpServletResponse)},
	 * when the user connecting is not known.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs
	 */
	@Test
	public void testHandleAuthentication_unknown() throws IOException {
		//Create request and response mocks
		HttpServletRequest request = createMockedRequest(ID1, null, false);
		HttpServletResponse response = createMockedResponse();

		//Set the waitning GameState
		TestUtil.setGameState(GameState.WAITING);

		//Game is not full, so user should get authenticated
		assertTrue(webServer.handleAuthentication(request, response));

		//The user should have been added
		assertEquals(1, webServer.getClients().size());

		//Verify that the cookie has been added, and that the response is correct
		verify(response).addCookie(any());
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("AUTHENTICATED");
	}
	
	/**
	 * Test method for {@link WebServer#handleAuthentication(HttpServletRequest, HttpServletResponse)},
	 * when the user connecting is not known, and the game is in progress.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs
	 */
	@Test
	public void testHandleAuthentication_unknown_inProgress() throws IOException {
		//Create request and response mocks
		HttpServletRequest request = createMockedRequest(ID1, null, false);
		HttpServletResponse response = createMockedResponse();

		//Set the running GameState
		TestUtil.setGameState(GameState.RUNNING);

		//Game is in progress, so user should not get authenticated
		assertFalse(webServer.handleAuthentication(request, response));

		//The user should not have been added
		assertEquals(0, webServer.getClients().size());

		//Verify that the response says the game is in progress
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("IN_PROGRESS");
	}
	
	/**
	 * Test method for {@link WebServer#handleAuthentication(HttpServletRequest, HttpServletResponse)},
	 * when the user connecting is not known, and the game is full.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs
	 */
	@Test
	public void testHandleAuthentication_unknown_full() throws IOException {
		//Ensure that the game is full
		for (int i = 0; i < WebServer.MAX_PLAYERS; i++) {
			webServer.getClients().put("" + i, new WebClient());
		}

		//Create request and response mocks
		HttpServletRequest request = createMockedRequest(ID1, null, false);
		HttpServletResponse response = createMockedResponse();

		//Set the waiting GameState
		TestUtil.setGameState(GameState.WAITING);

		//Game is full, so user should not get authenticated
		assertFalse(webServer.handleAuthentication(request, response));

		//The user should not have been added
		assertEquals(WebServer.MAX_PLAYERS, webServer.getClients().size());

		//Verify the response says the game is full
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("FULL");
	}
	
	/**
	 * Test method for {@link WebServer#handleAuthentication(HttpServletRequest, HttpServletResponse)},
	 * when the user connecting is known, and cookies are matching.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs
	 */
	@Test
	public void testHandleAuthentication_known_matching() throws IOException {
		//Add the client
		webServer.getClients().put(ID1, new WebClient());

		//Create request and response mocks
		HttpServletRequest request = createMockedRequest(ID1, ID1, true);
		HttpServletResponse response = createMockedResponse();

		//Set the waiting GameState
		TestUtil.setGameState(GameState.WAITING);

		//Game is not full, so users should get authenticated
		assertTrue(webServer.handleAuthentication(request, response));

		//The user should have been added
		assertEquals(1, webServer.getClients().size());

		//The user should get an authenticated response
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("AUTHENTICATED");
	}
	
	/**
	 * Test method for {@link WebServer#handleAuthentication(HttpServletRequest, HttpServletResponse)},
	 * when the user connecting is known, and cookies are NOT matching.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs
	 */
	@Test
	public void testHandleAuthentication_known_notMatching() throws IOException {
		//Add the client
		webServer.getClients().put(ID2, new WebClient());

		//Create request and response mocks
		HttpServletRequest request = createMockedRequest(ID1, ID2, true);
		HttpServletResponse response = createMockedResponse();

		//Set the wating GameState
		TestUtil.setGameState(GameState.WAITING);

		//Game is not full, so user should get authenticated
		assertTrue(webServer.handleAuthentication(request, response));

		//The user should have been added TWICE, as both their ID's, but we should have only one unique user
		assertEquals(2, webServer.getClients().size());
		assertEquals(1, webServer.getUniqueClientCount());

		//Verify the response sends "UPDATED"
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("UPDATED");
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
	 * Test method for {@link WebServer#createCookie(HttpServletRequest)}.
	 */
	@Test
	public void testCreateCookie() {
		//Create a mock request
		HttpServletRequest request = createMockedRequest("ID", null, false);
		
		Cookie cookie = WebServer.createCookie(request);

		//Check if correct information was set on the cookie
		assertEquals(WebServer.SESSION2_COOKIE, cookie.getName());
		assertEquals(request.getSession().getId(), cookie.getValue());
		assertEquals(24 * 60 * 60, cookie.getMaxAge());
	}

}
