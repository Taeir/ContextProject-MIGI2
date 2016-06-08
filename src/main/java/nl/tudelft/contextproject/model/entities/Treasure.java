package nl.tudelft.contextproject.model.entities;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;

/**
 * A treasure that can be found by the player, this ends the game.
 */
public class Treasure extends PlayerTrigger {
	
	private Spatial spatial;
	
	/**
	 * Create a treasure.
	 */
	public Treasure() {
		super(1.5f, 0f);
	}
	
	@Override
	public void onTrigger() {
		Main.getInstance().getCurrentGame().endGame(true);
	}
	
	@Override
	public Spatial getSpatial() {
		if (spatial != null) return spatial;

		spatial = Main.getInstance().getAssetManager().loadModel("Models/chest.blend");
		spatial.move(0, 1f, 0);
		return spatial;
	}
	
	@Override
	public void setSpatial(Spatial s) {
		this.spatial = s;
	}
	
	/**
	 * Loads a treasure entity from an array of String data.
	 * 
	 * @param position
	 * 		the position of the treasure
	 * @param data
	 * 		the data of the treasure
	 * @return
	 * 		the treasure represented by the given data
	 * @throws IllegalArgumentException
	 * 		if the given data array is of incorrect length
	 */
	public static Treasure loadEntity(Vector3f position, String[] data) {
		if (data.length != 4) throw new IllegalArgumentException("Invalid data length for loading treasure! Expected \"<X> <Y> <Z> Treasure\".");

		Treasure treasure = new Treasure();
		treasure.move(position);
		return treasure;
	}

	@Override
	public EntityType getType() {
		return EntityType.TREASURE;
	}
}