package nl.tudelft.contextproject.model.entities.control;

import com.jme3.input.controls.ActionListener;

import nl.tudelft.contextproject.model.entities.Entity;

/**
 * Implementation of EntityControl that does nothing.
 */
public class NoControl implements EntityControl, ActionListener {

	@Override
	public void move(float tpf) { }

	@Override
	public void setOwner(Entity owner) { }

	@Override
	public void onAction(String name, boolean isPressed, float tpf) { }

}
