package nl.tudelft.contextproject.model.entities;

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
import com.jme3.texture.Texture;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;

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
		Box cube1Mesh = new Box(1f, 1f, 1f);
		Geometry geometry = new Geometry("dink", cube1Mesh); 
		if (Main.getInstance().getAssetManager().loadModel("Models/door.blend") != null) {
			sp = Main.getInstance().getAssetManager().loadModel("Models/door.blend");
			sp.scale(2f);
		}
//		sp.rotate(0, (float) Math.PI, 0);
		Material mat3 = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat3.setColor("Color", color);

		if (Main.getInstance().getAssetManager().loadModel("Models/key.j3o") == null) {
			sp =  geometry;
		}
		if (sp instanceof Node) {
			Node node = (Node) sp;
			//node.setMaterial(mat);
			//node.getChild("Sphere").setMaterial(mat2);
			geometry = (Geometry) ((Node) node.getChild("Cube.001")).getChild(0);
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
	public void mapDraw(Graphics2D g, int resolution) {
		Vector3f trans = sp.getLocalTranslation();
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