package nl.tudelft.contextproject.model.level;

import java.io.File;

public class RandomGenerator {
    private static final String FOLDERSTRING = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "rooms";
    private static final File FOLDERFILE = new File(FOLDERSTRING);
    private static String[] sizes;

    public static void attempt() {
        File[] files = FOLDERFILE.listFiles();
        if (files == null) {
            throw new NullPointerException("There are no levels.");
        }
        sizes = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            sizes[i] = files[i].getName().substring(0, files[i].getName().indexOf("_"));
        }

    }
}
