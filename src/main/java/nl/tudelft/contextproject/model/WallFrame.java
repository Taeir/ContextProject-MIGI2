package nl.tudelft.contextproject.model;

import java.awt.Graphics2D;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import nl.tudelft.contextproject.Main;

/**
 * Entity that is a flat panel (mostly used on a wall) that displays an image.
 */
public class WallFrame extends Entity {
	private Spatial spatial;

	/**
	 * Constructor for the wall frame.
	 * @param position The position of the wall frame.
	 * @param texture The name of the resource for the displayed image.
	 * @param orientation The orientation of the panel.
	 */
	public WallFrame(Vector3f position, String texture, int orientation) {
		Quad b = new Quad(1, 3);
		spatial = new Geometry("WallFrame", b);
		AssetManager am = Main.getInstance().getAssetManager();
		Material mat = new Material(am, "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors", true);
		ColorRGBA color = ColorRGBA.White;
		mat.setColor("Diffuse", color);
		mat.setColor("Specular", ColorRGBA.White);
		mat.setFloat("Shininess", 64f);  // [0,128]
		mat.setColor("Ambient", color);
		mat.setTexture("LightMap", am.loadTexture(texture));
		this.spatial.setMaterial(mat); 

		spatial.setLocalTranslation(position);
		snapToWall(orientation);
	}
	
	/**
	 * Rotate and move the frame to snap to the side of a wall.
	 * @param orientation The orientation of the frame.
	 *
	 */
	//TODO change orientation to Enum?
	private void snapToWall(int orientation) {
		switch (orientation % 4) {
			case 1: spatial.rotate(0f, (float) Math.toRadians(90), 0f);	
					spatial.move(-.49f, 0, .5f);
					break;
			case 2:	spatial.rotate(0f, (float) Math.toRadians(180), 0f);	
					spatial.move(.5f, 0, .49f);
					break;
			case 3: spatial.rotate(0f, (float) Math.toRadians(270), 0f);	
					spatial.move(+.49f, 0, -.5f);
					break;
			default: spatial.move(-.5f, 0, -.49f);
		}
	}

	@Override
	public Spatial getSpatial() {
		return spatial;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void mapDraw(Graphics2D g, int resolution) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(float tpf) {
		
	}

}
