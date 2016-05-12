package nl.tudelft.contextproject.model.level.temporaryRandomGenerator;


import com.jme3.math.Vector2f;
import nl.tudelft.contextproject.util.Size;

public class GeneratorRoom {
    private int xLeft;
    private int xRight;
    private int yLeft;
    private int yRight;

    private Vector2f center;

    public GeneratorRoom(int xCoord, int yCoord, Size size) {
        this.xLeft = xCoord;
        this.xRight = xCoord + size.getWidth();
        this.yLeft = yCoord;
        this.yRight = yCoord + size.getHeight();

        this.center = new Vector2f((xLeft + xRight) / 2, (yLeft + yRight) / 2);
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

    public Vector2f getCenter() {
        return center;
    }
}
