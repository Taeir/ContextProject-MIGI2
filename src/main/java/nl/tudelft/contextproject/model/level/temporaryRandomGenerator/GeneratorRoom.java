package nl.tudelft.contextproject.model.level.temporaryRandomGenerator;

import lombok.ToString;

import nl.tudelft.contextproject.util.Size;

import static nl.tudelft.contextproject.model.level.temporaryRandomGenerator.GeneratorHelper.*;
import static nl.tudelft.contextproject.model.level.temporaryRandomGenerator.RandomGenerator.checkValid;

@ToString
public class GeneratorRoom {
    private int xStartPosition;
    private int yStartPosition;
    private int width;
    private int height;
    private Direction enteringCorridor;

    public GeneratorRoom(Size size) {
        width = size.getWidth();
        height = size.getHeight();
        xStartPosition = 0;
        yStartPosition = 0;
        /**
         * As the first room spawns in the bottom left corner it's safe to presume we don't want to go left and leave the map.
         * This guarantees that.
         */
        enteringCorridor = Direction.WEST;
    }

    public GeneratorRoom(Size size, GeneratorCorridor corridor) {
        boolean valid = false;
        while (!valid) {
            enteringCorridor = corridor.getDirection();
            width = size.getWidth();
            height = size.getHeight();

            switch (enteringCorridor) {
                case NORTH:
                    yStartPosition = corridor.endPositionY();
                    xStartPosition = getRandom(corridor.endPositionX() - width + 1, corridor.endPositionX());
                    break;
                case EAST:
                    xStartPosition = corridor.endPositionX();
                    yStartPosition = getRandom(corridor.endPositionY() - height + 1, corridor.endPositionY());
                    break;
                case SOUTH:
                    yStartPosition = corridor.endPositionY() - height + 1;
                    xStartPosition = getRandom(corridor.endPositionX() - width + 1, corridor.endPositionX());
                    break;
                case WEST:
                    xStartPosition = corridor.endPositionX() - width + 1;
                    yStartPosition = getRandom(corridor.endPositionY() - height + 1, corridor.endPositionY());
                    break;
                default:
                    throw new IllegalStateException("Not a valid direction");
            }
            valid = checkValid(this);
            System.out.println("Stuck ROOM");
            System.out.println(this.toString());
        }
    }

    public int getxStartPosition() {
        return xStartPosition;
    }

    public int getyStartPosition() {
        return yStartPosition;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Direction getEnteringCorridor() {
        return enteringCorridor;
    }
}
