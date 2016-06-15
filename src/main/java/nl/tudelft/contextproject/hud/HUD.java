package nl.tudelft.contextproject.hud;

import java.util.List;

import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.ui.Picture;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.GameThreadController;
import nl.tudelft.contextproject.model.Inventory;
import nl.tudelft.contextproject.model.Observer;
import nl.tudelft.contextproject.model.entities.exploding.Bomb;
import nl.tudelft.contextproject.model.entities.moving.VRPlayer;

import jmevr.util.VRGuiManager;

/**
 * Class to represent a Head Up Display.
 */
public class HUD implements Observer {

	private GameThreadController controller;

	private Node keyContainer;
	private Node heartContainer;
	private Node bombNode;

	private float screenHeight;
	private float screenWidth;

	private BitmapText gameTimer;
	private volatile BitmapText popupText;

	private volatile float popupTimer;

	//Variables for caching
	private float lastHealth;
	private int oldKeyColorHash;
	
	/**
	 * Creates a new HUD for the given controller.
	 * 
	 * @param controller
	 * 		the GameController
	 */
	public HUD(GameThreadController controller) {
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
	protected HUD(GameThreadController controller, float width, float height) {
		this.controller = controller;
		this.screenHeight = height;
		this.screenWidth = width;
	}
	
	/**
	 * Attaches the Hud to the renderer.
	 */
	public void attachHud() {
		attachHelmet();
		attachGameTimer();				
		attachHeartContainers();
		attachNose();
		keyContainer = new Node("Keys");
		controller.addGuiElement(keyContainer);	
		
		bombNode = new Node("Bombs");
		controller.addGuiElement(bombNode);
		
		// Register observers
		Main.getInstance().getCurrentGame().getPlayer().getInventory().registerObserver(this);
		Main.getInstance().getCurrentGame().getPlayer().registerObserver(this);
		Main.getInstance().registerObserver(this::updatePopupText);
	}

	/**
	 * Show a popup text in the HUD.
	 * 
	 * @param text
	 * 		the text to show
	 * @param color
	 * 		the color of the text
	 * @param duration
	 * 		the duration this text is shown
	 */
	public void showPopupText(String text, ColorRGBA color, float duration) {
		BitmapText oldText = popupText;
		if (oldText != null) {
			controller.removeGuiElement(oldText);
		}
		
		BitmapText newText = new BitmapText(Main.getInstance().getGuiFont(), false);
		newText.setSize(screenHeight / 10);
		newText.setColor(color);
		newText.setText(text);
		float height = (screenHeight - newText.getLineHeight()) / 2;
		float width = (screenWidth - newText.getLineWidth()) / 2;
		
		newText.setLocalTranslation(width, height, 0);
		controller.addGuiElement(newText);
		popupText = newText;
		popupTimer = duration;
	}

	/**
	 * Attach the game timer to the HUD.
	 */
	protected void attachGameTimer() {
		gameTimer = new BitmapText(Main.getInstance().getGuiFont(), false);
		gameTimer.setSize(screenHeight / 30);
		gameTimer.setColor(ColorRGBA.White);
		float height = gameTimer.getLineHeight() + screenHeight / 6;
		float width = gameTimer.getLineWidth() + screenWidth / 10;
		gameTimer.setLocalTranslation(width, height, 0);
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
	 * 
	 * @param bomb
	 * 		the bomb to attach
	 */
	protected void attachBomb(Bomb bomb) {
		if (bombNode.getChildren().size() == 0) {
			// Attach the bomb counter
			BitmapText textBomb = new BitmapText(Main.getInstance().getGuiFont(), false);
			textBomb.setSize(screenHeight / 30);
			textBomb.setColor(ColorRGBA.White);
			float width = screenWidth / 2f - (screenHeight / 30) * .8f;
			float height = screenHeight - (textBomb.getLineHeight() + screenHeight / 5);
			textBomb.setLocalTranslation(width, height, 0);
			bombNode.attachChild(textBomb);
		}
		
		BitmapText textBomb = (BitmapText) bombNode.getChild(0);
		String text = "" + Math.round(bomb.getTimer() * 10) / 10f;
		textBomb.setText(text);
	}
	
	/**
	 * Returns a picture of a healthContainer.
	 * 
	 * @param position
	 * 		position of the healthContainer
	 * @return
	 * 		picture of the health container
	 */
	public Picture getHealthContainer(int position) {
		Picture heart = new Picture("heartcontainer" + position);
		heart.setImage(Main.getInstance().getAssetManager(), "Textures/fullheart.png", true);
		heart.setWidth(screenWidth / 20);
		heart.setHeight(screenHeight / 20);
		float start = .5f - .06f * (VRPlayer.PLAYER_MAX_HEALTH / 2);
		heart.setPosition(screenWidth * (start + 0.06f * position), screenHeight * .80f);
		
		return heart;
	}
	
	/**
	 * Attaches a nose to the HUD.
	 */
	public void attachNose() {
		Picture nose = new Picture("Nose");
		nose.setImage(Main.getInstance().getAssetManager(), "Textures/nose.png", true);
		nose.setWidth(screenWidth * 0.6f);
		nose.setHeight(screenHeight * 0.6f);
		nose.setPosition(screenWidth * 0.15f, screenHeight * -0.25f);
		controller.addGuiElement(nose);
	}
	
	/**
	 * Attaches a helmet to the HUD.
	 */
	public void attachHelmet() {
		Picture helm = new Picture("Helm");
		helm.setImage(Main.getInstance().getAssetManager(), "Textures/helmet.png", true);
		helm.setWidth(screenWidth);
		helm.setHeight(screenHeight);
		helm.setPosition(0, 0);
		controller.addGuiElement(helm);
	}
	
	/**
	 * Return a picture of a key at the right position on the HUD.
	 * 
	 * @param total
	 * 		the total amount of keys
	 * @param position 
	 * 		position of the key
	 * @param color
	 * 		the color of the key
	 * @return
	 * 		picture of the key
	 */
	public Picture getKeyImage(int total, int position, ColorRGBA color) {
		Picture keyPicture = new Picture("key Picture");
		keyPicture.setWidth(screenWidth / 30);
		keyPicture.setHeight(screenHeight / 12);
		float start = 0.5f - (0.025f * total);
		keyPicture.setPosition(screenWidth * 0.15f + screenWidth * (start + 0.05f * position), 200);
		
		Material material = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		material.setTexture("ColorMap", Main.getInstance().getAssetManager().loadTexture("Textures/keyicon.png"));
		material.setColor("Color", color);
		keyPicture.setMaterial(material);
		
		return keyPicture;
	}
	
	/**
	 * Set a new value for the game timer.
	 * 
	 * @param timer
	 * 		the new value for the game timer
	 */
	public void setGameTimer(int timer) {
		if (timer == Integer.MAX_VALUE) {
			gameTimer.setCullHint(CullHint.Always);
		} else {
			gameTimer.setCullHint(CullHint.Inherit);
			gameTimer.setText("" + timer);
		}
	}

	@Override
	public void update(float tpf) {
		VRPlayer player = controller.getGame().getPlayer();
		Inventory inventory = player.getInventory();
		
		updateBombs(inventory);
		updateKeys(inventory);
		updateHearts(player);
	}

	/**
	 * Update the bomb display.
	 * 
	 * @param inventory
	 * 		the inventory that contains the bomb
	 */
	protected void updateBombs(Inventory inventory) {
		if (inventory.isHolding() && inventory.getHolding() instanceof Bomb) {
			attachBomb((Bomb) inventory.getHolding());
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
		float currentHealth = player.getHealth();
		if (lastHealth == currentHealth) return;
		
		lastHealth = currentHealth;
		for (int i = 0; i < VRPlayer.PLAYER_MAX_HEALTH; i++) {
			Picture picture = (Picture) heartContainer.getChild(i);
			float heartFill = -i + currentHealth;
			if (heartFill < .25f) {
				picture.setImage(Main.getInstance().getAssetManager(), "Textures/emptyheart.png", true);
			} else if (heartFill > .75f) {
				picture.setImage(Main.getInstance().getAssetManager(), "Textures/fullheart.png", true);
			} else {
				picture.setImage(Main.getInstance().getAssetManager(), "Textures/halfheart.png", true);
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
		List<ColorRGBA> keyColors = inventory.getKeyColors();
		int currentHash = keyColors.hashCode();
		
		//Don't update if nothing changed
		if (oldKeyColorHash == currentHash) return;
		
		oldKeyColorHash = currentHash;
		keyContainer.detachAllChildren();
		int i = 0;
		for (ColorRGBA color : keyColors) {
			keyContainer.attachChild(getKeyImage(keyColors.size(), i, color));
			i++;
		}
	}
	
	/**
	 * Method used for testing.
	 * 
	 * @param keyContainer
	 * 		the new container where keys will be stored
	 */
	protected void setKeyContainer(Node keyContainer) {
		this.keyContainer = keyContainer;
	}

	/**
	 * Method used for testing.
	 * 
	 * @param bombNode
	 * 		the node to replace the bomb node
	 */
	protected void setBombNode(Node bombNode) {
		this.bombNode = bombNode;
	}

	/**
	 * Method used for testing.
	 * 
	 * @param bitmapText
	 * 		the new bitmapText for the timer
	 */
	public void setTimerNode(BitmapText bitmapText) {
		gameTimer = bitmapText;
	}
	
	/**
	 * Update the popup text. 
	 * Removes the text when the timer is over.
	 * 
	 * @param tpf
	 * 		the tpf for this update
	 */
	public void updatePopupText(float tpf) {
		BitmapText text = popupText;
		if (popupText == null) return;
		
		popupTimer -= tpf;
		if (popupTimer < 0) {
			controller.removeGuiElement(text);
			if (popupText == text) {
				popupText = null;
				popupTimer = 0;
			}
		}
	}
}
