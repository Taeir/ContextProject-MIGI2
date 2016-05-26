package nl.tudelft.contextproject.model;

import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
/**
 * Class containing all the loaded in models.
 *
 */
public class Models {
	public Spatial key;
	public Spatial bomb;
	public Spatial door;


	/**
	 * Constructor for the models.
	 */
	public Models() {
		key = Main.getInstance().getAssetManager().loadModel("Models/key.blend");
		bomb = Main.getInstance().getAssetManager().loadModel("Models/bomb.blend");
		door = Main.getInstance().getAssetManager().loadModel("Models/key.blend");
	}
	public Spatial getKey(){
		return key;
	}
}
