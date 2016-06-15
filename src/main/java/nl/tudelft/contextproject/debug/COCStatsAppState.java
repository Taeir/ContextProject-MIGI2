package nl.tudelft.contextproject.debug;

import com.jme3.app.Application;
import com.jme3.app.StatsAppState;
import com.jme3.app.StatsView;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Quad;

import nl.tudelft.contextproject.Main;

/**
 * Debug stats view + FPS for VR.
 */
public class COCStatsAppState extends StatsAppState {
	private static final int DX = 100;
	private static final int DY = 300;
	private static final int DZ = 0;
	private Application app;
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.app = app;
		if (app instanceof Main) {
			Main simpleApp = (Main) app;
			if (guiNode == null) {
				guiNode = simpleApp.getGuiNode();
			}
			if (guiFont == null) {
				guiFont = simpleApp.getGuiFont();
			}
		}

		if (guiFont == null) {
			guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
		}

		app.getInputManager().addMapping("DEBUG_STATS", new KeyTrigger(KeyInput.KEY_F1));
		app.getInputManager().addListener((ActionListener) (name, ip, tpf) -> {
			if (ip) toggleStats();
		}, "DEBUG_STATS");
		super.initialize(stateManager, app);
	}
	
	@Override
	public void loadStatsView() {
		setDisplayStatView(false);
		statsView = new StatsView("Statistics View",
                app.getAssetManager(),
                app.getRenderer().getStatistics());
		// move it up so it appears above fps text
		statsView.setLocalTranslation(DX, DY + fpsText.getLineHeight() * 2, DZ);
		statsView.setEnabled(false);
		statsView.setCullHint(CullHint.Always);
		statsView.scale(2);
		guiNode.attachChild(statsView);
	}
	
	@Override
	public void loadFpsText() {
		setDisplayFps(false);
        if (fpsText == null) {
            fpsText = new BitmapText(guiFont, false);
        }

        fpsText.setLocalTranslation(DX, DY + fpsText.getLineHeight() * 2, DZ);
        fpsText.setText("Frames per second");
        fpsText.setCullHint(CullHint.Always);
        fpsText.scale(2);
        guiNode.attachChild(fpsText);
    }
	
	@Override
	public void loadDarken() {
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(0, 0, 0, 0.5f));
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        darkenFps = new Geometry("StatsDarken", new Quad(200, fpsText.getLineHeight()));
        darkenFps.setMaterial(mat);
        darkenFps.setLocalTranslation(DX, DY, DZ - 1);
        darkenFps.setCullHint(CullHint.Always);
        darkenFps.scale(2);
        guiNode.attachChild(darkenFps);

        darkenStats = new Geometry("StatsDarken", new Quad(200, statsView.getHeight()));
        darkenStats.setMaterial(mat);
        darkenStats.setLocalTranslation(DX, DY + fpsText.getHeight() * 2, DZ - 1);
        darkenStats.setCullHint(CullHint.Always);
        darkenStats.scale(2);
        guiNode.attachChild(darkenStats);
    }
}
