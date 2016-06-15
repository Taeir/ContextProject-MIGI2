package nl.tudelft.contextproject.webinterface.websockets;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import nl.tudelft.contextproject.webinterface.WebServer;

/**
 * Servlet class to handle client requests to create WebSockets.
 */
public class COCWebSocketServlet extends WebSocketServlet {
	private static final long serialVersionUID = -3247862472092878820L;
	
	private final transient WebServer server;
	
	/**
	 * Creates a new {@link COCWebSocketServlet} for the given server.
	 * 
	 * @param server
	 * 		the server to use
	 */
	public COCWebSocketServlet(WebServer server) {
		this.server = server;
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(COCSocket.class);
		factory.setCreator(new COCWebSocketCreator(server));
	}

}
