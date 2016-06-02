package nl.tudelft.contextproject.hud;

import java.util.List;

import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.model.Inventory;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import jmevr.util.VRGuiManager;

/**
 * Class to represent a Head Up Display.
 */
public class HUD implements TickListener {

	private GameController controller;

	private Node keyContainer;
	private Node heartContainer;
	private Node bombNode;

	private float screenHeight;
	private float screenWidth;

	private BitmapText gameTimer;

	
	
	/**
	 * Creates a new HUD for the given controller.
	 * 
	 * @param controller
	 * 		the GameController
	 */
	public HUD(GameController controller) {
		this.controller = controller;
		this.screenHeight = VRGuiManager.getCanvasSize().getY();
		this.screenWidth = VRGuiManager.getCanvasSize().getX();
	}
	
	/**
	 * Constructor used for testing only!
	 * This constructor allows to bypass VR/GUI settings.
	 * 
	 * @param controller
	 * 		the controller for this HUD
	 * @param width
	 * 		the screen width
	 * @param height
	 * 		the screen height
	 */
	protected HUD(GameController controller, float width, float height) {
		this.controller = controller;
		this.screenHeight = height;
		this.screenWidth = width;
	}
	
	/**
	 * Attaches the Hud to the renderer.
	 */
	public void attachHud() {		
		keyContainer = new Node("Keys");
		controller.addGuiElement(keyContainer);	
		
		bombNode = new Node("Bombs");
		controller.addGuiElement(bombNode);
		
		attachGameTimer();				
		attachHeartContainers();
		
		// Attach listeners
		Main.getInstance().getCurrentGame().getPlayer().getInventory().attachTickListener(this);
		Main.getInstance().getCurrentGame().getPlayer().attachTickListener(this);
	}

	protected void attachGameTimer() {
		gameTimer = new BitmapText(Main.getInstance().getGuiFont(), false);
		gameTimer.setSize(screenHeight / 30);
		gameTimer.setColor(ColorRGBA.White);
		float h = gameTimer.getLineHeight() + screenHeight / 10;
		gameTimer.setLocalTranslation(100, h, 0);
		controller.addGuiElement(gameTimer);
	}

	/**
	 * Attaches the heart containers to the HUD.
	 */
	protected void attachHeartContainers() {
		heartContainer = new Node("hearts");
		controller.addGuiElement(heartContainer);
		for (int i = 0; i < VRPlayer.PLAYER_MAX_HEALTH; i++) {
			heartContainer.attachChild(getHealthContainer(i));
		}
		updateHearts(Main.getInstance().getCurrentGame().getPlayer());
	}

	/**
	 * Attaches the bomb icon and counter to the HUD.
	 */
	protected void attachBomb(Bomb b) {
		if (bombNode.getChildren().size() == 0) {
			// Attach the bomb counter
			BitmapText textBomb = new BitmapText(Main.getInstance().getGuiFont(), false);
			textBomb.setSize(screenHeight / 30);
			textBomb.setColor(ColorRGBA.White);
			float w = screenWidth / 2f - (screenHeight / 30) * .8f;
			float h = textBomb.getLineHeight() + screenHeight / 7;
			textBomb.setLocalTranslation(w, h, 0);
			bombNode.attachChild(textBomb);
		}
		((BitmapText) bombNode.getChild(0)).setText("" + Math.round(b.getTimer() * 10) / 10.f);
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
		float start = .5f - .06f * (VRPlayer.PLAYER_MAX_HEALTH / 2);
		heart.setPosition(screenWidth * (start + 0.06f * pos), screenHeight * .9f);
		
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
	public Picture getKeyImage(int total, int pos, ColorRGBA color) {
		Picture keypic = new Picture("key Picture");
		keypic.setWidth(screenWidth / 30);
		keypic.setHeight(screenHeight / 12);
		float start = 0.5f - (0.025f * total);
		keypic.setPosition(screenWidth * (start + 0.05f * pos), 60);
		
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		mat.setTexture("ColorMap", Main.getInstance().getAssetManager().loadTexture("Textures/keyicon.png"));
		keypic.setMaterial(mat);
		
		return keypic;
	}
	
	public void setGameTimer(int timer) {
		if (timer == Integer.MAX_VALUE) {
			gameTimer.setText("");
		} else {
			gameTimer.setText("" + timer);
		}
	}

	@Override
	public void update(float tpf) {
		VRPlayer player = controller.getGame().getPlayer();
		Inventory inv = player.getInventory();
		updateBombs(inv);

		updateKeys(inv);
		updateHearts(player);
	}

	protected void updateBombs(Inventory inv) {
		if (inv.containsBomb()) {
			attachBomb(inv.getBomb());
		} else {
			bombNode.detachAllChildren();
		}
	}

	/**
	 * Update the heart containers to show the player's health.
	 * 
	 * @param player
	 * 		the player to get the health from
	 */
	protected void updateHearts(VRPlayer player) {
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

	/**
	 * Update the key display to show all keys in the player's inventory.
	 * 
	 * @param inventory
	 * 		the inventory that contains the keys
	 */
	protected void updateKeys(Inventory inventory) {
		keyContainer.detachAllChildren();
		int i = 0;
		List<ColorRGBA> keys = inventory.getKeyColors();
		for (ColorRGBA c : keys) {
			keyContainer.attachChild(getKeyImage(keys.size(), i, c));
			i++;
		}
	}
	
	/**
	 * Method used for testing.
	 * Sets the key container to a custom container.
	 * 
	 * @param container
	 * 		the new container where keys will be stored
	 */
	protected void setKeyContainer(Node container) {
		keyContainer = container;
	}

	protected void setBombNode(Node node) {
		bombNode = node;
	}

	public void setTimerNode(BitmapText m) {
		gameTimer = m;
	}
}
