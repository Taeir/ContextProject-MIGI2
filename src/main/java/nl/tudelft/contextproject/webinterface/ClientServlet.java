package nl.tudelft.contextproject.webinterface;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.servlet.DefaultServlet;

/**
 * Servlet for handling client requests.
 */
public class ClientServlet extends DefaultServlet {
	private static final long serialVersionUID = -8897739798010474802L;
	
	private final WebServer server;
	
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
		//Redirect the root to index.html
		if (request.getRequestURI().equals("/")) {
			response.sendRedirect("/index.html");
			return;
		}
		
		super.doGet(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("[DEBUG] POST Request: URI=\"" + request.getRequestURI() + "\", Parameters=" + request.getParameterMap());
		
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
		
		if (client == null) {
			//Client is not known: unauthorized request
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("UNAUTHORIZED");
			return;
		}
		
//		if (game.isInProgress()) {
//			//You cannot switch teams while the game is in progress, so we send back the current team to fix up the client.
//			response.setStatus(HttpStatus.OK_200);
//			response.getWriter().write(client.getTeam().toUpperCase());
//			return;
//		}
		
		String team = request.getParameter("team");
		if (team == null) {
			//No team was specified: invalid request
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("INVALID");
			return;
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
		
		if (client == null) {
			//Client is not known: unauthorized request
			response.setStatus(HttpStatus.OK_200);
			response.setContentType("text/json");
			response.getWriter().write("{auth: false}");
			return;
		}

		//We will send a JSON status update object.
		response.setStatus(HttpStatus.OK_200);
		response.setContentType("text/json");
		

		@SuppressWarnings("resource")
		PrintWriter writer = response.getWriter();
		writer.append("{team: ").append(client.getTeam().toUpperCase());
//		writer.append(", state: ").append(game.getState().name());
		

//		switch (game.getState()) {
//			//If the state is STARTED or PAUSED, then send map and player information
//			case STARTED:
//			case PAUSED:
//				//TODO Actual player information
//				writer.append(", player: { x: 0, y: 0, z: 0, hp: 0, bombs: 0, keys: [] }");
//		
//				//TODO Add map updates
//				break;
//
//			//If the state is ENDED, then send the game statistics
//			case ENDED:
//				//TODO Add game statistics
//				break;
//			
//			default:
//				break;
//		}
		
		writer.append(" }");
	}
}
