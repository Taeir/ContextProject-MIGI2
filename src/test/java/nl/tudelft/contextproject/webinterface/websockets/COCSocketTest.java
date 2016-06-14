package nl.tudelft.contextproject.webinterface.websockets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.test.TestUtil;
import nl.tudelft.contextproject.webinterface.Action;
import nl.tudelft.contextproject.webinterface.COCErrorCode;
import nl.tudelft.contextproject.webinterface.NormalHandler;
import nl.tudelft.contextproject.webinterface.WebClient;
import nl.tudelft.contextproject.webinterface.WebServer;
import nl.tudelft.contextproject.webinterface.WebTestBase;

/**
 * Test class for {@link COCSocket}.
 */
public class COCSocketTest extends WebTestBase {
	private WebServer server;
	private NormalHandler handler;
	private WebClient client;
	private COCSocket socket;
	
	/**
	 * Creates a new server, client and socket before every test.
	 */
	@Before
	public void setUp() {
		server = spy(new WebServer());
		handler = mock(NormalHandler.class);
		setHandler(handler, server);
		client = spy(new WebClient());
		socket = spy(new COCSocket(server, client));
	}

	/**
	 * Tests if {@link COCSocket#onWebSocketText} properly handles quit messages.
	 */
	@Test
	public void testOnWebSocketText_quit() {
		socket.onWebSocketText("quit");
		
		verify(server).disconnect(client, StatusCode.NORMAL);
	}
	
	/**
	 * Tests if {@link COCSocket#onWebSocketText} properly handles map messages.
	 * 
	 * @throws IOException
	 * 		will not occur because of mocks
	 */
	@Test
	public void testOnWebSocketText_map() throws IOException {
		socket.onWebSocketText("map");
		
		verify(handler).onMapRequest(client);
	}
	
	/**
	 * Tests if {@link COCSocket#onWebSocketText} properly handles action messages.
	 * 
	 * @throws IOException
	 * 		will not occur because of mocks
	 */
	@Test
	public void testOnWebSocketText_action() throws IOException {
		socket.onWebSocketText("0 1 2");
		
		verify(handler).onActionRequest(client, 1, 2, Action.getAction(0));
	}
	
	/**
	 * Tests if {@link COCSocket#onWebSocketText} properly handles invalid messages.
	 * @throws IOException
	 * 		will not occur because of mocks
	 */
	@Test
	public void testOnWebSocketText_invalid() throws IOException {
		socket.onWebSocketText("3 4");
		
		verify(client).sendMessage(COCErrorCode.ACTION_ILLEGAL.toString(), null);
	}

	/**
	 * Tests if {@link COCSocket#onWebSocketConnect} works properly.
	 * 
	 * @throws IOException
	 * 		will not occur because of mocks
	 */
	@Test
	public void testOnWebSocketConnect() throws IOException {
		TestUtil.setGameState(GameState.RUNNING);
		
		Session session = mock(Session.class);
		when(session.getRemote()).thenReturn(mock(RemoteEndpoint.class));
		
		socket.onWebSocketConnect(session);
		
		verify(session.getRemote()).sendString("" + GameState.RUNNING.ordinal());
		verify(Main.getInstance()).attachTickListener(socket);
		assertSame(socket, client.getWebSocket());
	}
	
	/**
	 * Tests if {@link COCSocket#onWebSocketConnect} properly handles errors.
	 * 
	 * @throws IOException
	 * 		will not occur because of mocks
	 */
	@Test
	public void testOnWebSocketConnect_error() throws IOException {
		TestUtil.setGameState(GameState.WAITING);
		
		RemoteEndpoint remote = mock(RemoteEndpoint.class);
		doThrow(IOException.class).when(remote).sendString(anyString());
		
		Session session = mock(Session.class);
		when(session.getRemote()).thenReturn(remote);
		
		socket.onWebSocketConnect(session);
		
		verify(session).close(StatusCode.SERVER_ERROR, null);
	}

	/**
	 * Tests if {@link COCSocket#onWebSocketClose} properly unregisters itself.
	 */
	@Test
	public void testOnWebSocketClose() {
		socket.onWebSocketClose(StatusCode.NORMAL, null);
		
		verify(Main.getInstance()).removeTickListener(socket);
		assertNull(client.getWebSocket());
	}

	/**
	 * Tests if {@link COCSocket#update} properly sends a status update.
	 * 
	 * @throws IOException
	 * 		will not occur because of mocks
	 */
	@Test
	public void testUpdate() throws IOException {
		when(socket.getSession()).thenReturn(mock(Session.class));
		
		socket.update(2 * COCSocket.UPDATE_INTERVAL);
		
		verify(handler).sendStatusUpdate(client, null);
	}

}
