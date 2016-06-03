package nl.tudelft.contextproject.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jme3.math.ColorRGBA;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.Key;

/**
 * Class representing the players inventory.
 */
public class Inventory implements TickProducer {
	ArrayList<ColorRGBA> keys;
	Bomb bomb;
	
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
	 * Adds a bomb to the inventory.
	 *
	 * @param bomb
	 * 		the bomb to be added
	 */
	public void add(Bomb bomb) {
		this.bomb = bomb;
		this.bomb.activate();
		this.bomb.setPickedup(true);
		updateTickListeners();
	}
	
	/**
	 * Removes a key or bomb from your inventory.
	 *
	 * @param ent
	 * 		the bomb/key to remove
	 */
	public void remove(Entity ent) {
		if (ent instanceof Bomb && bomb != null) {
			bomb = null;
			updateTickListeners();
		} else if (ent instanceof Key) {
			ColorRGBA c = ((Key) ent).getColor();
			keys.remove(c);
			updateTickListeners();
		}
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
	public Bomb getBomb() {
		return bomb;
	}

	/**
	 * Checks if the inventory contains a bomb.
	 *
	 * @return
	 * 		true if the inventory contains a bomb
	 */
	public boolean containsBomb() {
		return bomb != null;
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
	 * @return
	 * 		the current amount of items in the inventory (bombs and keys)
	 */
	public int size() {
		return keys.size() + (bomb == null ? 0 : 1);
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
	 * 		the tpf for this update.
	 */
	public void update(float tpf) {
		if (bomb != null) {
			bomb.update(tpf);
			if (!bomb.getActive()) {
				bomb = null;
			}
			updateTickListeners();
		}
	}
}
