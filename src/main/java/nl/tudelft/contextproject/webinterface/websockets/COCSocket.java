package nl.tudelft.contextproject.webinterface.websockets;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

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
public class COCSocket extends WebSocketAdapter implements TickListener {
	public static final float UPDATE_INTERVAL = 1.0f;
	
	public static final boolean KICK_ILLEGAL_CLIENTS = false;
	
	private final WebServer server;
	private final WebClient client;
	private float timer;
	
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
	
	@SneakyThrows(IOException.class)
	@Override
	public void onWebSocketText(String message) {
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
			RemoteEndpoint remote = getRemote();
			if (remote != null) remote.sendStringByFuture(COCErrorCode.ACTION_ILLEGAL.toString());
		}
	}
	
	@Override
	public void onWebSocketConnect(Session session) {
		super.onWebSocketConnect(session);
		timer = 0f;
		
		System.out.println("[DEBUG] [WebSocket] Websocket connected!");
		
		try {
			session.getRemote().sendString("OK");
		} catch (Exception ex) {
			System.out.println("Failed to send OK message!");
			ex.printStackTrace();
			session.close(StatusCode.SERVER_ERROR, null);
			return;
		}
		
		this.client.setWebSocket(this);
		Main.getInstance().attachTickListener(this);
	}
	
	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);

		System.out.println("[DEBUG] [WebSocket] Websocket close: [" + statusCode + "] " + reason);
		
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
}
