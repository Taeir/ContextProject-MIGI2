package nl.tudelft.contextproject.webinterface;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.EndingController;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.util.QRGenerator;
import nl.tudelft.contextproject.util.webinterface.ActionUtil;
import nl.tudelft.contextproject.util.webinterface.EntityUtil;
import nl.tudelft.contextproject.util.webinterface.WebUtil;

/**
 * Handler for non websocket requests.
 */
public class NormalHandler {
	private final transient WebServer server;
	
	/**
	 * Creates a new {@link NormalHandler} for the given server.
	 * 
	 * @param server
	 * 		the webserver to use
	 */
	public NormalHandler(WebServer server) {
		this.server = server;
	}
	
	/**
	 * Handles requests to join the game.
	 * 
	 * @param request
	 * 		the request the client made
	 * @param response
	 * 		the response to send back to the client
	 * @throws IOException
	 * 		if writing to the response causes an IOException
	 */
	public void onJoinRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		WebClient client = server.getUser(request);
		
		if (client == null) {
			attemptJoinNew(response);
		} else {
			attemptRejoin(response);
		}
	}
	
	/**
	 * Handles requests to join the game when the client is not known yet.
	 * 
	 * @param response
	 * 		the response to send back to the client
	 * @throws IOException
	 * 		if writing to the response causes an IOException
	 */
	public void attemptJoinNew(HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.OK_200);
		
		//Check if game started
		if (Main.getInstance().getGameState().isStarted()) {
			response.getWriter().write(COCErrorCode.AUTHENTICATE_FAIL_IN_PROGRESS.toString());
			return;
		}
		
		//Check if game is full
		if (server.getUniqueClientCount() >= WebServer.MAX_PLAYERS) {
			response.getWriter().write(COCErrorCode.AUTHENTICATE_FAIL_FULL.toString());
			return;
		}
		
		Cookie cookie = server.createCookie();
		server.getClients().put(cookie.getValue(), new WebClient());

		response.addCookie(cookie);
		response.getWriter().write("ALLOWED");
	}
	
	/**
	 * Handles requests to rejoin the game. This happens when a client drops out for
	 * some unknown reason.
	 * 
	 * @param response
	 * 		the object to write the response to
	 * @throws IOException
	 * 		if writing to the response causes an IOException
	 */
	public void attemptRejoin(HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.OK_200);
		response.getWriter().write("REJOIN");
	}
	
	/**
	 * Handles update requests from users in the index view.
	 * 
	 * @param request
	 * 		the request the client made
	 * @param response
	 * 		the object to write the response to
	 * @throws IOException
	 * 		if writing to the response causes an IOException
	 */
	public void onIndexRefresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
		WebClient client = server.getUser(request);
		if (client != null) {
			attemptRejoin(response);
			return;
		}
		
		response.setStatus(HttpStatus.OK_200);
		
		if (Main.getInstance().getGameState().isStarted() || server.getUniqueClientCount() >= WebServer.MAX_PLAYERS) {
			response.getWriter().write("n");
		} else {
			response.getWriter().write("y");
		}
	}
	
	/**
	 * Handles requests for the QR code.
	 * 
	 * @param response
	 * 		the object to write the response to
	 * @throws IOException
	 * 		if writing to the response causes an IOException
	 */
	public void onQrRequest(HttpServletResponse response) throws IOException {
		QRGenerator qrg = QRGenerator.getInstance();
		
		//Write the QR code as a Base64 encoded image on a plain page.
		response.setStatus(HttpStatus.OK_200);
		response.getWriter().write("<html><body><img src=\"data:image/png;base64,");
		response.getWriter().write(Base64.getEncoder().encodeToString(qrg.streamQRcode().toByteArray()));
		response.getWriter().write("\"/><br>");
		response.getWriter().write(qrg.getQRAddress());
		response.getWriter().write("</body></html>");
	}
	
	/**
	 * Handles a setTeam request.
	 * 
	 * @param request
	 * 		the HTTP request
	 * @param response
	 * 		the object to write the response to
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void onSetTeamRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.OK_200);
		
		//Users that are not in the game cannot set their team
		WebClient client = server.getUser(request);
		if (client == null) {
			response.getWriter().write(COCErrorCode.SETTEAM_UNAUTHORIZED.toString());
			return;
		}
		
		//Changing teams while the game is in progress is not allowed
		if (Main.getInstance().getGameState().isStarted()) {
			response.getWriter().write(COCErrorCode.SETTEAM_STARTED.toString());
			return;
		}
		
		//Attempt to set the team
		String team = request.getParameter("team");
		attemptSetTeam(client, team, response);
	}

	/**
	 * Attempts to set the team of the given client to the given team.
	 * Will write the corresponding success or failure to the response.
	 * 
	 * @param client
	 * 		the client to set the team of
	 * @param team
	 * 		the team to set to
	 * @param response
	 * 		the response to write to
	 * @throws IOException
	 * 		if writing to the response causes an IOException
	 */
	protected void attemptSetTeam(WebClient client, String team, HttpServletResponse response) throws IOException {
		if ("DWARFS".equals(team)) {
			if (server.getDwarfsCount() >= WebServer.MAX_DWARFS) {
				response.getWriter().write(COCErrorCode.SETTEAM_TEAM_FULL.toString());
			} else {
				client.setTeam(Team.DWARFS);
				response.getWriter().write("" + Team.DWARFS.ordinal());
			}
		} else if ("ELVES".equals(team)) {
			if (server.getElvesCount() >= WebServer.MAX_ELVES) {
				response.getWriter().write(COCErrorCode.SETTEAM_TEAM_FULL.toString());
			} else {
				client.setTeam(Team.ELVES);
				response.getWriter().write("" + Team.ELVES.ordinal());
			}
		} else {
			response.getWriter().write(COCErrorCode.SETTEAM_INVALID_TEAM.toString());
		}
	}
	
	/**
	 * Handles a map request.
	 * 
	 * @param request
	 * 		the HTTP request
	 * @param response
	 * 		the object to write the response to
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void onMapRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.OK_200);
		response.setContentType("text/json");
		
		WebClient client = server.getUser(request);
		if (client == null) {
			response.getWriter().write(COCErrorCode.UNAUTHORIZED.toJSON());
			return;
		}

		JSONObject json = Main.getInstance().getCurrentGame().getLevel().toWebJSON();
		response.getWriter().write(json.toString());
	}
	
	/**
	 * Handles a map request over a websocket.
	 * 
	 * @param client
	 * 		the client who issued the request
	 * @throws IOException
	 * 		if writing the response causes an IOException
	 */
	public void onMapRequest(WebClient client) throws IOException {
		JSONObject json = Main.getInstance().getCurrentGame().getLevel().toWebJSON();
		client.sendMessage(json, null);
	}
	
	/**
	 * Handles an action request.
	 *
	 * @param request
	 * 		the HTTP request
	 * @param response
	 * 		the object to write the response to
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void onActionRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		WebClient client = server.getUser(request);
		if (client == null) {
			response.getWriter().write(COCErrorCode.UNAUTHORIZED.toString());
			return;
		}

		int xCoord = Integer.parseInt(request.getParameter("x"));
		int yCoord = Integer.parseInt(request.getParameter("y"));
		Action action = Action.getAction(Integer.parseInt(request.getParameter("action")));

		attemptAction(client, action, xCoord, yCoord, response);
	}
	
	/**
	 * Handles an action request over a websocket.
	 * 
	 * @param client
	 * 		the webclient that issued the request
	 * @param xCoord
	 * 		the x coordinate of the action
	 * @param yCoord
	 * 		the y coordinate of the action
	 * @param action
	 * 		the action to perform
	 * @throws IOException
	 * 		if writing to the response causes an IOException
	 */
	public void onActionRequest(WebClient client, int xCoord, int yCoord, Action action) throws IOException {
		attemptAction(client, action, xCoord, yCoord, null);
	}
	
	/**
	 * Attempt to perform an action.
	 *
	 * @param client
	 * 		the client that is attempting the action
	 * @param action
	 * 		the action to perform
	 * @param xCoord
	 * 		the x coordinate to perform the action at
	 * @param yCoord
	 * 		the y coordinate to perform the action at
	 * @param response
	 * 		the object to write the response to. Can be null for websockets
	 * @throws IOException
	 * 		if writing to the response causes an IOException
	 */
	protected void attemptAction(WebClient client, Action action, int xCoord, int yCoord, HttpServletResponse response) throws IOException {
		if (!WebUtil.checkValidAction(action, client.getTeam())) {
			client.sendMessage(COCErrorCode.ACTION_ILLEGAL.toString(), response);
			return;
		}

		if (!WebUtil.checkValidLocation(xCoord, yCoord, action)) {
			client.sendMessage(COCErrorCode.ACTION_ILLEGAL_LOCATION.toString(), response);
			return;
		}

		if (!WebUtil.checkWithinCooldown(action, client)) {
			client.sendMessage(COCErrorCode.ACTION_COOLDOWN.toString(), response);
			return;
		}

		try {
			ActionUtil.perform(action, xCoord, yCoord);
			if (response != null) {
				client.confirmMessage(response);
			}
		} catch (Exception ex) {
			client.sendMessage(COCErrorCode.SERVER_ERROR.toString(), response);
			throw ex;
		}
	}
	
	/**
	 * Handles a statusUpdate request.
	 * 
	 * @param request
	 * 		the HTTP request
	 * @param response
	 * 		the object to write the response to
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void onStatusUpdateRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		WebClient client = server.getUser(request);
		if (client == null) {
			response.setStatus(HttpStatus.OK_200);
			response.setContentType("text/json");
			response.getWriter().write(COCErrorCode.UNAUTHORIZED.toJSON());
			return;
		}

		sendStatusUpdate(client, response);
	}
	
	/**
	 * Sends a status update.
	 * 
	 * @param client
	 * 		the client to send to
	 * @param response
	 * 		the object to write the response to. Can be null for websockets
	 * @throws IOException
	 * 		if writing to the response causes an IOException
	 */
	public void sendStatusUpdate(WebClient client, HttpServletResponse response) throws IOException {
		Main main = Main.getInstance();
		Game game = main.getCurrentGame();
		GameState state = main.getGameState();
		
		JSONObject json = new JSONObject();
		json.put("team", client.getTeam().ordinal());
		json.put("state", state.ordinal());
		
		switch (state) {
			case WAITING:
				//Fall through to running
			case RUNNING:
				json.put("entities", EntityUtil.entitiesToJson(game.getEntities(), game.getPlayer()));
				json.put("explored", game.getLevel().toExploredWebJSON());
				break;
			case PAUSED:
				//We don't send any other data when the game is paused
				break;
			case ENDED:
				Boolean elvesWin = ((EndingController) main.getController()).didElvesWin();
				json.put("winner", elvesWin);
				break;
			default:
				break;
		}

		client.sendMessage(json, response);
	}
}
