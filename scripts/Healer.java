import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Observer;
import nl.tudelft.contextproject.model.entities.VRPlayer;

/**
 * Script class for a Healer. The Healer will heal the player for 1 every update, until
 * they are full health again.
 */
public class Healer implements Observer {
	@Override
	public void update(float tpf) {
		VRPlayer player = Main.getInstance().getCurrentGame().getPlayer();
		if (player.getHealth() < VRPlayer.PLAYER_MAX_HEALTH) {
			player.takeDamage(-1.0F);
		}
	}
}
