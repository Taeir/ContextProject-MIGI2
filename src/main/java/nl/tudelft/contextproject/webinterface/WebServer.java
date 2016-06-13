package nl.tudelft.contextproject.webinterface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import nl.tudelft.contextproject.util.FileUtil;
import nl.tudelft.contextproject.webinterface.websockets.COCSocket;
import nl.tudelft.contextproject.webinterface.websockets.COCWebSocketServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Class to run the web interface.
 */
public class WebServer {
	public static final String COOKIE_NAME = "COC_SESSION";
	public static final int COOKIE_MAX_AGE = 24 * 60;
	public static final int MAX_PLAYERS = 4;
	public static final int MAX_DWARFS = 2;
	public static final int MAX_ELVES = 2;
	
	private Map<String, WebClient> clients = new ConcurrentHashMap<>();
	private boolean running;
	private int port;
	private Server server;
	private SessionIdManager sessionIdManager = new SessionIdManager();
	private NormalHandler normalHandler = new NormalHandler(this);
	private WebInventory inventory = new WebInventory();
	
	/**
	 * @return
	 * 		the map of authenticated clients
	 */
	public Map<String, WebClient> getClients() {
		return clients;
	}
	
	/**
	 * @return
	 * 		the amount of unique clients connected
	 */
	public int getUniqueClientCount() {
		return clients.size();
	}
	
	/**
	 * @return
	 * 		the amount of dwarfs
	 */
	public int getDwarfsCount() {
		return (int) clients.values().stream().filter(client -> client.isDwarf()).count();
	}
	
	/**
	 * @return
	 * 		the amount of elves
	 */
	public int getElvesCount() {
		return (int) clients.values().stream().filter(client -> client.isElf()).count();
	}
	
	/**
	 * Starts this webserver on the given port.
	 * 
	 * @param port
	 * 		the port to start the server on
	 * 
	 * @throws Exception
	 * 		if an exception occurs while attempting to start the server
	 */
	public synchronized void start(int port) throws Exception {
		if (running) throw new IllegalStateException("Server is already running");

		this.running = true;
		this.port = port;

		server = new Server(this.port);

		//Create the handler that chains everything together.
		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextHandler.setContextPath("/");
		contextHandler.setResourceBase(FileUtil.getFile("/webinterface/").getAbsolutePath());
		contextHandler.setInitParameter("cacheControl", "max-age=0,public");

		//Add the handler to the server
		server.setHandler(contextHandler);

		//Add a servlet for handling web sockets
		COCWebSocketServlet webServlet = new COCWebSocketServlet(this);
		contextHandler.addServlet(new ServletHolder(webServlet), "/ws/");

		//Add a servlet for handling sessions
		ClientServlet clientServlet = new ClientServlet(this);
		contextHandler.addServlet(new ServletHolder(clientServlet), "/");

		//Start the webserver
		server.start();
	}
	
	/**
	 * Stops this WebServer. If the WebServer was not running, this method has no effect.
	 * 
	 * @throws Exception
	 * 		if an exception occurs while attempting to stop the server
	 */
	public synchronized void stop() throws Exception {
		if (!running) return;
		
		server.stop();
		running = false;
	}
	
	/**
	 * @return
	 * 		if the server is currently running
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Returns the WebClient that is responsible for the given request.
	 * 
	 * <p>If the request has not been sent from a known client, then this method returns null.
	 * 
	 * @param request
	 * 		the request to get the WebClient of
	 * 
	 * @return
	 * 		the WebClient for the given request, or null if no such WebClient exists
	 */
	public WebClient getUser(HttpServletRequest request) {
		//Check the session cookie. If it is not present, then we don't have the user.
		Cookie session = findCookie(COOKIE_NAME, request.getCookies());
		if (session == null) return null;
		
		return getUser(session.getValue());
	}
	
	/**
	 * Gets the client for the given session ID.
	 * 
	 * @param session
	 * 		the session id
	 * @return
	 * 		the client with the given session ID, or null if there is no such client
	 */
	public WebClient getUser(String session) {
		return clients.get(session);
	}
	
	/**
	 * @return
	 * 		the inventory of this server
	 */
	public WebInventory getInventory() {
		return inventory;
	}
	
	/**
	 * Disconnects the given client from the game.
	 * 
	 * @param client
	 * 		the client to disconnect
	 * @param statusCode
	 * 		the status code to send
	 */
	public void disconnect(WebClient client, int statusCode) {
		COCSocket socket = client.getWebSocket();
		if (socket != null) socket.getSession().close(statusCode, null);
		
		clients.values().remove(client);
	}
	
	/**
	 * @return
	 * 		the handler for normal requests for this server
	 */
	public NormalHandler getNormalHandler() {
		return this.normalHandler;
	}
	
	/**
	 * Sets the normalhandler to the given handler.
	 * 
	 * <p>This method is only used by the tests.
	 * 
	 * @param handler
	 * 		the new NormalHandler to use
	 */
	protected void setNormalHandler(NormalHandler handler) {
		this.normalHandler = handler;
	}

	/**
	 * Clears the cooldowns set for all clients currently connected to the server.
	 */
	public void clearCooldowns() {
		for (WebClient client : this.clients.values()) {
			client.resetPerformed();
		}
	}

	/**
	 * Finds the cookie with the given name. Returns null if no such cookie is present in the
	 * given array of cookies.
	 * 
	 * @param name
	 * 		the name of the cookie
	 * @param cookies
	 * 		the array of cookies
	 * 
	 * @return
	 * 		the cookie with the given name, or null if there is no such cookie
	 */
	public static Cookie findCookie(String name, Cookie[] cookies) {
		if (cookies == null) return null;

		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie;
			}
		}

		return null;
	}
	
	/**
	 * Creates a new cookie for tracking a client.
	 * 
	 * @return
	 * 		the newly created cookie
	 */
	public Cookie createCookie() {
		Cookie cookie = new Cookie(COOKIE_NAME, sessionIdManager.createSessionId());
		cookie.setMaxAge(COOKIE_MAX_AGE);
		return cookie;
	}
}
