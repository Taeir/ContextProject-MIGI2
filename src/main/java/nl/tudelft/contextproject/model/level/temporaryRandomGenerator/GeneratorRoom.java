package nl.tudelft.contextproject.model.level.temporaryRandomGenerator;

import lombok.Getter;

import nl.tudelft.contextproject.util.Size;

import java.util.Random;

public class GeneratorRoom {
    @Getter private int x;
    @Getter private int y;
    @Getter private int width;
    @Getter private int height;
    @Getter private int enteringCorridor;
    private static Random rand = new Random();

    public void setupRoom(Size size, int dim) {
        width = size.getWidth();
        height = size.getHeight();
        x = Math.round(dim / 2f - width / 2f);
        y = Math.round(dim / 2f - height / 2f);
    }

    public void setupRoom(Size size, int dim, GeneratorCorridor corridor) {
        enteringCorridor = corridor.getDirection();
        width = size.getWidth();
        height = size.getHeight();

        switch (corridor.getDirection()) {
            case 0:
                y = corridor.endPositionY();
                x = getRandom(corridor.endPositionX() - width + 1, corridor.endPositionX());
                break;
            case 1:
                x = corridor.endPositionX();
                y = getRandom(corridor.endPositionY() - height + 1, corridor.endPositionY());
                break;
            case 2:
                y = corridor.endPositionY() - height + 1;
                x = getRandom(corridor.endPositionX() - width + 1, corridor.endPositionX());
                break;
            case 3:
                x = corridor.endPositionX() - width + 1;
                y = getRandom(corridor.endPositionY() - height + 1, corridor.endPositionY());
                break;
        }
        while (x >= dim) {
            x--;
        }
        while (x < 0) {
            x++;
        }
        while (y >= dim) {
            y--;
        }
        while (y < 0) {
            y++;
        }
    }

    protected static int getRandom(int min, int max) {
        return (rand.nextInt((max - min) + 1) + min);
    }
}
