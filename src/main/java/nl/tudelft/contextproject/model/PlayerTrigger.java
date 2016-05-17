package nl.tudelft.contextproject.model;

import java.awt.Graphics2D;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import nl.tudelft.contextproject.Main;

/**
 * Entity that triggers an action when the player is close enough.
 */
public class PlayerTrigger extends Entity {

	private float triggerDist;
	private TickListener action;
	private Spatial sp;
	private float timer;
	private float coolDown;
	
	/**
	 * Constructor for a PlayerTrigger entity.
	 * @param triggerDist How close the player needs to be to trigger the action.
	 * @param coolDown How long (in seconds) the delay is between two triggers.
	 * @param action A TickListener that is updated when the action is triggered.
	 * @param position The position of the playerTrigger in the world.
	 */
	public PlayerTrigger(float triggerDist, float coolDown, TickListener action, Vector3f position) {
		this.triggerDist = triggerDist;
		this.action = action;
		this.coolDown = coolDown;
		getSpatial();
		sp.move(position);
	}

	@Override
	public Spatial getSpatial() {
		if (sp != null) return sp;
		Box b = new Box(.4f, .01f, .4f);
		this.sp = new Geometry("plate", b);
		Material mat = new Material(Main.getInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		mat.setBoolean("UseMaterialColors", true);    
		mat.setColor("Diffuse", ColorRGBA.Orange);
		mat.setColor("Specular", ColorRGBA.White);
		mat.setFloat("Shininess", 64f);
		mat.setColor("Ambient", ColorRGBA.Orange);
		this.sp.setMaterial(mat); 
		this.sp.move(0, 0.505f, 0);
		return sp;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.sp = spatial;
	}

	@Override
	public void mapDraw(Graphics2D g, int resolution) { }

	@Override
	public void update(float tpf) {
		if (timer <= 0 && collidesWithPlayer(triggerDist)) {
			action.update(tpf);
			timer = coolDown;
		}
		if (timer > 0) {
			timer -= tpf;
		}
	}
}
