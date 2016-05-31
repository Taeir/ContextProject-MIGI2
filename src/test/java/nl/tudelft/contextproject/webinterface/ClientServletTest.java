package nl.tudelft.contextproject.webinterface;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.RandomLevelFactory;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.test.TestUtil;
import nl.tudelft.contextproject.util.QRGenerator;

/**
 * Test class for {@link ClientServlet}.
 */
public class ClientServletTest extends WebTestBase {
	private static final String ID1 = "TESTID1";
	private static final String JSON_CONTENT_TYPE = "text/json";
	private static final String JSON_UNAUTHORIZED = "{auth: false}";
	private static final String SET_TEAM = "/setteam";
	private static final String TEAM = "team";

	private static Level level;
	
	public WebServer webServer;
	public ClientServlet servlet;

	/**
	 * Initializes a level for the tests.
	 */
	@BeforeClass
	public static void initializeLevel() {
		//Generate a new seeded level
		level = new RandomLevelFactory(5, false).generateSeeded(1);
	}

	/**
	 * Creates a new GameController and a new ClientServlet before every test, and sets the game
	 * state to WAITING.
	 */
	@Before
	public void setUp() {
		//Create a new controller and set it
		GameController controller = new GameController(Main.getInstance(), level);
		Main.getInstance().setController(controller);
		
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
	 * 		if an exception occurs calling doGet of the servlet
	 */
	@Test
	public void testDoGet_index() throws Exception {
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, true, "/");
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
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, true, "/qr");
		HttpServletResponse response = createMockedResponse();

		servlet.doGet(request, response);

		//Verify that we have been redirected to the QRCode page
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("<html><body><img src=\"data:image/png;base64,");
		verify(response.getWriter()).write(Base64.getEncoder().encodeToString(QRGenerator.getInstance().streamQRcode().toByteArray()));
		verify(response.getWriter()).write("\"/></body></html>");
	}

	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /login.
	 * 
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet
	 */
	@Test
	public void testDoPost_login() throws Exception {
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, false, "/login");
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
	 * 		if an exception occurs calling doPost of the servlet
	 */
	@Test
	public void testDoPost_setteam() throws Exception {
		//Create a request to set the team to elves.
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, false, SET_TEAM);
		setParameter(request, TEAM, "ELVES");
		
		HttpServletResponse response = createMockedResponse();

		//Ensure that the original method does not get called
		doNothing().when(servlet).setTeam(any(), any());

		//Call the post
		servlet.doPost(request, response);

		//Verify that setTeam has been called
		verify(servlet).setTeam(request, response);
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
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, false, "/map");
		HttpServletResponse response = createMockedResponse();

		//Ensure that the original method does not get called
		doNothing().when(servlet).getMap(any(), any());

		//Call the post
		servlet.doPost(request, response);

		//Verify that getMap method has been called
		verify(servlet).getMap(request, response);
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
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, false, "/status");
		HttpServletResponse response = createMockedResponse();

		//Ensure that the original method does not get called
		doNothing().when(servlet).statusUpdate(any(), any());

		//Call the post
		servlet.doPost(request, response);

		//Verify that the statusUpdate method has been called
		verify(servlet).statusUpdate(request, response);
	}

	/**
	 * Test method for {@link ClientServlet#doPost}, when posting to /requestaction.
	 *
	 * @throws Exception
	 * 		if an exception occurs calling doPost of the servlet
	 */
	@Test
	public void testDoPost_requestaction() throws Exception {
		//Create a request to get the status
		HttpServletRequest request = createMockedRequest(ID1, ID1, false, false, "/requestaction");
		HttpServletResponse response = createMockedResponse();

		//Ensure that the original method does not get called
		doNothing().when(servlet).statusUpdate(any(), any());

		//Call the post
		servlet.doPost(request, response);

		//Verify that the requestAction method has been called
		verify(servlet).requestAction(request, response);
	}

	/**
	 * Test method for {@link ClientServlet#checkAuthorized}, when the user is not authorized, and
	 * the response is in plain text.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling checkAuthorized of the servlet
	 */
	@Test
	public void testCheckAuthorized_notAuthorized_plain() throws IOException {
		HttpServletResponse response = createMockedResponse();

		//Should not be authorized
		assertFalse(servlet.checkAuthorized(null, response, false));

		//Proper response should have been made in plain text
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("UNAUTHORIZED");
	}
	
	/**
	 * Test method for {@link ClientServlet#checkAuthorized}, when the user is not authorized, and
	 * the response is in json format.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling checkAuthorized of the servlet
	 */
	@Test
	public void testCheckAuthorized_notAuthorized_json() throws IOException {
		HttpServletResponse response = createMockedResponse();

		//Should not be authorized
		assertFalse(servlet.checkAuthorized(null, response, true));

		//Response should have ben done in JSON format
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response).setContentType(JSON_CONTENT_TYPE);
		verify(response.getWriter()).write(JSON_UNAUTHORIZED);
	}
	
	/**
	 * Test method for {@link ClientServlet#checkAuthorized}, when the user is authorized.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling checkAuthorized of the servlet
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
	 * 		if an IOException occurs calling setTeam of the servlet
	 */
	@Test
	public void testSetTeam_unauthorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, SET_TEAM);
		HttpServletResponse response = createMockedResponse();
		
		servlet.setTeam(request, response);

		//Verify that the user was given an UNAUTHORIZED response
		verify(response.getWriter()).write("UNAUTHORIZED");
	}
	
	/**
	 * Test method for {@link ClientServlet#setTeam}, when the game is already running.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling setTeam of the servlet
	 */
	@Test
	public void testSetTeam_running() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, SET_TEAM);
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
	 * 		if an IOException occurs calling setTeam of the servlet
	 */
	@Test
	public void testSetTeam_noParameter() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, SET_TEAM);
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
	 * 		if an IOException occurs calling setTeam of the servlet
	 */
	@Test
	public void testSetTeam_dwarfs() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, SET_TEAM);
		setParameter(request, TEAM, "DWARFS");
		
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
	 * 		if an IOException occurs calling setTeam of the servlet
	 */
	@Test
	public void testSetTeam_elves() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, SET_TEAM);
		setParameter(request, TEAM, "ELVES");
		
		HttpServletResponse response = createMockedResponse();

		//Simulate that the user is authorized
		WebClient client = spy(new WebClient());
		doReturn(client).when(webServer).getUser(any());

		//Set the team
		servlet.setTeam(request, response);

		//The team should have been set to true
		assertTrue(client.isElf());
		verify(client).setTeam(true);

		//ELVES should have been written
		verify(response.getWriter()).write("ELVES");
	}
	
	/**
	 * Test method for {@link ClientServlet#setTeam}, when the team is set to none.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling setTeam of the servlet
	 */
	@Test
	public void testSetTeam_none() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, SET_TEAM);
		setParameter(request, TEAM, "NONE");
		
		HttpServletResponse response = createMockedResponse();

		//Simulate that the user is authorized, and is an elf at the start
		WebClient client = spy(new WebClient());
		client.setTeam(true);
		doReturn(client).when(webServer).getUser(any());

		//Set the team
		servlet.setTeam(request, response);

		//The team should have been set to "None"
		assertEquals("None", client.getTeam());
		verify(client).setTeam(null);

		//None should have been written
		verify(response.getWriter()).write("NONE");
	}
	
	/**
	 * Test method for {@link ClientServlet#setTeam}, when the team is set to an invalid team.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling setTeam of the servlet
	 */
	@Test
	public void testSetTeam_invalid() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, SET_TEAM);
		setParameter(request, TEAM, "THEATEAM");
		
		HttpServletResponse response = createMockedResponse();

		//Simulate that the user is authorized
		WebClient client = spy(new WebClient());
		doReturn(client).when(webServer).getUser(any());

		//Set the team
		servlet.setTeam(request, response);

		//The team should not have been changed
		verify(client, never()).setTeam(any());

		//INVALID should have been written
		verify(response.getWriter()).write("INVALID");
	}
	
	/**
	 * Test method for {@link ClientServlet#getMap}, when the user is unauthorized.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling getMap of the servlet
	 */
	@Test
	public void testGetMap_unauthorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, "/map");
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
	 * 		if an IOException occurs calling getMap of the servlet
	 */
	@Test
	public void testGetMap_authorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, "/map");
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
	 * Test method for {@link ClientServlet#statusUpdate}, when the user is unauthorized.
	 * 
	 * @throws IOException
	 * 		if an IOException occurs calling statusUpdate of the servlet
	 */
	@Test
	public void testStatusUpdate_unauthorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, "/status");
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
	 * 		if an IOException occurs calling statusUpdate of the servlet
	 */
	@Test
	public void testStatusUpdate_authorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, "/status");
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

	/**
	 * Test method for {@link ClientServlet#requestAction}, when the user is unauthorized.
	 *
	 * @throws IOException
	 * 		if an IOException occurs calling requestAction of the servlet
	 */
	@Test
	public void testRequestAction_unauthorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, "/requestaction");
		HttpServletResponse response = createMockedResponse();

		servlet.requestAction(request, response);

		//{auth: false} JSON should have been written
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("UNAUTHORIZED");
	}

	/**
	 * Test method for {@link ClientServlet#requestAction}, when the user is authorized.
	 *
	 * @throws IOException
	 * 		if an IOException occurs calling requestAction of the servlet
	 */
	@Test
	public void testRequestAction_authorized() throws IOException {
		HttpServletRequest request = createMockedRequest(ID1, ID1, true, false, "/requestaction");
		HttpServletResponse response = createMockedResponse();

		//Simulate that the user is authorized
		WebClient client = spy(new WebClient());
		client.setTeam(false);
		doReturn(client).when(webServer).getUser(any());

		when(request.getParameter(anyString())).thenReturn("0");

		Game mockedGame = mock(Game.class);
		Level mockedLevel = mock(Level.class);
		MazeTile mockedTile = mock(MazeTile.class);
		VRPlayer mockedPlayer = mock(VRPlayer.class);

		when(Main.getInstance().getCurrentGame()).thenReturn(mockedGame);
		when(mockedGame.getLevel()).thenReturn(mockedLevel);
		when(mockedLevel.getTile(anyInt(), anyInt())).thenReturn(mockedTile);
		when(mockedTile.getTileType()).thenReturn(TileType.FLOOR);
		when(mockedGame.getEntities()).thenReturn(new HashSet<>());
		when(mockedGame.getPlayer()).thenReturn(mockedPlayer);
		when(mockedPlayer.getLocation()).thenReturn(new Vector3f(1000, 1000, 1000));

		servlet.requestAction(request, response);

		//Some JSON should have been written
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("ACTION PERFORMED");
	}

	/**
	 * Test method for {@link ClientServlet#attemptAction}, when the action is invalid.
	 *
	 * @throws IOException
	 * 		if an IOException occurs calling requestAction of the servlet
	 */
	@Test
	public void testAttemptAction_incorrectAction() throws IOException {
		HttpServletResponse response = createMockedResponse();
		WebClient mockedClient = mock(WebClient.class);
		Map<Action, List<Long>> performedActions = new HashMap<>();
		performedActions.put(Action.DROPBAIT, new ArrayList<>());

		when(mockedClient.getTeam()).thenReturn("Elves");
		when(mockedClient.getPerformedActions()).thenReturn(performedActions);

		//Try to place a bomb as an elf, which is impossible
		servlet.attemptAction(0, 0, Action.PLACEBOMB, mockedClient, response);

		//Verify the action has been denied
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("ACTION INVALID, NOT PERFORMED");
	}

	/**
	 * Test method for {@link ClientServlet#attemptAction}, when the action is on an invalid location.
	 *
	 * @throws IOException
	 * 		if an IOException occurs calling requestAction of the servlet
	 */
	@Test
	public void testAttemptAction_incorrectLocation() throws IOException {
		HttpServletResponse response = createMockedResponse();

		Game mockedGame = mock(Game.class);
		Level mockedLevel = mock(Level.class);
		MazeTile mockedTile = mock(MazeTile.class);
		WebClient mockedClient = mock(WebClient.class);

		Map<Action, List<Long>> performedActions = new HashMap<>();
		performedActions.put(Action.PLACEBOMB, new ArrayList<>());

		when(mockedClient.getTeam()).thenReturn("Dwarfs");
		when(mockedClient.getPerformedActions()).thenReturn(performedActions);
		when(Main.getInstance().getCurrentGame()).thenReturn(mockedGame);
		when(mockedGame.getLevel()).thenReturn(mockedLevel);
		when(mockedLevel.getTile(anyInt(), anyInt())).thenReturn(mockedTile);
		when(mockedTile.getTileType()).thenReturn(TileType.WALL);

		//Try to place a bomb as a dwarf
		servlet.attemptAction(0, 0, Action.PLACEBOMB, mockedClient, response);

		//Verify the action has been accepted
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("ACTION ON INVALID LOCATION, NOT PERFORMED");
	}


	/**
	 * Test method for {@link ClientServlet#attemptAction}, when the action is on an invalid location.
	 *
	 * @throws IOException
	 * 		if an IOException occurs calling requestAction of the servlet
	 */
	@Test
	public void testAttemptAction_incorrectCooldown() throws IOException {
		HttpServletResponse response = createMockedResponse();

		Game mockedGame = mock(Game.class);
		Level mockedLevel = mock(Level.class);
		MazeTile mockedTile = mock(MazeTile.class);
		VRPlayer mockedPlayer = mock(VRPlayer.class);
		WebClient mockedClient = mock(WebClient.class);

		Map<Action, List<Long>> performedActions = new HashMap<>();
		ArrayList<Long> set = new ArrayList<>();

		for (int i = 0; i < Action.PLACEBOMB.getMaxAmount(); i++) {
			set.add(Long.MAX_VALUE);
		}

		performedActions.put(Action.PLACEBOMB, set);

		when(mockedClient.getTeam()).thenReturn("Dwarfs");
		when(mockedClient.getPerformedActions()).thenReturn(performedActions);

		when(Main.getInstance().getCurrentGame()).thenReturn(mockedGame);
		when(mockedGame.getLevel()).thenReturn(mockedLevel);
		when(mockedLevel.getTile(anyInt(), anyInt())).thenReturn(mockedTile);
		when(mockedTile.getTileType()).thenReturn(TileType.FLOOR);
		when(mockedGame.getEntities()).thenReturn(new HashSet<>());
		when(mockedGame.getPlayer()).thenReturn(mockedPlayer);
		when(mockedPlayer.getLocation()).thenReturn(new Vector3f(1000, 1000, 1000));

		//Try to place a bomb as a dwarf
		servlet.attemptAction(0, 0, Action.PLACEBOMB, mockedClient, response);

		//Verify the action has been accepted
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("ACTION IN COOLDOWN, NOT PERFORMED");
	}

	/**
	 * Test method for {@link ClientServlet#attemptAction}, when the action is valid.
	 *
	 * @throws IOException
	 * 		if an IOException occurs calling requestAction of the servlet
	 */
	@Test
	public void testAttemptAction_correct() throws IOException {
		HttpServletResponse response = createMockedResponse();

		Game mockedGame = mock(Game.class);
		Level mockedLevel = mock(Level.class);
		MazeTile mockedTile = mock(MazeTile.class);
		VRPlayer mockedPlayer = mock(VRPlayer.class);
		WebClient mockedClient = mock(WebClient.class);

		Map<Action, List<Long>> performedActions = new HashMap<>();
		performedActions.put(Action.PLACEBOMB, new ArrayList<>());

		when(mockedClient.getTeam()).thenReturn("Dwarfs");
		when(mockedClient.getPerformedActions()).thenReturn(performedActions);
		when(Main.getInstance().getCurrentGame()).thenReturn(mockedGame);
		when(mockedGame.getLevel()).thenReturn(mockedLevel);
		when(mockedLevel.getTile(anyInt(), anyInt())).thenReturn(mockedTile);
		when(mockedTile.getTileType()).thenReturn(TileType.FLOOR);
		when(mockedGame.getEntities()).thenReturn(new HashSet<>());
		when(mockedGame.getPlayer()).thenReturn(mockedPlayer);
		when(mockedPlayer.getLocation()).thenReturn(new Vector3f(1000, 1000, 1000));

		//Try to place a bomb as a dwarf
		servlet.attemptAction(0, 0, Action.PLACEBOMB, mockedClient, response);

		//Verify the action has been accepted
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("ACTION PERFORMED");
	}
}
