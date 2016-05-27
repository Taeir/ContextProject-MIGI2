package nl.tudelft.contextproject.util.webinterface;

import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing the EntityUtil.
 */
public class EntityUtilTest extends TestBase {

	/**
	 * Test getting code for Bomb.
	 */
	@Test
	public void testGetJSONCodedBomb() {
		assertEquals(EntityUtil.getJSONCoded("Bomb"), 1);
	}

	/**
	 * Test getting code for Door.
	 */
	@Test
	public void testGetJSONCodedDoor() {
		assertEquals(EntityUtil.getJSONCoded("Door"), 2);
	}

	/**
	 * Test getting code for Key.
	 */
	@Test
	public void testGetJSONCodedKey() {
		assertEquals(EntityUtil.getJSONCoded("Key"), 3);
	}

	/**
	 * Test getting code for VRPlayer.
	 */
	@Test
	public void testGetJSONCodedVRPlayer() {
		assertEquals(EntityUtil.getJSONCoded("VRPlayer"), 4);
	}

	/**
	 * Test getting code for PlayerTrigger.
	 */
	@Test
	public void testGetJSONCodePlayerTrigger() {
		assertEquals(EntityUtil.getJSONCoded("PlayerTrigger"), 5);
	}

	/**
	 * Test getting code for Pitfall.
	 */
	@Test
	public void testGetJSONCodePitfall() {
		assertEquals(EntityUtil.getJSONCoded("Pitfall"), 6);
	}

	/**
	 * Test getting code for LandMine.
	 */
	@Test
	public void testGetJSONCodeLandMine() {
		assertEquals(EntityUtil.getJSONCoded("LandMine"), 7);
	}

	/**
	 * Test getting code for Carrot.
	 */
	@Test
	public void testGetJSONCodeCarrot() {
		assertEquals(EntityUtil.getJSONCoded("Carrot"), 8);
	}

	/**
	 * Test getting code for KillerBunny.
	 */
	@Test
	public void testGetJSONCodeKillerBunny() {
		assertEquals(EntityUtil.getJSONCoded("KillerBunny"), 9);
	}

	/**
	 * Test getting code for non existing entity.
	 */
	@Test
	public void testGetJSONCodedDefault() {
		assertEquals(EntityUtil.getJSONCoded("NotAnActualEntity"), 0);
	}
}
