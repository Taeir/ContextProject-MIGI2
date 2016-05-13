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
public class Key extends Entity {
	private Geometry geometry;
	private Spatial sp;
	/**
	 * Constructor for a key.
	 */
	public Key() {
		Box cube1Mesh = new Box(1f, 1f, 1f);
		geometry = new Geometry("dink", cube1Mesh); 
		if (Main.getInstance().getAssetManager().loadModel("Models/key.j3o") == null) {
			sp =  geometry;
		} else {
			sp = Main.getInstance().getAssetManager().loadModel("Models/key.j3o");
		}
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Yellow);
		sp.setMaterial(mat);
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
		Vector3f trans = geometry.getLocalTranslation();
		int x = (int) trans.x * resolution;
		int y = (int) trans.y * resolution;
		int width = resolution / 2;
		int offset = resolution / 4;

		g.fillOval(x + offset, y + offset, width, width);
	}

	@Override
	public PhysicsControl getPhysicsObject() {
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sp);
		RigidBodyControl rigidBody = new RigidBodyControl(sceneShape, 0);
		rigidBody.setPhysicsLocation(sp.getLocalTranslation());
		return rigidBody;
	}
}
