package nl.tudelft.contextproject.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jme3.math.ColorRGBA;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.Holdable;
import nl.tudelft.contextproject.model.entities.Key;

/**
 * Class representing the players inventory.
 */
public class Inventory implements TickProducer {
	private ArrayList<ColorRGBA> keys;
	private Holdable holding;
	private Set<TickListener> listeners;

	/**
	 * Constructor for the inventory, starts empty with 0 keys and doors.
	 */
	public Inventory() {
		this.keys = new ArrayList<>();
		this.listeners = new HashSet<>();
	}
	
	/**
	 * Gets the number of keys in the inventory.
	 * 
	 * @return 
	 * 		the number of keys
	 */
	public int numberOfKeys() {
		return keys.size();
	}
	
	/**
	 * Adds a key to the inventory.
	 *
	 * @param key
	 * 		the key to be added
	 */
	public void add(Key key) {
		keys.add(key.getColor());
		updateTickListeners();
	}
	
	/**
	 * Pickup a holdable.
	 *
	 * @param holdable
	 * 		the holdable to be picked up
	 */
	public void pickUp(Holdable holdable) {
		this.holding = holdable;
		holdable.pickUp();
		updateTickListeners();
	}
	
	/**
	 * Removes a key or bomb from your inventory.
	 *
	 * @param entity
	 * 		the bomb/key to remove
	 */
	public void remove(Entity entity) {
		if (entity instanceof Key) {
			ColorRGBA c = ((Key) entity).getColor();
			keys.remove(c);
			updateTickListeners();
		}
	}
	
	/**
	 * Drop the holdable that the inventory holds.
	 * 
	 * @return
	 * 		the holdable that was held or null when nothing was held
	 */
	public Holdable drop() {
		if (holding == null) return null;
		holding.drop();
		Holdable res = holding;
		holding = null;
		updateTickListeners();
		return res;
	}
	
	/**
	 * Returns a key of the given color in the players inventory.
	 *
	 * @param color
	 * 		the color of the key
	 * @return
	 * 		the key of that color
	 */
	public Key getKey(ColorRGBA color) {
		if (keys.contains(color)) {
			return new Key(color);
		}
		return null;
	}
	
	/**
	 * Returns a bomb from the inventory.
	 *
	 * @return
	 * 		if the inventory contains a bomb, returns that bomb. Otherwise returns null
	 */
	public Holdable getHolding() {
		return holding;
	}

	/**
	 * Checks if the inventory contains a bomb.
	 *
	 * @return
	 * 		true if the inventory contains a bomb
	 */
	public boolean isHolding() {
		return holding != null;
	}

	/**
	 * Checks if the inventory contains a key.
	 *
	 * @return
	 * 		true if the inventory contains a key
	 */
	public boolean containsKey() {
		return !keys.isEmpty();
	}

	/**
	 * Checks if the inventory contains a certain color key.
	 *
	 * @param color
	 * 		color of the key
	 * @return
	 * 		true if the inventory contains a key of the wanted color
	 */
	public boolean containsColorKey(ColorRGBA color) {
		return keys.contains(color);
	}
	
	/**
	 * Gives the size of the current inventory.
	 * 
	 * @return
	 * 		the current amount of items in the inventory (bombs and keys)
	 */
	public int size() {
		return keys.size() + (holding == null ? 0 : 1);
	}

	/**
	 * @return
	 * 		a list of all colors of keys in the inventory
	 */
	public List<ColorRGBA> getKeyColors() {
		return keys;
	}

	@Override
	public Set<TickListener> getTickListeners() {
		return listeners;
	}
	
	/**
	 * Update the bomb in the inventory.
	 * 
	 * @param tpf
	 * 		the tpf for this update
	 */
	public void update(float tpf) {
		if (holding == null) return;
		
		holding.update(tpf);
		if (!holding.isPickedUp()) {
			holding = null;
		}
		updateTickListeners();
	}
}
