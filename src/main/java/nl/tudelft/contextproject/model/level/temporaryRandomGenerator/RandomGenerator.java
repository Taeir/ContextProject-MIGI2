package nl.tudelft.contextproject.model.level.temporaryRandomGenerator;

import nl.tudelft.contextproject.util.Size;

import java.util.ArrayList;

public class RandomGenerator {
    private static final int MAX_HEIGHT = 50;
    private static final int MAX_WIDTH = 50;
    private static final int MAX_ATTEMPTS = 10;

    public static void makeMeSomeRoomsForTesting() {
        ArrayList<GeneratorRoom> rooms = create(5, false);
        int[][] carved = carve(rooms);
        for (int x = 0; x < carved.length; x++) {
            for (int y = 0; y < carved[0].length; y++) {
                if (carved[x][y] == 1) {
                    System.out.print(1);
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

    public static int[][] carve(ArrayList<GeneratorRoom> rooms) {
        int[][] carved = new int[MAX_WIDTH][MAX_HEIGHT];
        for (GeneratorRoom r : rooms) {
            for (int x = r.getxLeft(); x < r.getxRight(); x++) {
                for (int y = r.getyLeft(); y < r.getyRight(); y++) {
                    carved[x][y] = 1;
                }
            }
        }
        return carved;
    }
}
