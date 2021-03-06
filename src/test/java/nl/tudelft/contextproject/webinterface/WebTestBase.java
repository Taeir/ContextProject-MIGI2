package nl.tudelft.contextproject.webinterface;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.junit.BeforeClass;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.moving.VRPlayer;
import nl.tudelft.contextproject.model.entities.util.EntityType;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

import lombok.SneakyThrows;

/**
 * Base class for the WebServer tests, with some convenience methods.
 */
public class WebTestBase extends TestBase {
	
	/**
	 * Turn web server specific logging off.
	 * Turns off the "webInterface" logger and the jetty logger.
	 * 
	 * The jetty logger is turned of by making use of Mockito as a mocked method by
	 * default returns nothing if called, when no return statement is defined. This
	 * causes the jetty logger to not show up in the logs during testing.
	 */
	@BeforeClass
	public static void webBeforeClassSetUp() {
		Logger.getLogger("WebInterface").setLevel(java.util.logging.Level.OFF);
		
		//Create mocked logger that does nothing
		org.eclipse.jetty.util.log.Logger noLoggerMock = mock(org.eclipse.jetty.util.log.Logger.class);
		when(noLoggerMock.getName()).thenReturn("No logging.");
		when(noLoggerMock.getLogger(any())).thenReturn(noLoggerMock);
		org.eclipse.jetty.util.log.Log.setLog(noLoggerMock);
	}
	
	/**
	 * Sets the given NormalHandler on the given WebServer.
	 * 
	 * @param handler
	 * 		the NormalHandler to set
	 * @param server
	 * 		the server to set the handler of
	 */
	public void setHandler(NormalHandler handler, WebServer server) {
		server.setNormalHandler(handler);
	}
	
	/**
	 * Creates a spied cookie with the given id.
	 * 
	 * @param id
	 * 		the id of the cookie
	 * @return
	 * 		a spied cookie with the given id
	 */
	public Cookie createCookie(String id) {
		Cookie cookie = new Cookie(WebServer.COOKIE_NAME, id);
		cookie.setMaxAge(WebServer.COOKIE_MAX_AGE);
		return spy(cookie);
	}
	
	/**
	 * Creates a mocked HttpServletRequest with the given session id.
	 * The request will use method "GET" and will access URI "/".
	 * 
	 * @param id
	 * 		the session id of the request
	 * @return
	 * 		a mocked HttpServletRequest
	 */
	public HttpServletRequest createMockedRequest(String id) {
		return createMockedRequest(id, true, "/");
	}
	
	/**
	 * Creates a mocked HttpServletRequest with the given parameters.
	 * 
	 * @param id
	 * 		the session id of the request
	 * @param method
	 * 		the method of the request. true = GET, false = POST
	 * @param uri
	 * 		the uri accessed (/index.html)
	 * @return
	 * 		a mocked HttpServletRequest
	 */
	public HttpServletRequest createMockedRequest(String id, boolean method, String uri) {
		//Mock the request
		HttpServletRequest request = mock(HttpServletRequest.class);

		//Set the session2 cookie
		if (id != null) {
			Cookie cookie = createCookie(id);
			when(request.getCookies()).thenReturn(new Cookie[] { cookie });
		}

		//Set the method (GET/POST)
		String sMethod = method ? "GET" : "POST";
		when(request.getMethod()).thenReturn(sMethod);

		//Set the request URI (/index.html)
		when(request.getRequestURI()).thenReturn(uri);

		//Set a parameter map
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
			//Remove from map
			request.getParameterMap().remove(param);

			//"Unmock" methods
			when(request.getParameterValues(param)).thenReturn(null);
			when(request.getParameter(param)).thenReturn(null);
			
			return;
		}

		//Add to the parameter map
		request.getParameterMap().put(param, values);

		//Stub the getParamterValues method
		when(request.getParameterValues(param)).thenReturn(values);
		
		if (values.length == 1) {
			//If there is only one value, then stub the getParamter method
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
	
	/**
	 * Mocks the level, such that all tiles in the level have the given TileType.
	 * 
	 * @param tileType
	 * 		the type to use for all tiles
	 */
	public void mockLevel(TileType tileType) {
		Game mockedGame = mock(Game.class);
		Level mockedLevel = mock(Level.class);
		MazeTile mockedTile = mock(MazeTile.class);
		VRPlayer mockedPlayer = mock(VRPlayer.class);
		
		//Main
		when(Main.getInstance().getCurrentGame()).thenReturn(mockedGame);
		
		//Game
		when(mockedGame.getLevel()).thenReturn(mockedLevel);
		when(mockedGame.getEntities()).thenReturn(new HashSet<>());
		when(mockedGame.getPlayer()).thenReturn(mockedPlayer);
		
		//Level
		when(mockedLevel.getTile(anyInt(), anyInt())).thenReturn(mockedTile);
		when(mockedLevel.toWebJSON()).thenReturn(new JSONObject());
		when(mockedLevel.toExploredWebJSON()).thenReturn(new JSONObject());
		
		//Tile
		when(mockedTile.getTileType()).thenReturn(tileType);
		
		//Player
		when(mockedPlayer.getLocation()).thenReturn(new Vector3f(1000, 1000, 1000));
		when(mockedPlayer.getType()).thenReturn(EntityType.PLAYER);
	}
}
