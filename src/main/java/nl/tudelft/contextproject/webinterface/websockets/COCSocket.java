package nl.tudelft.contextproject.webinterface.websockets;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.webinterface.Action;
import nl.tudelft.contextproject.webinterface.COCErrorCode;
import nl.tudelft.contextproject.webinterface.WebClient;
import nl.tudelft.contextproject.webinterface.WebServer;

import lombok.SneakyThrows;

/**
 * Class that is a WebSocket to a single client.
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class COCSocket implements TickListener {
	public static final float UPDATE_INTERVAL = 1.0f;
	
	public static final boolean KICK_ILLEGAL_CLIENTS = false;
	
	private final WebServer server;
	private final WebClient client;
	private float timer;
	private Session session;
	private RemoteEndpoint remote;
	
	/**
	 * Creates a new COCSocket for the given client.
	 * 
	 * @param server
	 * 		the server to use
	 * @param client
	 * 		the client to create the socket for
	 */
	public COCSocket(WebServer server, WebClient client) {
		this.server = server;
		this.client = client;
	}
	
	/**
	 * Handles messages from the client.
	 * 
	 * <p>The only messages that the client can send are action messages,
	 * <code>&lt;action&gt; &lt;x&gt; &lt;y&gt;</code>, and quit messages,
	 * <code>QUIT</code>.
	 * 
	 * @param message
	 * 		the message that was sent by the client
	 * @throws IOException
	 * 		if writing to the response causes an exception
	 */
	@OnWebSocketMessage
	public void onMessage(String message) throws IOException {
		System.out.println("[DEBUG] [WebSocket] Message received: " + message);
		
		if ("QUIT".equals(message)) {
			server.disconnect(client, StatusCode.NORMAL);
			return;
		}
		
		String[] parts = message.split(" ");
		if (parts.length != 3) {
			illegalAction();
			return;
		}
		
		int actionCode = Integer.parseInt(parts[0]);
		Action action = Action.getAction(actionCode);
		int x = Integer.parseInt(parts[1]);
		int y = Integer.parseInt(parts[2]);
		
		server.getNormalHandler().onActionRequest(client, x, y, action);
	}

	/**
	 * Based on the kicking policy, either kicks this client or sends an ACTION_ILLEGAL error code.
	 */
	private void illegalAction() {
		if (KICK_ILLEGAL_CLIENTS) {
			server.disconnect(client, StatusCode.POLICY_VIOLATION);
		} else {
			remote.sendStringByFuture(COCErrorCode.ACTION_ILLEGAL.toString());
		}
	}
	
	/**
	 * Handles new connections.
	 * 
	 * @param session
	 * 		the session the client connected with
	 * @throws IOException
	 * 		if sending the "OK" response fails
	 */
	@OnWebSocketConnect
	public void onConnect(Session session) throws IOException {
		this.session = session;
		this.remote = session.getRemote();
		
		System.out.println("[DEBUG] [WebSocket] Websocket connected!");
		
		try {
			session.getRemote().sendString("OK");
		} catch (Exception ex) {
			System.out.println("Failed to send OK message!");
			ex.printStackTrace();
			
			session.disconnect();
			return;
		}
		
		this.client.setWebSocket(this);
		Main.getInstance().attachTickListener(this);
	}
	
	/**
	 * Handles socket close.
	 * 
	 * @param status
	 * 		the status code with which the socket is being closed
	 * @param reason
	 * 		the reason the socket is being closed
	 */
	@OnWebSocketClose
	public void onClose(int status, String reason) {
		System.out.println("[DEBUG] [WebSocket] Websocket close: [" + status + "] " + reason);
		
		Main.getInstance().removeTickListener(this);
		this.client.removeWebSocket(this);
	}

	@SneakyThrows(IOException.class)
	@Override
	public void update(float tpf) {
		timer += tpf;
		if (timer < UPDATE_INTERVAL) return;
		
		timer = 0f;
		server.getNormalHandler().sendStatusUpdate(client, null);
	}
	
	/**
	 * @return
	 * 		the session of this socket
	 */
	public Session getSession() {
		return session;
	}
	
	/**
	 * @return
	 * 		the remote endpoint of this socket
	 */
	public RemoteEndpoint getRemote() {
		return remote;
	}
}
