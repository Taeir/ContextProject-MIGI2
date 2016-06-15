package nl.tudelft.contextproject.model.entities.moving;

import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.control.EntityControl;

/**
 * Class representing an {@link Entity} with {@link EntityControl}.
 */
public abstract class MovingEntity extends Entity {

	private EntityControl control;

	/**
	 * Constructor that attaches the {@link EntityControl} to the entity.
	 * 
	 * @param control
	 * 		the {@link EntityControl} for this entity.
	 */
	public MovingEntity(EntityControl control) {
		this.control = control;
		this.control.setOwner(this);
	}
	
	/**
	 * Set the {@link EntityControl} of this {@link MovingEntity}.
	 * 
	 * @param control
	 * 		the new {@link EntityControl} of this instance.
	 * @return
	 * 		the old control of this instance.
	 */
	public EntityControl setAI(EntityControl control) {
		EntityControl old = control;
		this.control = control;
		this.control.setOwner(this);
		return old;
	}
	
	/**
	 * Get the {@link EntityControl} of this entity.
	 * 
	 * @return
	 * 		the connected {@link EntityControl}
	 */
	public EntityControl getControl() {
		return control;
	}
	
	@Override
	public abstract Spatial getSpatial();

	@Override
	public abstract void setSpatial(Spatial spatial);

	@Override
	public void update(float tpf) {
		control.move(tpf);
	}

	@Override
	public abstract void move(float x, float y, float z);

}
