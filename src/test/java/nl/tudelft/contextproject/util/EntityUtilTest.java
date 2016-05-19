package nl.tudelft.contextproject.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class EntityUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetJSONCodedBomb() {
        assertEquals(EntityUtil.getJSONCoded("Bomb"), 1);
    }

    @Test
    public void testGetJSONCodedDoor() {
        assertEquals(EntityUtil.getJSONCoded("Door"), 2);
    }

    @Test
    public void testGetJSONCodedKey() {
        assertEquals(EntityUtil.getJSONCoded("Key"), 3);
    }

    @Test
    public void testGetJSONCodedVRPlayer() {
        assertEquals(EntityUtil.getJSONCoded("VRPlayer"), 4);
    }

    @Test
    public void testGetJSONCodedDefault() {
        thrown.expect(IllegalArgumentException.class);
        EntityUtil.getJSONCoded("NotAnActualEntity");
    }
}
