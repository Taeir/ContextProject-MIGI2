package nl.tudelft.contextproject.model.level.temporaryRandomGenerator;


import nl.tudelft.contextproject.util.Point;
import nl.tudelft.contextproject.util.Size;

public class GeneratorRoom {
    private int xLeft;
    private int xRight;
    private int yLeft;
    private int yRight;

    private Point center;

    public GeneratorRoom(int xCoord, int yCoord, Size size) {
        this.xLeft = xCoord;
        this.xRight = xCoord + size.getWidth();
        this.yLeft = yCoord;
        this.yRight = yCoord + size.getHeight();

        this.center = new Point((xLeft + xRight) / 2, (yLeft + yRight) / 2);
    }

    public boolean intersects(GeneratorRoom room) {
        return (
                this.xLeft <= room.xRight &&
                this.xRight >= room.xLeft &&
                this.yLeft <= room.yRight &&
                this.yRight >= room.yLeft
        );
    }

    public int getxLeft() {
        return xLeft;
    }

    public int getxRight() {
        return xRight;
    }

    public int getyLeft() {
        return yLeft;
    }

    public int getyRight() {
        return yRight;
    }

    public Point getCenter() {
        return center;
    }
}
