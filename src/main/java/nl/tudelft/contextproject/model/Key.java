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
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;
/**
 * Class representing a key.
 */
public class Key extends Entity implements PhysicsObject {
	private Spatial sp;
	private ColorRGBA color;
	private RigidBodyControl rb;
	/**
	 * Constructor for a key.
	 * @param col
	 * 		The color of the key
	 */
	public Key(ColorRGBA col) {
		color = col;
		Box cube1Mesh = new Box(1f, 1f, 1f);
		Geometry geometry = new Geometry("dink", cube1Mesh); 
		if (Main.getInstance().getAssetManager().loadModel("Models/key.j3o") == null) {
			sp =  geometry;
		} else {
			sp = Main.getInstance().getAssetManager().loadModel("Models/nicebombtest.blend");
			//Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			//mat.setColor("Color", color);
			//sp.setMaterial(mat);
		}
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
	public void mapDraw(Graphics2D g, int resolution) {
		Vector3f trans = sp.getLocalTranslation();
		int x = (int) trans.x * resolution;
		int y = (int) trans.y * resolution;
		int width = resolution / 2;
		int offset = resolution / 4;

		g.fillOval(x + offset, y + offset, width, width);
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
	 * Gets the color of the key.
	 * @return color of the key
	 */
	public ColorRGBA getColor() {
		return color;
	}
	
	/**
	 * Sets the color of the key.
	 * @param col
	 * 		color that gets set
	 */
	public void setColor(ColorRGBA col) {
		color = col;
	}
}
