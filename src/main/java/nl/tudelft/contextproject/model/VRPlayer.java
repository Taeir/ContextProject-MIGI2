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
	private Geometry geometry;
	
	private final int jumpSpeed = 20;
	private final int fallSpeed = 30;
	private final int playerGravity = 30;
	
	private final float playerStepHeight = 0.5f;
	private final float playerRadius = 1.5f;
	private final float playerHeight = 6f;
	private final int playerAxis = 1;
	
	/**
	 * Constructor for a default player.
	 * This player is (for now) a red sphere.
	 */
	public VRPlayer() { }

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
	 * @return	
	 * 				player object
	 */
	@Override
	public Object getSpatial() {

		//create a shape that implements PhysicsControl
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(playerRadius, playerHeight, playerAxis);
		CharacterControl player = new CharacterControl(capsuleShape, playerStepHeight);
		
		//Add physical constants of player
		player.setJumpSpeed(jumpSpeed);
		player.setFallSpeed(fallSpeed);
		player.setGravity(playerGravity);
		
		//set physics location of player
		player.setPhysicsLocation(geometry.getLocalTranslation());
		
		return player;
	}
}
