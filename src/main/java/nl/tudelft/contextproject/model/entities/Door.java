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
 * Class representing a door.
 */
public class Door extends Entity implements PhysicsObject {
	private Spatial sp;
	private ColorRGBA color;
	private RigidBodyControl rb;

	/**
	 * Constructor for a door.
	 *
	 * @param col
	 * 		Color of the door's lock
	 */
	public Door(ColorRGBA col) {
		color = col;
		sp = Main.getInstance().getAssetManager().loadModel("Models/door.blend");
		sp.scale(2f);
		Material mat3 = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat3.setColor("Color", color);
		if (sp instanceof Node) {
			Node node = (Node) sp;
			Geometry geometry = (Geometry) ((Node) node.getChild("Cube.001")).getChild(0);
			Material mat = geometry.getMaterial();
			mat.setColor("Ambient", color);
			geometry.setMaterial(mat);
		}
	}

	@Override
	public Spatial getSpatial() {
		return sp;
	}

	@Override
	public void update(float tdf) { }

	@Override
	public void setSpatial(Spatial spatial) {
		sp = spatial;
	}

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
		if (rb == null) getPhysicsObject();

		sp.move(x, y, z);
		rb.setPhysicsLocation(rb.getPhysicsLocation().add(x, y, z));
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
}