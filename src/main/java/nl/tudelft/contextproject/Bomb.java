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
 * Class representing a bomb.
 */
public class Bomb extends Entity {
	private Geometry geometry;
	private Spatial sp;

	/**
	 * Constructor for a bomb.
	 */
	public Bomb() {
		Box cube1Mesh = new Box(1f, 1f, 1f);
		geometry = new Geometry("dink", cube1Mesh);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		Material matb = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		matb.setColor("Color", ColorRGBA.White);
		if (Main.getInstance().getAssetManager().loadModel("Models/Bomb.j3o") == null) {
			sp = geometry;
		} else {
			Node nod = (Node) Main.getInstance().getAssetManager().loadModel("Models/Bomb.j3o");
			nod.setMaterial(mat);
			((Node) nod.getChild("Cylinder.001")).getChild(0).setMaterial(matb);
			sp = nod;
		}			
	}

	@Override
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * @return
	 * 		the spatial.
	 */
	public Spatial getSpatial() {
		return sp;
	}

	@Override
	public void simpleUpdate(float tdf) {
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
	public void setGeometry(Geometry geo) {
		geometry = geo;

	}

	/**
	 * @param spatial
	 * 		the new spatial
	 */
	public void setSpatial(Spatial spatial) {
		sp = spatial;
	}
}
