package nl.tudelft.contextproject.webinterface.websockets;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.WriteCallback;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.model.Observer;
import nl.tudelft.contextproject.util.Vec2I;
import nl.tudelft.contextproject.webinterface.Action;
import nl.tudelft.contextproject.webinterface.COCErrorCode;
import nl.tudelft.contextproject.webinterface.WebClient;
import nl.tudelft.contextproject.webinterface.WebServer;

import lombok.SneakyThrows;

/**
 * Class that is a WebSocket to a single client.
 */
public class COCSocket extends WebSocketAdapter implements Observer {
	public static final float UPDATE_INTERVAL = 0.15f;
	public static final long TIMEOUT = 10_000;
	
	private final WebServer server;
	private final WebClient client;
	private float timer;
	private volatile long lastMessage;
	
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
	public void onWebSocketText(String msg) {
		lastMessage = System.currentTimeMillis();
		if ("quit".equals(msg)) {
			server.disconnect(client, StatusCode.NORMAL);
			return;
		} else if ("map".equals(msg)) {
			server.getNormalHandler().onMapRequest(client);
			return;
		}
		
		String[] parts = msg.split(" ");
		if (parts.length != 3) {
			client.sendMessage(COCErrorCode.ACTION_ILLEGAL.toString(), null);
			return;
		}
		
		int actionCode = Integer.parseInt(parts[0]);
		Action action = Action.getAction(actionCode);
		int x = Integer.parseInt(parts[1]);
		int y = Integer.parseInt(parts[2]);
		
		server.getNormalHandler().onActionRequest(client, new Vec2I(x, y), action);
	}
	
	@SneakyThrows
	@Override
	public void onWebSocketConnect(Session session) {
		super.onWebSocketConnect(session);
		lastMessage = System.currentTimeMillis();
		timer = 0f;

		try {
			session.getRemote().sendString("" + Main.getInstance().getGameState().ordinal());
		} catch (Exception ex) {
			session.close(StatusCode.SERVER_ERROR, null);
			return;
		}

		//Send the map
		server.getNormalHandler().onMapRequest(client);
		
		this.client.setWebSocket(this);
		Main.getInstance().registerObserver(this);
	}
	
	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		
		Main.getInstance().removeObserver(this);
		this.client.removeWebSocket(this);
		
		//Remove the client if they drop in the waiting state
		if (Main.getInstance().getGameState() == GameState.WAITING) {
			server.getClients().values().remove(client);
			return;
		}
	}

	@SneakyThrows(IOException.class)
	@Override
	public void update(float tpf) {
		if (getSession() == null) return;
		
		timer += tpf;
		if (timer < UPDATE_INTERVAL) return;
		timer = 0f;
		
		//Kick clients from the the game after a timeout.
		if (Main.getInstance().getGameState() == GameState.WAITING && System.currentTimeMillis() - lastMessage > TIMEOUT) {
			server.disconnect(client, StatusCode.NORMAL);
			return;
		}
		
		server.getNormalHandler().sendStatusUpdate(client, null);
	}
	
	/**
	 * Sends a message over this socket.
	 * 
	 * @param msg
	 * 		the message to send to this client
	 * @return
	 * 		true if the message was queued, false otherwise
	 */
	public boolean sendMessage(String msg) {
		try {
			getRemote().sendString(msg, new WriteCallback() {
				@Override
				public void writeSuccess() {
					lastMessage = System.currentTimeMillis();
				}
				
				@Override
				public void writeFailed(Throwable x) { }
			});
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
