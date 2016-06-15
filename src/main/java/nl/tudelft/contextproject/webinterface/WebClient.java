package nl.tudelft.contextproject.webinterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;

import nl.tudelft.contextproject.webinterface.websockets.COCSocket;

import java.util.List;

/**
 * Class to represent connected web clients.
 */
public class WebClient {
	private Team team = Team.NONE;
	private Map<Action, List<Long>> performedActions = new HashMap<>();
	private COCSocket webSocket;
	
	/**
	 * @return
	 * 		the websocket of this client, or null if this client has no websocket
	 */
	public synchronized COCSocket getWebSocket() {
		return this.webSocket;
	}
	
	/**
	 * Sets the websocket of this client.
	 * 
	 * @param socket
	 * 		the socket to set
	 */
	public synchronized void setWebSocket(COCSocket socket) {
		this.webSocket = socket;
	}
	
	/**
	 * Removes the websocket from this client, if it is equal to the given socket.
	 * 
	 * @param socket
	 * 		the socket to remove
	 */
	public synchronized void removeWebSocket(COCSocket socket) {
		if (this.webSocket == socket) {
			this.webSocket = null;
		}
	}

	/**
	 * @return
	 * 		if this client is an elf
	 */
	public boolean isElf() {
		return team == Team.ELVES;
	}
	
	/**
	 * @return
	 * 		if this client is a dwarf
	 */
	public boolean isDwarf() {
		return team == Team.DWARFS;
	}
	
	/**
	 * Team names are None, Elves and Dwarfs.
	 * 
	 * @return
	 * 		the name of the team of this player
	 */
	public Team getTeam() {
		return this.team;
	}
	
	/**
	 * Sets the team of this client.
	 * Also resets the performedActions hashmap.
	 * 
	 * @param team
	 * 		the team of this client
	 */
	public void setTeam(Team team) {
		this.team = team;
		resetPerformed();
	}

	/**
	 * Reset the performed actions for the web client.
	 */
	public void resetPerformed() {
		this.performedActions.clear();

		if (isElf()) {
			setUpPerformedElves();
		} else {
			setUpPerformedDwarfs();
		}
	}

	/**
	 * Create sets in the HashMap for all elves actions.
	 */
	private void setUpPerformedElves() {
		performedActions.put(Action.DROPBAIT, new ArrayList<>());
		performedActions.put(Action.PLACETILE, new ArrayList<>());
		performedActions.put(Action.DROPCRATE, new ArrayList<>());
		performedActions.put(Action.OPENGATE, new ArrayList<>());
	}

	/**
	 * Create sets in the HashMap for all dwarfs actions.
	 */
	private void setUpPerformedDwarfs() {
		performedActions.put(Action.PLACEMINE, new ArrayList<>());
		performedActions.put(Action.PLACEBOMB, new ArrayList<>());
		performedActions.put(Action.PLACEMINE, new ArrayList<>());
		performedActions.put(Action.PLACEPITFALL, new ArrayList<>());
		performedActions.put(Action.SPAWNENEMY, new ArrayList<>());
	}

	/**
	 * @return
	 * 		the map containing the actions performed by this client
	 */
	public Map<Action, List<Long>> getPerformedActions() {
		return performedActions;
	}
	
	/**
	 * Sends a JSON message to this client.
	 * 
	 * @param msg
	 * 		the JSON message to respond with
	 * @param response
	 * 		the response to send to, can be null
	 * @throws IOException
	 * 		if writing the response results in an error
	 */
	public void sendMessage(JSONObject msg, HttpServletResponse response) throws IOException {
		if (response == null) {
			COCSocket socket = this.webSocket;
			if (socket != null) {
				socket.sendMessage(msg.toString());
			}
		} else {
			response.setStatus(HttpStatus.OK_200);
			response.setContentType("text/json");
			response.getWriter().write(msg.toString());
		}
	}
	
	/**
	 * Send a message to this client.
	 * 
	 * @param msg
	 * 		the message to respond with
	 * @param response
	 * 		the response to send to, can be null
	 * @throws IOException
	 * 		if writing the response results in an error
	 */
	public void sendMessage(String msg, HttpServletResponse response) throws IOException {
		if (response == null) {
			COCSocket socket = this.webSocket;
			if (socket != null) {
				socket.sendMessage(msg);
			}
		} else {
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write(msg);
		}
	}
	
	/**
	 * Send a confirmation message to the client.
	 * 
	 * @param response
	 * 		the response to send to, can be null
	 */
	public void confirmMessage(HttpServletResponse response) {
		if (response == null) return;
		
		response.setStatus(HttpStatus.NO_CONTENT_204);
	}

	@Override
	public String toString() {
		return "WebClient<team=" + getTeam() + ">";
	}
}
