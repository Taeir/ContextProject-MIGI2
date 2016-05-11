package nl.tudelft.contextproject.model.level.temporaryRandomGenerator;

import nl.tudelft.contextproject.util.Size;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class which is used to create random levels.
 */
public final class GeneratorHelper {
    private static Random rand;
    private static final String FOLDERSTRING =
            "src" + File.separator + "main" + File.separator + "resources" + File.separator + "rooms";
    private static final File FOLDERFILE = new File(FOLDERSTRING);

    /**
     * Avoid instantiation of the helper.
     */
    private GeneratorHelper() {}

    /**
     * Return a random number between two values.
     * @param min
     *          The minimum value the random number can be.
     * @param max
     *          The maximum value the random number can be.
     * @return
     *          The random number.
     */
    public static int getRandom(int min, int max) {
        if (rand == null) {
            createRNG(System.currentTimeMillis());
        }
        return (rand.nextInt((max - min)) + min);
    }

    /**
     * If the random number generator has not been instantiated yet,
     * created one with the given seed.
     * @param seed
     *          The seed to use for creation of the RNG.
     */
    public static void createRNG(long seed) {
        if (rand == null) {
            rand = new Random(seed);
        }
    }

    public static ArrayList<Size> loadRooms() {
        File[] files = FOLDERFILE.listFiles();
        if (files == null) {
            throw new NullPointerException("There are no rooms.");
        }
        ArrayList<Size> sizes = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String size = files[i].getName().substring(0, files[i].getName().indexOf("_"));
            int width = Integer.parseInt(size.substring(0, size.indexOf("x")));
            int height = Integer.parseInt(size.substring(size.indexOf("x") + 1, size.length()));
            sizes.add(new Size(width, height));
        }
        return sizes;
    }
}
