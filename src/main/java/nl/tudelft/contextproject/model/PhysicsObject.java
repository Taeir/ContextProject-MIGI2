package nl.tudelft.contextproject.model;

import com.jme3.bullet.control.PhysicsControl;

/**
 * Collidable interface.
 * Each object that uses some kind of physics should implement this interface.
 */
public interface PhysicsObject {
	
	/**
	 * Get spatial object with physics control.
	 * Each Collidable object has to have some kind of spatial object that 
	 * implements the PhysicsControl interface.
	 *
	 * @return
	 * 		an object that implements PhysicsControl
	 */
	PhysicsControl getPhysicsObject();
}
