package nl.tudelft.contextproject.model.entities;

import com.jme3.scene.Spatial;

/**
 * Class representing an {@link Entity} with {@link EntityAI}.
 */
public abstract class Enemy extends Entity {

	private EntityAI ai;

	/**
	 * Constructor that attaches the {@link EntityAI} to the entity.
	 * 
	 * @param ai
	 * 		the {@link EntityAI} for this entity.
	 */
	public Enemy(EntityAI ai) {
		this.ai = ai;
		this.ai.setOwner(this);
	}
	
	@Override
	public abstract Spatial getSpatial();

	@Override
	public abstract void setSpatial(Spatial spatial);

	@Override
	public void update(float tpf) {
		ai.move(tpf);
	}

	@Override
	public abstract void move(float x, float y, float z);

}
