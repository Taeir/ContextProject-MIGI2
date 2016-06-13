package nl.tudelft.contextproject.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.jme3.math.ColorRGBA;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Door;
import nl.tudelft.contextproject.model.entities.Holdable;
import nl.tudelft.contextproject.model.entities.Key;

/**
 * Test class for the Key class.
 */
public class InventoryTest extends TestBase {
	private Inventory inventory;

	/**
	 * Setup method.
	 * Creates a fresh inventory, key and bomb for every test.
	 */
	@Before
	public void setUp() {
		inventory = new Inventory();
	}

	/**
	 * Tests if adding a key adds it to the inventory.
	 */
	@Test
	public void testAddKey() {
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inventory.add(key);
		assertEquals(inventory.getKey(ColorRGBA.Yellow).getColor(), (ColorRGBA.Yellow));
	}

	/**
	 * Tests if getting a key returns the right key.
	 */
	@Test 
	public void testGetKey() {
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inventory.add(key);
		Key key2 = inventory.getKey(ColorRGBA.Yellow);
		assertEquals(key.getColor(), key2.getColor());

	}

	/**
	 * Tests if the get method doesn't return a key when there is none or one of the wrong color in the inventory.
	 */
	@Test 
	public void testBadWeatherGetKey() {
		Bomb bomb = new Bomb();
		inventory.pickUp(bomb);
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inventory.add(key);
		Key key2 = inventory.getKey(ColorRGBA.Red);
		assertEquals(key2, null);

	}

	/**
	 * Tests if getting a bomb returns the right bomb.
	 */
	@Test 
	public void testGetBomb() {
		Bomb bomb = new Bomb();
		inventory.pickUp(bomb);
		Holdable hold = inventory.getHolding();
		assertEquals(bomb, hold);

	}

	/**
	 * Tests if the get method doesn't return a bomb when there is none in the inventory.
	 */
	@Test 
	public void testBadWeatherGetBomb() {
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inventory.add(key);
		Holdable hold = inventory.getHolding();
		assertEquals(hold, null);

	}

	/**
	 * Tests if adding a bomb adds it to the inventory.
	 */
	@Test
	public void testAddHoldable() {
		Bomb bomb = new Bomb();
		inventory.pickUp(bomb);
		assertTrue(inventory.isHolding());
	}

	/**
	 * Tests if containsBomb method knows if the inventory contains a bomb.
	 */
	@Test
	public void testContainsBomb() {
		Bomb bomb = new Bomb();
		inventory.pickUp(bomb);
		assertTrue(inventory.isHolding());
	}

	/**
	 * Tests if containsKey method knows if the inventory contains a key.
	 */
	@Test
	public void testContainsKey() {
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inventory.add(key);
		assertTrue(inventory.containsKey());
	}

	/**
	 * Tests if the containsColorKey knows if an inventory contains the right color key.
	 */
	@Test
	public void testContainsColorKey() {
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inventory.add(key);
		assertTrue(inventory.containsColorKey(ColorRGBA.Yellow));
	}

	/**
	 * Tests if the containsColorKey doesn't return true when a certain color key is not in the inventory.
	 */
	@Test
	public void testBadWeatherContainsColorKey() {
		Bomb bomb = new Bomb();
		inventory.pickUp(bomb);
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inventory.add(key);
		assertFalse(inventory.containsColorKey(ColorRGBA.Red));
	}
	
	/**
	 * Tests if the containsColorKey returns false if there are no keys in the inventory.
	 */
	@Test
	public void testContainsNoKeys() {
		Bomb bomb = new Bomb();
		inventory.pickUp(bomb);
		assertFalse(inventory.containsColorKey(ColorRGBA.Red));
	}

	/**
	 * Tests if removing a bomb removes it from the inventory.
	 */
	@Test
	public void testRemoveBomb() {
		Bomb bomb = new Bomb();
		inventory.pickUp(bomb);
		inventory.drop();
		assertFalse(inventory.isHolding());
	}

	/**
	 * Tests if the size method updates correctly.
	 */
	@Test
	public void testSize() {
		Bomb bomb = new Bomb();
		Key key = new Key(ColorRGBA.Blue);
		inventory.pickUp(bomb);
		inventory.add(key);
		inventory.add(key);
		assertSame(inventory.size(), 3);
	}

	/**
	 * Tests if removing a bomb only removes one bomb.
	 */
	@Test
	public void testRemovesOnlyOneBomb() {
		Bomb bomb = new Bomb();
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inventory.add(key);
		inventory.pickUp(bomb);
		inventory.drop();
		assertSame(1, inventory.size());
		assertFalse(inventory.isHolding());
	}

	/**
	 * Tests if removing a key removes it from the inventory.
	 */
	@Test
	public void testRemoveKey() {
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		inventory.add(key);
		inventory.remove(key);
		assertFalse(inventory.containsKey());
	}

	/**
	 * Tests if wanting to remove a key of a different color to one in the inventory doesn't remove the key.
	 */
	@Test
	public void testRemoveWrongKey() {
		Bomb bomb = new Bomb();
		inventory.pickUp(bomb);
		Key key = new Key(ColorRGBA.Yellow);
		Key key2 = new Key(ColorRGBA.Red);
		inventory.add(key);
		inventory.remove(key2);
		assertTrue(inventory.containsKey());
	}

	/**
	 * Tests if nothing is removed if you want to remove something thats not a bomb or key.
	 */
	@Test
	public void testRemoveNothing() {
		Bomb bomb = new Bomb();
		inventory.pickUp(bomb);
		ColorRGBA color = ColorRGBA.Yellow;
		Key key = new Key(color);
		Door door = new Door(color);
		inventory.add(key);
		int currentsize = inventory.size();
		inventory.remove(door);
		assertSame(currentsize, inventory.size());
	}
	
	/**
	 * Tests the number of keys method.
	 */
	@Test
	public void testNumberOfKeys() {
		inventory.add(new Key(ColorRGBA.Yellow));
		inventory.add(new Key(ColorRGBA.Red));
		assertEquals(inventory.numberOfKeys(), 2);
	}
	
	/**
	 * Test if updating the inventory updates the item in the holding slot.
	 */
	@Test
	public void testUpdate() {
		Bomb mock = mock(Bomb.class);
		inventory.pickUp(mock);
		inventory.update(12f);
		
		verify(mock, times(1)).update(12f);
	}
}
