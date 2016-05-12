package nl.tudelft.contextproject.model.level.temporaryRandomGenerator;

import com.jme3.math.Vector2f;
import nl.tudelft.contextproject.util.Size;

import java.util.ArrayList;

public class RandomGenerator {
    private static final int MAX_HEIGHT = 50;
    private static final int MAX_WIDTH = 50;
    private static final int MAX_ATTEMPTS = 10;

    public static void makeMeSomeRoomsForTesting() {
        ArrayList<GeneratorRoom> rooms = create(10, true);
        int[][] carved = carveRooms(rooms);
        carved = carveCorridors(carved, rooms);
        for (int x = 0; x < carved.length; x++) {
            for (int y = 0; y < carved[0].length; y++) {
                if (carved[x][y] != 0) {
                    System.out.print(carved[x][y]);
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

    }

    public static ArrayList<GeneratorRoom> create(int amount, boolean allowDuplicates) {
        ArrayList<GeneratorRoom> rooms = new ArrayList<>();
        ArrayList<Size> sizes = GeneratorHelper.loadRooms();
        if (!allowDuplicates && sizes.size() < amount) {
            throw new IllegalArgumentException("You are requesting more rooms than there are.");
        }

        for (int i = 0; i < amount; i++) {
            int attempts = 0;
            boolean success = false;
            Size rSize = getRandomSizes(sizes, allowDuplicates);
            GeneratorRoom newRoom = null;
            if (rSize.getWidth() >= MAX_WIDTH || rSize.getHeight() >= MAX_HEIGHT) {
                throw new IllegalArgumentException("It is impossible for this level to fit in your map size.");
            }


            while (!success && attempts < MAX_ATTEMPTS) {
                success = true;
                int xCoord = GeneratorHelper.getRandom(0, MAX_WIDTH - rSize.getWidth());
                int yCoord = GeneratorHelper.getRandom(0, MAX_HEIGHT - rSize.getHeight());
                newRoom = new GeneratorRoom(xCoord, yCoord, rSize);
                for (GeneratorRoom r : rooms) {
                    if (newRoom.intersects(r)) {
                        success = false;
                        attempts++;
                    }
                }
            }

            if (success) {
                rooms.add(newRoom);
            } else {
                break;
            }
        }

        if (rooms.size() == amount) {
            return rooms;
        } else {
            return create(amount, allowDuplicates);
        }
    }

    private static Size getRandomSizes(ArrayList<Size> sizes, boolean allowDuplicates) {
        Size selected = sizes.get(GeneratorHelper.getRandom(0, sizes.size()));
        if (!allowDuplicates) {
            sizes.remove(selected);
        }
        return selected;
    }

    public static int[][] carveRooms(ArrayList<GeneratorRoom> rooms) {
        int[][] carved = new int[MAX_WIDTH][MAX_HEIGHT];
        for (GeneratorRoom r : rooms) {
            for (int x = r.getxLeft(); x < r.getxRight(); x++) {
                for (int y = r.getyLeft(); y < r.getyRight(); y++) {
                    if (x == r.getxLeft() || x == r.getxRight() - 1 || y == r.getyLeft() || y == r.getyRight() - 1) {
                        carved[x][y] = 3;
                    } else {
                        carved[x][y] = 1;
                    }
                }
            }
        }
        return carved;
    }

    public static int[][] carveCorridors(int[][] map, ArrayList<GeneratorRoom> rooms) {
        for (int i = 1; i < rooms.size(); i++) {
            Vector2f prevCenter = rooms.get(i - 1).getCenter();
            Vector2f currCenter = rooms.get(i).getCenter();
            int rn = GeneratorHelper.getRandom(0, 2);
            if (rn == 1) {
                map = hCorridor(map, prevCenter.getX(), currCenter.getX(), prevCenter.getY());
                map = vCorridor(map, prevCenter.getY(), currCenter.getY(), currCenter.getX());
            } else {
                map = vCorridor(map, prevCenter.getY(), currCenter.getY(), prevCenter.getX());
                map = hCorridor(map, prevCenter.getX(), currCenter.getX(), currCenter.getY());
            }
        }
        return map;
    }

    public static int[][] hCorridor(int[][] map, float x1, float x2, float yF) {
        int min = (int) Math.floor(Math.min(x1, x2));
        int max = (int) Math.floor(Math.max(x1, x2));
        int y = (int) Math.floor(yF);
        for (int x = min; x < max + 1; x++) {
            if (map[x][y] == 0 || map[x][y] == 3) {
                map[x][y] = 2;
            }
        }
        return map;
    }

    public static int[][] vCorridor(int[][] map, float y1, float y2, float xF) {
        int min = (int) Math.floor(Math.min(y1, y2));
        int max = (int) Math.floor(Math.max(y1, y2));
        int x = (int) Math.floor(xF);
        for (int y = min; y < max + 1; y++) {
            if (map[x][y] == 0 || map[x][y] == 3) {
                map[x][y] = 2;
            }
        }
        return map;
    }
}
