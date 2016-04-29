package nl.tudelft.contextproject;

import java.awt.Graphics2D;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

/**
 * Class representing the player wearing the VR headset.
 */
public class VRPlayer extends Entity {
	private Geometry geometry;

	/**
	 * Constructor for a default player.
	 * This player is (for now) a red sphere.
	 */
	public VRPlayer() {
		Sphere b = new Sphere(10, 10, .2f);
		geometry = new Geometry("blue cube", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		geometry.setMaterial(mat);
	}

	@Override
	public Geometry getGeometry() {
		return geometry;
	}

	@Override
	public void simpleUpdate(float tdf) {
		geometry.move(1 * tdf, 0, 0);
	}

	@Override
	public void mapDraw(Graphics2D g, int resolution) {
		//TODO
	}
}
