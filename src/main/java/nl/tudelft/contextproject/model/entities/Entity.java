package nl.tudelft.contextproject.model.entities;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Drawable;
import nl.tudelft.contextproject.model.TickListener;

/**
 * Abstract parent class for all entities.
 * All entities have a state, are Drawable and are Collidable.
 */
public abstract class Entity implements Drawable, TickListener {
	private EntityState state = EntityState.NEW;

	/**
	 * Get the current state of this entity.
	 *
	 * @return
	 * 		the current state
	 */
	public EntityState getState() {
		return state;
	}

	/**
	 * @param newState
	 * 		the new state of this entity
	 */
	public void setState(EntityState newState) {
		this.state = newState;
	}
	
	/**
	 * Move the entity by the specified amounts.
	 * If the entity is a {@link PhysicsObject}, the physics location should also be moved.
	 *
	 * @param vector
	 * 		the amount of movement
	 */
	public final void move(Vector3f vector) {
		move(vector.getX(), vector.getY(), vector.getZ());
	}
	
	/**
	 * Move the entity by the specified amounts.
	 * If the entity is a {@link PhysicsObject}, the physics location should also be moved.
	 *
	 * @param x
	 * 		the amount of movement in the x-axis
	 * @param y
	 * 		the amount of movement in the y-axis
	 * @param z
	 * 		the amount of movement in the z-axis
	 */
	public abstract void move(float x, float y, float z);

	/**
	 * Checks if this object is closer to the player than the specified distance.
	 *
	 * @param dist
	 * 		the maximum distance to the player to detect the collision
	 * @return
	 * 		true if the player is closert than dist
	 */
	public boolean collidesWithPlayer(float dist) {
		Vector3f playerLoc = Main.getInstance().getCurrentGame().getPlayer().getSpatial().getLocalTranslation();
		Vector3f thisLoc = getSpatial().getLocalTranslation();
		return thisLoc.distance(playerLoc) < dist;
	}

	/**
	 * Get the location of an entity.
	 *
	 * @return
	 * 		a Vector3f representing the location
	 */
	public Vector3f getLocation() {
		return this.getSpatial().getLocalTranslation();
	}

}
