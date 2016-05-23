package nl.tudelft.contextproject.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.Listener;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.dummy.DummyKeyInput;
import com.jme3.input.dummy.DummyMouseInput;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.JmeSystem;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.level.Level;

/**
 * Utility class for testing.
 * This class has en excessive amount of comments, as things could
 * change if we expand on our game.
 */
public final class TestUtil extends TestBase {
	private TestUtil() { }
	
	/**
	 * Ensures that Main.getInstance is a mock or spy.
	 * 
	 * <p>The created Main will have non-null return values for the following methods:
	 * <ul>
	 * <li>Spy on normal: {@link Main#getAssetManager()}</li>
	 * <li>Mock: {@link Main#getAudioRenderer()}</li>
	 * <li>Spy on normal: {@link Main#getCamera()}</li>
	 * <li>Spy on normal: {@link Main#getListener()}</li>
	 * <li>Spy on dummy: {@link Main#getInputManager()}</li>
	 * <li>Spy on normal: {@link Main#getRootNode()}</li>
	 * <li>Spy on normal: {@link Main#getGuiNode()}</li>
	 * </ul>
	 * 
	 * @param resetInvocations
	 * 		if true, invocations made on Main are reset.
	 * 		if false, invocations are only reset when it's safe to do so.
	 */
	public static void ensureMainMocked(boolean resetInvocations) {
		Main main = Main.getInstance();
		
		//Check if the current instance is already mocked by us
		if ("TestUtilMockedMain".equals(main.toString())) return;
		
		boolean doReset = false;
		
		//Check if main is a mock
		if (isMock(main)) {
			//Main is mocked, but not by us!
			
			//Ensure that tick listeners work properly
			doCallRealMethod().when(main).attachTickListener(any());
			doCallRealMethod().when(main).removeTickListener(any());
			
			//We will only reset if the caller asked for it
			doReset = resetInvocations;
		} else {
			//Main is not mocked
			
			//We can safely reset
			doReset = true;
			
			//Spy on the main and set the instance
			main = spy(main);
			Main.setInstance(main);
		}
		
		//Get all necessary fields before resetting.
		AssetManager assetManager 	= main.getAssetManager();
		AudioRenderer audioRenderer = main.getAudioRenderer();
		Camera camera 				= main.getCamera();
		FlyByCamera flyByCamera 	= main.getFlyByCamera();
		Listener listener 			= main.getListener();
		InputManager inputManager 	= main.getInputManager();
		Node rootNode 				= main.getRootNode();
		Node guiNode 				= main.getGuiNode();
		
		//Reset the mock so that we do not keep track of the above getter methods
		if (doReset) {
			reset(main);
		}
		
		//We need to be able to check if we created the main instance
		when(main.toString()).thenReturn("TestUtilMockedMain");

		//Ensure that there is a spy AssetManager.
		assetManager = toMockito(assetManager, () -> JmeSystem.newAssetManager(JmeSystem.getPlatformAssetConfigURL()));
		doReturn(assetManager).when(main).getAssetManager();
		
		//Ensure that there is a mock AudioRenderer
		audioRenderer = toMockito(audioRenderer, AudioRenderer.class, false);
		doReturn(audioRenderer).when(main).getAudioRenderer();
		
		//Ensure that there is a spy Camera
		camera = toMockito(camera, () -> new Camera(640, 480));
		doReturn(camera).when(main).getCamera();
		
		//Ensure that there is a spy FlyByCamera
		final Camera cam = camera;
		flyByCamera = toMockito(flyByCamera, () -> new FlyByCamera(cam));
		doReturn(flyByCamera).when(main).getFlyByCamera();
		
		//Ensure that there is a spy Listener
		listener = toMockito(listener, Listener.class, true);
		doReturn(listener).when(main).getListener();
		
		//Ensure that there is a spy InputManager
		inputManager = toMockito(inputManager, () -> {
			//Use dummy mouse and key input
			DummyMouseInput dmi = spy(DummyMouseInput.class);
			DummyKeyInput dki = spy(DummyKeyInput.class);
			
			//Create the InputManager
			InputManager tbr = spy(new InputManager(dmi, dki, null, null));
			
			//Reset the mouse and key input spies
			reset(dmi, dki);
			
			return tbr;
		});
		doReturn(inputManager).when(main).getInputManager();
		
		//Ensure that there is a spy root Node
		rootNode = toMockito(rootNode, () -> new Node("Root Node"));
		doReturn(rootNode).when(main).getRootNode();
		
		//Ensure that there is a spy gui Node
		guiNode = toMockito(guiNode, () -> new Node("Gui Node"));
		doReturn(guiNode).when(main).getGuiNode();
	}
	
	/**
	 * This method returns a mockito mock or spy for the given object.
	 * 
	 * <p>If the object is null, then this method will create a new mock or spy of the given class.
	 * <br>If the object is not null, but not a spy or mock, then this method will wrap the object in a spy.
	 * <br>Otherwise, the object is returned as is.
	 * 
	 * @param <T>
	 * 		the type of the object
	 * @param <S>
	 * 		the type that should be spied/mocked when object is null
	 * @param object
	 * 		the current object
	 * @param clazz
	 * 		the Class to create a mock/spy of if object is null
	 * @param trySpy
	 * 		if true, will create a spy when object is null.
	 * 		If false, will create a mock when object is null.
	 * @return
	 * 		a spy or mock of the given object
	 */
	public static <T, S extends T> T toMockito(T object, Class<S> clazz, boolean trySpy) {
		if (object == null) {
			//The object is null, so we will create a spy or mock with the provided class.
			if (trySpy) {
				return spy(clazz);
			} else {
				return mock(clazz);
			}
		} else if (!isMock(object)) {
			//The object is not null, but not a mock yet, so we spy on it.
			return spy(object);
		}
		
		//The object already is a mock, so we can return it as-is.
		return object;
	}
	
	/**
	 * This method returns a mockito mock or spy for the given object.
	 * 
	 * <p>If the object is null, then this method will create a new spy of the result of the given supplier.
	 * <br>If the object is not null, but not a spy or mock, then this method will wrap the object in a spy.
	 * <br>Otherwise, the object is returned as is.
	 * 
	 * @param <T>
	 * 		the type of the object
	 * @param <S>
	 * 		the type that should be spied when object is null
	 * @param object
	 * 		the current object
	 * @param constructor
	 * 		a supplier to create a new instance of S if object is null
	 * @return
	 * 		a spy or mock of the given object
	 */
	public static <T, S extends T> T toMockito(T object, Supplier<S> constructor) {
		if (object == null) {
			//The object is null, so create a mock using the supplier
			S spyObject = constructor.get();
			
			//If the supplier already returned a mock, simply return it.
			if (isMock(spyObject)) return spyObject;
			
			//Otherwise return a spy
			return spy(spyObject);
		} else if (!isMock(object)) {
			//The object is not null, but not a mock, so we spy on it.
			return spy(object);
		}
		
		//The object already is a mock, so we can return it as-is.
		return object;
	}
	
	/**
	 * Checks if an object is a mock or spy.
	 * 
	 * @param object
	 * 		the object to check
	 * @return
	 * 		if the given object is a mock (or spy)
	 */
	public static boolean isMock(Object object) {
		if (object == null) return false;
		
		return mockingDetails(object).isMock();
	}
	
	/**
	 * Ensures that {@link Main#getCurrentGame()} returns a mocked game.
	 * The game returned will be a Mockito spy.
	 */
	public static void mockGame() {
		//Ensure that main is mocked.
		ensureMainMocked(false);
		
		Main main = Main.getInstance();
		Game game = main.getCurrentGame();
		
		if (game == null) {
			//There is no game yet, so we need to create one.
			
			//Mock a level
			Level level = mock(Level.class);
			
			//Create a new game and spy on it
			game = spy(new Game(level));
			
			//Return the spied game
			doReturn(game).when(main).getCurrentGame();
		} else if (!mockingDetails(game).isMock()) {
			//There is already a game, but it is not a mock.
			
			//Spy on the current game
			game = spy(game);
			
			//Return the spied game
			doReturn(game).when(main).getCurrentGame();
		}
	}
	
	/**
	 * Sets the current game state to the given state.
	 * 
	 * <p>This mocks the GameState.
	 * 
	 * @param state
	 * 		the GameState to set
	 */
	public static void setGameState(GameState state) {
		//Ensure that main is mocked
		ensureMainMocked(false);
		
		//Set the state
		Main main = Main.getInstance();
		doReturn(state).when(main).getGameState();
	}
}
