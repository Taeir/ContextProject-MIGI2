package nl.tudelft.contextproject.webinterface;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.util.JSONUtil;
import nl.tudelft.contextproject.util.QRGenerator;

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
		LOG.fine("Received GET Request: URI=\"" + request.getRequestURI() + "\", Parameters=" + request.getParameterMap());

		if (request.getRequestURI().equals("/")) {
			response.sendRedirect("/index.html");
			return;
		} else if (request.getRequestURI().equals("/qr")) {
			//Write the QR code as a Base64 encoded image on a plain page.
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("<html><body><img src=\"data:image/png;base64,");
			response.getWriter().write(Base64.getEncoder().encodeToString(QRGenerator.getInstance().streamQRcode().toByteArray()));
			response.getWriter().write("\"/></body></html>");
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
			case "login":
				server.handleAuthentication(request, response);
				break;
			case "setteam":
				setTeam(request, response);
				break;
			case "map":
				getMap(request, response);
				break;
			case "explored":
				getExplored(request, response);
				break;
			case "status":
				statusUpdate(request, response);
				break;
			case "entities":
				getEntities(request, response);
				break;
			case "placebomb":
				placeBomb(request, response);
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
	 * Handles a setTeam request.
	 * 
	 * @param request
	 * 		the HTTP request
	 * @param response
	 * 		the HTTP response object
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void setTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
		WebClient client = server.getUser(request);

		if (!checkAuthorized(client, response, false)) return;
		
		if (Main.getInstance().getGameState().isStarted()) {
			//You cannot switch teams while the game is in progress, so we send back the current team to fix up the client.
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write(client.getTeam().toUpperCase());
			return;
		}
		
		String team = request.getParameter("team");
		if (team == null) {
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("INVALID");
		} else if (team.equals("DWARFS")) {
			client.setTeam(false);
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("DWARFS");
		} else if (team.equals("ELVES")) {
			client.setTeam(true);
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("ELVES");
		} else if (team.equals("NONE")) {
			client.setTeam(null);
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("NONE");
		} else {
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
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void getMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
		WebClient client = server.getUser(request);

		if (!checkAuthorized(client, response, true)) return;

		JSONObject json = Main.getInstance().getCurrentGame().getLevel().toWebJSON();

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
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void getExplored(HttpServletRequest request, HttpServletResponse response) throws IOException {
		WebClient client = server.getUser(request);

		if (!checkAuthorized(client, response, true)) return;

		JSONObject json = Main.getInstance().getCurrentGame().getLevel().toExploredWebJSON();

		response.setStatus(HttpStatus.OK_200);
		response.setContentType(CONTENT_TYPE_JSON);
		response.getWriter().write(json.toString());
	}

	/**
	 * Handles an entities request.
	 *
	 * @param request
	 * 		the HTTP request
	 * @param response
	 * 		the HTTP response object
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void getEntities(HttpServletRequest request, HttpServletResponse response) throws IOException {
		WebClient client = server.getUser(request);

		if (!checkAuthorized(client, response, true)) return;

		JSONObject json = JSONUtil.entitiesToJson(Main.getInstance().getCurrentGame().getEntities(), Main.getInstance().getCurrentGame().getPlayer());

		response.setStatus(HttpStatus.OK_200);
		response.setContentType("text/json");
		response.getWriter().write(json.toString());
	}

	/**
	 * Handles a placeBomb request.
	 *
	 * @param request
	 * 		the HTTP request
	 * @param response
	 * 		the HTTP response object
	 * @throws IOException
	 * 		if sending the response to the client causes an IOException
	 */
	public void placeBomb(HttpServletRequest request, HttpServletResponse response) throws IOException {
		WebClient client = server.getUser(request);

		if (!checkAuthorized(client, response, false)) return;

		int xCoordinate = Integer.parseInt(request.getParameter("x"));
		int yCoordinate = Integer.parseInt(request.getParameter("y"));

		Bomb newBomb = new Bomb();
		newBomb.move(xCoordinate, 1, yCoordinate);
		Main.getInstance().getCurrentGame().addEntity(newBomb);

		response.setStatus(HttpStatus.OK_200);
		response.getWriter().write("BOMB HAS BEEN PLACED.");
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
		json.put("team", client.getTeam().toUpperCase());
		json.put("state", Main.getInstance().getGameState().name());
		
		switch (Main.getInstance().getGameState()) {
			case RUNNING:
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

		response.getWriter().write(json.toString());
	}
}
