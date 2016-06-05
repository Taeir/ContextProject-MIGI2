package nl.tudelft.contextproject.webinterface.websockets;

import java.net.HttpCookie;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import nl.tudelft.contextproject.webinterface.WebClient;
import nl.tudelft.contextproject.webinterface.WebServer;

import lombok.SneakyThrows;

/**
 * Class for creating WebSockets when clients request them.
 */
public class COCWebSocketCreator implements WebSocketCreator {
	private static final NotAuthorizedSocket UNAUTHORIZED_SOCKET = new NotAuthorizedSocket();
	private final transient WebServer server;
	
	
	/**
	 * Creates a new {@link COCWebSocketCreator} for the given server.
	 * 
	 * @param server
	 * 		the WebServer to use
	 */
	public COCWebSocketCreator(WebServer server) {
		this.server = server;
	}
	
	@SneakyThrows
	@Override
	public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
		WebClient client = null;
		for (HttpCookie cookie : req.getCookies()) {
			if (!cookie.getName().equals(WebServer.COOKIE_NAME)) continue;
			
			System.out.println("Client session ID found: " + cookie.getValue());
			client = server.getUser(cookie.getValue());
			if (client == null) {
				System.out.println(" - but session ID was not bound to a WebClient");
			}
			
			break;
		}
		
		if (client == null) {
			System.out.println("WebClient not found!");
			return UNAUTHORIZED_SOCKET;
		}
		
		System.out.println("Matching WebClient found, creating COCSocket");
		//Use the text protocol
		//resp.setAcceptedSubProtocol("text");
		return new COCSocket(server, client);
	}

}
