package nl.tudelft.contextproject.model.entities;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.PhysicsObject;

/**
 * A platform that can be placed in the void.
 */
public class VoidPlatform extends AbstractPhysicsEntity implements PhysicsObject {

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;

		Box b = new Box(.5f, .01f, .5f);
		this.spatial = new Geometry("plate", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors", true);  
		ColorRGBA color = ColorRGBA.Pink.mult(.5f);
		mat.setColor("Diffuse", color);
		mat.setColor("Specular", color);
		mat.setFloat("Shininess", 114f);
		mat.setColor("Ambient", color);
		this.spatial.setMaterial(mat); 
		this.spatial.move(0, 0.5f, 0);
		return spatial;	
	}

	@Override
	public void move(float x, float y, float z) {
		getPhysicsObject();
		rigidBody.setPhysicsLocation(getLocation().add(x, y, z));
		spatial.setLocalTranslation(rigidBody.getPhysicsLocation());
	}

	/**
	 * Loads a void platform entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the void platform
	 * @param data
	 * 		the data of the void platform
	 * @return
	 * 		the void platform represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static VoidPlatform loadEntity(Vector3f position, String[] data) {
		if (data.length != 4) throw new IllegalArgumentException("Invalid data length for loading Void Platform! Expected \"<X> <Y> <Z> VoidPlatform\".");

		VoidPlatform platform = new VoidPlatform();
		platform.move(position);
		return platform;
	}

	@Override
	public EntityType getType() {
		return EntityType.VOID_PLATFORM;
	}
}
