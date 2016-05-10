package nl.tudelft.contextproject.webinterface;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Scanner;

import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import nl.tudelft.contextproject.logging.Log;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Class to run the web interface.
 */
public class WebServer {
	//The name of the SESSION2 cookie
	private static final String SESSION2_COOKIE = "COC_SESSION2";
	
	private HashMap<String, WebClient> clients = new HashMap<>();
	private boolean running;
	private int port;
	private Server server;
	private HashSessionIdManager sessionIdManager;
	private ServletContextHandler contextHandler;
	
	/**
	 * Temporary main method, for debugging.
	 * 
	 * @param args
	 * 		the program arguments
	 * 
	 * @throws Exception
	 * 		if an exception occurs
	 */
	public static void main(String[] args) throws Exception {
		WebServer ws = new WebServer();
		ws.start(8080);
		
		//DEBUG
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String s;
		while ((s = sc.next()) != null) {
			if (s.equalsIgnoreCase("stop")) break;
			System.out.println(ws.clients);
		}
		
		ws.stop();
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
		//Check if the server is already running.
		if (running) throw new IllegalStateException("Server is already running");
		
		//Mark the server as now running and set the port
		this.running = true;
		this.port = port;
		
		//We create the server
		server = new Server(this.port);
		
		//We need a way to track users, so we use a session manager that uses cookies.
		
		//Create the session id manager
		sessionIdManager = new HashSessionIdManager();
		server.setSessionIdManager(sessionIdManager);
		
		//Create the session manager
		HashSessionManager sessionManager = new HashSessionManager();
		sessionManager.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));

		//Set the cookie config
		SessionCookieConfig scc = sessionManager.getSessionCookieConfig();
		scc.setComment("CoC Session Cookie");
		scc.setName("COC_SESSION");
		
		SessionHandler sessionHandler = new SessionHandler(sessionManager);
		
		//Create the handler that chains everything together.
		contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		
		//Handle requests on /
		contextHandler.setContextPath("/");
		
		//Fetch pages from the webinterface folder
		/* DEBUG */ contextHandler.setResourceBase(new File("src/main/resources/webinterface").getAbsolutePath());
		//contextHandler.setResourceBase(new File("webinterface").getAbsolutePath());
		
		//Set the session handler
		contextHandler.setSessionHandler(sessionHandler);
		
		//Add the handler to the server
		server.setHandler(contextHandler);
		
		//Add a servlet for handling sessions
		ClientServlet cs = new ClientServlet(this);
		contextHandler.addServlet(new ServletHolder(cs), "/");
		
		//Debug info about sessions
		sessionManager.addEventListener(new HttpSessionListener() {
			
			@Override
			public void sessionDestroyed(HttpSessionEvent se) {
				System.out.println("[DEBUG] Session destroyed: " + se.getSession().getId());
			}
			
			@Override
			public void sessionCreated(HttpSessionEvent se) {
				System.out.println("[DEBUG] Session created: " + se.getSession().getId());
			}
		});
		
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
		//Get the session. If it is null, we don't have the user.
		HttpSession session = request.getSession(false);
		if (session == null) return null;
		
		//Get the client for the SESSION1 id. If we find one, we return it.
		WebClient client = clients.get(session.getId());
		if (client != null) return client;
		
		//Check the SESSION2 cookie. If it is not present, then we don't have the user.
		Cookie session2 = findCookie(SESSION2_COOKIE, request.getCookies());
		if (session2 == null) return null;
		
		//If the SESSION2 id is the same as the SESSION1 id, then we don't have the user.
		if (session2.getValue().equals(session.getId())) return null;
		
		//Return the WebClient of the SESSION2 id.
		return clients.get(session2.getValue());
	}
	
	/**
	 * Checks if the user of the given request is authenticated.
	 * 
	 * @param request
	 * 		the http request of the user
	 * 
	 * @return
	 * 		true if the user is authenticated, false otherwise
	 */
	public boolean isAuthenticated(HttpServletRequest request) {
		Cookie cookie = findCookie(SESSION2_COOKIE, request.getCookies());
		if (cookie == null) return false;
		
		return clients.containsKey(cookie.getValue());
	}
	
	/**
	 * Handles authentication. This method returns true if the user has already been authenticated as a user
	 * in the game, and returns false otherwise.
	 * 
	 * <p>If the game has not yet started and is not full, the user will be authenticated as a new user and
	 * redirected to the select team page. This method will still return false in that case.
	 * 
	 * <p>NOTE: If this method returns false, then the response will be committed and unusable.
	 * 
	 * @param request
	 * 		the http request the user has made
	 * @param response
	 * 		the http response to the user
	 * 
	 * @return
	 * 		true if the user is authenticated, false otherwise
	 * 
	 * @throws IOException
	 * 		if writing the response to the client causes an IOException
	 */
	public boolean handleAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//Check if the client has a SESSION2 cookie
		Cookie cookie = findCookie(SESSION2_COOKIE, request.getCookies());
		if (cookie == null || !clients.containsKey(cookie.getValue())) {
			//This is a new user
			
//			if (game.isStarted()) {
//				//User attempts to connect while game has already started
//				Log.getLog("WebInterface").fine("Disallowed user from joining game: cannot join in progress game");
//				response.setStatus(HttpStatus.OK_200);
//				response.getWriter().write("IN_PROGRESS");
//			
//				return false;
//			}
//			
//			if (game.isFull()) {
//				//The game is full, user cannot join.
//				Log.getLog("WebInterface").fine("Disallowing user from joining game: game is full");
//				
//				response.setStatus(HttpStatus.OK_200);
//				response.getWriter().write("FULL");
//			
//				return false;
//			}
//			
			//User is allowed to join
			Log.getLog("WebInterface").fine("Allowing user to join");

			//Create a cookie
			cookie = createCookie(request);
			
			//Create a new client
			clients.put(cookie.getValue(), new WebClient());
			
			//Add the cookie to the response
			response.addCookie(cookie);
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("AUTHENTICATED");

			return true;
		}
		
		//This client is known in the current session
		WebClient client = clients.get(cookie.getValue());
		
		//Check if the session has changed
		HttpSession session = request.getSession(true);
		if (!cookie.getValue().equals(session.getId())) {
			//Client session was lost, so recreate it.
			cookie.setValue(session.getId());
			cookie.setMaxAge(24 * 60 * 60);
			
			//Add the client under their new session ID
			//We do not remove the client from their old ID, as other requests might still be using the old ID.
			clients.put(session.getId(), client);
			
			//Add the cookie to the response to update it
			response.addCookie(cookie);
			
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("UPDATED");
			return true;
		} else {
			//Client session and cookie match, so we are all good.
			response.setStatus(HttpStatus.OK_200);
			response.getWriter().write("AUTHENTICATED");
			return true;
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
	public Cookie findCookie(String name, Cookie[] cookies) {
		//There are no cookies, so return null
		if (cookies == null) return null;
		
		//Find the cookie
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie;
			}
		}
		
		//Cookie not found
		return null;
	}
	
	/**
	 * Creates a new SESSION2 cookie for the given request.
	 * 
	 * <p>To create the SESSION2 cookie, a session is created as well.
	 * 
	 * @param request
	 * 		the request to create the cookie for
	 * 
	 * @return
	 * 		the newly created cookie
	 */
	public Cookie createCookie(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Cookie cookie = new Cookie(SESSION2_COOKIE, session.getId());
		cookie.setMaxAge(24 * 60 * 60);
		return cookie;
	}
}
