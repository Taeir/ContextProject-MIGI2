package nl.tudelft.contextproject.hud;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
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
	private static final String ICON_EMPTY_HEART = "Textures/emptyheart.png";
	private static final String ICON_EMPTY_KEY = "Textures/emptykeyicon.png";

	private GameController controller;
	
	private Picture keypicyellow, keypicred, keypicblue;
	private Picture bombPicture;
	private BitmapText textBomb;
	private Picture heart1, heart2, heart3;
	
	
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
		float height = VRGuiManager.getCanvasSize().getX();
		float width = VRGuiManager.getCanvasSize().getX();
		
		//Attach the keycounter
		keypicyellow = keyGUI(1, height, width);
		keypicred = keyGUI(2, height, width);
		keypicblue = keyGUI(3, height, width);
		controller.addGuiElement(keypicyellow);
		controller.addGuiElement(keypicred);
		controller.addGuiElement(keypicblue);
		
		//Attach the bomb counter
		textBomb = new BitmapText(Main.getInstance().getGuiFont(), false);
		textBomb.setSize(height / 30);
		textBomb.setColor(ColorRGBA.White);
		textBomb.setText("" + Main.getInstance().getCurrentGame().getPlayer().getInventory().numberOfBombs());
		textBomb.setLocalTranslation(width / 3f, textBomb.getLineHeight() + height / 40, 0);
		controller.addGuiElement(textBomb);
		
		//Attach bomb icon
		bombPicture = new Picture("Bomb Picture");
		bombPicture.setImage(Main.getInstance().getAssetManager(), "Textures/bombicon.png", true);
		bombPicture.setWidth(width / 12);
		bombPicture.setHeight(height / 10);
		bombPicture.setPosition(width / 4f, 60);
		controller.addGuiElement(bombPicture);

		//Attach the health counter
		heart1 = healthContainer(1, height, width);
		controller.addGuiElement(heart1);
		heart2 = healthContainer(2, height, width);
		controller.addGuiElement(heart2);
		heart3 = healthContainer(3, height, width);
		controller.addGuiElement(heart3);
		
		//Update the hud
		Main.getInstance().attachTickListener(this);
	}
	
	/**
	 * Returns a picture of a healthContainer.
	 * 
	 * @param pos
	 * 		position of the healthContainer
	 * @param height
	 * 		height of the screen
	 * @param width
	 * 		width of the screen
	 * @return
	 * 		picture of the health container
	 */
	public Picture healthContainer(int pos, float height, float width) {
		Picture heart = new Picture("heartcontainer" + pos);
		heart.setImage(Main.getInstance().getAssetManager(), "Textures/fullheart.png", true);
		heart.setWidth(width / 20);
		heart.setHeight(height / 20);
		if (pos > 0) {
			heart.setPosition((width / 3f), height + 50);
		}
		if (pos > 1) {
			heart.setPosition((width / 2.6f), height + 50);
		}
		if (pos > 2) {
			heart.setPosition((width / 2.3f), height + 50);
		}
		return heart;
	}
	
	/**
	 * Return a picture of a key at the right position on the HUD.
	 * 
	 * @param pos 
	 * 		position of the key
	 * @param height
	 * 		height of the screen 
	 * @param width
	 * 		width of the screen
	 * @return
	 * 		picture of the key
	 */
	public Picture keyGUI(int pos, float height, float width) {
		Picture keypic = new Picture("key Picture");
		if (pos == 1) {
			keypic = new Picture("key Picture");
			keypic.setImage(Main.getInstance().getAssetManager(), ICON_EMPTY_KEY, true);
			keypic.setWidth(width / 30);
			keypic.setHeight(height / 12);
			keypic.setPosition(width * 0.5f, 60);
		} else if (pos == 2) {
			keypic = new Picture("key2Picture");
			keypic.setImage(Main.getInstance().getAssetManager(), ICON_EMPTY_KEY, true);
			keypic.setWidth(width / 30);
			keypic.setHeight(height / 12);
			keypic.setPosition(width * 0.55f, 60);
		} else if (pos == 3) {
			keypic = new Picture("key3Picture");
			keypic.setImage(Main.getInstance().getAssetManager(), ICON_EMPTY_KEY, true);
			keypic.setWidth(width / 30);
			keypic.setHeight(height / 12);
			keypic.setPosition(width * 0.6f, 60);
		}
		
		return keypic;
	}

	@Override
	public void update(float tpf) {
		//Update the bomb count in the HUD
		textBomb.setText("" + Main.getInstance().getCurrentGame().getPlayer().getInventory().numberOfBombs()); 

		VRPlayer player = controller.getGame().getPlayer();
		
		//Update the health icons in the HUD
		if (player.getHealth() <= 2.5) {
			heart3.setImage(Main.getInstance().getAssetManager(), ICON_EMPTY_HEART, true);
		}
		if (player.getHealth() <= 1.5) {
			heart2.setImage(Main.getInstance().getAssetManager(), ICON_EMPTY_HEART, true);
		}
		if (player.getHealth() <= 0.5) {
			heart1.setImage(Main.getInstance().getAssetManager(), ICON_EMPTY_HEART, true);
		}
		
		Inventory inventory = player.getInventory();
		
		//Update the keys in the HUD
		if ((inventory.containsColorKey(ColorRGBA.Yellow.set(1.0f, 1.0f, 0.0f, 0.0f)))) {
			keypicyellow.setImage(Main.getInstance().getAssetManager(), "Textures/yellowkeyicon.png", true);
		} else {
			keypicyellow.setImage(Main.getInstance().getAssetManager(), ICON_EMPTY_KEY, true);
		}
		
		if ((inventory.containsColorKey(ColorRGBA.Blue.set(0.0f, 0.0f, 1.0f, 0.0f)))) {
			keypicblue.setImage(Main.getInstance().getAssetManager(), "Textures/bluekeyicon.png", true);
		} else {
			keypicblue.setImage(Main.getInstance().getAssetManager(), ICON_EMPTY_KEY, true);
		}
		
		if ((inventory.containsColorKey(ColorRGBA.Red.set(1.0f, 0.0f, 0.0f, 0.0f)))) {
			keypicred.setImage(Main.getInstance().getAssetManager(), "Textures/redkeyicon.png", true);
		} else {
			keypicred.setImage(Main.getInstance().getAssetManager(), ICON_EMPTY_KEY, true);
		}
	}
}
