package nl.tudelft.contextproject.hud;

import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.model.Inventory;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import jmevr.util.VRGuiManager;

/**
 * Class to represent a Head Up Display.
 */
public class HUD implements TickListener {

	private GameController controller;
	
	private Picture bombPicture;
	private BitmapText textBomb;

	private Node keyContainer;
	private Node heartContainer;

	private float screenHeight;

	private float screenWidth;
	
	
	/**
	 * Creates a new HUD for the given controller.
	 * 
	 * @param controller
	 * 		the GameController
	 */
	public HUD(GameController controller) {
		this.controller = controller;
	}
	
	/**
	 * Attaches the Hud to the renderer.
	 */
	public void attachHud() {
		screenHeight = VRGuiManager.getCanvasSize().getX();
		screenWidth = VRGuiManager.getCanvasSize().getX();
		
		keyContainer = new Node("Keys");
		controller.addGuiElement(keyContainer);
		heartContainer = new Node("hearts");
		controller.addGuiElement(heartContainer);
		
		// Attach the bomb counter
		textBomb = new BitmapText(Main.getInstance().getGuiFont(), false);
		textBomb.setSize(screenHeight / 30);
		textBomb.setColor(ColorRGBA.White);
		textBomb.setText("" + Main.getInstance().getCurrentGame().getPlayer().getInventory().numberOfBombs());
		textBomb.setLocalTranslation(screenWidth / 3f, textBomb.getLineHeight() + screenHeight / 40, 0);
		controller.addGuiElement(textBomb);
		
		// Attach bomb icon
		bombPicture = new Picture("Bomb Picture");
		bombPicture.setImage(Main.getInstance().getAssetManager(), "Textures/bombicon.png", true);
		bombPicture.setWidth(screenWidth / 12);
		bombPicture.setHeight(screenHeight / 10);
		bombPicture.setPosition(screenWidth / 4f, 60);
		controller.addGuiElement(bombPicture);
		
		// Attach heart containers
		for (int i = 0; i < VRPlayer.PLAYER_MAX_HEALTH; i++) {
			heartContainer.attachChild(getHealthContainer(i));
		}
		
		// Attach listeners
		Main.getInstance().getCurrentGame().getPlayer().getInventory().attachListener(this);
		Main.getInstance().getCurrentGame().getPlayer().attachListener(this);
	}
	
	/**
	 * Returns a picture of a healthContainer.
	 * 
	 * @param pos
	 * 		position of the healthContainer
	 * @return
	 * 		picture of the health container
	 */
	public Picture getHealthContainer(int pos) {
		Picture heart = new Picture("heartcontainer" + pos);
		heart.setImage(Main.getInstance().getAssetManager(), "Textures/fullheart.png", true);
		heart.setWidth(screenWidth / 20);
		heart.setHeight(screenHeight / 20);
		heart.setPosition(screenWidth * (0.44f + 0.06f * pos), screenHeight + 50);
		
		return heart;
	}
	
	/**
	 * Return a picture of a key at the right position on the HUD.
	 * 
	 * @param pos 
	 * 		position of the key
	 * @param color
	 * 		the color of the key
	 * @return
	 * 		picture of the key
	 */
	public Picture getKeyImage(int pos, ColorRGBA color) {
		Picture keypic = new Picture("key Picture");
		keypic.setWidth(screenWidth / 30);
		keypic.setHeight(screenHeight / 12);
		keypic.setPosition(screenWidth * (0.5f + 0.05f * pos), 60);
		
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		mat.setTexture("ColorMap", Main.getInstance().getAssetManager().loadTexture("Textures/keyicon.png"));
		keypic.setMaterial(mat);
		
		return keypic;
	}

	@Override
	public void update(float tpf) {
		VRPlayer player = controller.getGame().getPlayer();
		Inventory inv = player.getInventory();
		// Update the bomb count in the HUD
		textBomb.setText("" + inv.numberOfBombs()); 

		// update keys
		keyContainer.detachAllChildren();
		int i = 0;
		for (ColorRGBA c : inv.getKeyColors()) {
			keyContainer.attachChild(getKeyImage(i, c));
			i++;
		}

		// update hearts
		int health = Math.round(player.getHealth());
		for (int j = 0; j < VRPlayer.PLAYER_MAX_HEALTH; j++) {
			Picture p = (Picture) heartContainer.getChild(j);
			if (j < health) {
				p.setImage(Main.getInstance().getAssetManager(), "Textures/fullheart.png", true);
			} else {
				p.setImage(Main.getInstance().getAssetManager(), "Textures/emptyheart.png", true);					
			}
		}
	}
}
