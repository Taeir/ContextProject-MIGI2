package nl.tudelft.contextproject.model;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.ColorRGBA;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.Key;

/**
 * Class representing the players inventory.
 */
public class Inventory implements TickProducer {
	ArrayList<ColorRGBA> keys;
	ArrayList<Bomb> bombs;
	
	ArrayList<TickListener> listeners;

	/**
	 * Constructor for the inventory, starts empty with 0 keys and doors.
	 */
	public Inventory() {
		this.keys = new ArrayList<>();
		this.bombs = new ArrayList<>();
		this.listeners = new ArrayList<>();
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
	 * Returns the number of bombs in the inventory.
	 * 
	 * @return 
	 * 		the number of bombs 
	 */
	public int numberOfBombs() {
		return bombs.size();
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
		bombs.add(bomb);
		updateTickListeners();
	}
	
	/**
	 * Removes a key or bomb from your inventory.
	 *
	 * @param ent
	 * 		the bomb/key to remove
	 */
	public void remove(Entity ent) {
		if (ent instanceof Bomb && bombs.size() > 0) {
			bombs.remove(0);
			updateTickListeners();
			return;
		}
		if (ent instanceof Key) {
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
		if (bombs.size() == 0) return null;
		return bombs.get(0);
	}

	/**
	 * Checks if the inventory contains a bomb.
	 *
	 * @return
	 * 		true if the inventory contains a bomb
	 */
	public boolean containsBomb() {
		return !bombs.isEmpty();
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
		return keys.size() + bombs.size();
	}

	/**
	 * @return
	 * 		a list of all colors of keys in the inventory
	 */
	public List<ColorRGBA> getKeyColors() {
		return keys;
	}

	@Override
	public List<TickListener> getTickListeners() {
		return listeners;
	}
}
