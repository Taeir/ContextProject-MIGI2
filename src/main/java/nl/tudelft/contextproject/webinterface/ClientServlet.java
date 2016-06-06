package nl.tudelft.contextproject.webinterface;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.json.JSONObject;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.EndingController;
import nl.tudelft.contextproject.logging.Log;
import nl.tudelft.contextproject.util.webinterface.EntityUtil;

/**
 * Servlet for handling client requests.
 */
public class ClientServlet extends DefaultServlet {
	private static final long serialVersionUID = -8897739798010474802L;
	
	private static final String CONTENT_TYPE_JSON = "text/json";
	private static final Log LOG = Log.getLog("WebInterface");

	private final transient WebServer server;
	
	/**
	 * Creates a new ClientServlet for the given WebServer.
	 * 
	 * @param server
	 * 		the WebServer using this ClientServlet
	 */
	public ClientServlet(WebServer server) {
		this.server = server;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.fine("Received GET Request: URI=\"" + request.getRequestURI() + "\", Parameters=" + request.getParameterMap());

		String uri = request.getRequestURI();
		if (uri.equals("/")) {
			response.sendRedirect("/index.html");
			return;
		} else if (uri.equals("/qr")) {
			server.getNormalHandler().onQrRequest(response);
			return;
		}
		
		super.doGet(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.fine("Received POST Request: URI=\"" + request.getRequestURI() + "\", Parameters=" + request.getParameterMap());

		//Handle the post request differently based on the requested URL.
		String uri = request.getRequestURI().substring(1);
		switch (uri) {
			//Intentional fall through
			case "join":
			case "login":
				server.getNormalHandler().onJoinRequest(request, response);
				break;
			case "setteam":
				server.getNormalHandler().onSetTeamRequest(request, response);
				break;
			case "map":
				server.getNormalHandler().onMapRequest(request, response);
				break;
			case "status":
				statusUpdate(request, response);
				break;
			case "requestaction":
				server.getNormalHandler().onActionRequest(request, response);
				break;
			default:
				//Unknown post request, so propagate to superclass
				super.doPost(request, response);
				break;
		}
	}
	
	/**
	 * If the given client is not null, this method returns true.
	 * 
	 * <p>If the client is null, UNAUTHORIZED is sent in the response, and this method returns
	 * false.
	 * 
	 * @param client
	 * 		the client to check
	 * @param response
	 * 		the response to write to in case of not authorized
	 * @param json
	 * 		if true, uses json for the response
	 * @return
	 * 		true if the client is authorized
	 * 
	 * @throws IOException
	 * 		if writing to the response causes an IOException
	 */
	public boolean checkAuthorized(WebClient client, HttpServletResponse response, boolean json) throws IOException {
		if (client != null) return true;
		
		//Client is not known: unauthorized request
		response.setStatus(HttpStatus.OK_200);
		if (json) {
			response.setContentType(CONTENT_TYPE_JSON);
			response.getWriter().write("{auth: false}");
		} else {
			response.getWriter().write("UNAUTHORIZED");
		}
		return false;
	}
	
	/**
	 * Handles a statusUpdate request.
	 * 
	 * @param request
	 * 		the HTTP request
	 * @param response
	 * 		the HTTP response object
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void statusUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		WebClient client = server.getUser(request);

		if (!checkAuthorized(client, response, true)) return;

		response.setStatus(HttpStatus.OK_200);
		response.setContentType(CONTENT_TYPE_JSON);
		
		JSONObject json = new JSONObject();
		json.put("team", client.getTeam().ordinal());
		json.put("state", Main.getInstance().getGameState().name());
		
		switch (Main.getInstance().getGameState()) {
			case WAITING:
				//Fall through to running
			case RUNNING:
				json.put("entities",
						EntityUtil.entitiesToJson(Main.getInstance().getCurrentGame().getEntities(), Main.getInstance().getCurrentGame().getPlayer()));
				json.put("explored", Main.getInstance().getCurrentGame().getLevel().toExploredWebJSON());
				break;
			case PAUSED:
				//We don't send any other data when the game is paused
				break;
			case ENDED:
				Boolean elvesWin = ((EndingController) Main.getInstance().getController()).didElvesWin();
				json.put("winner", elvesWin);
				break;
			default:
				break;
		}

		response.getWriter().write(json.toString());
	}
}
