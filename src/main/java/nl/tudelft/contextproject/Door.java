package nl.tudelft.contextproject;

import java.awt.Graphics2D;


import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * Class representing a door.
 */
public class Door implements Drawable {
	private Geometry geometry;
	private Spatial sp;
	/**
	 * Constructor for a door.
	 */
	public Door() {
		Box cube1Mesh = new Box(1f, 1f, 1f);
		geometry = new Geometry("dink", cube1Mesh); 
		sp = Main.getInstance().getAssetManager().loadModel("Models/door.blend");
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Brown);
		Material mat2 = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color", ColorRGBA.Gray);
		Material mat3 = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color", ColorRGBA.Yellow);
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
	}

	@Override
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * 
	 * @return
	 * 		The Spatial
	 */
	public Spatial getSpatial() {
		return sp;
	}

	@Override
	public void simpleUpdate(float tdf) { }

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
	public void setGeometry(Geometry geo) {
		geometry = geo;

	}

	/**
	 * 
	 * @param spatial
	 * 			The spatial to be set
	 */
	public void setSpatial(Spatial spatial) {
		sp = spatial;

	}
}
