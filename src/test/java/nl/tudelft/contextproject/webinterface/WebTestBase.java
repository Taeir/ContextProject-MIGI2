package nl.tudelft.contextproject.webinterface;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.SneakyThrows;

/**
 * Base class for the WebServer tests, with some convenience methods.
 */
public class WebTestBase {
	/**
	 * Creates a spied session2 cookie with the given id.
	 * 
	 * @param id
	 * 		the id of the cookie
	 * 
	 * @return
	 * 		a spied session2 cookie with the given id
	 */
	public Cookie createSession2Cookie(String id) {
		Cookie cookie = new Cookie(WebServer.SESSION2_COOKIE, id);
		cookie.setMaxAge(24 * 60 * 60);
		return spy(cookie);
	}
	
	/**
	 * Creates a mocked session with the given id.
	 * 
	 * @param id
	 * 		the id of the session
	 * 
	 * @return
	 * 		a mocked session with the given id
	 */
	public HttpSession createSession(String id) {
		HttpSession session = mock(HttpSession.class);
		when(session.getId()).thenReturn(id);
		
		return session;
	}
	
	/**
	 * Creates a mocked HttpServletRequest with the given parameters.
	 * The request will use method "GET" and will access URI "/".
	 * 
	 * @param id1
	 * 		the session id of the request
	 * @param id2
	 * 		the session2 id of the request. If null, the request will not have a session2 cookie.
	 * @param auth
	 * 		if false, then getSession(false) will return null
	 * 
	 * @return
	 * 		a mocked HttpServletRequest
	 */
	public HttpServletRequest createMockedRequest(String id1, String id2, boolean auth) {
		return createMockedRequest(id1, id2, auth, "GET", "/");
	}
	
	/**
	 * Creates a mocked HttpServletRequest with the given parameters.
	 * 
	 * @param id1
	 * 		the session id of the request
	 * @param id2
	 * 		the session2 id of the request. If null, the request will not have a session2 cookie.
	 * @param auth
	 * 		if false, then getSession(false) will return null
	 * @param method
	 * 		the method (GET/POST) of the request
	 * @param uri
	 * 		the uri accessed (/index.html)
	 * 
	 * @return
	 * 		a mocked HttpServletRequest
	 */
	public HttpServletRequest createMockedRequest(String id1, String id2, boolean auth, String method, String uri) {
		//Mock the request
		HttpServletRequest request = mock(HttpServletRequest.class);
		
		//Create spied/mocked sessions
		HttpSession session = createSession(id1);
		
		//Set the session
		if (auth) when(request.getSession(false)).thenReturn(session);
		when(request.getSession(true)).thenReturn(session);
		when(request.getSession()).thenReturn(session);
		
		//Set the session2 cookie
		if (id2 != null) {
			Cookie cookie2 = createSession2Cookie(id2);
			when(request.getCookies()).thenReturn(new Cookie[] { cookie2 });
		}
		
		//Set the method (GET/POST)
		when(request.getMethod()).thenReturn(method);
		
		//Set the request URI (/index.html)
		when(request.getRequestURI()).thenReturn(uri);
		
		return request;
	}
	
	/**
	 * Creates a mocked HTTP response.
	 * 
	 * @return
	 * 		a mocked HTTP response
	 */
	@SneakyThrows(IOException.class)
	public HttpServletResponse createMockedResponse() {
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(response.getWriter()).thenReturn(mock(PrintWriter.class));
		
		return response;
	}
}
