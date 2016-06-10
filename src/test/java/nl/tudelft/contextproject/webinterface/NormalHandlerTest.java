package nl.tudelft.contextproject.webinterface;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.TileType;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link NormalHandler}.
 */
public class NormalHandlerTest extends WebTestBase {
	private NormalHandler handler;
	private WebServer server;
	
	/**
	 * Create a new server and handler before every test.
	 */
	@Before
	public void setUp() {
		server = spy(new WebServer());
		handler = spy(new NormalHandler(server));
	}
	
	/**
	 * Test method for {@link NormalHandler#onJoinRequest}, when the user connecting is known.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnJoinRequest_known() throws IOException {
		server.getClients().put("A", new WebClient());
		
		doNothing().when(handler).attemptJoinNew(any());
		doNothing().when(handler).attemptRejoin(any());

		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		handler.onJoinRequest(request, response);
		
		verify(handler).attemptRejoin(response);
	}
	
	/**
	 * Test method for {@link NormalHandler#onJoinRequest}, when the user connecting is unknown.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnJoinRequest_unknown() throws IOException {
		doNothing().when(handler).attemptJoinNew(any());
		doNothing().when(handler).attemptRejoin(any());

		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		handler.onJoinRequest(request, response);
		
		verify(handler).attemptJoinNew(response);
	}

	/**
	 * Test method for {@link NormalHandler#attemptJoinNew}, when joining is allowed.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testAttemptJoinNew_allowed() throws IOException {
		TestUtil.setGameState(GameState.WAITING);
		
		HttpServletResponse response = createMockedResponse();
		handler.attemptJoinNew(response);
		
		//Response should be the current game state, and client should have been added.
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response).addCookie(any());
		verify(response.getWriter()).write("" + GameState.WAITING.ordinal());
		
		assertEquals(1, server.getUniqueClientCount());
	}
	
	/**
	 * Test method for {@link NormalHandler#attemptJoinNew}, when joining is not allowed as the
	 * game has already started.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testAttemptJoinNew_started() throws IOException {
		TestUtil.setGameState(GameState.RUNNING);
		
		HttpServletResponse response = createMockedResponse();
		handler.attemptJoinNew(response);
		
		//Response should be an error, and client should not have been added.
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write(COCErrorCode.AUTHENTICATE_FAIL_IN_PROGRESS.toString());
		
		assertEquals(0, server.getUniqueClientCount());
	}
	
	/**
	 * Test method for {@link NormalHandler#attemptJoinNew}, when joining is not allowed as the
	 * game is full.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testAttemptJoinNew_full() throws IOException {
		TestUtil.setGameState(GameState.WAITING);
		
		for (int i = 0; i < WebServer.MAX_PLAYERS; i++) {
			server.getClients().put("" + i, new WebClient());
		}
		
		HttpServletResponse response = createMockedResponse();
		handler.attemptJoinNew(response);
		
		//Response should be an error, and client should not have been added.
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write(COCErrorCode.AUTHENTICATE_FAIL_FULL.toString());
		
		assertEquals(WebServer.MAX_PLAYERS, server.getUniqueClientCount());
	}

	/**
	 * Test method for {@link NormalHandler#attemptRejoin}.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testAttemptRejoin() throws IOException {
		TestUtil.setGameState(GameState.RUNNING);
		
		HttpServletResponse response = createMockedResponse();
		handler.attemptRejoin(response);
		
		//Client should have rejoined
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("" + GameState.RUNNING.ordinal());
	}
	
	/**
	 * Test method for {@link NormalHandler#onQuitRequest}, when the user is known.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnQuitRequest_known() throws IOException {
		WebClient client = new WebClient();
		server.getClients().put("A", client);
		
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		handler.onQuitRequest(request, response);
		
		verify(server).disconnect(client, StatusCode.NORMAL);
		verify(response).setStatus(HttpStatus.NO_CONTENT_204);
	}
	
	/**
	 * Test method for {@link NormalHandler#onQuitRequest}, when the user is unknown.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnQuitRequest_unknown() throws IOException {
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		handler.onQuitRequest(request, response);
		
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write(COCErrorCode.UNAUTHORIZED.toString());
	}

	/**
	 * Test method for {@link NormalHandler#onIndexRefresh}, when the client can still join.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnIndexRefresh_allowed() throws IOException {
		TestUtil.setGameState(GameState.WAITING);
		
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		handler.onIndexRefresh(request, response);
		
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("y");
	}
	
	/**
	 * Test method for {@link NormalHandler#onIndexRefresh}, when the client can not join because
	 * the game has started.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnIndexRefresh_started() throws IOException {
		TestUtil.setGameState(GameState.RUNNING);
		
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		handler.onIndexRefresh(request, response);
		
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("n");
	}
	
	/**
	 * Test method for {@link NormalHandler#onIndexRefresh}, when the client can not join because
	 * the game is full.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnIndexRefresh_full() throws IOException {
		TestUtil.setGameState(GameState.WAITING);
		
		for (int i = 0; i < WebServer.MAX_PLAYERS; i++) {
			server.getClients().put("" + i, new WebClient());
		}
		
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		handler.onIndexRefresh(request, response);
		
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("n");
	}
	
	/**
	 * Test method for {@link NormalHandler#onIndexRefresh}, when the client was connected before.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnIndexRefresh_rejoin() throws IOException {
		TestUtil.setGameState(GameState.WAITING);
		
		server.getClients().put("A", new WebClient());
		
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		handler.onIndexRefresh(request, response);
		
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write("" + GameState.WAITING.ordinal());
	}

	/**
	 * Test method for {@link NormalHandler#onQrRequest}.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnQrRequest() throws IOException {
		HttpServletResponse response = createMockedResponse();
		handler.onQrRequest(response);
		
		verify(response.getWriter(), times(5)).write(anyString());
	}

	/**
	 * Test method for {@link NormalHandler#onSetTeamRequest}, when the client is unauthorized.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnSetTeamRequest_unauthorized() throws IOException {
		TestUtil.setGameState(GameState.WAITING);
		
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		
		handler.onSetTeamRequest(request, response);
		
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write(COCErrorCode.UNAUTHORIZED.toString());
	}
	
	/**
	 * Test method for {@link NormalHandler#onSetTeamRequest}, when the game has started.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnSetTeamRequest_started() throws IOException {
		TestUtil.setGameState(GameState.RUNNING);
		
		server.getClients().put("A", new WebClient());
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		
		handler.onSetTeamRequest(request, response);
		
		verify(response).setStatus(HttpStatus.OK_200);
		verify(response.getWriter()).write(COCErrorCode.SETTEAM_STARTED.toString());
	}
	
	/**
	 * Test method for {@link NormalHandler#onSetTeamRequest}, when the client's request is allowed.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnSetTeamRequest_okay() throws IOException {
		TestUtil.setGameState(GameState.WAITING);
		
		doNothing().when(handler).attemptSetTeam(any(), any(), any());
		
		WebClient client = new WebClient();
		server.getClients().put("A", client);
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		
		setParameter(request, "team", Team.ELVES.name());
		handler.onSetTeamRequest(request, response);
		
		verify(handler).attemptSetTeam(client, Team.ELVES.name(), response);
	}
	
	/**
	 * Test method for {@link NormalHandler#attemptSetTeam}, when switching to elves.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testAttemptSetTeam_elves() throws IOException {
		WebClient client = new WebClient();
		server.getClients().put("A", client);
		HttpServletResponse response = createMockedResponse();
		
		handler.attemptSetTeam(client, Team.ELVES.name(), response);
		
		//Team should have been set
		assertTrue(client.isElf());
		assertEquals(1, server.getElvesCount());
		verify(response.getWriter()).write("" + Team.ELVES.ordinal());
	}
	
	/**
	 * Test method for {@link NormalHandler#attemptSetTeam}, when switching to elves, when the
	 * elves team is already full.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testAttemptSetTeam_elves_full() throws IOException {
		when(server.getElvesCount()).thenReturn(WebServer.MAX_ELVES);
		
		WebClient client = new WebClient();
		server.getClients().put("A", client);
		HttpServletResponse response = createMockedResponse();
		
		handler.attemptSetTeam(client, Team.ELVES.name(), response);
		
		//Team should not be changed, and response should be the full error.
		assertFalse(client.isElf());
		verify(response.getWriter()).write(COCErrorCode.SETTEAM_TEAM_FULL.toString());
	}
	
	/**
	 * Test method for {@link NormalHandler#attemptSetTeam}, when switching to dwarfs.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testAttemptSetTeam_dwarfs() throws IOException {
		WebClient client = new WebClient();
		server.getClients().put("A", client);
		HttpServletResponse response = createMockedResponse();
		
		handler.attemptSetTeam(client, Team.DWARFS.name(), response);
		
		//Team should have been set
		assertTrue(client.isDwarf());
		assertEquals(1, server.getDwarfsCount());
		verify(response.getWriter()).write("" + Team.DWARFS.ordinal());
	}
	
	/**
	 * Test method for {@link NormalHandler#attemptSetTeam}, when switching to dwarfs, when the
	 * dwarfs team is already full.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testAttemptSetTeam_dwarfs_full() throws IOException {
		when(server.getDwarfsCount()).thenReturn(WebServer.MAX_DWARFS);
		
		WebClient client = new WebClient();
		server.getClients().put("A", client);
		HttpServletResponse response = createMockedResponse();
		
		handler.attemptSetTeam(client, Team.DWARFS.name(), response);
		
		//Team should not be changed, and response should be the full error.
		assertFalse(client.isDwarf());
		verify(response.getWriter()).write(COCErrorCode.SETTEAM_TEAM_FULL.toString());
	}
	
	/**
	 * Test method for {@link NormalHandler#attemptSetTeam}, when attempting to switch to an
	 * invalid team.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testAttemptSetTeam_invalid() throws IOException {
		WebClient client = new WebClient();
		server.getClients().put("A", client);
		HttpServletResponse response = createMockedResponse();
		
		handler.attemptSetTeam(client, "INVALID", response);
		
		verify(response.getWriter()).write(COCErrorCode.SETTEAM_INVALID_TEAM.toString());
	}
	
	/**
	 * Test method for {@link NormalHandler#onMapRequest(HttpServletRequest, HttpServletResponse)},
	 * when the client is not using websockets and the client is unknown.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnMapRequest_normal_unknown() throws IOException {
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		handler.onMapRequest(request, response);
		
		verify(response.getWriter()).write(COCErrorCode.UNAUTHORIZED.toJSON());
	}

	/**
	 * Test method for {@link NormalHandler#onMapRequest(HttpServletRequest, HttpServletResponse)},
	 * when the client is not using websockets and the client is known.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnMapRequest_normal_known() throws IOException {
		JSONObject json = mockLevel();
		
		server.getClients().put("A", new WebClient());
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		handler.onMapRequest(request, response);
		
		verify(response.getWriter()).write(json.toString());
	}

	/**
	 * Test method for {@link NormalHandler#onMapRequest(WebClient)}, when the client is using
	 * websockets.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnMapRequest_socket() throws IOException {
		JSONObject json = mockLevel();
		
		WebClient clientMock = mock(WebClient.class);
		handler.onMapRequest(clientMock);
		
		assertEquals(json.get("type"), "map");
		verify(clientMock).sendMessage(json, null);
	}

	/**
	 * Mocks the game and level and creates a spy JSONObject.
	 * 
	 * @return
	 * 		the spied JSONObject
	 */
	private JSONObject mockLevel() {
		Game gameMock = mock(Game.class);
		Level levelMock = mock(Level.class);
		JSONObject json = spy(new JSONObject());
		
		when(Main.getInstance().getCurrentGame()).thenReturn(gameMock);
		when(gameMock.getLevel()).thenReturn(levelMock);
		when(levelMock.toWebJSON()).thenReturn(json);
		return json;
	}

	/**
	 * Test method for {@link NormalHandler#onActionRequest(HttpServletRequest, HttpServletResponse)},
	 * when the client is not using websockets and is not known.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnActionRequest_normal_unknown() throws IOException {
		doNothing().when(handler).attemptAction(any(), any(), anyInt(), anyInt(), any());
		
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		
		handler.onActionRequest(request, response);
		
		verify(response.getWriter()).write(COCErrorCode.UNAUTHORIZED.toString());
	}

	/**
	 * Test method for {@link NormalHandler#onActionRequest(HttpServletRequest, HttpServletResponse)},
	 * when the client is not using websockets and is known.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnActionRequest_normal_known() throws IOException {
		doNothing().when(handler).attemptAction(any(), any(), anyInt(), anyInt(), any());
		
		WebClient client = new WebClient();
		server.getClients().put("A", client);
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		
		setParameter(request, "x", "1");
		setParameter(request, "y", "2");
		setParameter(request, "action", "" + Action.PLACEBOMB.ordinal());
		
		handler.onActionRequest(request, response);
		
		verify(handler).attemptAction(client, Action.PLACEBOMB, 1, 2, response);
	}

	/**
	 * Test method for {@link NormalHandler#onActionRequest(WebClient, int, int, Action)}, when the
	 * client is using websockets.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnActionRequest_sockets() throws IOException {
		doNothing().when(handler).attemptAction(any(), any(), anyInt(), anyInt(), any());
		
		WebClient client = new WebClient();
		int x = 1;
		int y = 2;
		Action action = Action.PLACEMINE;
		
		handler.onActionRequest(client, x, y, action);
		
		verify(handler).attemptAction(client, action, x, y, null);
	}

	/**
	 * Test method for {@link NormalHandler#onStatusUpdateRequest}, when the client is not known.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnStatusUpdateRequest_unknown() throws IOException {
		doNothing().when(handler).sendStatusUpdate(any(), any());
		
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		handler.onStatusUpdateRequest(request, response);
		
		verify(response.getWriter()).write(COCErrorCode.UNAUTHORIZED.toJSON());
	}
	
	/**
	 * Test method for {@link NormalHandler#onStatusUpdateRequest}, when the client is known.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testOnStatusUpdateRequest_known() throws IOException {
		doNothing().when(handler).sendStatusUpdate(any(), any());
		
		WebClient client = new WebClient();
		server.getClients().put("A", client);
		HttpServletRequest request = createMockedRequest("A");
		HttpServletResponse response = createMockedResponse();
		handler.onStatusUpdateRequest(request, response);
		
		verify(handler).sendStatusUpdate(client, response);
	}
	
	/**
	 * Test method for {@link NormalHandler#sendStatusUpdate}, when the game is running.
	 * 
	 * @throws IOException
	 * 		will not happen because of mocks
	 */
	@Test
	public void testSendStatusUpdate_running() throws IOException {
		//Setup the game in the Running state, with a mocked level, and a single elf client.
		TestUtil.setGameState(GameState.RUNNING);
		
		mockLevel(TileType.FLOOR);
		HttpServletResponse response = createMockedResponse();
		
		WebClient client = spy(new WebClient());
		client.setTeam(Team.ELVES);
		doNothing().when(client).sendMessage(any(JSONObject.class), any());
		doNothing().when(client).sendMessage(anyString(), any());
		
		//Invoke
		handler.sendStatusUpdate(client, response);

		//Verify that the correct response was sent
		ArgumentCaptor<JSONObject> ac = ArgumentCaptor.forClass(JSONObject.class);
		verify(client).sendMessage(ac.capture(), eq(response));
		
		assertEquals(GameState.RUNNING.ordinal(), ac.getValue().getInt("state"));
		assertEquals(Team.ELVES.ordinal(), ac.getValue().getInt("team"));
	}

	/**
	 * Test method for {@link NormalHandler#attemptAction}, when the action is invalid.
	 *
	 * @throws IOException
	 * 		will not occur because of mocks
	 */
	@Test
	public void testAttemptAction_incorrectAction() throws IOException {
		HttpServletResponse response = createMockedResponse();

		WebClient clientMock = mockClient(Team.ELVES);
		clientMock.getPerformedActions().put(Action.DROPBAIT, new ArrayList<>());

		//Try to place a bomb as an elf, which is impossible
		handler.attemptAction(clientMock, Action.PLACEBOMB, 0, 0, response);

		//Verify the action has been denied
		verify(clientMock).sendMessage(COCErrorCode.ACTION_ILLEGAL.toString(), response);
	}

	/**
	 * Test method for {@link NormalHandler#attemptAction}, when the action is on an invalid location.
	 *
	 * @throws IOException
	 * 		will not occur because of mocks
	 */
	@Test
	public void testAttemptAction_incorrectLocation() throws IOException {
		HttpServletResponse response = createMockedResponse();

		mockLevel(TileType.WALL);

		WebClient clientMock = mockClient(Team.DWARFS);
		clientMock.getPerformedActions().put(Action.PLACEBOMB, new ArrayList<>());

		//Try to place a bomb as a dwarf
		handler.attemptAction(clientMock, Action.PLACEBOMB, 0, 0, response);

		//Verify the action is rejected for illegal location
		verify(clientMock).sendMessage(COCErrorCode.ACTION_ILLEGAL_LOCATION.toString(), response);
	}

	/**
	 * Test method for {@link NormalHandler#attemptAction}, when the action is on an invalid location.
	 *
	 * @throws IOException
	 * 		will not occur because of mocks
	 */
	@Test
	public void testAttemptAction_incorrectCooldown() throws IOException {
		HttpServletResponse response = createMockedResponse();

		mockLevel(TileType.FLOOR);

		WebClient clientMock = mockClient(Team.DWARFS);

		ArrayList<Long> set = new ArrayList<>();
		for (int i = 0; i < Action.PLACEBOMB.getMaxAmount(); i++) {
			set.add(Long.MAX_VALUE);
		}
		clientMock.getPerformedActions().put(Action.PLACEBOMB, set);

		//Try to place a bomb as a dwarf
		handler.attemptAction(clientMock, Action.PLACEBOMB, 0, 0, response);

		//Verify the action is rejected for cooldown
		verify(clientMock).sendMessage(COCErrorCode.ACTION_COOLDOWN.toString(), response);
	}

	/**
	 * Test method for {@link NormalHandler#attemptAction}, when the action is on an invalid location.
	 *
	 * @throws IOException
	 * 		will not occur because of mocks
	 */
	@Test
	public void testAttemptAction_radius() throws IOException {
		HttpServletResponse response = createMockedResponse();

		mockLevel(TileType.FLOOR);
		when(Main.getInstance().getCurrentGame().getPlayer().getLocation()).thenReturn(new Vector3f());

		WebClient clientMock = mockClient(Team.DWARFS);
		clientMock.getPerformedActions().put(Action.SPAWNENEMY, new ArrayList<>());

		//Try to spawn a rabbit as a dwarf
		handler.attemptAction(clientMock, Action.SPAWNENEMY, 0, 0, response);

		//Verify the action is rejected for radius
		verify(clientMock).sendMessage(COCErrorCode.ACTION_RADIUS.toString(), response);
	}
	
	/**
	 * Test method for {@link NormalHandler#attemptAction}, when the action is valid.
	 *
	 * @throws IOException
	 * 		will not occur because of mocks
	 */
	@Test
	public void testAttemptAction_correct() throws IOException {
		HttpServletResponse response = createMockedResponse();

		mockLevel(TileType.FLOOR);

		WebClient clientMock = mockClient(Team.DWARFS);
		clientMock.getPerformedActions().put(Action.PLACEBOMB, new ArrayList<>());

		//Try to place a bomb as a dwarf
		handler.attemptAction(clientMock, Action.PLACEBOMB, 0, 0, response);

		//Verify the action has been accepted
		verify(clientMock).confirmMessage(response);
	}
	
	/**
	 * Creates a mocked client for the given team.
	 * 
	 * @param team
	 * 		the team to use
	 * @return
	 * 		the mocked client
	 */
	private WebClient mockClient(Team team) {
		WebClient mockedClient = mock(WebClient.class);

		when(mockedClient.getTeam()).thenReturn(team);
		when(mockedClient.getPerformedActions()).thenReturn(new HashMap<>());
		
		return mockedClient;
	}
}
