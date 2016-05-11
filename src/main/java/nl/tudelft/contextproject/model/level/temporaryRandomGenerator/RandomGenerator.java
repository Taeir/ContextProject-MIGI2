package nl.tudelft.contextproject.model.level.temporaryRandomGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import nl.tudelft.contextproject.util.Size;

/**
 * Extremely hacked together and will be refactored before it's merged.
 */
public final class RandomGenerator {
    private static final String FOLDERSTRING =
            "src" + File.separator + "main" + File.separator + "resources" + File.separator + "rooms";
    private static final File FOLDERFILE = new File(FOLDERSTRING);
    private static ArrayList<Size> sizes = new ArrayList<>();
    private static int[][] tiles;
    private static GeneratorRoom[] rooms;
    private static GeneratorCorridor[] corridors;
    private static int hDim = Integer.MIN_VALUE;
    private static int wDim = Integer.MIN_VALUE;
    private static Random rand = new Random();

    private RandomGenerator() {}

    public static void attempt(int cRooms) {
        File[] files = FOLDERFILE.listFiles();
        if (files == null) {
            throw new NullPointerException("There are no rooms.");
        } else if (files.length > cRooms) {
            throw new NullPointerException("There are not enough rooms.");
        }
        for (int i = 0; i < files.length; i++) {
            String size = files[i].getName().substring(0, files[i].getName().indexOf("_"));
            int width = Integer.parseInt(size.substring(0, size.indexOf("x")));
            if (width > wDim) {
                wDim = width;
            }
            int height = Integer.parseInt(size.substring(size.indexOf("x") + 1, size.length()));
            if (height > hDim) {
                hDim = height;
            }
            sizes.add(new Size(width, height));
            System.out.println(sizes.get(sizes.size() - 1).toString());
        }
        wDim = cRooms * wDim;
        hDim = cRooms * hDim;
        tiles = new int[wDim][hDim];
        rooms = new GeneratorRoom[cRooms];
        corridors = new GeneratorCorridor[cRooms - 1];

        createRoomsAndCorridors();
        setTilesValuesForRooms();
        setTilesValuesForCorridors();

        for (int i = 0; i < wDim; i++) {
            for (int j = 0; j < hDim; j++) {
                System.out.print(tiles[i][j]);
            }
            System.out.println();
        }
    }

    private static Size getRandomSize() {
        Size selected = sizes.get(rand.nextInt(sizes.size()));
        sizes.remove(selected);
        return selected;
    }

    private static void createRoomsAndCorridors() {
        rooms[0] = new GeneratorRoom();
        corridors[0] = new GeneratorCorridor();
        Size selected = getRandomSize();
        rooms[0].setupRoom(selected);
        corridors[0].setupCorridor(rooms[0], wDim, hDim, true);

        for (int i = 1; i < rooms.length; i++) {
            rooms[i] = new GeneratorRoom();
            selected = getRandomSize();
            rooms[i].setupRoom(selected, wDim, hDim, corridors[i - 1]);

            if (i < corridors.length) {
                corridors[i] = new GeneratorCorridor();
                corridors[i].setupCorridor(rooms[i], wDim, hDim, false);
            }
        }
    }

    private static void setTilesValuesForRooms() {
        for (int i = 0; i < rooms.length; i++) {
            GeneratorRoom curr = rooms[i];
            for (int j = 0; j < curr.getWidth(); j++) {
                int x = curr.getX() + j;
                for (int k = 0; k < curr.getHeight(); k++) {
                    int y = curr.getY() + k;
                    tiles[x][y] = 1;
                }
            }
        }
    }

    private static void setTilesValuesForCorridors() {
        for (int i = 0; i < corridors.length; i++) {
            GeneratorCorridor curr = corridors[i];
            for (int j = 0; j < curr.getLength(); j++) {
                int x = curr.getX();
                int y = curr.getY();

                switch (curr.getDirection()) {
                    case 0:
                        y += j;
                        break;
                    case 1:
                        x += j;
                        break;
                    case 2:
                        y -= j;
                        break;
                    case 3:
                        x -= j;
                        break;
                    default:
                        throw new NullPointerException("Not a valid direction");
                }

                tiles[x][y] = 1;
            }
        }
    }
}
