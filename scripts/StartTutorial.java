import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.TutorialController;
import nl.tudelft.contextproject.model.Observer;

/**
 * Script class for starting the tutorial.
 */
public class StartTutorial implements Observer {
	@Override
	public void update(float tpf) {
		Main main = Main.getInstance();
		main.setController(new TutorialController(main));
	}
}
