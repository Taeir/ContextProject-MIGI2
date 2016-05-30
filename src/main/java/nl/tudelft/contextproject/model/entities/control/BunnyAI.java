package nl.tudelft.contextproject.model.entities.control;

import java.util.Set;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.Carrot;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.KillerBunny;
import nl.tudelft.contextproject.model.entities.VRPlayer;

/**
 * A simple AI for a bunny.
 * Jumps randomly and goes to the closest player or carrot.
 */
public class BunnyAI implements EntityControl {

	private static final float JUMP_FREQUENCY = 2;
	private KillerBunny owner;
	private VRPlayer player;
	private Set<Entity> entities;

	/**
	 * Constructs an instance of a {@link BunnyAI}.
	 */
	public BunnyAI() {
		Game game  = Main.getInstance().getCurrentGame();
		this.player = game.getPlayer();
		this.entities = game.getEntities();
	}
	
	@Override
	public void move(float tpf) {
		owner.getSpatial().lookAt(player.getSpatial().getWorldTranslation(), Vector3f.UNIT_Y);
		float playerdist = player.getLocation().distance(owner.getLocation());
		if (playerdist < .4) {
			player.takeDamage(tpf * 2);
			return;
		}
		
		randomJump(tpf);
		Entity target = findTarget(playerdist, tpf);
		Vector3f move = target.getLocation().subtract(owner.getLocation()).normalize().mult(tpf);
		owner.move(move.x, move.y, move.z);
	}

	/**
	 * Finds the closest target.
	 * This is either a carrot or the player.
	 * 
	 * @param playerDist
	 * 		the distance to the player
	 * @param tpf
	 * 		the time per frame of this update tick
	 * @return
	 * 		the target entity
	 */
	protected Entity findTarget(float playerDist, float tpf) {
		Carrot c = findClosestCarrot();
		if (c == null) return player;
		float carrotDist = c.getLocation().distance(owner.getLocation());
		if (carrotDist < 0.4f) {
			c.eat(tpf);
		}
		if (carrotDist < playerDist) {
			return c;
		} else {
			return player;
		}
	}

	/**
	 * Finds the closest carrot in the game.
	 * 
	 * @return
	 * 		the closest carrot or null when no carrot is found
	 */
	protected Carrot findClosestCarrot() {
		Carrot c = null;
		float dist = Float.MAX_VALUE;
		for (Entity e : entities) {
			if (!(e instanceof Carrot)) continue;
			float d = e.getLocation().distance(owner.getLocation());
			if (c == null || d < dist) {
				c = (Carrot) e;
				dist = d;
			}
		}
		return c;
	}

	/**
	 * Randomly jump.
	 * 
	 * @param tpf
	 * 		the time per frame of this tick
	 */
	protected void randomJump(float tpf) {
		if (Math.random() < tpf / JUMP_FREQUENCY) {
			((CharacterControl) owner.getPhysicsObject()).jump();
		}
	}

	@Override
	public void setOwner(Entity owner) {
		if (!(owner instanceof KillerBunny)) throw new IllegalArgumentException(owner.getClass().getSimpleName() + " is not a bunny.");
		this.owner = (KillerBunny) owner;
	}

}
