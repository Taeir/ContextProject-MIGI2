package nl.tudelft.contextproject.model.level;

import nl.tudelft.contextproject.util.Size;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Class for testing the RandomLevelFactory.
 */
public class RandomLevelFactoryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RandomLevelFactory factory;

    /**
     * Create the factory used for all the testing and set the seed
     * to 0.
     */
    @Before
    public void setUp() {
        factory = new RandomLevelFactory(1, false);
        factory.createRNG(0);
    }

    /**
     * Test the method for getting a random integer in a certain
     * interval.
     */
    @Test
    public void testGetRandom() {
        assertEquals(0, factory.getRandom(0, 10));
    }

    /**
     * Test to check you can't spawn less than 1 room.
     */
    @Test
    public void testInvalidAmount() {
        thrown.expect(IllegalArgumentException.class);
        new RandomLevelFactory(-3, false);
    }

    /**
     * Test to check the loading of rooms.
     */
    @Test
    public void testLoadRooms() {
        assertNotNull(factory.loadRooms().get(0) != null);
    }

    /**
     * Test for carveRooms.
     */
    @Test
    public void testCarveRooms() {
        ArrayList<GeneratorRoom> rooms = new ArrayList<>();
        rooms.add(new GeneratorRoom(0, 0, new Size(3, 3)));
        assertSame(TileType.WALL, factory.carveRooms(rooms, 3, 3)[0][0]);
        assertSame(TileType.FLOOR, factory.carveRooms(rooms, 3, 3)[1][1]);
    }

    /**
     * Test for carveCorridors given a list of one element.
     */
    @Test
    public void testCarveCorridorsOne() {
        TileType[][] expected = new TileType[0][0];
        ArrayList<GeneratorRoom> rooms = new ArrayList<>();
        rooms.add(new GeneratorRoom(0, 0, new Size(3, 3)));
        assertSame(expected, factory.carveCorridors(expected, rooms));
    }

    /**
     * Test for carveCorridors given a list of two elements.
     */
    @Test
    public void testCarveCorridorsTwoFirstOption() {
        ArrayList<GeneratorRoom> rooms = new ArrayList<>();
        TileType[][] map = new TileType[2][2];
        map[0][0] = TileType.WALL;
        map[1][1] = TileType.WALL;
        rooms.add(new GeneratorRoom(0, 0, new Size(1, 1)));
        rooms.add(new GeneratorRoom(1, 1, new Size(1, 1)));
        assertSame(TileType.CORRIDOR, factory.carveCorridors(map, rooms)[1][0]);
    }

    /**
     * Test for carveCorridors given a list of two elements.
     */
    @Test
    public void testCarveCorridorsTwoSecondOption() {
        ArrayList<GeneratorRoom> rooms = new ArrayList<>();
        TileType[][] map = new TileType[2][2];
        map[0][0] = TileType.WALL;
        map[1][1] = TileType.WALL;
        rooms.add(new GeneratorRoom(0, 0, new Size(1, 1)));
        rooms.add(new GeneratorRoom(1, 1, new Size(1, 1)));

        /**
         * Get two random numbers to further the RNG.
         */
        factory.getRandom(0, 5);
        factory.getRandom(0, 5);

        assertSame(TileType.CORRIDOR, factory.carveCorridors(map, rooms)[0][1]);
    }

    /**
     * Test for getting a random size from a list of sizes.
     */
    @Test
    public void testGetRandomSizeAllowDuplicates() {
        ArrayList<Size> sizes = new ArrayList<>();
        sizes.add(new Size(0, 0));
        Size selected = factory.getRandomSize(sizes, false);
        assertSame(selected.getWidth(), 0);
        assertSame(selected.getHeight(), 0);
        assertSame(sizes.size(), 0);
    }

    /**
     * Test for getting a random size from a list of sizes.
     */
    @Test
    public void testGetRandomSizeDenyDuplicates() {
        ArrayList<Size> sizes = new ArrayList<>();
        sizes.add(new Size(0, 0));
        Size selected = factory.getRandomSize(sizes, true);
        assertSame(selected.getWidth(), 0);
        assertSame(selected.getHeight(), 0);
        assertSame(sizes.size(), 1);
    }

    /**
     * Test for create with invalid parameters.
     */
    @Test
    public void testCreateIncorrect() {
        thrown.expect(IllegalArgumentException.class);
        RandomLevelFactory invalid = new RandomLevelFactory(Integer.MAX_VALUE, false);
        invalid.create();
    }

    /**
     * Test for create with valid parameters.
     * As the static final parameters will probably change during development it is
     * pointless to check the entire creation process at this time.
     * Just checking it goes right as enough.
     */
    @Test
    public void testCreateCorrect() {
        assertSame(factory.create().size(), 1);
    }

    /**
     * Test for creating an entire level.
     * As the static final parameters will probably change during development it is
     * pointless to check the entire creation process at this time.
     * Just checking it goes right as enough.
     */
    @Test
    public void testGenerateSeeded() {
        assertNotNull(factory.generateSeeded(0));
    }
}