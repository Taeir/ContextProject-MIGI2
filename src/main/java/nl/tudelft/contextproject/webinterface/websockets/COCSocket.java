package nl.tudelft.contextproject.webinterface.websockets;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.util.webinterface.ActionUtil;
import nl.tudelft.contextproject.util.webinterface.EntityUtil;
import nl.tudelft.contextproject.util.webinterface.WebUtil;
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
	
	private final transient WebServer server;
	private final transient WebClient client;
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
	
	@SneakyThrows
	@OnWebSocketMessage
	public void onText(String message) {
		System.out.println("[DEBUG] [WebSocket] Message received: " + message);
		
		//ACTION: Info
		if (message.startsWith("RequestAction")) {
			//TODO
		}
		
	}
	
	/**
	 * Method to handle a new connection.
	 * 
	 * @param session
	 * 		the session the client connected with
	 * @throws IOException
	 * 		if sending the "OK" response fails
	 */
	@OnWebSocketConnect
	public void onConnect(Session session) throws IOException {
		System.out.println("Websocket connected!");
		
		try {
			session.getRemote().sendString("OK");
		} catch (Exception ex) {
			System.out.println("Failed to send OK message!");
			ex.printStackTrace();
			
			session.disconnect();
			return;
		}
		
		this.client.setWebSocket(this);
		this.session = session;
		this.remote = session.getRemote();
		
		Main.getInstance().attachTickListener(this);
	}
	
	@OnWebSocketClose
	public void onClose(Session session, int status, String reason) {
		System.out.println("Websocket close: [" + status + "] " + reason);
		
		Main.getInstance().removeTickListener(this);
		this.client.removeWebSocket(this);
	}

	@Override
	public void update(float tpf) {
		timer += tpf;
		if (timer < UPDATE_INTERVAL) return;
		
		timer = 0f;
		remote.sendString(statusUpdate(), new WriteCallback() {

			@Override
			public void writeFailed(Throwable x) {
				System.err.println("[WebSocket] WRITE FAILED!");
				x.printStackTrace();
			}

			@Override
			public void writeSuccess() {
				System.out.println("[DEBUG] [WebSocket] Message written");
			}
		});
	}
	
	private String statusUpdate() {
		JSONObject json = new JSONObject();
		json.put("team", client.getTeam().toUpperCase());
		json.put("state", Main.getInstance().getGameState().name());
		
		Game game = Main.getInstance().getCurrentGame();
		
		switch (Main.getInstance().getGameState()) {
			case WAITING:
				//For now fall through to running
			case RUNNING:
				json.put("entities", EntityUtil.entitiesToJson(game.getEntities(), game.getPlayer()));
				json.put("explored", game.getLevel().toExploredWebJSON());
				break;
			case PAUSED:
				//TODO Actual player information
				//json.put("player", VRPlayer().toJSON());
				//TODO Add entity updates
				//TODO Add explored updates
				break;
			case ENDED:
				//TODO Add game statistics
				break;
			default:
				break;
		}
		
		return json.toString();
	}
	
	/**
	 * Attempt to perform an action.
	 *
	 * @param xCoord
	 * 		the x coordinate to perform the action on
	 * @param yCoord
	 * 		the y coordinate to perform the action on
	 * @param action
	 * 		the action to perform
	 * @param client
	 * 		the client who wants to perform the action
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	protected void attemptAction(int xCoord, int yCoord, Action action, WebClient client) throws IOException {
		if (!WebUtil.checkValidAction(action, client.getTeam())) {
			//The client performed an illegal action for it's team. This is not possible under normal circumstances.
			if (KICK_ILLEGAL_CLIENTS) session.close(StatusCode.POLICY_VIOLATION, null);
			return;
		}

		if (!WebUtil.checkValidLocation(xCoord, yCoord, action)) {
			remote.sendStringByFuture(COCErrorCode.ACTION_ILLEGAL_LOCATION.toJSON());
			return;
		}

		if (!WebUtil.checkWithinCooldown(action, client)) {
			remote.sendStringByFuture(COCErrorCode.ACTION_COOLDOWN.toJSON());
			return;
		}

		try {
			ActionUtil.perform(action, xCoord, yCoord);
		} catch (Exception ex) {
			remote.sendStringByFuture(COCErrorCode.SERVER_ERROR.toJSON());
			throw ex;
		}
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
