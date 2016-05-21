package nl.tudelft.contextproject.model;

import java.util.ArrayList;

import com.jme3.math.ColorRGBA;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.Key;

/**
 * Class representing the players inventory.
 */
public class Inventory {
	private ArrayList<Entity> pickedUpEntities;
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
		if (ent instanceof Bomb) {
			for (Entity entity : pickedUpEntities) {
				if (entity instanceof Bomb) {
					pickedUpEntities.remove(entity);
					bombs--;
					return;
				}
			}
		}

		if (ent instanceof Key) {
			for (Entity entity : pickedUpEntities) {
				if (entity instanceof Key) {
					Key key = (Key) entity;
					if (key.getColor().equals(((Key) ent).getColor())) {
						pickedUpEntities.remove(entity);
						keys--;
						return;
					}
				}
			}
		}
	}
	
	/**
	 * Returns a bomb from the inventory.
	 *
	 * @return
	 * 		a bomb from the inventory if the inventory contains a bomb
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
	 * 		the current size of the inventory
	 */
	public int size() {
		return pickedUpEntities.size();
	}
}
