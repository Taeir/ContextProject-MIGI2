package nl.tudelft.contextproject.model.level;

import java.io.File;

import nl.tudelft.contextproject.util.Size;


public class RandomGenerator {
    private static final String FOLDERSTRING =
            "src" + File.separator + "main" + File.separator + "resources" + File.separator + "rooms";
    private static final File FOLDERFILE = new File(FOLDERSTRING);
    private static Size[] sizes;

    public static void attempt() {
        File[] files = FOLDERFILE.listFiles();
        if (files == null) {
            throw new NullPointerException("There are no levels.");
        }
        sizes = new Size[files.length];
        for (int i = 0; i < files.length; i++) {
            String size = files[i].getName().substring(0, files[i].getName().indexOf("_"));
            int width = Integer.parseInt(size.substring(0, size.indexOf("x")));
            int height = Integer.parseInt(size.substring(size.indexOf("x") + 1, size.length()));
            sizes[i] = new Size(width, height);
            System.out.println(sizes[i].toString());
        }

    }
}
