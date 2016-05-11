package nl.tudelft.contextproject.model;

import java.awt.Graphics2D;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
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

	private Geometry geometry;

	/**
	 * Constructor for a default player.
	 * This player is (for now) a red sphere.
	 */
	public VRPlayer() { 
		//Set geometry of player
	}

	@Override
	public Geometry getGeometry() {		
		if (geometry != null) return geometry;
		Sphere b = new Sphere(10, 10, .2f);
		geometry = new Geometry("blue cube", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		geometry.setMaterial(mat);
		return geometry;
	}

	@Override
	public void update(float tdf) {
		geometry.move(1 * tdf, 0, 0);
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
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * Get the player hit box.
	 * 
	 * @return	
	 * 				player physics object
	 */
	@Override
	public Object getSpatial() {
		if (geometry == null) {
			this.getGeometry();
		}
		
		//create a shape that implements PhysicsControl
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(PLAYER_RADIUS, PLAYER_HEIGHT, PLAYER_AXIS);
		CharacterControl player = new CharacterControl(capsuleShape, PLAYER_STEP_HEIGHT);

		//Add physical constants of player
		player.setJumpSpeed(JUMP_SPEED);
		player.setFallSpeed(FALL_SPEED);
		player.setGravity(PLAYER_GRAVITY);

		//set physics location of player
		player.setPhysicsLocation(geometry.getLocalTranslation());

		return player;
	}
}
