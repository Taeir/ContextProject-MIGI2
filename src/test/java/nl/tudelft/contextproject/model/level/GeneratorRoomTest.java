package nl.tudelft.contextproject.model.level;

import com.jme3.math.Vector2f;
import nl.tudelft.contextproject.util.Size;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class for creating a GeneratorRoom.
 * This is a room only used by the generator.
 */
public class GeneratorRoomTest {
    private Size mockedSize;

    /**
     * Create a mocked Size object to make sure this class is
     * completely independent.
     */
    @Before
    public void setUp() {
        mockedSize = mock(Size.class);
        when(mockedSize.getHeight()).thenReturn(10);
        when(mockedSize.getWidth()).thenReturn(10);
    }

    /**
     * Test the constructor and all getters.
     */
    @Test
    public void testConstructor() {
        GeneratorRoom testRoom = new GeneratorRoom(0, 0, mockedSize);

        assertEquals(testRoom.getxLeft(), 0);
        assertEquals(testRoom.getyLeft(), 0);

        assertEquals(testRoom.getxRight(), 10);
        assertEquals(testRoom.getyRight(), 10);

        assertEquals(testRoom.getCenter(), new Vector2f(5, 5));
    }

    /**
     * Test if two rooms which should intersect do intersect.
     */
    @Test
    public void testIntersectTrue() {
        GeneratorRoom testRoom1 = new GeneratorRoom(0, 0, mockedSize);
        GeneratorRoom testRoom2 = new GeneratorRoom(0, 0, mockedSize);

        assertTrue(testRoom1.intersects(testRoom2));
        assertTrue(testRoom2.intersects(testRoom1));
    }

    /**
     * Test if two rooms which shouldn't intersect don't intersect.
     */
    @Test
    public void testIntersectFalse() {
        GeneratorRoom testRoom1 = new GeneratorRoom(0, 0, mockedSize);
        GeneratorRoom testRoom2 = new GeneratorRoom(11, 0, mockedSize);

        assertFalse(testRoom1.intersects(testRoom2));
        assertFalse(testRoom2.intersects(testRoom1));
    }
}
