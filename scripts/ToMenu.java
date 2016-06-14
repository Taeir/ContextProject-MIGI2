import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.WaitingController;
import nl.tudelft.contextproject.model.Observer;

/**
 * Script class for going back to the menu.
 */
public class ToMenu implements Observer {
	@Override
	public void update(float tpf) {
		Main main = Main.getInstance();
		main.setController(new WaitingController(main));
	}
}
