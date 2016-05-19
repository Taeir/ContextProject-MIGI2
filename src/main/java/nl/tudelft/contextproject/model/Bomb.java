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
 * Class representing a bomb.
 */
public class Bomb extends Entity implements PhysicsObject {
	private Geometry geometry;
	private Spatial sp;
	private RigidBodyControl rb;

	/**
	 * Constructor for a bomb.
	 * @param x
	 * 		The x coordinate of the bomb
	 * @param y
	 * 		The y coordinate of the bomb
	 * @param z
	 * 		The z coordinate of the bomb
	 */
	public Bomb(int x, int y, int z) {
		Box cube1Mesh = new Box(1f, 1f, 1f);
		geometry = new Geometry("dink", cube1Mesh);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		Material matb = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		matb.setColor("Color", ColorRGBA.White);
		if (Main.getInstance().getAssetManager().loadModel("Models/Bomb.j3o") == null) {
			sp = geometry;
			sp.move(x, y, z);
		} else {
			Node nod = (Node) Main.getInstance().getAssetManager().loadModel("Models/Bomb.j3o");
			nod.setMaterial(mat);
			((Node) nod.getChild("Cylinder.001")).getChild(0).setMaterial(matb);
			sp = nod;
			sp.move(x, y, z);
		}
		
	}

	@Override
	public Spatial getSpatial() {
		return sp;
	}

	@Override
	public void update(float tdf) {
	}

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
	 * Method to move the bomb.
	 * @param x
	 * 		The x coordinate of the bomb
	 * @param y
	 * 		The y coordinate of the bomb
	 * @param z
	 * 		The z coordinate of the bomb
	 */
	public void move(int x, int y, int z) {
		sp.move(x, y, z);
	}
}
