package nl.tudelft.contextproject.webinterface.websockets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.HttpCookie;
import java.util.ArrayList;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.webinterface.WebClient;
import nl.tudelft.contextproject.webinterface.WebServer;

/**
 * Test class for {@link COCWebSocketCreator}.
 */
public class COCWebSocketCreatorTest {
	private COCWebSocketCreator creator;
	private WebServer server;
	
	/**
	 * Creates a new WebServer and socket creator before every test.
	 */
	@Before
	public void setUp() {
		server = new WebServer();
		creator = new COCWebSocketCreator(server);
	}
	
	/**
	 * Tests if {@link COCWebSocketCreator#createWebSocket} properly handles unauthorized clients.
	 */
	@Test
	public void testCreateWebSocket_unauthorized() {
		ServletUpgradeRequest request = mock(ServletUpgradeRequest.class);
		ServletUpgradeResponse response = mock(ServletUpgradeResponse.class);
		
		when(request.getCookies()).thenReturn(new ArrayList<>());
		
		Object obj = creator.createWebSocket(request, response);
		assertTrue(obj instanceof NotAuthorizedSocket);
	}

	/**
	 * Tests if {@link COCWebSocketCreator#createWebSocket} properly handles authorized clients.
	 */
	@Test
	public void testCreateWebSocket_authorized() {
		ServletUpgradeRequest request = mock(ServletUpgradeRequest.class);
		ServletUpgradeResponse response = mock(ServletUpgradeResponse.class);
		
		HttpCookie cookie = new HttpCookie(WebServer.COOKIE_NAME, "A");
		when(request.getCookies()).thenReturn(new ArrayList<>());
		request.getCookies().add(cookie);
		
		server.getClients().put("A", new WebClient());
		
		Object obj = creator.createWebSocket(request, response);
		assertTrue(obj instanceof COCSocket);
	}
}
