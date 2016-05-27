package nl.tudelft.contextproject.model;

import java.util.ArrayList;
import java.util.Iterator;

import com.jme3.math.ColorRGBA;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.Key;

/**
 * Class representing the players inventory.
 */
public class Inventory {
	ArrayList<Entity> pickedUpEntities;
	private int keys;
	private int bombs;

	/**
	 * Constructor for the inventory, starts empty with 0 keys and doors.
	 */
	public Inventory() {
		this.pickedUpEntities = new ArrayList<>();
		this.keys = 0;
		this.bombs = 0;
	}
	
	/**
	 * Gets the number of keys in the inventory.
	 * 
	 * @return 
	 * 		the number of keys
	 */
	public int numberOfKeys() {
		return keys;
	}
	/**
	 * Returns the number of bombs in the inventory.
	 * 
	 * @return 
	 * 		the number of bombs 
	 */
	public int numberOfBombs() {
		return bombs;
	}
	/**
	 * Adds a key to the inventory.
	 *
	 * @param key
	 * 		the key to be added
	 */
	public void add(Key key) {
		pickedUpEntities.add(key);
		keys++;	
	}
	
	/**
	 * Adds a bomb to the inventory.
	 *
	 * @param bomb
	 * 		the bomb to be added
	 */
	public void add(Bomb bomb) {
		pickedUpEntities.add(bomb);
		bombs++;
	}
	
	/**
	 * Removes a key or bomb from your inventory.
	 *
	 * @param ent
	 * 		the bomb/key to remove
	 */
	public void remove(Entity ent) {
		Iterator<Entity> it = pickedUpEntities.iterator();
		if (ent instanceof Bomb) {
			while (it.hasNext()) {
				Entity entity = it.next();
				if (!(entity instanceof Bomb)) continue;
				
				it.remove();
				bombs--;
				return;
			}
		} else if (ent instanceof Key) {
			Key toRemove = (Key) ent;
			while (it.hasNext()) {
				Entity entity = it.next();
				if (!(entity instanceof Key)) continue;
				
				Key key = (Key) entity;
				if (key.getColor().equals(toRemove.getColor())) {
					it.remove();
					keys--;
					return;
				}
			}
		}
	}
	
	/**
	 * Returns a bomb from the inventory.
	 *
	 * @return
	 * 		if the inventory contains a bomb, returns that bomb. Otherwise returns null
	 */
	public Bomb getBomb() {
		for (Entity ent : pickedUpEntities) {
			if (ent instanceof Bomb) {
				return (Bomb) ent;
			}
		}
		return null;
	}

	/**
	 * Checks if the inventory contains a bomb.
	 *
	 * @return
	 * 		true if the inventory contains a bomb
	 */
	public boolean containsBomb() {
		return (bombs > 0);
	}

	/**
	 * Checks if the inventory contains a key.
	 *
	 * @return
	 * 		true if the inventory contains a key
	 */
	public boolean containsKey() {
		return (keys > 0);
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
		if (!containsKey()) {
			return false;
		}

		for (Entity ent : pickedUpEntities) {
			if (ent instanceof Key) {
				Key key = (Key) ent;
				if (key.getColor().equals(color)) {
					return true;
				}
			}
		}

		return false;
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
		for (Entity ent : pickedUpEntities) {
			if (ent instanceof Key) {
				Key key = (Key) ent;
				if (key.getColor().equals(color)) {
					return key;
				}
			}
		}

		return null;
	}
	
	/**
	 * Gives the size of the current inventory.
	 * @return
	 * 		the current amount of items in the inventory (bombs and keys)
	 */
	public int size() {
		return pickedUpEntities.size();
	}
}
