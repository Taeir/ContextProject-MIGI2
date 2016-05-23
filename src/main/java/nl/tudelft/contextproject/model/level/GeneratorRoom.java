package nl.tudelft.contextproject.model.level;


import com.jme3.math.Vector2f;
import nl.tudelft.contextproject.util.Size;

/**
 * Class which represents a room used by the RandomLevelFactory.
 */
public class GeneratorRoom {
	private int xLeft;
	private int xRight;
	private int yLeft;
	private int yRight;

	private Vector2f center;

	/**
	 * Create a Generator Room.
	 *
	 * @param xCoord
	 * 		the starting x coordinate
	 * @param yCoord
	 * 		the starting y coordinate
	 * @param size
	 * 		the size object representing the size of the room
	 */
	public GeneratorRoom(int xCoord, int yCoord, Size size) {
		this.xLeft = xCoord;
		this.xRight = xCoord + size.getWidth();
		this.yLeft = yCoord;
		this.yRight = yCoord + size.getHeight();

		this.center = new Vector2f((xLeft + xRight) / 2.f, (yLeft + yRight) / 2.f);
	}

	/**
	 * Method to determine of two rooms intersect with each other.
	 *
	 * @param room
	 * 		the room to check the intersection on
	 * @return
	 * 		true if the rooms intersect, false if not
	 */
	public boolean intersects(GeneratorRoom room) {
		return
				this.xLeft <= room.xRight &&
				this.xRight >= room.xLeft &&
				this.yLeft <= room.yRight &&
				this.yRight >= room.yLeft;
	}

	/**
	 * @return
	 * 		the xLeft coordinate
	 */
	public int getxLeft() {
		return xLeft;
	}

	/**
	 * @return
	 * 		the xRight coordinate
	 */
	public int getxRight() {
		return xRight;
	}

	/**
	 * @return
	 * 		the yLeft coordinate
	 */
	public int getyLeft() {
		return yLeft;
	}

	/**
	 * @return
	 * 		the yRight coordinate
	 */
	public int getyRight() {
		return yRight;
	}

	/**
	 * @return
	 * 		the vector representing the center of the room
	 */
	public Vector2f getCenter() {
		return center;
	}
}
