package nl.tudelft.contextproject.model.entities;

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
	 *
	 * @param position
	 * 		the position of the wall frame
	 * @param texture
	 * 		the name of the resource for the displayed image
	 * @param orientation
	 * 		the orientation of the panel
	 * @param width
	 * 		the width of the frame
	 * @param height
	 * 		the height of the frame
	 */
	public WallFrame(Vector3f position, String texture, Direction orientation, float width, float height) {
		Quad b = new Quad(width, height);
		spatial = new Geometry("WallFrame", b);
		AssetManager am = Main.getInstance().getAssetManager();
		Material mat = new Material(am, "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors", true);
		ColorRGBA color = ColorRGBA.White;
		mat.setColor("Diffuse", color);
		mat.setColor("Specular", color);
		mat.setFloat("Shininess", 64f);
		mat.setColor("Ambient", color);
		mat.setTexture("LightMap", am.loadTexture(texture));
		this.spatial.setMaterial(mat); 

		spatial.setLocalTranslation(position);
		snapToWall(orientation);
	}

	/**
	 * Rotate and move the frame to snap to the side of a wall.
	 *
	 * @param orientation
	 * 		the orientation of the frame
	 */
	protected void snapToWall(Direction orientation) {
		switch (orientation) {
			case WEST: 
				spatial.rotate(0f, (float) Math.toRadians(90), 0f);	
				spatial.move(-.49f, 0, .5f);
				break;
			case NORTH:	
				spatial.rotate(0f, (float) Math.toRadians(180), 0f);	
				spatial.move(.5f, 0, .49f);
				break;
			case EAST: 
				spatial.rotate(0f, (float) Math.toRadians(270), 0f);	
				spatial.move(+.49f, 0, -.5f);
				break;
			case SOUTH: 
				spatial.move(-.5f, 0, -.49f);
				break;
			default: 
				throw new IllegalStateException("Illegal orientation " + orientation + ".");
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
	public void update(float tpf) { }

	@Override
	public void move(float x, float y, float z) {
		getSpatial().move(x, y, z);
	}

	/**
	 * Loads a wallframe entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the wallframe
	 * @param data
	 * 		the data of the wallframe
	 * @return
	 * 		the wallframe represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static WallFrame loadEntity(Vector3f position, String[] data) {
		if (data.length != 8) {
			throw new IllegalArgumentException("Invalid data length for loading wall frame! "
					+ "Expected \"<X> <Y> <Z> WallFrame <Direction> <Texture> <Width> <Height>\".");
		}
		
		Direction direction = Direction.valueOf(data[4]);
		String texture = data[3] + data[5];
		float width = Float.parseFloat(data[6]);
		float height = Float.parseFloat(data[7]);
		
		WallFrame frame = new WallFrame(position, texture, direction, width, height);
		return frame;
	}

	@Override
	public EntityType getType() {
		return EntityType.WALLFRAME;
	}
}
