package nl.tudelft.contextproject.model;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.material.MatParam;
import com.jme3.material.MaterialDef;
import com.jme3.math.ColorRGBA;

import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Door;
import nl.tudelft.contextproject.model.entities.Key;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the Key class.
 */
public class InventoryTest extends TestBase {
	private Inventory inv;

	/**
	 * Setup method.
	 * Creates a fresh inventory, key and bomb for every test.
	 */
	@Before
	public void setUp() {
		inv = new Inventory();
	}

	/**
	 * Tests if adding a key adds it to the inventory.
	 */
	@Test
	public void testAddKey() {
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inv.add(key);
		assertEquals(inv.getKey(ColorRGBA.Yellow).getColor(), (ColorRGBA.Yellow));
	}

	/**
	 * Tests if getting a key returns the right key.
	 */
	@Test 
	public void testGetKey() {
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inv.add(key);
		Key key2 = inv.getKey(ColorRGBA.Yellow);
		assertEquals(key, key2);

	}

	/**
	 * Tests if the get method doesn't return a key when there is none or one of the wrong color in the inventory.
	 */
	@Test 
	public void testBadWeatherGetKey() {
		Bomb bomb = new Bomb();
		inv.add(bomb);
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inv.add(key);
		Key key2 = inv.getKey(ColorRGBA.Red);
		assertEquals(key2, null);

	}

	/**
	 * Tests if getting a bomb returns the right bomb.
	 */
	@Test 
	public void testGetBomb() {
		Bomb bomb = new Bomb();
		inv.add(bomb);
		Bomb bomb2 = inv.getBomb();
		assertEquals(bomb, bomb2);

	}

	/**
	 * Tests if the get method doesn't return a bomb when there is none in the inventory.
	 */
	@Test 
	public void testBadWeatherGetBomb() {
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inv.add(key);
		Bomb bomb2 = inv.getBomb();
		assertEquals(bomb2, null);

	}

	/**
	 * Tests if adding a bomb adds it to the inventory.
	 */
	@Test
	public void testAddBomb() {
		Bomb bomb = new Bomb();
		inv.add(bomb);
		assertTrue(inv.pickedUpEntities.get(0) instanceof Bomb);
	}

	/**
	 * Tests if containsBomb method knows if the inventory contains a bomb.
	 */
	@Test
	public void testContainsBomb() {
		Bomb bomb = new Bomb();
		inv.add(bomb);
		assertTrue(inv.containsBomb());
	}

	/**
	 * Tests if containsKey method knows if the inventory contains a key.
	 */
	@Test
	public void testContainsKey() {
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inv.add(key);
		assertTrue(inv.containsKey());
	}

	/**
	 * Tests if the containsColorKey knows if an inventory contains the right color key.
	 */
	@Test
	public void testContainsColorKey() {
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inv.add(key);
		assertTrue(inv.containsColorKey(ColorRGBA.Yellow));
	}

	/**
	 * Tests if the containsColorKey doesn't return true when a certain color key is not in the inventory.
	 */
	@Test
	public void testBadWeatherContainsColorKey() {
		Bomb bomb = new Bomb();
		inv.add(bomb);
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inv.add(key);
		assertFalse(inv.containsColorKey(ColorRGBA.Red));
	}
	
	/**
	 * Tests if the containsColorKey returns false if there are no keys in the inventory.
	 */
	@Test
	public void testContainsNoKeys() {
		Bomb bomb = new Bomb();
		inv.add(bomb);
		assertFalse(inv.containsColorKey(ColorRGBA.Red));
	}

	/**
	 * Tests if removing a bomb removes it from the inventory.
	 */
	@Test
	public void testRemoveBomb() {
		Bomb bomb = new Bomb();
		inv.add(bomb);
		inv.remove(bomb);
		assertFalse(inv.containsBomb());
	}

	/**
	 * Tests if the size method updates correctly.
	 */
	@Test
	public void testSize() {
		Bomb bomb = new Bomb();
		Key key = new Key(ColorRGBA.Blue);
		inv.add(bomb);
		inv.add(bomb);
		inv.add(key);
		assertSame(inv.size(), 3);
	}

	/**
	 * Tests if removing a bomb only removes one bomb.
	 */
	@Test
	public void testRemovesOnlyOneBomb() {
		Bomb bomb = new Bomb();
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inv.add(key);
		inv.add(bomb);
		inv.add(bomb);
		inv.remove(bomb);
		assertSame(inv.size(), 2);
	}

	/**
	 * Tests if removing a key removes it from the inventory.
	 */
	@Test
	public void testRemoveKey() {
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inv.add(key);
		inv.remove(key);
		assertFalse(inv.containsKey());
	}

	/**
	 * Tests if wanting to remove a key of a different color to one in the inventory doesn't remove the key.
	 */
	@Test
	public void testRemoveWrongKey() {
		Bomb bomb = new Bomb();
		inv.add(bomb);
		Key key = new Key(ColorRGBA.Yellow);
		Key key2 = new Key(ColorRGBA.Red);
		inv.add(key);
		inv.remove(key2);
		assertTrue(inv.containsKey());
	}

	/**
	 * Tests if nothing is removed if you want to remove something thats not a bomb or key.
	 */
	@Test
	public void testRemoveNothing() {
		Bomb bomb = new Bomb();
		inv.add(bomb);
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		Door door = new Door(color);
		inv.add(key);
		int currentsize = inv.size();
		inv.remove(door);
		assertSame(currentsize, inv.size());
	}
	
	/**
	 * Tests the number of keys method.
	 */
	@Test
	public void testNumberOfKeys() {
		inv.add(new Key(ColorRGBA.Yellow));
		inv.add(new Key(ColorRGBA.Red));
		assertEquals(inv.numberOfKeys(), 2);
	}
	
	/**
	 * Tests the number of bombs method.
	 */
	@Test
	public void testNumberOfBombs() {
		inv.add(new Bomb());
		inv.add(new Bomb());
		inv.add(new Bomb());
		assertEquals(inv.numberOfKeys(), 3);
	}
}
