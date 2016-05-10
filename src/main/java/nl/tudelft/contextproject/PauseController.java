package nl.tudelft.contextproject;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;

/**
 * Controller for a pause screen.
 */
public class PauseController extends Controller {

	private Controller resumeController;

	/**
	 * Constructor for the PauseController.
	 * @param old The old controller to resume to.
	 * @param app The main app.
	 */
	public PauseController(Controller old, SimpleApplication app) {
		super(app, "PauseController");
		this.resumeController = old;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		ActionListener al = new ActionListener() {
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				if (!isPressed) {
					removeInputListener(this);
					Main.getInstance().setController(resumeController);
				}
			}
		};
		addInputListener(al, "pause");
		
		BitmapFont guiFont = Main.getInstance().getAssetManager().loadFont("Interface/Fonts/Default.fnt");
		BitmapText hudText = guiFont.createLabel("Paused"); 
		hudText.setLocalTranslation(200, 200, 0);
		addGuiElement(hudText);
	}
	
	@Override
	public void update(float tpf) { }

	@Override
	public GameState getGameState() {
		return GameState.PAUSED;
	}
}
