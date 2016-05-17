package nl.tudelft.contextproject.model;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;

/**
 * Abstract parent class for all entities.
 * All entities have a state, are Drawable and are Collidable.
 */
public abstract class Entity implements Drawable, TickListener {
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
	
	/**
	 * Checks if this object is closer to the player than the specified distance.
	 * @param dist The maximum distance to the player to detect the collision.
	 * @return True if the player is closert than dist.
	 */
	public boolean collidesWithPlayer(float dist) {
		Vector3f playerLoc = Main.getInstance().getCurrentGame().getPlayer().getSpatial().getLocalTranslation();
		Vector3f thisLoc = getSpatial().getLocalTranslation();
		return thisLoc.distance(playerLoc) < dist;
	}
}
