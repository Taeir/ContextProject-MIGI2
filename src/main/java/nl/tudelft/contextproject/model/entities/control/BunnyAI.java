package nl.tudelft.contextproject.model.entities.control;

import java.util.Set;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

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

	//The damage per second that a bunny will do
	private static final float ATTACK_DAMAGE = 1f;
	//The range in which the bunny attacks
	private static final double ATTACK_RANGE = .8;
	//The frequency of jumps of the bunny
	private static final float JUMP_FREQUENCY = 2;
	
	private KillerBunny owner;
	private VRPlayer player;
	private Set<Entity> entities;
	
	/**
	 * Constructs an instance of a {@link BunnyAI}.
	 */
	public BunnyAI() {
		Game game  = Main.getInstance().getCurrentGame();
		if (game != null) {
			this.player = game.getPlayer();
			this.entities = game.getEntities();
		}
	}

	@Override
	public void move(float tpf) {
		Game game = Main.getInstance().getCurrentGame();
		if (game == null) return;
		
		this.player = game.getPlayer();
		if (player == null) return;
		this.entities = game.getEntities();
		
		float playerdist = player.getLocation().distance(owner.getLocation());
		if (playerdist < ATTACK_RANGE) {
			player.takeDamage(tpf * ATTACK_DAMAGE);
			return;
		}

		randomJump(tpf);
		Entity target = findTarget(playerdist, tpf);
		Spatial spatial = owner.getSpatial();
		if (spatial != null) {
			spatial.lookAt(target.getLocation(), Vector3f.UNIT_Y);
			spatial.rotate(0, (float) Math.toRadians(-90), 0);
			}
		Vector3f move = target.getLocation().subtract(owner.getLocation()).normalize().mult(tpf);
		owner.move(move.x, move.y, move.z);
	}

	/**
	 * Finds the closest target.
	 * This is either a carrot or the player.
	 * 
	 * @param playerDistance
	 * 		the distance to the player
	 * @param tpf
	 * 		the time per frame of this update tick
	 * @return
	 * 		the target entity
	 */
	protected Entity findTarget(float playerDistance, float tpf) {
		Carrot carrot = findClosestCarrot();
		if (carrot == null) return player;
		
		float carrotDist = carrot.getLocation().distance(owner.getLocation());
		if (carrotDist < ATTACK_RANGE) {
			carrot.eat(tpf);
		}
		
		if (carrotDist < playerDistance) {
			return carrot;
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
		Carrot carrot = null;
		float distance = Float.MAX_VALUE;
		for (Entity entity : entities) {
			if (!(entity instanceof Carrot)) continue;
			
			float entityDistanaceOfOwner = entity.getLocation().distance(owner.getLocation());
			if (carrot == null || entityDistanaceOfOwner < distance) {
				carrot = (Carrot) entity;
				distance = entityDistanaceOfOwner;
			}
		}
		return carrot;
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
