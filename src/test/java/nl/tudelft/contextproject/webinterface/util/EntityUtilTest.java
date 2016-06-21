package nl.tudelft.contextproject.webinterface.util;

import org.junit.Test;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.exploding.Bomb;
import nl.tudelft.contextproject.model.entities.moving.VRPlayer;
import nl.tudelft.contextproject.model.entities.util.EntityType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;

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
		bomb.move(3, 0, 2);
		JSONArray json = EntityUtil.entityToJson(bomb);
		assertEquals(json.getInt(0), EntityType.BOMB.getWebId()); //Type
		assertEquals(json.getInt(1), 3); //X
		assertEquals(json.getInt(2), 2); //Y
		assertEquals(json.getInt(3), 0); //Counter
	}
}
