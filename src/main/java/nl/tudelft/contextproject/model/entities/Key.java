package nl.tudelft.contextproject.model.entities;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.model.entities.util.AbstractPhysicsEntity;
import nl.tudelft.contextproject.model.entities.util.EntityType;
import nl.tudelft.contextproject.util.ParserUtil;

/**
 * Class representing a key.
 */
public class Key extends AbstractPhysicsEntity implements PhysicsObject {
	private ColorRGBA color;

	/**
	 * Constructor for a key.
	 *
	 * @param color
	 * 		The color of the key
	 */
	public Key(ColorRGBA color) {
		this.color = color;
		spatial = Main.getInstance().getAssetManager().loadModel("Models/key.blend");
		spatial.move(0, 1, 0);
		Node node = (Node) spatial;
		Geometry geometry = (Geometry) ((Node) node.getChild("Cube")).getChild(0);
		Material material = geometry.getMaterial();
		material.setColor("Ambient", this.color);
	}

	/**
	 * Gets the color of the key.
	 *
	 * @return
	 * 		the color of the key
	 */
	public ColorRGBA getColor() {
		return color;
	}

	/**
	 * Sets the color of the key.
	 *
	 * @param color
	 * 		the color that gets set
	 */
	public void setColor(ColorRGBA color) {
		this.color = color;
	}

	/**
	 * Loads a key entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the key
	 * @param data
	 * 		the data of the key
	 * @return
	 * 		the key represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static Key loadEntity(Vector3f position, String[] data) {
		if (data.length != 5) throw new IllegalArgumentException("Invalid data length for loading key! Expected \"<X> <Y> <Z> Key <Color>\".");

		Key key = new Key(ParserUtil.getColor(data[4]));
		key.move(position);

		return key;
	}

	@Override
	public EntityType getType() {
		return EntityType.KEY;
	}
}
