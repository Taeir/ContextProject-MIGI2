package nl.tudelft.contextproject.model.level.temporaryRandomGenerator;

import lombok.Getter;
import lombok.ToString;

import static nl.tudelft.contextproject.model.level.temporaryRandomGenerator.GeneratorHelper.*;
import static nl.tudelft.contextproject.model.level.temporaryRandomGenerator.RandomGenerator.*;

@ToString
public class GeneratorCorridor {
    @Getter int x;
    @Getter private int y;
    @Getter private int length;
    @Getter private Direction direction;

    public int endPositionX() {
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            return x;
        } else if (direction == Direction.EAST) {
            return x + length - 1;
        } else if (direction == Direction.WEST) {
            return x - length + 1;
        } else {
            throw new IllegalStateException("Not a valid direction.");
        }
    }

    public int endPositionY() {
        if (direction == Direction.EAST || direction == Direction.WEST) {
            return y;
        } else if (direction == Direction.NORTH) {
            return y + length - 1;
        } else if (direction == Direction.SOUTH) {
            return y - length + 1;
        } else {
            throw new IllegalStateException("Not a valid direction.");
        }
    }

    public GeneratorCorridor(GeneratorRoom room, int minCorr, int maxCorr) {
        boolean valid = false;
        while (!valid) {
            length = getRandom(minCorr, maxCorr);
            direction = room.getEnteringCorridor().getRotated(getRandom(0, 4));
            switch (direction) {
                case NORTH:
                    x = getRandom(room.getxStartPosition(), room.getxStartPosition() + room.getWidth() - 1);
                    y = room.getyStartPosition() + room.getHeight();
                    break;
                case EAST:
                    x = room.getxStartPosition() + room.getWidth();
                    y = getRandom(room.getyStartPosition(), room.getyStartPosition() + room.getHeight() - 1);
                    break;
                case SOUTH:
                    x = getRandom(room.getxStartPosition(), room.getxStartPosition() + room.getWidth());
                    y = room.getyStartPosition();
                    break;
                case WEST:
                    x = room.getxStartPosition();
                    y = getRandom(room.getyStartPosition(), room.getyStartPosition() + room.getHeight());
                    break;
                default:
                    throw new IllegalStateException("Not a valid direction");
            }
            valid = checkValid(this);
            System.out.println("Stuck");
            System.out.println(this.toString());
        }
    }
}
