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
	 * Test method for {@link WebServer#handleAuthentication(HttpServletRequest, HttpServletResponse)},
	 * when the user connecting is not known.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs
	 */
	@Test
	public void testHandleAuthentication_unknown() throws IOException {
		//Create request and response mocks
		HttpServletRequest request = createMockedRequest(null);
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
		HttpServletRequest request = createMockedRequest(null);
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
		HttpServletRequest request = createMockedRequest(null);
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
	 * when the user connecting is known.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs
	 */
	@Test
	public void testHandleAuthentication_known() throws IOException {
		//Add the client
		webServer.getClients().put(ID, new WebClient());

		//Create request and response mocks
		HttpServletRequest request = createMockedRequest(ID);
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

}
