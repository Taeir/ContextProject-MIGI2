package nl.tudelft.contextproject.model;

import java.awt.Graphics2D;


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
import com.jme3.scene.shape.Box;
import nl.tudelft.contextproject.Main;

/**
 * Class representing a door.
 */
public class Door extends Entity implements PhysicsObject {
	private Geometry geometry;
	private Spatial sp;
	private ColorRGBA color;
	/**
	 * Constructor for a door.
	 * @param col
	 * 		Color of the door's lock
	 * @param x
	 * 		The x coordinate of the door
	 * @param y
	 * 		The y coordinate of the door
	 * @param z
	 * 		The z coordinate of the door
	 */
	public Door(ColorRGBA col, int x, int y, int z) {
		color = col;
		Box cube1Mesh = new Box(1f, 1f, 1f);
		geometry = new Geometry("dink", cube1Mesh); 
		sp = Main.getInstance().getAssetManager().loadModel("Models/door.blend");
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Brown);
		Material mat2 = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color", ColorRGBA.Gray);
		Material mat3 = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color", color);
		if (Main.getInstance().getAssetManager().loadModel("Models/key.j3o") == null) {
			sp =  geometry;
		}
		if (sp instanceof Node) {
			Node node = (Node) sp;
			node.setMaterial(mat);
			node.getChild("Sphere").setMaterial(mat2);
			geometry = (Geometry) ((Node) node.getChild("Cube.001")).getChild(0);
			geometry.setMaterial(mat3);
		}
		if (sp instanceof Geometry) {
			geometry = (Geometry) sp;
		}
		sp.move(x, y, z);
	}

	@Override
	public Spatial getSpatial() {
		return sp;
	}

	@Override
	public void update(float tdf) { }

	@Override
	public void mapDraw(Graphics2D g, int resolution) {
		Vector3f trans = geometry.getLocalTranslation();
		int x = (int) trans.x * resolution;
		int y = (int) trans.y * resolution;
		int width = resolution / 2;
		int offset = resolution / 4;

		g.fillOval(x + offset, y + offset, width, width);
	}

	@Override
	public void setSpatial(Spatial spatial) {
		sp = spatial;
	}

	@Override
	public PhysicsControl getPhysicsObject() {
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sp);
		RigidBodyControl rigidBody = new RigidBodyControl(sceneShape, 0);
		rigidBody.setPhysicsLocation(sp.getLocalTranslation());
		return rigidBody;
	}
	
	/**
	 * Sets the color of the doors lock.
	 * @param col
	 * 		The color of the doors lock
	 */
	public void setColor(ColorRGBA col) {
		color = col;
	}
	
	/**
	 * Gets the color of the doors lock.
	 * @return The color of the doors lock
	 */
	public ColorRGBA getColor() {
		return color;
	}
}