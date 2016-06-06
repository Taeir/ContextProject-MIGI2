package nl.tudelft.contextproject.webinterface;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.DefaultServlet;

import nl.tudelft.contextproject.logging.Log;

/**
 * Servlet for handling client requests.
 */
public class ClientServlet extends DefaultServlet {
	private static final long serialVersionUID = -8897739798010474802L;
	
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
			case "indexrefresh":
				server.getNormalHandler().onIndexRefresh(request, response);
				break;
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
				server.getNormalHandler().onStatusUpdateRequest(request, response);
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
}
