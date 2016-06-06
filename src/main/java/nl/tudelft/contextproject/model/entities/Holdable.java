package nl.tudelft.contextproject.model.entities;

public interface Holdable {
	boolean isPickedUp();
	void pickUp();
	void drop();
	default void update(float tpf) { }
}
