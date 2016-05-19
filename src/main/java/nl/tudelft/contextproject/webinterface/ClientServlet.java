package nl.tudelft.contextproject.webinterface;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.json.JSONObject;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.logging.Log;

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
		//Log event
		LOG.fine("Received GET Request: URI=\"" + request.getRequestURI() + "\", Parameters=" + request.getParameterMap());
		
		//Redirect the root to index.html
		if (request.getRequestURI().equals("/")) {
			response.sendRedirect("/index.html");
			return;
		}
		
		super.doGet(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Log event
		LOG.fine("Received POST Request: URI=\"" + request.getRequestURI() + "\", Parameters=" + request.getParameterMap());
		
		//Handle the post request differently based on the requested URL.
		String uri = request.getRequestURI().substring(1);
		switch (uri) {
			case "login":
				//Handle authentication of this user.
				server.handleAuthentication(request, response);
				break;
				
			case "setteam":
				//Client wants to change their team
				setTeam(request, response);
				break;
				
			case "map":
				//Client wants the initial map
				getMap(request, response);
				break;
				
			case "explored":
				//Client wants explored tiles
				getExplored(request, response);
				break;
				
			case "status":
				//Client wants a status update
				statusUpdate(request, response);
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
	 * 
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
	 * Handles a setTeam request.
	 * 
	 * @param request
	 * 		the HTTP request
	 * @param response
	 * 		the HTTP response object
	 * 
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void setTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//Get the WebClient of the request
		WebClient client = server.getUser(request);
		
		//Check authorized
		if (!checkAuthorized(client, response, false)) return;
		
		if (Main.getInstance().getGameState().isStarted()) {
			//You cannot switch teams while the game is in progress, so we send back the current team to fix up the client.
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write(client.getTeam().toUpperCase());
			return;
		}
		
		String team = request.getParameter("team");
		if (team == null) {
			//No team was specified: invalid request
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("INVALID");
		} else if (team.equals("DWARFS")) {
			//Change team to dwarfs
			client.setTeam(false);
			
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("DWARFS");
		} else if (team.equals("ELVES")) {
			//Change team to elves
			client.setTeam(true);
			
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("ELVES");
		} else if (team.equals("NONE")) {
			//Change team to no team
			client.setTeam(null);
			
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("NONE");
		} else {
			//Not a valid team: invalid request
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("INVALID");
		}
	}
	
	/**
	 * Handles a map request.
	 * 
	 * @param request
	 * 		the HTTP request
	 * @param response
	 * 		the HTTP response object
	 * 
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void getMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//Get the WebClient of the request
		WebClient client = server.getUser(request);
		
		//Check authorized
		if (!checkAuthorized(client, response, true)) return;
		
		//We need to send the map to the clients
		//We send the map as a width, height and a bunch of tiles.
		//Entities (doors, keys, bombs, player, enemies) and such, are sent differently
		
		//Encode the level in JSON
		JSONObject json = Main.getInstance().getCurrentGame().getLevel().toWebJSON();

		//Send the response
		response.setStatus(HttpStatus.OK_200);
		response.setContentType(CONTENT_TYPE_JSON);
		response.getWriter().write(json.toString());
	}
	
	/**
	 * Handles a explored request.
	 * 
	 * @param request
	 * 		the HTTP request
	 * @param response
	 * 		the HTTP response object
	 * 
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void getExplored(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//Get the WebClient of the request
		WebClient client = server.getUser(request);
		
		//Check authorized
		if (!checkAuthorized(client, response, true)) return;
		
		//Send the explored map
		JSONObject json = Main.getInstance().getCurrentGame().getLevel().toExploredWebJSON();

		//Send the response
		response.setStatus(HttpStatus.OK_200);
		response.setContentType(CONTENT_TYPE_JSON);
		response.getWriter().write(json.toString());
	}
	
	/**
	 * Handles a statusUpdate request.
	 * 
	 * @param request
	 * 		the HTTP request
	 * @param response
	 * 		the HTTP response object
	 * 
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void statusUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//Get the WebClient of the request
		WebClient client = server.getUser(request);
		
		//Check authorization
		if (!checkAuthorized(client, response, true)) return;

		//We will send a JSON status update object.
		response.setStatus(HttpStatus.OK_200);
		response.setContentType(CONTENT_TYPE_JSON);
		
		JSONObject json = new JSONObject();
		json.put("team", client.getTeam().toUpperCase());
		json.put("state", Main.getInstance().getGameState().name());
		
		switch (Main.getInstance().getGameState()) {
			//If the state is RUNNING or PAUSED, then send map and player information
			case RUNNING:
			case PAUSED:
				//TODO Actual player information
				//json.put("player", VRPlayer().toJSON());
		
				//TODO Add entity updates
				//TODO Add explored updates
				break;

			//If the state is ENDED, then send the game statistics
			case ENDED:
				//TODO Add game statistics
				break;
			
			default:
				break;
		}
		
		//Write the json to the response
		response.getWriter().write(json.toString());
	}
}
