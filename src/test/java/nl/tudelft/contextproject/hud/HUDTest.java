package nl.tudelft.contextproject.hud;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.ui.Picture;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.controller.GameThreadController;
import nl.tudelft.contextproject.model.Inventory;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Key;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link HUD}.
 */
public class HUDTest extends TestBase {

	private GameThreadController controller;
	private HUD hud;

	/**
	 * Create a fresh HUD for each test.
	 */
	@Before
	public void setUp() {
		TestUtil.mockGame();
		Main.getInstance().setGuiFont();
		controller = mock(GameThreadController.class);
		hud = new HUD(controller, 200, 200);
	}

	/**
	 * Test if attaching the hud adds all gui elements and attaches his listeners.
	 */
	@Test
	public void testAttachHud() {
		hud.attachHud();
		
		verify(controller, times(6)).addGuiElement(any(Spatial.class));
		
		VRPlayer player = Main.getInstance().getCurrentGame().getPlayer();
		assertTrue(player.getTickListeners().contains(hud));
		assertTrue(player.getInventory().getTickListeners().contains(hud));
	}
	
	/**
	 * Test if attaching a bomb attaches both the text and image.
	 */
	@Test
	public void testAttachBomb() {
		Node node = mock(Node.class);
		BitmapText text = mock(BitmapText.class);
		when(node.getChildren()).thenReturn(new ArrayList<>());
		when(node.getChild(0)).thenReturn(text);
		
		hud.setBombNode(node);
		hud.attachBomb(new Bomb());
		verify(text, times(1)).setText(anyString());
		verify(node, times(1)).attachChild(any(BitmapText.class));
	}
	
	/**
	 * Test if attaching the heart containers attaches the main container.
	 */
	@Test
	public void testAttachHeartContainers() {
		hud.attachHeartContainers();
		verify(controller, times(1)).addGuiElement(any(Node.class));
	}
	
	/**
	 * Test if attaching the helmet attaches it to the HUD.
	 */
	@Test
	public void testAttachHelmet() {
		hud.attachHelmet();
		verify(controller, times(1)).addGuiElement(any(Node.class));
	}
	
	/**
	 * Tests if attaching the nose attaches it to the HUD.
	 */
	@Test
	public void testAttachNose() {
		hud.attachNose();
		verify(controller, times(1)).addGuiElement(any(Node.class));
	}
	/**
	 * Test if the key Image gets the correct position when created.
	 */
	@Test
	public void testGetKeyImagePosition() {
		Picture picture = hud.getKeyImage(1, 2, ColorRGBA.Red);
		Vector3f location = picture.getLocalTranslation();
		assertEquals(145f, location.x, 1e-5);
		assertEquals(200f, location.y, 1e-5);
	}
	
	/**
	 * Test if the health container gets the correct position when created.
	 */
	@Test
	public void testGetHeartPosition() {
		Picture picture = hud.getHealthContainer(2);
		Vector3f location = picture.getLocalTranslation();
		float start = .5f - (VRPlayer.PLAYER_MAX_HEALTH / 2) * 0.06f;
		assertEquals(200 * (start + 0.12f), location.x, 1e-5);
		assertEquals(160f, location.y, 1e-5);
	}
	
	/**
	 * Test if the color of the key is correct.
	 */
	@Test
	public void testGetKeyImageColor() {
		Picture picture = hud.getKeyImage(1, 2, ColorRGBA.Red);
		ColorRGBA color = (ColorRGBA) picture.getMaterial().getParam("Color").getValue();
		assertEquals(ColorRGBA.Red, color);
	}
	
	/**
	 * Test if updating the displayed keys works correctly.
	 */
	@Test 
	public void testUpdateKeys() {
		Node node = mock(Node.class);
		hud.setKeyContainer(node);
		Inventory inventory = Main.getInstance().getCurrentGame().getPlayer().getInventory();
		inventory.add(new Key(ColorRGBA.Blue));
		
		hud.updateKeys(inventory);
		
		verify(node, times(1)).detachAllChildren();
		verify(node, times(1)).attachChild(any(Picture.class));
	}
	
	/**
	 * Test setting the game timer to Integer.MAX_VALUE.
	 */
	@Test
	public void testSetGameTimerMAX_VALUE() {
		BitmapText bitmapText = mock(BitmapText.class);
		hud.setTimerNode(bitmapText);
		
		hud.setGameTimer(Integer.MAX_VALUE);
		
		verify(bitmapText, times(1)).setText("");
	}
	
	/**
	 * Test setting the game timer.
	 */
	@Test
	public void testSetGameTimer() {
		BitmapText bitmapText = mock(BitmapText.class);
		hud.setTimerNode(bitmapText);
		
		hud.setGameTimer(12);
		
		verify(bitmapText, times(1)).setText("12");
	}
	
	/**
	 * Test updating the bombs when no bomb is in the inventory.
	 */
	@Test
	public void updateBombsNotInInventory() {
		Node node = mock(Node.class);
		hud.setBombNode(node);
		Inventory inventory = Main.getInstance().getCurrentGame().getPlayer().getInventory();
		inventory.remove(new Bomb());
		
		hud.updateBombs(inventory);
		
		verify(node, times(1)).detachAllChildren();
	}
	
	/**
	 * Test updating the bombs when the inventory contains one.
	 */
	@Test
	public void updateBombsInInventory() {
		Node node = mock(Node.class);
		hud.setBombNode(node);
		Inventory inventory = Main.getInstance().getCurrentGame().getPlayer().getInventory();
		inventory.pickUp(new Bomb());
		when(node.getChildren()).thenReturn(new ArrayList<>());
		when(node.getChild(0)).thenReturn(mock(BitmapText.class));
		
		hud.updateBombs(inventory);
		
		verify(node, times(1)).attachChild(any(BitmapText.class));
	}
}
