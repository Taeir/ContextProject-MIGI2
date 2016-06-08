package nl.tudelft.contextproject.webinterface.websockets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.junit.Test;

/**
 * Test class for {@link NotAuthorizedSocket}.
 */
public class NotAuthorizedSocketTest {

	/**
	 * Tests {@link NotAuthorizedSocket#onWebSocketConnect}.
	 */
	@Test
	public void testOnWebSocketConnect() {
		Session session = mock(Session.class);
		
		NotAuthorizedSocket socket = new NotAuthorizedSocket();
		socket.onWebSocketConnect(session);
		
		verify(session).close(StatusCode.NORMAL, "Not Authorized");
	}

}
