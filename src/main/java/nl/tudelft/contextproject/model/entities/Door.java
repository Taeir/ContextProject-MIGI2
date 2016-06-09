package nl.tudelft.contextproject.model.entities;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.util.ParserUtil;

/**
 * Class representing a door.
 */
public class Door extends AbstractPhysicsEntity implements PhysicsObject {
	private ColorRGBA color;

	/**
	 * Constructor for a door.
	 *
	 * @param col
	 * 		Color of the door's lock
	 */
	public Door(ColorRGBA col) {
		color = col;
		spatial = Main.getInstance().getAssetManager().loadModel("Models/door.blend");
		spatial.scale(1.2f, 2.2f, 1.2f);
		spatial.move(0, .5f, 0);
		spatial.rotate(0, (float) (Math.PI), 0);
		Material mat3 = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat3.setColor("Color", color);
		if (spatial instanceof Node) {
			Node node = (Node) spatial;
			Geometry geometry = (Geometry) ((Node) node.getChild("Cube.001")).getChild(0);
			Material mat = geometry.getMaterial();
			mat.setColor("Ambient", color);
			geometry.setMaterial(mat);
		}
	}

	/**
	 * Sets the color of the doors lock.
	 * 
	 * @param col
	 * 		the color of the doors lock
	 */
	public void setColor(ColorRGBA col) {
		color = col;
	}

	/**
	 * @return
	 * 		the color of the doors lock
	 */
	public ColorRGBA getColor() {
		return color;
	}
	
	/**
	 * Loads a door entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the door
	 * @param data
	 * 		the data of the door
	 * @return
	 * 		the door represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static Door loadEntity(Vector3f position, String[] data) {
		if (data.length != 5) throw new IllegalArgumentException("Invalid data length for loading door! Expected \"<X> <Y> <Z> Door <Color>\".");
		
		Door door = new Door(ParserUtil.getColor(data[4]));
		door.move(position);
		
		return door;
	}

	@Override
	public EntityType getType() {
		return EntityType.DOOR;
	}
}