package nl.tudelft.contextproject.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing the EntityUtil.
 */
public class EntityUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
     * Test getting code for non existing entity.
     */
    @Test
    public void testGetJSONCodedDefault() {
        thrown.expect(IllegalArgumentException.class);
        EntityUtil.getJSONCoded("NotAnActualEntity");
    }
}
