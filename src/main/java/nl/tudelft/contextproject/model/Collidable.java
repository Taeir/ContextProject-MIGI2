package nl.tudelft.contextproject.model;

/**
 * Collidable interface.
 *	Each object that uses some kind of physics should implement this class.
 */
public interface Collidable {
	
	/**
	 * Get spatial object with physics control.
	 * Each Collidable object has to have some kind of spatial object that 
	 * implements the PhysicsControl interface.
	 * @return
	 * 		an object that implements PhysicsControl.
	 */
	public Object getSpatial();

}
