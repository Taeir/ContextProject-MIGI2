package nl.tudelft.contextproject.webinterface;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.RandomLevelFactory;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link ClientServlet}.
 */
public class ClientServletTest extends WebTestBase {
	private static final String ID1 = "TESTID1";
	private static final String JSON_CONTENT_TYPE = "text/json";
	private static final String JSON_UNAUTHORIZED = "{auth: false}";

	public WebServer webServer;
	public ClientServlet servlet;
	
	/**
	 * Initializes a level for the tests.
	 */
	@BeforeClass
	public static void initializeLevel() {
		//Create a new seeded level
		Level level = new RandomLevelFactory(5, false).generateSeeded(1);
		
		//Create a new controller with that level
		GameController controller = new GameController(Main.getInstance(), level);
		
		//Set the controller on Main
		Main.getInstance().setController(controller);
	}

	/**
	 * Creates a new ClientServlet before every test, and sets the game state to WAITING.
	 */
	@Before
	public void setUp() {
		webServer = spy(new WebServer());
		servlet = spy(new ClientServlet(webServer));
		
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
	}

	/**
	 * Test method for {@link ClientServlet#doGet}.
	 * 
	 * @throws Exception
	 * 		if an exception occurs calling doGet of the servlet.
	 */
	@Test
	public void testDoGet() throws Exception {
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, "GET", "/");
		HttpServletResponse response = createMockedResponse();
		
		servlet.doGet(request, response);
		
		//Verify that we have been redirected to the index.html page.
		verify(response).sendRedirect("/index.html");
	}

	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /login.
	 * 
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet.
	 */
	@Test
	public void testDoPost_login() throws Exception {
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, "POST", "/login");
		HttpServletResponse response = createMockedResponse();
		
		//Ensure that the original method does not get called
		doReturn(false).when(webServer).handleAuthentication(any(), any());
		
		//Call the post
		servlet.doPost(request, response);
		
		//Verify that handleAuthentication has been called
		verify(webServer).handleAuthentication(request, response);
	}
	
	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /setteam.
	 * 
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet.
	 */
	@Test
	public void testDoPost_setteam() throws Exception {
		//Create a request to set the team to ELVES.
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, "POST", "/setteam");
		setParameter(request, "team", "ELVES");
		
		HttpServletResponse response = createMockedResponse();
		
		//Ensure that the original method does not get called
		doNothing().when(servlet).setTeam(any(), any());
		
		//Call the post
		servlet.doPost(request, response);
		
		//Verify that setTeam method has been called
		verify(servlet).setTeam(request, response);
	}
	
	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /map.
	 * 
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet.
	 */
	@Test
	public void testDoPost_map() throws Exception {
		//Create a request to get the map
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, "POST", "/map");
		HttpServletResponse response = createMockedResponse();
		
		//Ensure that the original method does not get called
		doNothing().when(servlet).getMap(any(), any());
		
		//Call the post
		servlet.doPost(request, response);
		
		//Verify that getMap method has been called
		verify(servlet).getMap(request, response);
	}
	
	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /explored.
	 * 
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet.
	 */
	@Test
	public void testDoPost_explored() throws Exception {
		//Create a request to get explored tiles
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, "POST", "/explored");
		HttpServletResponse response = createMockedResponse();
		
		//Ensure that the original method does not get called
		doNothing().when(servlet).getExplored(any(), any());
		
		//Call the post
		servlet.doPost(request, response);
		
		//Verify that the getExplored method has been called
		verify(servlet).getExplored(request, response);
	}
	
	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /status.
	 * 
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet.
	 */
	@Test
	public void testDoPost_status() throws Exception {
		//Create a request to get the status
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, "POST", "/status");
		HttpServletResponse response = createMockedResponse();
		
		//Ensure that the original method does not get called
		doNothing().when(servlet).statusUpdate(any(), any());
		
		//Call the post
		servlet.doPost(request, response);
		
		//Verify that the statusUpdate method has been called
		verify(servlet).statusUpdate(request, response);
	}

	/**
	 * Test method for {@link ClientServlet#checkAuthorized}, when the user is not authorized, and
	 * the response is in plain text.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling checkAuthorized of the servlet.
	 */
	@Test
	public void testCheckAuthorized_notAuthorized_plain() throws IOException {
		HttpServletResponse response = createMockedResponse();
		
		//Should not be authorized
		assertFalse(servlet.checkAuthorized(null, response, false));
		
		//Proper response should have been made in plain text.
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("UNAUTHORIZED");
	}
	
	/**
	 * Test method for {@link ClientServlet#checkAuthorized}, when the user is not authorized, and
	 * the response is in json format.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling checkAuthorized of the servlet.
	 */
	@Test
	public void testCheckAuthorized_notAuthorized_json() throws IOException {
		HttpServletResponse response = createMockedResponse();
		
		//Should not be authorized
		assertFalse(servlet.checkAuthorized(null, response, true));
		
		//Response should have been done in JSON format
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response).setContentType(JSON_CONTENT_TYPE);
		verify(response.getWriter()).write(JSON_UNAUTHORIZED);
	}
	
	/**
	 * Test method for {@link ClientServlet#checkAuthorized}, when the user is authorized.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling checkAuthorized of the servlet.
	 */
	@Test
	public void testCheckAuthorized_authorized() throws IOException {
		HttpServletResponse response = createMockedResponse();
		
		//Should be authorized
		assertTrue(servlet.checkAuthorized(new WebClient(), response, false));
		
		//Response should not have been modified
		verifyZeroInteractions(response);
	}

	/**
	 * Test method for {@link ClientServlet#setTeam}, when the user is unauthorized.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling setTeam of the servlet.
	 */
	@Test
	public void testSetTeam_unauthorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/setteam");
		HttpServletResponse response = createMockedResponse();
		
		servlet.setTeam(request, response);
		
		//Verify that the user was given an UNAUTHORIZED response
		verify(response.getWriter()).write("UNAUTHORIZED");
	}
	
	/**
	 * Test method for {@link ClientServlet#setTeam}, when the game is already running.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling setTeam of the servlet.
	 */
	@Test
	public void testSetTeam_running() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/setteam");
		HttpServletResponse response = createMockedResponse();
		
		//Set the game state to in progress
		TestUtil.setGameState(GameState.RUNNING);
		
		//Simulate that the user is authorized
		WebClient client = spy(new WebClient());
		doReturn(client).when(webServer).getUser(any());
		
		//Put the client in FAKETEAM
		when(client.getTeam()).thenReturn("FAKETEAM");
		
		servlet.setTeam(request, response);
		
		//The clients current team, FAKETEAM, should have been written
		verify(response.getWriter()).write("FAKETEAM");
	}
	
	/**
	 * Test method for {@link ClientServlet#setTeam}, when no team parameter was passed.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling setTeam of the servlet.
	 */
	@Test
	public void testSetTeam_noParameter() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/setteam");
		HttpServletResponse response = createMockedResponse();
		
		//Simulate that the user is authorized
		WebClient client = spy(new WebClient());
		doReturn(client).when(webServer).getUser(any());
		
		//Set the team
		servlet.setTeam(request, response);
		
		//INVALID should have been written
		verify(response.getWriter()).write("INVALID");
	}
	
	/**
	 * Test method for {@link ClientServlet#setTeam}, when the team is set to dwarfs.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling setTeam of the servlet.
	 */
	@Test
	public void testSetTeam_dwarfs() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/setteam");
		setParameter(request, "team", "DWARFS");
		
		HttpServletResponse response = createMockedResponse();
		
		//Simulate that the user is authorized
		WebClient client = spy(new WebClient());
		doReturn(client).when(webServer).getUser(any());
		
		//Set the team
		servlet.setTeam(request, response);
		
		//The team should have been set to false
		assertTrue(client.isDwarf());
		verify(client).setTeam(false);
		
		//DWARFS should have been written
		verify(response.getWriter()).write("DWARFS");
	}
	
	/**
	 * Test method for {@link ClientServlet#setTeam}, when the team is set to elves.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling setTeam of the servlet.
	 */
	@Test
	public void testSetTeam_elves() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/setteam");
		setParameter(request, "team", "ELVES");
		
		HttpServletResponse response = createMockedResponse();
		
		//Simulate that the user is authorized
		WebClient client = spy(new WebClient());
		doReturn(client).when(webServer).getUser(any());
		
		//Set the team
		servlet.setTeam(request, response);
		
		//The team should have been set to true
		assertTrue(client.isElf());
		verify(client).setTeam(true);
		
		//DWARFS should have been written
		verify(response.getWriter()).write("ELVES");
	}
	
	/**
	 * Test method for {@link ClientServlet#setTeam}, when the team is set to none.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling setTeam of the servlet.
	 */
	@Test
	public void testSetTeam_none() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/setteam");
		setParameter(request, "team", "NONE");
		
		HttpServletResponse response = createMockedResponse();
		
		//Simulate that the user is authorized, and is an elf at the start.
		WebClient client = spy(new WebClient());
		client.setTeam(true);
		doReturn(client).when(webServer).getUser(any());
		
		//Set the team
		servlet.setTeam(request, response);
		
		//The team should have been set to false
		assertEquals("None", client.getTeam());
		verify(client).setTeam(null);
		
		//NONE should have been written
		verify(response.getWriter()).write("NONE");
	}
	
	/**
	 * Test method for {@link ClientServlet#setTeam}, when the team is set to an invalid team.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling setTeam of the servlet.
	 */
	@Test
	public void testSetTeam_invalid() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/setteam");
		setParameter(request, "team", "THEATEAM");
		
		HttpServletResponse response = createMockedResponse();
		
		//Simulate that the user is authorized
		WebClient client = spy(new WebClient());
		doReturn(client).when(webServer).getUser(any());
		
		//Set the team
		servlet.setTeam(request, response);
		
		//The team should have not been changed
		verify(client, never()).setTeam(any());
		
		//INVALID should have been written
		verify(response.getWriter()).write("INVALID");
	}
	
	/**
	 * Test method for {@link ClientServlet#getMap}, when the user is unauthorized.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling getMap of the servlet.
	 */
	@Test
	public void testGetMap_unauthorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/map");
		HttpServletResponse response = createMockedResponse();
		
		servlet.getMap(request, response);
		
		//auth: false should have been written
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response).setContentType(JSON_CONTENT_TYPE);
		verify(response.getWriter()).write(JSON_UNAUTHORIZED);
	}

	/**
	 * Test method for {@link ClientServlet#getMap}, when the user is authorized.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling getMap of the servlet.
	 */
	@Test
	public void testGetMap_authorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/map");
		HttpServletResponse response = createMockedResponse();
		
		//Simulate that the user is authorized
		WebClient client = spy(new WebClient());
		doReturn(client).when(webServer).getUser(any());
		
		servlet.getMap(request, response);
		
		//Some JSON should have been written
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response).setContentType(JSON_CONTENT_TYPE);
		verify(response.getWriter()).write(matches("\\{.*\\}"));
	}
	
	/**
	 * Test method for {@link ClientServlet#getExplored}, when the user is unauthorized.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling getExplored of the servlet.
	 */
	@Test
	public void testGetExplored_unauthorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/explored");
		HttpServletResponse response = createMockedResponse();
		
		servlet.getExplored(request, response);
		
		//auth: false should have been written
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response).setContentType(JSON_CONTENT_TYPE);
		verify(response.getWriter()).write(JSON_UNAUTHORIZED);
	}

	/**
	 * Test method for {@link ClientServlet#getExplored}, when the user is authorized.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling getExplored of the servlet.
	 */
	@Test
	public void testGetExplored_authorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/explored");
		HttpServletResponse response = createMockedResponse();
		
		//Simulate that the user is authorized
		WebClient client = spy(new WebClient());
		doReturn(client).when(webServer).getUser(any());
		
		servlet.getExplored(request, response);
		
		//Some JSON should have been written
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response).setContentType(JSON_CONTENT_TYPE);

		verify(response.getWriter()).write(matches("\\{.*\\}"));
	}
	
	/**
	 * Test method for {@link ClientServlet#statusUpdate}, when the user is authorized.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling statusUpdate of the servlet.
	 */
	@Test
	public void testStatusUpdate_unauthorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/status");
		HttpServletResponse response = createMockedResponse();
		
		servlet.statusUpdate(request, response);
		
		//{auth: false} JSON should have been written
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response).setContentType(JSON_CONTENT_TYPE);
		verify(response.getWriter()).write(JSON_UNAUTHORIZED);
	}

	/**
	 * Test method for {@link ClientServlet#statusUpdate}, when the user is authorized.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling statusUpdate of the servlet.
	 */
	@Test
	public void testStatusUpdate_authorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, "POST", "/status");
		HttpServletResponse response = createMockedResponse();
		
		//Simulate that the user is authorized
		WebClient client = spy(new WebClient());
		client.setTeam(true);
		doReturn(client).when(webServer).getUser(any());
		
		servlet.statusUpdate(request, response);
		
		//Some JSON should have been written
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response).setContentType(JSON_CONTENT_TYPE);
		
		ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
		verify(response.getWriter()).write(ac.capture());
		
		assertTrue(ac.getValue().contains("\"state\":\"WAITING\""));
		assertTrue(ac.getValue().contains("\"team\":\"ELVES\""));
	}

}