package nl.tudelft.contextproject;

import java.util.Arrays;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.state.AbstractAppState;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

import nl.tudelft.contextproject.audio.AudioManager;
import nl.tudelft.contextproject.audio.BackgroundMusic;
import nl.tudelft.contextproject.controller.Controller;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.controller.GameState;
import nl.tudelft.contextproject.controller.PauseController;
import nl.tudelft.contextproject.controller.WaitingController;
import nl.tudelft.contextproject.util.FileUtil;
import nl.tudelft.contextproject.logging.Log;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.webinterface.WebServer;

import jmevr.app.VRApplication;

/**
 * Main class of the game 'The Cave of Caerbannog'.
 */
public class Main extends VRApplication {

	//TODO this should be set in a setting class preferably.
	public static final int PORT_NUMBER = 8080;
	
	private static boolean debugHud;
	
	private static Main instance;
	private Controller controller;
	private WebServer webServer;
	private List<TickListener> tickListeners = new LinkedList<>();

	/**
	 * Main method that is called when the program is started.
	 *
	 * @param args
	 * 		run-specific arguments
	 */
	public static void main(String[] args) {
		FileUtil.init();
		Main main = getInstance();
		List<String> a = Arrays.asList(args);
		debugHud = a.contains("--debugHud");

		AppSettings settings = new AppSettings(true);
		settings.setUseJoysticks(true);
		main.setSettings(settings);

		//Disable the SteamVR compositor (kinda needed at the moment)
		//main.preconfigureVRApp(PRECONFIG_PARAMETER.USE_STEAMVR_COMPOSITOR, false);
		
		//Use full screen distortion, maximum FOV, possibly quicker (not compatible with instancing)
		main.preconfigureVRApp(PRECONFIG_PARAMETER.USE_CUSTOM_DISTORTION, false);
		//Runs faster when set to false, but will allow mirroring
		main.preconfigureVRApp(PRECONFIG_PARAMETER.ENABLE_MIRROR_WINDOW, true);
		//Render two eyes, regardless of SteamVR
		main.preconfigureVRApp(PRECONFIG_PARAMETER.FORCE_VR_MODE, true);
		main.preconfigureVRApp(PRECONFIG_PARAMETER.SET_GUI_CURVED_SURFACE, true);
		main.preconfigureVRApp(PRECONFIG_PARAMETER.FLIP_EYES, true);
		//Show gui even if it is behind things
		main.preconfigureVRApp(PRECONFIG_PARAMETER.SET_GUI_OVERDRAW, true);
		//Faster VR rendering, requires some vertex shader changes (see jmevr/shaders/Unshaded.j3md)
		main.preconfigureVRApp(PRECONFIG_PARAMETER.INSTANCE_VR_RENDERING, false);
		main.preconfigureVRApp(PRECONFIG_PARAMETER.NO_GUI, false);
		//Set frustum distances here before app starts
		main.preconfigureFrustrumNearFar(0.1f, 512f);
		
		
		//You can downsample for performance reasons
		//main.preconfigureResolutionMultiplier(0.666f);

		main.start();
	}

	/**
	 * Method used for testing.
	 * Sets the instance of this singleton to the provided instance.
	 *
	 * @param main
	 * 		the new value of the instance
	 */
	public static void setInstance(Main main) {
		instance = main;
	}
	
	/**
	 * Set the controller for the current scene.
	 * Cleans up the old scene before initializing the new one.
	 *
	 * @param c
	 * 		the new controller
	 * @return
	 * 		true is the controller was changed, false otherwise
	 */
	public boolean setController(Controller c) {
		if (c != controller) {
			if (controller != null) {
				getStateManager().detach(controller);
			}
			controller = c;
			
			if (controller != null) {
				getStateManager().attach(controller);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Get the instance of the current game.
	 *
	 * @return
	 * 		the current instance of the game or null when no game is running
	 */
	public Game getCurrentGame() {
		if (controller instanceof GameController) {
			return ((GameController) controller).getGame();				
		}
		if (controller instanceof PauseController) {
			return ((PauseController) controller).getPausedController().getGame();				
		}
		return null;
	}
	
	/**
	 * Method used for testing.
	 * Sets the rootNode of Main to a new Node.
	 *
	 * @param rn
	 * 		the new node to replace the rootNode
	 */
	public void setRootNode(Node rn) {
		rootNode = rn;
	}

	/**
	 * Method used for testing.
	 * Sets the guiNode of Main to a new Node.
	 *
	 * @param gn
	 * 		the new node to replace the guiNode.
	 */
	public void setGuiNode(Node gn) {
		guiNode = gn;
	}

	/**
	 * Method used for testing.
	 * Sets the list of tickListeners to the specified list.
	 *
	 * @param listeners
	 * 		the new List of TickListeners
	 */
	public void setTickListeners(List<TickListener> listeners) {
		tickListeners = listeners;
	}

	@Override
	public void simpleInitApp() {
		if (VRApplication.getVRHardware() != null) {
			Log.getLog("VR").info("Attached device: " + VRApplication.getVRHardware().getName());
		} else {
			Log.getLog("VR").info("Attached device: No");
		}
		//setDisplayFps(debugHud);
		//setDisplayStatView(debugHud);
		
		//TODO if VR support is implemented the flyby camera should be disabled
		//getFlyByCamera().setZoomSpeed(0);
		
		getViewPort().setBackgroundColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 1f));
		getCamera().lookAtDirection(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
		
		setupControlMappings();
		
		setController(new WaitingController(this));
		setupWebServer();

		AudioManager.getInstance().init();
		BackgroundMusic.getInstance().start();
		
		//Register an AppState to properly clean up the game.
		getStateManager().attach(new AbstractAppState() {
			@Override
			public void cleanup() {
				super.cleanup();
				
				onGameStopped();
			}
		});
	}

	/**
	 * Setup all the key mappings.
	 */
	protected void setupControlMappings() {
		InputManager im = getInputManager();

		if (isControllerConnected()) {
			//getFlyByCamera().onAction(CameraInput.FLYCAM_INVERTY, false, 0);
			Joystick j = im.getJoysticks()[0];
		
			im.addMapping("Up", new JoyAxisTrigger(0, 0, true));
			im.addMapping("Down", new JoyAxisTrigger(0, 0, false));
			im.addMapping("Left", new JoyAxisTrigger(0, 1, true));
			im.addMapping("Right", new JoyAxisTrigger(0, 1, false));			
						
			j.getButton("0").assignButton("Jump");				// A
			j.getButton("3").assignButton("SIMPLEAPP_Exit");	// Y
			j.getButton("2").assignButton("Bomb");				// X
			j.getButton("1").assignButton("Pickup");			// B
		} else {
			im.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
			im.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
			im.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
			im.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
			im.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
			im.addMapping("Bomb", new KeyTrigger(KeyInput.KEY_Q));
			im.addMapping("Pickup", new KeyTrigger(KeyInput.KEY_E));
		}

		im.addMapping("pause", new KeyTrigger(KeyInput.KEY_P));
	}
	
	//TODO this will be removed when camera type is changed
	/**
	 * Creates the web server and starts it.
	 */
	protected void setupWebServer() {
		webServer = new WebServer();
		
		try {
			webServer.start(PORT_NUMBER);
		} catch (Exception ex) {
			Log.getLog("WebInterface").severe("Exception while trying to start webserver", ex);
		}
	}
	
	/**
	 * Move the camera to a new location.
	 *
	 * @param newLoc
	 *		the new location of the camera
	 */ 
	public void moveCameraTo(Vector3f newLoc) {
		getCamera().setLocation(newLoc);
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		getInputManager().setCursorVisible(true);
		for (TickListener tl : tickListeners) {
			tl.update(tpf);
		}

		getListener().setLocation(getCamera().getLocation());
		getListener().setRotation(getCamera().getRotation());

		BackgroundMusic.getInstance().update();
	}
	
	/**
	 * Add a TickListener.
	 *
	 * @param tl
	 * 		the TickListener to add
	 */
	public void attachTickListener(TickListener tl) {
		tickListeners.add(tl);
	}
	
	/**
	 * Remove a registered TickListener.
	 *
	 * @param tl
	 * 		the TickListener to remove
	 */
	public void removeTickListener(TickListener tl) {
		tickListeners.remove(tl);
	}

	/**
	 * Return the singleton instance of the game.
	 *
	 * @return
	 * 		the running instance of the game
	 */
	public static Main getInstance() {
		if (instance == null) {
			instance = new Main();
		}
		return instance;
	}
	
	/**
	 * Get the current game state.
	 *
	 * @return
	 * 		the current game state
	 */
	public GameState getGameState() {
		if (controller == null) return null;
		return controller.getGameState();
	}
	
	/**
	 * Called when the game is stopped.
	 */
	public void onGameStopped() {
		try {
			webServer.stop();
		} catch (Exception ex) {
			Log.getLog("WebInterface").warning("Exception while trying to stop webserver", ex);
		}

		BackgroundMusic.getInstance().stop();
	}

	/**
	 * Check if the debug Hud is shown.
	 *
	 * @return
	 * 		true when shown, false otherwise.
	 */
	public static boolean isDebugHudShown() {
		return debugHud;
	}

	/**
	 * Check if a controller is connected.
	 *
	 * @return
	 * 		true if a controller is connected, false otherwise.
	 */
	public boolean isControllerConnected() {
		Joystick[] sticks = getInputManager().getJoysticks();
		return sticks != null && sticks.length > 0;
	}
	
	
}