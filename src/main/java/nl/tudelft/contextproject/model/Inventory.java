package nl.tudelft.contextproject.model;

import java.util.ArrayList;

import com.jme3.math.ColorRGBA;

/**
 * Class representing the players inventory.
 *
 */
public class Inventory {
	ArrayList<Entity> array; 
	private int keys;
	private int bombs;

	/**
	 * Constructor for the inventory, starts empty with 0 keys and doors.
	 */
	public Inventory() {
		array = new ArrayList<Entity>();
		keys = 0;
		bombs = 0;
	}

	/**
	 * Adds a key to the inventory.
	 * @param key
	 * 		The key to be added
	 */
	public void add(Key key) {
		array.add(key);
		keys++;	
	}
	
	/**
	 * Adds a bomb to the inventory.
	 * @param bomb
	 * 		The bomb to be added
	 */
	public void add(Bomb bomb) {
		array.add(bomb);
		bombs++;
	}
	
	/**
	 * Removes a key or bomb from your inventory.
	 * @param entity
	 * 		The bomb/key to remove
	 */
	public void remove(Entity entity) {
		if (entity instanceof Bomb) {
			for (Entity ent : array) {
				if (ent instanceof Bomb) {
					array.remove(ent);
					bombs--;
					return;
				}
			}
		}
		if (entity instanceof Key) {
			for (Entity ent : array) {
				if (ent instanceof Key) {
					Key key = (Key) ent;
					if (key.getColor().equals(((Key) ent).getColor())) {
						array.remove(ent);
						keys--;
						return;
					}
				}
			}
		}
		return;
	}
	
	/**
	 * Returns a bomb from the inventory.
	 * @return
	 * 		A bomb from the inventory
	 */
	public Bomb getBomb() {
		Bomb bomb = null;
		for (Entity ent : array) {
			if (ent instanceof Bomb) {
				return (Bomb) ent;
			}
		}
		return bomb;
	}

	/**
	 * Checks if the inventory contains a bomb.
	 * @return returns true if you have a bomb
	 */
	public boolean containsBomb() {
		return (bombs > 0);
	}

	/**
	 * Checks if the inventory contains a key.
	 * @return returns true if you have a key
	 */
	public boolean containsKey() {
		return (keys > 0);
	}

	/**
	 * Checks if the inventory contains a certain color key.
	 * @param color
	 * 		Color of the key
	 * 
	 * @return returns true if you have the certain color key
	 */
	public boolean containsColorKey(ColorRGBA color) {
		if (!containsKey()) {
			return false;
		}
		for (Entity ent : array) {
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
	 * @param color
	 * 		The color of the key
	 * @return The key of that color
	 */
	public Key getKey(ColorRGBA color) {
		for (Entity ent : array) {
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
	 * 		Current size
	 */
	public int size() {
		return array.size();
	}
}
