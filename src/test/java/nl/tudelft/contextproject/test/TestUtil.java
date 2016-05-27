package nl.tudelft.contextproject.test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.function.Supplier;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.Listener;
import com.jme3.input.InputManager;
import com.jme3.input.dummy.DummyKeyInput;
import com.jme3.input.dummy.DummyMouseInput;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.JmeSystem;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.level.Level;

/**
 * Utility class for testing.
 * 
 * <p>This class has an excessive amount of comments, as it does quite complicated things.
 */
public final class TestUtil extends TestBase {
	private static Main globalMain;
	
	private TestUtil() { }
	
	/**
	 * This method should be called in an {@code @BeforeClass} method.
	 */
	public static void recreateGlobalMain() {
		globalMain = new Main();
	}
	
	/**
	 * Ensures that Main.getInstance is a spy.
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
	 * @return
	 * 		the spied Main instance that was set
	 */
	public static Main setupMainForTesting() {
		if (globalMain == null) {
			globalMain = new Main();
		}
		
		Main mainNoSpy = globalMain;
		
		//Spy on the main and set the instance
		Main mainSpy = spy(globalMain);
		Main.setInstance(mainSpy);
		
		//Get all necessary fields before resetting.
		AssetManager assetManager 	= mainNoSpy.getAssetManager();
		AudioRenderer audioRenderer = mainNoSpy.getAudioRenderer();
		Camera camera 				= mainNoSpy.getCamera();
		//FlyByCamera flyByCamera 	= mainNoSpy.getFlyByCamera();
		Listener listener 			= mainNoSpy.getListener();
		InputManager inputManager 	= mainNoSpy.getInputManager();
		Node rootNode 				= mainNoSpy.getRootNode();
		Node guiNode 				= mainNoSpy.getGuiNode();
		
		//We need to be able to check if we created the main instance
		when(mainSpy.toString()).thenReturn("TestUtilSpyMain");

		//Ensure that there is a spy AssetManager.
		assetManager = toMockito(assetManager, () -> JmeSystem.newAssetManager(JmeSystem.getPlatformAssetConfigURL()));
		doReturn(assetManager).when(mainSpy).getAssetManager();
		
		//Ensure that there is a mock AudioRenderer
		audioRenderer = toMockito(audioRenderer, AudioRenderer.class, false);
		doReturn(audioRenderer).when(mainSpy).getAudioRenderer();
		
		//Ensure that there is a spy Camera
		camera = toMockito(camera, () -> new Camera(640, 480));
		doReturn(camera).when(mainSpy).getCamera();
		
		//Ensure that there is a spy Listener
		listener = toMockito(listener, Listener.class, true);
		doReturn(listener).when(mainSpy).getListener();
		
		//Ensure that there is a spy InputManager
		inputManager = toMockito(inputManager, () -> {
			//Use dummy mouse and key input
			DummyMouseInput dmi = spy(DummyMouseInput.class);
			DummyKeyInput dki = spy(DummyKeyInput.class);
			
			//Create the InputManager
			InputManager tbr = spy(new InputManager(dmi, dki, null, null));
			
			//Initialize the inputs
			dmi.initialize();
			dki.initialize();
			
			//Reset the mouse and key input spies
			reset(dmi, dki);
			
			return tbr;
		});
		when(mainSpy.getInputManager()).thenReturn(inputManager);
		
		//Ensure that there is a spy root Node
		rootNode = toMockito(rootNode, () -> new Node("Root Node"));
		mainNoSpy.setRootNode(rootNode);
		
		//Ensure that there is a spy gui Node
		guiNode = toMockito(guiNode, () -> new Node("Gui Node"));
		mainNoSpy.setGuiNode(guiNode);
		
		//Reset the controller and tick listeners
		mainNoSpy.setTickListeners(new ArrayList<>());
		mainNoSpy.setController(null);
		return mainSpy;
	}

	/**
	 * Cleans up the Main instance.
	 */
	public static void cleanupMain() {
		Main main = Main.getInstance();
		
		if (isMock(main)) {
			reset(main);
		}
		
		Main.setInstance(null);
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
	 * @return
	 * 		if the main class is mocked by us or not
	 */
	public static boolean isMainMocked() {
		Main main = Main.getInstance();
		
		return main.toString().equals("TestUtilSpyMain");
	}
	
	/**
	 * Ensures that {@link Main#getCurrentGame()} returns a mocked game.
	 * The game returned will be a Mockito spy.
	 */
	public static void mockGame() {
		Main main;
		if (!isMainMocked()) {
			main = setupMainForTesting();
		} else {
			main = Main.getInstance();
		}
		
		//Create a mocked level in a real GameController
		Level level = mock(Level.class);
		GameController gc = new GameController(main, level);
		
		//Spy on the game
		Game game = spy(gc.getGame());
		gc.setGame(game);
		
		//Spy on the game controller
		gc = spy(gc);
		main.setController(gc);
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
		Main main;
		if (!isMainMocked()) {
			main = setupMainForTesting();
		} else {
			main = Main.getInstance();
		}
		
		doReturn(state).when(main).getGameState();
	}
}
