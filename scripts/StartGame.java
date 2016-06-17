import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.model.Observer;

/**
 * Script class for starting a new game.
 */
public class StartGame implements Observer {
	@Override
	public void update(float tpf) {
		Main main = Main.getInstance();
		main.setController(new GameController(main, "/maps/MainLevel/", 300.0F, true));
	}
}
