package nl.tudelft.contextproject.hud;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.ui.Picture;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.model.Inventory;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Key;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class for {@link HUD}.
 */
public class HUDTest extends TestBase {

	private GameController controller;
	private HUD hud;

	/**
	 * Create a fresh HUD for each test.
	 */
	@Before
	public void setUp() {
		TestUtil.mockGame();
		Main.getInstance().setGuiFont();
		controller = mock(GameController.class);
		hud = new HUD(controller, 200, 200);
	}

	/**
	 * Test if attaching the hud adds all gui elements and attaches his listeners.
	 */
	@Test
	public void testAttachHud() {
		hud.attachHud();
		
		verify(controller, times(4)).addGuiElement(any(Spatial.class));
		
		VRPlayer player = Main.getInstance().getCurrentGame().getPlayer();
		assertTrue(player.getTickListeners().contains(hud));
		assertTrue(player.getInventory().getTickListeners().contains(hud));
	}
	
	/**
	 * Test if attaching a bomb attaches both the text and image.
	 */
	@Test
	public void testAttachBomb() {
		hud.attachBomb(new Bomb());
		verify(controller, times(2)).addGuiElement(any(Spatial.class));
	}
	
	/**
	 * Test if attaching the heart containers attache the main conrtainer.
	 */
	@Test
	public void testAttachHeartContainers() {
		hud.attachHeartContainers();
		verify(controller, times(1)).addGuiElement(any(Node.class));
	}
	
	/**
	 * Test if the key Image gets the correct position when created.
	 */
	@Test
	public void testGetKeyImagePosition() {
		Picture p = hud.getKeyImage(2, ColorRGBA.Red);
		Vector3f loc = p.getLocalTranslation();
		assertEquals(120f, loc.x, 1e-5);
		assertEquals(60f, loc.y, 1e-5);
	}
	
	/**
	 * Test if the health container gets the correct position when created.
	 */
	@Test
	public void testGetHeartPosition() {
		Picture p = hud.getHealthContainer(2);
		Vector3f loc = p.getLocalTranslation();
		assertEquals(106f, loc.x, 1e-5);
		assertEquals(250f, loc.y, 1e-5);
	}
	
	/**
	 * Test if the color of the key is correct.
	 */
	@Test
	public void testGetKeyImageColor() {
		Picture p = hud.getKeyImage(2, ColorRGBA.Red);
		ColorRGBA c = (ColorRGBA) p.getMaterial().getParam("Color").getValue();
		assertEquals(ColorRGBA.Red, c);
	}
	
	/**
	 * Test if updating the displayed keys works correctly.
	 */
	@Test 
	public void testUpdateKeys() {
		Node c = mock(Node.class);
		hud.setKeyContainer(c);
		Inventory inv = Main.getInstance().getCurrentGame().getPlayer().getInventory();
		inv.add(new Key(ColorRGBA.Blue));
		
		hud.updateKeys(inv);
		
		verify(c, times(1)).detachAllChildren();
		verify(c, times(1)).attachChild(any(Picture.class));
	}
}
