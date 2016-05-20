package nl.tudelft.contextproject.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing the EntityUtil.
 */
public class EntityUtilTest {

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
     * Test getting code for VRPlayer.
     */
    @Test
    public void testGetJSONCodePlayerTrigger() {
        assertEquals(EntityUtil.getJSONCoded("PlayerTrigger"), 5);
    }

    /**
     * Test getting code for non existing entity.
     */
    @Test
    public void testGetJSONCodedDefault() {
        assertEquals(EntityUtil.getJSONCoded("NotAnActualEntity"), 0);
    }
}
