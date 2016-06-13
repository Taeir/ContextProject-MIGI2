package nl.tudelft.contextproject.debug;

import java.util.ArrayList;

import org.eclipse.jetty.websocket.api.StatusCode;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.controller.EndingController;
import nl.tudelft.contextproject.controller.TutorialController;
import nl.tudelft.contextproject.controller.WaitingController;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.Bomb;
import nl.tudelft.contextproject.model.entities.Carrot;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.EntityState;
import nl.tudelft.contextproject.model.entities.Gate;
import nl.tudelft.contextproject.model.entities.KillerBunny;
import nl.tudelft.contextproject.model.entities.LandMine;
import nl.tudelft.contextproject.model.entities.Pitfall;
import nl.tudelft.contextproject.model.entities.PlayerTrigger;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import nl.tudelft.contextproject.model.entities.VoidPlatform;
import nl.tudelft.contextproject.webinterface.Team;
import nl.tudelft.contextproject.webinterface.WebClient;
import nl.tudelft.contextproject.webinterface.WebServer;

/**
 * Class to provide different debug controls.
 */
public final class COCDebug {
	private static final String KICK_ALL_WEB 				= "DEBUG_KICK_ALL_WEB";
	private static final String CLEAR_TEAMS 				= "DEBUG_CLEAR_TEAMS";
	private static final ActionListener LISTENER_WEB 		= new ActionListener() {
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			if (isPressed) return;
			
			WebServer server = Main.getInstance().getWebServer();
			if (server == null) return;
			
			switch (name) {
				case KICK_ALL_WEB:
					ArrayList<WebClient> list = new ArrayList<>(server.getClients().values());
					for (WebClient client : list) {
						server.disconnect(client, StatusCode.NORMAL);
					}
					break;
				case CLEAR_TEAMS:
					for (WebClient client : server.getClients().values()) {
						client.setTeam(Team.NONE);
					}
					break;
				default:
					break;
			}
		}
	};
	
	private static final String TOGGLE_LEVEL_DOWN 			= "DEBUG_TOGGLE_LEVEL_DOWN";
	private static final String TOGGLE_LEVEL_UP 			= "DEBUG_TOGGLE_LEVEL_UP";
	private static final ActionListener LISTENER_GAMESTATE 	= new ActionListener() {
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			if (!isPressed) return;

			switch (name) {
				case TOGGLE_LEVEL_DOWN:
					toggleDown();
					break;
				case TOGGLE_LEVEL_UP:
					toggleUp();
					break;
				default:
					break;
			}
		}
		
		private void toggleUp() {
			Main main = Main.getInstance();
			switch (main.getGameState()) {
				case ENDED:
					if (((EndingController) main.getController()).didElvesWin()) {
						toggleToState(0);
					} else {
						toggleToState(3);
					}
					break;
				case RUNNING:
					toggleToState(2);
					break;
				case TUTORIAL:
					toggleToState(2);
					break;
				case WAITING:
					toggleToState(1);
					break;
				default:
					break;
			}
		}
		
		private void toggleDown() {
			Main main = Main.getInstance();
			switch (main.getGameState()) {
				case ENDED:
					if (((EndingController) main.getController()).didElvesWin()) {
						toggleToState(2);
					} else {
						toggleToState(1);
					}
					break;
				case RUNNING:
					toggleToState(1);
					break;
				case TUTORIAL:
					toggleToState(0);
					break;
				case WAITING:
					toggleToState(3);
					break;
				default:
					break;
			}
		}
		
		private void toggleToState(int state) {
			Main main = Main.getInstance();
			Game game = main.getCurrentGame();
			
			switch (state) {
				case 0:
					main.setController(new WaitingController(main));
					break;
				case 1:
					main.setController(new TutorialController(main));
					break;
				case 2:
					game.endGame(false);
					break;
				case 3:
					game.endGame(true);
					break;
				default:
					break;
			}
		}
	};
	
	private static final String KILL_ENEMIES 				= "DEBUG_KILL_ENEMIES";
	private static final String KILL_NEAREST 				= "DEBUG_KILL_NEAREST";
	private static final String RESPAWN 					= "DEBUG_RESPAWN";
	private static final String JUMP_HIGH 					= "DEBUG_JUMP_HIGH";
	private static final String FLY 						= "DEBUG_FLY";
	private static final String FLY_UP 						= "DEBUG_FLY_UP";
	private static final String FLY_DOWN 					= "DEBUG_FLY_DOWN";
	private static final String NOCLIP 						= "DEBUG_NOCLIP";
	private static final ActionListener LISTENER_GAME 		= new ActionListener() {
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			if (!isPressed) return;
			
			Main main = Main.getInstance();
			Game game = main.getCurrentGame();
			switch (name) {
				case KILL_ENEMIES:
					for (Entity entity : game.getEntities()) {
						if (entity instanceof KillerBunny) entity.setState(EntityState.DEAD);
					}
					break;
				case KILL_NEAREST:
					Vector3f loc = game.getPlayer().getLocation();
					
					Entity entity = game.getEntities().stream()
							.filter(e -> !(e instanceof PlayerTrigger))
							.min((a, b) -> Float.compare(a.getLocation().distanceSquared(loc), b.getLocation().distanceSquared(loc)))
							.orElse(null);
					
					if (entity != null) {
						entity.setState(EntityState.DEAD);
					}
					break;
				case RESPAWN:
					Vector3f newLoc = game.getLevel().getPlayerSpawnPosition();
					game.getPlayer().getSpatial().setLocalTranslation(newLoc);
					game.getPlayer().getPhysicsObject().setPhysicsLocation(newLoc);
					break;
				case JUMP_HIGH:
					jump = !jump;
					if (jump) {
						game.getPlayer().getPhysicsObject().setJumpSpeed(15f);
					} else {
						game.getPlayer().getPhysicsObject().setJumpSpeed(VRPlayer.JUMP_SPEED);
					}
					break;
				case FLY:
					fly = !fly;
					if (fly) {
						game.getPlayer().getPhysicsObject().setFallSpeed(0f);
						Main.getInstance().getInputManager().addListener(LISTENER_FLY, FLY_UP, FLY_DOWN);
					} else {
						game.getPlayer().getPhysicsObject().setFallSpeed(VRPlayer.FALL_SPEED);
						Main.getInstance().getInputManager().removeListener(LISTENER_FLY);
					}
					break;
				case NOCLIP:
					noclip = !noclip;
					CharacterControl cc = game.getPlayer().getPhysicsObject();
					if (noclip) {
						cc.setCollisionGroup(CharacterControl.COLLISION_GROUP_02);
						cc.removeCollideWithGroup(CharacterControl.COLLISION_GROUP_01);
					} else {
						cc.addCollideWithGroup(CharacterControl.COLLISION_GROUP_01);
						cc.setCollisionGroup(CharacterControl.COLLISION_GROUP_01);
					}
					break;
				default:
					break;
			}
		}
	};
	
	private static final ActionListener LISTENER_FLY = new ActionListener() {
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			switch (name) {
				case FLY_UP:
					fly_up = isPressed;
					break;
				case FLY_DOWN:
					fly_down = isPressed;
					break;
				default:
					break;
			}
		}
	};
	
	private static final String SPAWN_BOMB 					= "DEBUG_SPAWN_BOMB";
	private static final String SPAWN_CARROT 				= "DEBUG_SPAWN_CARROT";
	private static final String SPAWN_GATE 					= "DEBUG_SPAWN_GATE";
	private static final String SPAWN_KILLER 				= "DEBUG_SPAWN_KILLER";
	private static final String SPAWN_LANDMINE 				= "DEBUG_SPAWN_LANDMINE";
	private static final String SPAWN_PITFALL 				= "DEBUG_SPAWN_PITFALL";
	private static final String SPAWN_VOIDPLATFORM			= "DEBUG_SPAWN_VOIDPLATFORM";
	private static final ActionListener LISTENER_SPAWN 		= new ActionListener() {
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			if (!isPressed) return;
			
			Main main = Main.getInstance();
			Game game = main.getCurrentGame();
			Vector3f loc = Main.getInstance().getCurrentGame().getPlayer().getLocation();
			Entity entity = null;
			switch (name) {
				case SPAWN_BOMB:
					entity = new Bomb();
					entity.move(loc);
					break;
				case SPAWN_CARROT:
					entity = new Carrot();
					entity.move(loc);
					break;
				case SPAWN_GATE:
					entity = new Gate();
					entity.move(loc);
					break;
				case SPAWN_KILLER:
					entity = new KillerBunny(loc);
					break;
				case SPAWN_LANDMINE:
					entity = new LandMine();
					entity.move(loc);
					break;
				case SPAWN_PITFALL:
					entity = new Pitfall(1);
					entity.move(loc);
					break;
				case SPAWN_VOIDPLATFORM:
					entity = new VoidPlatform();
					entity.move(loc);
					break;
				default:
					break;
			}
			
			if (entity != null) game.addEntity(entity);
		}
	};
	
	private static boolean jump, fly, noclip;
	private static boolean fly_up, fly_down;
	
	private COCDebug() { }
	
	/**
	 * Initializes the debug mode.
	 */
	public static void init() {
		InputManager im = Main.getInstance().getInputManager();
		//Game
		im.addMapping(KILL_NEAREST, new KeyTrigger(KeyInput.KEY_BACK));
		im.addMapping(KILL_ENEMIES, new KeyTrigger(KeyInput.KEY_DELETE));
		im.addMapping(RESPAWN, new KeyTrigger(KeyInput.KEY_HOME));
		im.addMapping(JUMP_HIGH, new KeyTrigger(KeyInput.KEY_J));
		im.addMapping(FLY, new KeyTrigger(KeyInput.KEY_F));
		im.addMapping(FLY_UP, new KeyTrigger(KeyInput.KEY_Q));
		im.addMapping(FLY_DOWN, new KeyTrigger(KeyInput.KEY_Z));
		im.addMapping(NOCLIP, new KeyTrigger(KeyInput.KEY_N));
		im.addListener(LISTENER_GAME, KILL_ENEMIES, KILL_NEAREST, RESPAWN, JUMP_HIGH, FLY, NOCLIP);
		Main.getInstance().attachTickListener(COCDebug::update);
		
		//Web interface
		im.addMapping(KICK_ALL_WEB, new KeyTrigger(KeyInput.KEY_F5));
		im.addMapping(CLEAR_TEAMS, new KeyTrigger(KeyInput.KEY_F6));
		im.addListener(LISTENER_WEB, KICK_ALL_WEB, CLEAR_TEAMS);
		
		//Game states
		im.addMapping(TOGGLE_LEVEL_DOWN, new KeyTrigger(KeyInput.KEY_PGDN));
		im.addMapping(TOGGLE_LEVEL_UP, new KeyTrigger(KeyInput.KEY_PGUP));
		im.addListener(LISTENER_GAMESTATE, TOGGLE_LEVEL_DOWN, TOGGLE_LEVEL_UP);
		
		//Entity spawning
		im.addMapping(SPAWN_BOMB, new KeyTrigger(KeyInput.KEY_B));
		im.addMapping(SPAWN_CARROT, new KeyTrigger(KeyInput.KEY_C));
		im.addMapping(SPAWN_GATE, new KeyTrigger(KeyInput.KEY_G));
		im.addMapping(SPAWN_KILLER, new KeyTrigger(KeyInput.KEY_K));
		im.addMapping(SPAWN_LANDMINE, new KeyTrigger(KeyInput.KEY_L));
		im.addMapping(SPAWN_VOIDPLATFORM, new KeyTrigger(KeyInput.KEY_V));
		im.addListener(LISTENER_SPAWN,
				SPAWN_BOMB,
				SPAWN_CARROT,
				SPAWN_GATE,
				SPAWN_KILLER,
				SPAWN_LANDMINE,
				SPAWN_VOIDPLATFORM);
	}
	
	/**
	 * Updates the player's flight position.
	 * 
	 * @param tpf
	 * 		the time per frame
	 */
	private static void update(float tpf) {
		if (fly_up) {
			Main.getInstance().getCurrentGame().getPlayer().move(0, 2 * tpf, 0);
		}
		
		if (fly_down) {
			Main.getInstance().getCurrentGame().getPlayer().move(0, -2 * tpf, 0);
		}
	}
	
	/**
	 * Switches to no clip mode, and enables flight as well.
	 */
	public static void setNoclip() {
		if (!noclip) {
			noclip = true;
			CharacterControl cc = Main.getInstance().getCurrentGame().getPlayer().getPhysicsObject();
			cc.setCollisionGroup(CharacterControl.COLLISION_GROUP_02);
			cc.removeCollideWithGroup(CharacterControl.COLLISION_GROUP_01);
		}
		
		if (!fly) {
			fly = true;
			Main.getInstance().getCurrentGame().getPlayer().getPhysicsObject().setFallSpeed(0f);
			Main.getInstance().getInputManager().addListener(LISTENER_FLY, FLY_UP, FLY_DOWN);
		}
	}
}