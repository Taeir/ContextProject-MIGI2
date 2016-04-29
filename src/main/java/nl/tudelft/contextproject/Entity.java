package nl.tudelft.contextproject;

import java.awt.Graphics2D;

import com.jme3.math.Vector3f;

/**
 * Abstract parent class for all entities.
 * All entities have a state and are Drawable.
 */
public abstract class Entity implements Drawable {
	private EntityState state = EntityState.NEW;

	/**
	 * Get the current state of this entity.
	 * @return The current state.
	 */
	public EntityState getState() {
		return state;
	}

	/**
	 * Set the state of this entity to a new state.
	 * @param newState The new state of this entity.
	 */
	public void setState(EntityState newState) {
		this.state = newState;
	}

	public void mapDraw(Graphics2D g, int resolution) {
		Vector3f trans = getGeometry().getLocalTranslation();
		int x = (int) trans.x * resolution;
		int y = (int) trans.y * resolution;

		g.drawOval(x, y, resolution, resolution);
	}
}
