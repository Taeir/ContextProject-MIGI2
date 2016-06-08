package nl.tudelft.contextproject.webinterface.websockets;

import java.net.HttpCookie;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import nl.tudelft.contextproject.webinterface.WebClient;
import nl.tudelft.contextproject.webinterface.WebServer;

/**
 * Class for creating WebSockets when clients request them.
 */
public class COCWebSocketCreator implements WebSocketCreator {
	private static final NotAuthorizedSocket UNAUTHORIZED_SOCKET = new NotAuthorizedSocket();
	private final WebServer server;
	
	/**
	 * Creates a new {@link COCWebSocketCreator} for the given server.
	 * 
	 * @param server
	 * 		the WebServer to use
	 */
	public COCWebSocketCreator(WebServer server) {
		this.server = server;
	}
	
	@Override
	public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
		WebClient client = null;
		for (HttpCookie cookie : req.getCookies()) {
			if (!cookie.getName().equals(WebServer.COOKIE_NAME)) continue;
			
			client = server.getUser(cookie.getValue());
			break;
		}
		
		if (client == null) return UNAUTHORIZED_SOCKET;
		
		return new COCSocket(server, client);
	}

}
