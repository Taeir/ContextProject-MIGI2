package nl.tudelft.contextproject.model.entities;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Drawable;
import nl.tudelft.contextproject.model.Observer;
import nl.tudelft.contextproject.model.entities.util.EntityState;
import nl.tudelft.contextproject.model.entities.util.EntityType;

/**
 * Interface to represent an Entity in the game.
 * Entities are always Drawable and Observer.
 */
public interface Entity extends Drawable, Observer {
	/**
	 * Get the current state of this entity.
	 *
	 * @return
	 * 		the current state
	 */
	EntityState getState();

	/**
	 * @param newState
	 * 		the new state of this entity
	 */
	void setState(EntityState newState);
	
	/**
	 * Move the entity by the specified amounts.
	 * If the entity is a {@link PhysicsObject}, the physics location should also be moved.
	 *
	 * @param vector
	 * 		the amount of movement
	 */
	default void move(Vector3f vector) {
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
	void move(float x, float y, float z);

	/**
	 * Checks if this object is closer to the player than the specified distance.
	 *
	 * @param distance
	 * 		the maximum distance to the player to detect the collision
	 * @return
	 * 		true if the player is closert than dist
	 */
	default boolean collidesWithPlayer(float distance) {
		Vector3f playerLoc = Main.getInstance().getCurrentGame().getPlayer().getSpatial().getLocalTranslation();
		Vector3f thisLoc = getSpatial().getLocalTranslation();
		return thisLoc.distanceSquared(playerLoc) < distance * distance;
	}

	/**
	 * Get the location of an entity.
	 *
	 * @return
	 * 		a Vector3f representing the location
	 */
	default Vector3f getLocation() {
		return getSpatial().getLocalTranslation();
	}

	/**
	 * @return
	 * 		the EntityType of this entity
	 */
	EntityType getType();
}
