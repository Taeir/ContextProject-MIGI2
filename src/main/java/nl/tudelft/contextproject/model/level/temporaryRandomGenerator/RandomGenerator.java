package nl.tudelft.contextproject.model.level.temporaryRandomGenerator;

import static nl.tudelft.contextproject.model.level.temporaryRandomGenerator.GeneratorHelper.*;

import java.io.File;
import java.util.ArrayList;

import nl.tudelft.contextproject.util.Size;
import org.lwjgl.system.libffi.Closure;


/**
 * Extremely hacked together and will be refactored before it's merged.
 */
public final class RandomGenerator {
    private static final String FOLDERSTRING =
            "src" + File.separator + "main" + File.separator + "resources" + File.separator + "rooms";
    private static final File FOLDERFILE = new File(FOLDERSTRING);
    private static int[][] tiles;
    private static GeneratorRoom[] rooms;
    private static GeneratorCorridor[] corridors;
    private static int hDim = Integer.MIN_VALUE;
    private static int wDim = Integer.MIN_VALUE;
    private static int highestRoom = Integer.MIN_VALUE;
    private static int widestRoom = Integer.MIN_VALUE;
    private static int maxCorrLength;
    private static int minCorrLength;

    private RandomGenerator() {}

    public static void attempt(int cRooms, int corrLengthMax, int corrLengthMin, long seed) {
        createRNG(seed);
        attempt(cRooms, corrLengthMax, corrLengthMin);
    }

    private static ArrayList<Size> loadRooms() {
        File[] files = FOLDERFILE.listFiles();
        if (files == null) {
            throw new NullPointerException("There are no rooms.");
        }
        ArrayList<Size> sizes = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String size = files[i].getName().substring(0, files[i].getName().indexOf("_"));
            int width = Integer.parseInt(size.substring(0, size.indexOf("x")));
            if (width > wDim) {
                wDim = width;
                widestRoom = width;
            }
            int height = Integer.parseInt(size.substring(size.indexOf("x") + 1, size.length()));
            if (height > hDim) {
                hDim = height;
                highestRoom = height;
            }
            sizes.add(new Size(width, height));
        }
        return sizes;
    }

    public static void attempt(int cRooms, int corrLengthMax, int corrLengthMin) {
        ArrayList<Size> foundSizes = loadRooms();
        if (foundSizes.size() < cRooms) {
            throw new IllegalArgumentException("You want more rooms than there are available.");
        }
        maxCorrLength = corrLengthMax;
        minCorrLength = corrLengthMin;
        wDim = cRooms * wDim + (cRooms - 1) * maxCorrLength;
        hDim = cRooms * hDim + (cRooms - 1) * maxCorrLength;
        tiles = new int[wDim][hDim];
        rooms = new GeneratorRoom[cRooms];
        corridors = new GeneratorCorridor[cRooms - 1];

        createRoomsAndCorridors(foundSizes);
        for (int i = wDim - 1; i >= 0; i--) {
            for (int j = 0; j < hDim; j++) {
                if(tiles[i][j] == 0) {
                    System.out.print(" ");
                } else {
                    System.out.print(tiles[i][j]);
                }
            }
            System.out.println();
        }
    }

    private static Size getRandomSizeAndRemove(ArrayList<Size> sizes) {
        Size selected = sizes.get(getRandom(0, sizes.size() - 1));
        sizes.remove(selected);
        return selected;
    }

    private static void createRoomsAndCorridors(ArrayList<Size> sizes) {
        Size selected = getRandomSizeAndRemove(sizes);
        rooms[0] = new GeneratorRoom(selected);
        setTilesValuesForRoom(rooms[0]);
        corridors[0] = new GeneratorCorridor(rooms[0], minCorrLength, maxCorrLength);
        setTilesValuesForCorridor(corridors[0]);

        for (int i = 1; i < rooms.length; i++) {
            selected = getRandomSizeAndRemove(sizes);
            rooms[i] = new GeneratorRoom(selected, wDim, hDim, corridors[i - 1]);
            setTilesValuesForRoom(rooms[i]);

            if (i < corridors.length) {
                corridors[i] = new GeneratorCorridor(rooms[i], minCorrLength, maxCorrLength);
                setTilesValuesForCorridor(corridors[i]);
            }
        }
    }

    private static void setTilesValuesForRoom(GeneratorRoom curr) {
        for (int j = 0; j < curr.getWidth(); j++) {
            int x = curr.getxStartPosition() + j;
            for (int k = 0; k < curr.getHeight(); k++) {
                int y = curr.getyStartPosition() + k;
                tiles[x][y] = 1;
            }
        }
    }

    private static void setTilesValuesForCorridor(GeneratorCorridor curr) {
        for (int j = 0; j < curr.getLength(); j++) {
            int x = curr.getX();
            int y = curr.getY();

            switch (curr.getDirection()) {
                case NORTH:
                    y += j;
                    break;
                case EAST:
                    x += j;
                    break;
                case SOUTH:
                    y -= j;
                    break;
                case WEST:
                    x -= j;
                    break;
                default:
                    throw new IllegalStateException("Not a valid direction");
            }

            tiles[x][y] = 1;
        }
    }

    public static boolean checkValid(GeneratorCorridor toCheck) {
        if (toCheck.getY() < 0 || toCheck.getX() < 0 || toCheck.getY() >= hDim || toCheck.getX() >= wDim) {
            return false;
        }
        switch (toCheck.getDirection()) {
            case NORTH:
                if (toCheck.endPositionY() + highestRoom > hDim) {
                    return false;
                }
                for (int y = toCheck.getY(); y < toCheck.endPositionY(); y++) {
                    if (tiles[toCheck.getX()][y] == 1) {
                        return false;
                    }
                }
                break;
            case EAST:
                if (toCheck.endPositionX() + widestRoom > wDim) {
                    return false;
                }
                for (int x = toCheck.getX(); x < toCheck.endPositionX(); x++) {
                    if (tiles[x][toCheck.getY()] == 1) {
                        return false;
                    }
                }
                break;
            case SOUTH:
                if (toCheck.endPositionY() - highestRoom < 0) {
                    return false;
                }
                for (int y = toCheck.getY(); y > toCheck.endPositionY(); y--) {
                    if (tiles[toCheck.getX()][y] == 1) {
                        return false;
                    }
                }
                break;
            case WEST:
                if (toCheck.endPositionX() - widestRoom < 0) {
                    return false;
                }
                for (int x = toCheck.getX(); x > toCheck.endPositionX(); x--) {
                    if (tiles[x][toCheck.getY()] == 1) {
                        return false;
                    }
                }
                break;
            default:
                return false;
        }
        return true;
    }
}
