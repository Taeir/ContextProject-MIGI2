package nl.tudelft.contextproject.util.webinterface;

import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.VRPlayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

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
	
	/**
	 * Test for getting a json representing all entities.
	 */
	@Test
	public void testEntitiesToJson() {
		Set<Entity> set = ConcurrentHashMap.newKeySet();

		Bomb bomb = new Bomb();
		set.add(bomb);

		JSONArray jArray = EntityUtil.entitiesToJson(set, new VRPlayer());
		assertNotNull(jArray.get(0));
	}


	/**
	 * Test for getting a json representing an entity.
	 */
	@Test
	public void testEntityToJson() {
		Bomb bomb = new Bomb();
		JSONObject json = EntityUtil.entityToJson(bomb);
		assertEquals(json.getInt("x"), 0);
		assertEquals(json.getInt("y"), 0);
		assertEquals(json.getInt("type"), 1);
	}
}
