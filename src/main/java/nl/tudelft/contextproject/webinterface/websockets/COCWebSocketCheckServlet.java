package nl.tudelft.contextproject.webinterface.websockets;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import nl.tudelft.contextproject.webinterface.WebServer;

/**
 * Servlet class that handles client WebSocket requests on the check address.
 */
public class COCWebSocketCheckServlet extends WebSocketServlet {

	private static final long serialVersionUID = -3247862472092878820L;

	//The client timeout in milliseconds
	private static final long CLIENT_TIMEOUT = 10_000L;
	
	private final transient WebServer server;
	
	/**
	 * Creates a new {@link COCWebSocketCheckServlet} for the given server.
	 * 
	 * @param server
	 * 		the server to use
	 */
	public COCWebSocketCheckServlet(WebServer server) {
		this.server = server;
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.getPolicy().setIdleTimeout(CLIENT_TIMEOUT);
		factory.register(COCSocket.class);
		factory.setCreator(new COCWebSocketCreator(server));
	}

}
