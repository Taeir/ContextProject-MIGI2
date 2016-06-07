package nl.tudelft.contextproject.webinterface;

import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link ClientServlet}.
 */
public class ClientServletTest extends WebTestBase {
	private static final String ID = "TESTID";
	
	public WebServer webServer;
	public ClientServlet servlet;
	public NormalHandler handler;

	/**
	 * Creates a new WebServer, ClientServlet and NormalHandler before every test, and sets the
	 * game state to WAITING.
	 */
	@Before
	public void setUp() {
		webServer = spy(new WebServer());
		servlet = spy(new ClientServlet(webServer));
		handler = spy(new NormalHandler(webServer));
		
		webServer.setNormalHandler(handler);
		
		TestUtil.setGameState(GameState.WAITING);
	}

	/**
	 * Clean up the servlet after every test.
	 */
	@After
	public void tearDown() {
		servlet.destroy();
		
		webServer = null;
		servlet = null;
		handler = null;
	}

	/**
	 * Test method for {@link ClientServlet#doGet}.
	 *
	 * @throws Exception
	 * 		if an exception occurs calling doGet of the servlet
	 */
	@Test
	public void testDoGet_index() throws Exception {
		HttpServletRequest request = createMockedRequest(ID, true, "/");
		HttpServletResponse response = createMockedResponse();

		servlet.doGet(request, response);

		//Verify that we have been redirected to the index.html page
		verify(response).sendRedirect("/index.html");
	}

	/**
	 * Test method for {@link ClientServlet#doGet}.
	 *
	 * @throws Exception
	 * 		if an exception occurs calling doGet of the servlet
	 */
	@Test
	public void testDoGet_qr() throws Exception {
		HttpServletRequest request = createMockedRequest(ID, true, "/qr");
		HttpServletResponse response = createMockedResponse();
		
		//Ensure that the original method does not get called
		doNothing().when(handler).onQrRequest(any());

		servlet.doGet(request, response);

		//Verify that onQrRequest has been called
		verify(handler).onQrRequest(response);
	}

	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /indexrefresh.
	 * 
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet
	 */
	@Test
	public void testDoPost_indexrefresh() throws Exception {
		HttpServletRequest request = createMockedRequest(ID, false, "/indexrefresh");
		HttpServletResponse response = createMockedResponse();

		//Ensure that the original method does not get called
		doNothing().when(handler).onIndexRefresh(any(), any());

		//Call the post
		servlet.doPost(request, response);

		//Verify that onIndexRefresh has been called
		verify(handler).onIndexRefresh(request, response);
	}
	
	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /login.
	 * 
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet
	 */
	@Test
	public void testDoPost_login() throws Exception {
		HttpServletRequest request = createMockedRequest(ID, false, "/login");
		HttpServletResponse response = createMockedResponse();

		//Ensure that the original method does not get called
		doNothing().when(handler).onJoinRequest(any(), any());

		//Call the post
		servlet.doPost(request, response);

		//Verify that onJoinRequest has been called
		verify(handler).onJoinRequest(request, response);
	}
	
	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /setteam.
	 * 
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet
	 */
	@Test
	public void testDoPost_setteam() throws Exception {
		//Create a request to set the team to elves.
		HttpServletRequest request = createMockedRequest(ID, false, "/setteam");
		setParameter(request, "team", Team.ELVES.name());
		
		HttpServletResponse response = createMockedResponse();

		//Ensure that the original method does not get called
		doNothing().when(handler).onSetTeamRequest(any(), any());

		//Call the post
		servlet.doPost(request, response);

		//Verify that onSetTeamRequest has been called
		verify(handler).onSetTeamRequest(request, response);
	}
	
	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /map.
	 * 
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet
	 */
	@Test
	public void testDoPost_map() throws Exception {
		//Create a request to get the map
		HttpServletRequest request = createMockedRequest(ID, false, "/map");
		HttpServletResponse response = createMockedResponse();

		//Ensure that the original method does not get called
		doNothing().when(handler).onMapRequest(any(), any());

		//Call the post
		servlet.doPost(request, response);

		//Verify that onMapRequest method has been called
		verify(handler).onMapRequest(request, response);
	}
	
	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /status.
	 *
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet
	 */
	@Test
	public void testDoPost_status() throws Exception {
		//Create a request to get the status
		HttpServletRequest request = createMockedRequest(ID, false, "/status");
		HttpServletResponse response = createMockedResponse();

		//Ensure that the original method does not get called
		doNothing().when(handler).onStatusUpdateRequest(any(), any());

		//Call the post
		servlet.doPost(request, response);

		//Verify that the statusUpdate method has been called
		verify(handler).onStatusUpdateRequest(request, response);
	}

	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /requestaction.
	 *
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet
	 */
	@Test
	public void testDoPost_requestaction() throws Exception {
		//Create a request to request an action
		HttpServletRequest request = createMockedRequest(ID, false, "/requestaction");
		HttpServletResponse response = createMockedResponse();

		//Ensure that the original method does not get called
		doNothing().when(handler).onActionRequest(any(), any());

		//Call the post
		servlet.doPost(request, response);

		//Verify that the onActionRequest method has been called
		verify(handler).onActionRequest(request, response);
	}
}
