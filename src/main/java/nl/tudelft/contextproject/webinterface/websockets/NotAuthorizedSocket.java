package nl.tudelft.contextproject.webinterface.websockets;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

/**
 * WebSocket implementation that instantly closes the connection with a "Not Authorized" message.
 * 
 * <p>This class is used to give the client a way to check if they properly support WebSockets.
 */
public class NotAuthorizedSocket extends WebSocketAdapter {
	/**
	 * Called when a client connects.
	 * 
	 * @param session
	 * 		the session of the client connected
	 */
	@Override
	public void onWebSocketConnect(Session session) {
		session.close(StatusCode.NORMAL, "Not Authorized");
	}
}
