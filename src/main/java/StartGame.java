import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.RandomLevelFactory;

/**
 * StartGame script for the start button in the menu level.
 * 
 * <p>This script generates a new random map using {@link RandomLevelFactory}.
 */
public class StartGame implements TickListener {
	@Override
	public void update(float tpf) {
		Main main = Main.getInstance();
		Level level = new RandomLevelFactory(5, true).generateRandom();
		main.setController(new GameController(main, level));
	}
}