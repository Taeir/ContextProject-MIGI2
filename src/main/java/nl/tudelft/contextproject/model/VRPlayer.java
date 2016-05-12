package nl.tudelft.contextproject.model;

import java.awt.Graphics2D;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

import nl.tudelft.contextproject.Main;

/**
 * Class representing the player wearing the VR headset.
 */
public class VRPlayer extends Entity {
	//Physics interaction constants
	public static final int JUMP_SPEED = 20;
	public static final int FALL_SPEED = 30;
	public static final int PLAYER_GRAVITY = 30;

	//Physical collision model
	public static final float PLAYER_STEP_HEIGHT = 0.5f;
	public static final float PLAYER_RADIUS = 1.5f;
	public static final float PLAYER_HEIGHT = 6f;
	public static final int PLAYER_AXIS = 1;

	private Spatial spatial;
	private CharacterControl physicObject;

	/**
	 * Constructor for a default player.
	 * This player is (for now) a red sphere.
	 */
	public VRPlayer() { 
		//Set geometry of player
	}

	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;
		Sphere b = new Sphere(10, 10, .2f);
		spatial = new Geometry("blue cube", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		spatial.setMaterial(mat);
		spatial.setLocalTranslation(-1f, 0f, 4f);
		return spatial;
	}

	@Override
	public void update(float tdf) {
		spatial.move(1 * tdf, 0f, 0f);
		System.out.println("spatial loc" + spatial.getLocalTranslation().toString());
		physicObject.setPhysicsLocation(spatial.getLocalTranslation());
		System.out.println("Physics loc" + physicObject.getPhysicsLocation().toString());
		Main.getInstance().getCamera().setLocation(physicObject.getPhysicsLocation());
	}

	@Override
	public void mapDraw(Graphics2D g, int resolution) {
		Vector3f trans = spatial.getLocalTranslation();
		int x = (int) trans.x * resolution;
		int y = (int) trans.y * resolution;
		int width = resolution / 2;
		int offset = resolution / 4;

		g.fillOval(x + offset, y + offset, width, width);
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	/**
	 * Get the player hit box.
	 * 
	 * @return	
	 * 				player physics object
	 */
	@Override
	public CharacterControl getPhysicsObject() {
		if (spatial == null) {
			this.getSpatial();
		}
		if (physicObject != null) return physicObject;
		//create a shape that implements PhysicsControl
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(PLAYER_RADIUS, PLAYER_HEIGHT, PLAYER_AXIS);
		physicObject = new CharacterControl(capsuleShape, PLAYER_STEP_HEIGHT);

		//Add physical constants of player
		physicObject.setJumpSpeed(JUMP_SPEED);
		physicObject.setFallSpeed(FALL_SPEED);
		physicObject.setGravity(PLAYER_GRAVITY);

		//set physics location of player
		physicObject.setPhysicsLocation(spatial.getLocalTranslation());

		return physicObject;
	}
}
