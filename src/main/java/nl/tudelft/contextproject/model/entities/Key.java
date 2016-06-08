package nl.tudelft.contextproject.model.entities;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;
import nl.tudelft.contextproject.util.ParserUtil;

/**
 * Class representing a key.
 */
public class Key extends Entity implements PhysicsObject {
	private Spatial sp;
	private ColorRGBA color;
	private RigidBodyControl rb;

	/**
	 * Constructor for a key.
	 *
	 * @param col
	 * 		The color of the key
	 */
	public Key(ColorRGBA col) {
		color = col;
		sp = Main.getInstance().getAssetManager().loadModel("Models/key.blend");
		sp.move(0, 1, 0);
		Node node = (Node) sp;
		Geometry geometry = (Geometry) ((Node) node.getChild("Cube")).getChild(0);
		Material mat = geometry.getMaterial();
		mat.setColor("Ambient", color);
	}

	@Override
	public Spatial getSpatial() {
		return sp;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		sp = spatial;
	}

	@Override
	public void update(float tdf) { }

	@Override
	public PhysicsControl getPhysicsObject() {
		if (rb != null) return rb;

		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sp);
		rb = new RigidBodyControl(sceneShape, 0);
		rb.setPhysicsLocation(sp.getLocalTranslation());
		return rb;
	}

	@Override
	public void move(float x, float y, float z) {
		sp.move(x, y, z);
		if (rb == null) getPhysicsObject();

		rb.setPhysicsLocation(rb.getPhysicsLocation().add(x, y, z));
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
	 * @param col
	 * 		the color that gets set
	 */
	public void setColor(ColorRGBA col) {
		color = col;
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
