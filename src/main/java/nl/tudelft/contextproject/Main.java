package nl.tudelft.contextproject;

import java.awt.Desktop;
import java.net.URI;
import java.util.Arrays;


import java.util.LinkedList;
import java.util.List;

import com.jme3.app.state.AbstractAppState;
import com.jme3.font.BitmapFont;
import com.jme3.input.DefaultJoystickAxis;
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
import jmevr.util.VRGuiManager;
import jmevr.util.VRGuiManager.POSITIONING_MODE;

import lombok.SneakyThrows;

/**
 * Main class of the game 'The Cave of Caerbannog'.
 */
public class Main extends VRApplication {
	public static final int PORT_NUMBER = 8080;
	//Set to false to disable VR
	public static final boolean VR = true;
	//Decrease for better performance and worse graphics
	public static final float RESOLUTION = 1.0f;
	//If the mirror window is shown
	public static final boolean MIRROR_WINDOW = true;
	
	private static boolean hideQR;
	
	private static Main instance;
	
	private Controller controller;
	private WebServer webServer;
	private List<TickListener> tickListeners = new LinkedList<>();
	private BitmapFont guifont;
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
		hideQR = a.contains("--hideQR");
		
		AppSettings settings = new AppSettings(true);
		settings.setUseJoysticks(true);
		main.setSettings(settings);

		//Set if we want to run in VR mode or not.
		main.preconfigureVRApp(PRECONFIG_PARAMETER.DISABLE_VR, !VR);
		
		//Use full screen distortion, maximum FOV, possibly quicker (not compatible with instancing)
		main.preconfigureVRApp(PRECONFIG_PARAMETER.USE_CUSTOM_DISTORTION, false);
		//Runs faster when set to false, but will allow mirroring
		main.preconfigureVRApp(PRECONFIG_PARAMETER.ENABLE_MIRROR_WINDOW, MIRROR_WINDOW);
		//Render two eyes, regardless of SteamVR
		main.preconfigureVRApp(PRECONFIG_PARAMETER.FORCE_VR_MODE, true);
		main.preconfigureVRApp(PRECONFIG_PARAMETER.SET_GUI_CURVED_SURFACE, true);
		main.preconfigureVRApp(PRECONFIG_PARAMETER.FLIP_EYES, false);
		//Show gui even if it is behind things
		main.preconfigureVRApp(PRECONFIG_PARAMETER.SET_GUI_OVERDRAW, true);
		//Faster VR rendering, requires some vertex shader changes (see jmevr/shaders/Unshaded.j3md)
		main.preconfigureVRApp(PRECONFIG_PARAMETER.INSTANCE_VR_RENDERING, false);
		main.preconfigureVRApp(PRECONFIG_PARAMETER.NO_GUI, false);
		
		//Set frustum distances here before app starts
		main.preconfigureFrustrumNearFar(0.1f, 512f);
		
		//You can downsample for performance reasons
		main.preconfigureResolutionMultiplier(RESOLUTION);

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
		if (VRApplication.isInVR() && VRApplication.getVRHardware() != null) {
			Log.getLog("VR").info("Attached device: " + VRApplication.getVRHardware().getName());
		} else {
			Log.getLog("VR").info("Attached device: No");
		}
		
		getAssetManager().loadFont("Interface/Fonts/Default.fnt");
		
		getViewPort().setBackgroundColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 1f));
		getCamera().lookAtDirection(new Vector3f(0, 0, 1), new Vector3f(0, 1, 0));
		
		VRGuiManager.setPositioningMode(POSITIONING_MODE.AUTO_CAM_ALL);
		VRGuiManager.setGuiScale(0.50f);
		VRGuiManager.setPositioningElasticity(0f);
		
		setupControlMappings();
		
		
		setController(new WaitingController(this));
		setupWebServer();
		
		if (!hideQR) {
			showQRCode();
		}

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
	@SneakyThrows
	protected void setupControlMappings() {
		InputManager im = getInputManager();
		
		//Add mouse controls when No VR is attached.
		if (!VRApplication.isInVR()) {
			new NoVRMouseManager(getCamera()).registerWithInput(im);
		}

		if (isControllerConnected()) {
			Joystick j = im.getJoysticks()[0];

			mapJoystickAxes(j);

			j.getButton("0").assignButton("Jump");				// A
			j.getButton("3").assignButton("Unmapped");			// Y
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

		im.addMapping("Exit", new KeyTrigger(KeyInput.KEY_ESCAPE));
		im.addMapping("pause", new KeyTrigger(KeyInput.KEY_P));
	}

	/**
	 * Maps the Joystick Axes to our game controls.
	 * 
	 * @param joystick
	 * 		the joystick to map the axes of.
	 */
	private void mapJoystickAxes(Joystick joystick) {
		//Set the deadzones to 0.3
		if (joystick.getXAxis() instanceof DefaultJoystickAxis) {
			((DefaultJoystickAxis) joystick.getXAxis()).setDeadZone(0.30f);
			((DefaultJoystickAxis) joystick.getYAxis()).setDeadZone(0.30f);
		}
		
		joystick.getXAxis().assignAxis("Right", "Left");
		joystick.getYAxis().assignAxis("Up", "Down");
	}
	
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
	 * Opens the QR code to join the game in the default browser.
	 */
	private void showQRCode() {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI("http://localhost:8080/qr"));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
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
	 * Check if the qr code is shown on startup.
	 *
	 * @return
	 * 		true when shown, false otherwise.
	 */
	public static boolean isQRShown() {
		return !hideQR;
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
	
	/**
	 * Returns the BitmapFont.
	 * @return the BitmapFont
	 */
	public BitmapFont getGuiFont() {
		return guifont;
	}
	

	
	/**
	 * Returns the Controller().
	 * @return the Controller()
	 */
	public Controller getController() {
		return controller;
	}
}