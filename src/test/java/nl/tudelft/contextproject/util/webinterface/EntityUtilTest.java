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
