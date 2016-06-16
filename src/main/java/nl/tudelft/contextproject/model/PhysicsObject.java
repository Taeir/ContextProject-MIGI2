package nl.tudelft.contextproject.model;

import com.jme3.bullet.control.PhysicsControl;

/**
 * Interface for objects that use physics. This includes collisions, gravity, et cetera.
 */
public interface PhysicsObject {
	
	/**
	 * The PhysicsControl of this PhysicsObject.
	 * 
	 * <p>Solid objects should use RigidBodyControl, while movable objects should use CharacterControl.
	 *
	 * @return
	 * 		an object that implements PhysicsControl
	 */
	PhysicsControl getPhysicsObject();
}
