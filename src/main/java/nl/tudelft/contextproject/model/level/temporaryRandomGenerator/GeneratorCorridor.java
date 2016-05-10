package nl.tudelft.contextproject.model.level.temporaryRandomGenerator;

import lombok.Getter;

import nl.tudelft.contextproject.util.Size;

import java.util.Random;

import static nl.tudelft.contextproject.model.level.temporaryRandomGenerator.GeneratorRoom.getRandom;

public class GeneratorCorridor {
    @Getter int x;
    @Getter private int y;
    @Getter private int length;
    @Getter private int direction;
    private static Random rand = new Random();

    public int endPositionX() {
        if (direction == 0 || direction == 2) {
            return x;
        }
        if (direction == 1) {
            return x + length - 1;
        }
        return x - length + 1;
    }

    public int endPositionY() {
        if (direction == 1 || direction == 3) {
            return y;
        }
        if (direction == 0) {
            return y + length - 1;
        }
        return y - length + 1;
    }

    public void setupCorridor(GeneratorRoom room, int dim, boolean fst) {
        direction = rand.nextInt(4);
        int oppositeDirection = (room.getEnteringCorridor() + 2) % 4;

        if (!fst && direction == oppositeDirection) {
            direction++;
            direction %= 4;
        }

        length = rand.nextInt(8);
        int maxLength = 8;
        switch (direction) {
            case 0:
                x = getRandom(room.getX(), room.getX() + room.getWidth() - 1);
                y = room.getY() + room.getHeight();
                maxLength = dim - y - room.getHeight();
                break;
            case 1:
                x = room.getX() + room.getWidth();
                y = getRandom(room.getY(), room.getY() + room.getHeight() - 1);
                maxLength = dim - x - room.getWidth();
                break;
            case 2:
                x = getRandom(room.getX(), room.getX() + room.getWidth());
                y = room.getY();
                maxLength = y - room.getHeight();
                break;
            case 3:
                x = room.getX();
                y = getRandom(room.getY(), room.getY() + room.getHeight());
                maxLength = x - room.getWidth();
                break;
        }
        while (length > maxLength) {
            length--;
        }
        while (length <= 0) {
            length++;
        }
    }

}
