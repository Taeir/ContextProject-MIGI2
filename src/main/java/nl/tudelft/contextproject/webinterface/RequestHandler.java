package nl.tudelft.contextproject.webinterface;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.json.JSONObject;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.util.webinterface.ActionUtil;
import nl.tudelft.contextproject.util.webinterface.WebUtil;

/**
 * Class for handling client requests.
 */
public class RequestHandler {
	public static final boolean KICK_ILLEGAL_CLIENTS = false;
	
	private final transient WebServer server;
	
	/**
	 * Creates a new RequestHandler for the given server.
	 * 
	 * @param server
	 * 		the server to use
	 */
	public RequestHandler(WebServer server) {
		this.server = server;
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
	 * @param response
	 * 		the object to which the response should be written. Can be null if websockets are used
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void attemptAction(int xCoord, int yCoord, Action action, WebClient client, HttpServletResponse response) throws IOException {
		if (!WebUtil.checkValidAction(action, client.getTeam())) {
			//The client performed an illegal action for it's team. This is not possible under normal circumstances.
			if (KICK_ILLEGAL_CLIENTS) server.disconnect(client, StatusCode.POLICY_VIOLATION);
			return;
		}

		if (!WebUtil.checkValidLocation(xCoord, yCoord, action)) {
			client.sendMessage(COCErrorCode.ACTION_ILLEGAL_LOCATION.toJSON(), response);
			return;
		}

		if (!WebUtil.checkWithinCooldown(action, client)) {
			client.sendMessage(COCErrorCode.ACTION_COOLDOWN.toJSON(), response);
			return;
		}

		try {
			ActionUtil.perform(action, xCoord, yCoord);
			client.confirmMessage("", response);
		} catch (Exception ex) {
			client.sendMessage(COCErrorCode.SERVER_ERROR.toJSON(), response);
			throw ex;
		}
	}
	
	/**
	 * Handles a map request.
	 * 
	 * @param client
	 * 		the client who wants the map
	 * @param response
	 * 		the HTTP response object
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void getMap(WebClient client, HttpServletResponse response) throws IOException {
		JSONObject json = Main.getInstance().getCurrentGame().getLevel().toWebJSON();

		client.sendMessage(json, response);
	}
}
