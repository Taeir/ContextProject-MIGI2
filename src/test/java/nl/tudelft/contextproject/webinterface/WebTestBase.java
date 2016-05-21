package nl.tudelft.contextproject.webinterface;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.test.TestUtil;

import lombok.SneakyThrows;

/**
 * Base class for the WebServer tests, with some convenience methods.
 */
public class WebTestBase {
	private static Main main;
	
	/**
	 * Ensures that {@link Main#getInstance()} is properly set up before any tests run.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		main = Main.getInstance();
		Main.setInstance(null);
		TestUtil.ensureMainMocked(true);
	}

	/**
	 * Restores the original Main instance after all tests are done.
	 */
	@AfterClass
	public static void tearDownAfterClass() {
		Main.setInstance(main);
	}
	
	/**
	 * Creates a spied session2 cookie with the given id.
	 * 
	 * @param id
	 * 		the id of the cookie
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
	 * @return
	 * 		a mocked HttpServletRequest
	 */
	public HttpServletRequest createMockedRequest(String id1, String id2, boolean auth) {
		return createMockedRequest(id1, id2, auth, true, "/");
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
	 * 		the method of the request. true = GET, false = POST
	 * @param uri
	 * 		the uri accessed (/index.html)
	 * @return
	 * 		a mocked HttpServletRequest
	 */
	public HttpServletRequest createMockedRequest(String id1, String id2, boolean auth, boolean method, String uri) {
		HttpServletRequest request = mock(HttpServletRequest.class);

		HttpSession session = createSession(id1);

		if (auth) when(request.getSession(false)).thenReturn(session);
		when(request.getSession(true)).thenReturn(session);
		when(request.getSession()).thenReturn(session);

		if (id2 != null) {
			Cookie cookie2 = createSession2Cookie(id2);
			when(request.getCookies()).thenReturn(new Cookie[] { cookie2 });
		}

		String sMethod = method ? "GET" : "POST";
		when(request.getMethod()).thenReturn(sMethod);

		when(request.getRequestURI()).thenReturn(uri);

		Map<String, String[]> map = new HashMap<>();
		when(request.getParameterMap()).thenReturn(map);
		when(request.getParameterNames()).thenAnswer(i -> Collections.enumeration(map.keySet()));

		return request;
	}
	
	/**
	 * Sets a parameter on a mocked request.
	 * 
	 * <p>If values is null, then the parameter is removed
	 * 
	 * @param request
	 * 		the mocked request to set a parameter of
	 * @param param
	 * 		the name of the parameter to set
	 * @param values
	 * 		the values of this parameter
	 */
	public void setParameter(HttpServletRequest request, String param, String... values) {
		if (values == null) {
			request.getParameterMap().remove(param);

			when(request.getParameterValues(param)).thenReturn(null);
			when(request.getParameter(param)).thenReturn(null);
			
			return;
		}

		request.getParameterMap().put(param, values);

		when(request.getParameterValues(param)).thenReturn(values);
		
		if (values.length == 1) {
			String param1 = values[0];
			when(request.getParameter(param)).thenReturn(param1);
		}
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
