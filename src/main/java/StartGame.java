import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.GameController;
import nl.tudelft.contextproject.model.TickListener;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MSTBasedLevelFactory;

public class StartGame implements TickListener {

 @Override
 public void update(float tpf) {
  Main main = Main.getInstance();
  Level level = new MSTBasedLevelFactory("/maps/testGridMap/").generateRandom();
  main.setController(new GameController(main, level));
 }
 
}