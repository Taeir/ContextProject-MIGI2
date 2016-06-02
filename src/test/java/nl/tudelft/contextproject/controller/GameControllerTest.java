package nl.tudelft.contextproject.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.InputManager;
import com.jme3.input.controls.InputListener;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.hud.HUD;
import nl.tudelft.contextproject.model.entities.Entity;
import nl.tudelft.contextproject.model.entities.EntityState;
import nl.tudelft.contextproject.model.Game;
import nl.tudelft.contextproject.model.entities.VRPlayer;
import nl.tudelft.contextproject.model.level.Level;
import nl.tudelft.contextproject.model.level.MazeTile;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the GameController class.
 */
public class GameControllerTest extends ControllerTest {
	private GameController controller;
	private Main main;
	private Game game;
	private Level level;
	private Node rootNode;
	private InputManager inputManager;
	private BulletAppState phe;

	/**
	 * Setup method for the test suit.
	 * Creates a new instance of the controller for each test.
	 */
	@Before
	public void setUp() {
		main = Main.getInstance();
		Level l = null;
		controller = new GameController(main, l, 10f);

		Light light = mock(Light.class);
		rootNode = mock(Node.class);
		LightList lightList = new LightList(rootNode);
		lightList.add(light);
		when(rootNode.getLocalLightList()).thenReturn(lightList);
		controller.setRootNode(rootNode);
		inputManager = mock(InputManager.class);
		controller.setInputManager(inputManager);

		Set<Entity> entities = ConcurrentHashMap.newKeySet();
		Entity entity = mock(Entity.class);
		when(entity.getSpatial()).thenReturn(mock(Spatial.class));
		when(entity.getState()).thenReturn(EntityState.ALIVE);

		phe = mock(BulletAppState.class);
		PhysicsSpace phs = mock(PhysicsSpace.class);
		when(phe.getPhysicsSpace()).thenReturn(phs);

		entities.add(entity);
		VRPlayer player = mock(VRPlayer.class);
		when(player.getSpatial()).thenReturn(mock(Spatial.class));

		MazeTile t = mock(MazeTile.class);
		when(t.getSpatial()).thenReturn(mock(Spatial.class));
		MazeTile[][] tiles = {{t, t}, {null, t}, {t, null}};
		LinkedList<Light> lights = new LinkedList<>();
		lights.add(light);
		level = new Level(tiles, lights);
		game = new Game(level, player, entities, controller, 10f);

		controller.setGame(game);
		controller.setPhysicsEnvironmentNode(phe);
	}

	/**
	 * Test if initializing adds a keyListener for pausing the game.
	 */
	@Test
	public void testInitialize2() {
		AppStateManager sm = mock(AppStateManager.class);
		controller.initialize(sm, main);
		verify(inputManager, times(1)).addListener(any(InputListener.class), eq("pause"));
	}

	/**
	 * Test the update on the player.
	 */
	@Test
	public void testUpdatePlayer() {
		HUD hud = mock(HUD.class);
		controller.setHUD(hud);
		
		controller.update(0.5f);
		
		verify(hud, times(1)).setGameTimer(anyInt());
		verify(game.getPlayer(), times(1)).update(Math.min(GameController.MAX_TPF, .5f));
	}

	/**
	 * Test if a NEW entity is updated correctly.
	 */
	@Test
	public void testUpdateEntityNEW() {
		Set<Entity> set = controller.getGame().getEntities();
		Entity eMock = mock(Entity.class);
		Node rn = mock(Node.class);
		Geometry geom = mock(Geometry.class);

		when(eMock.getState()).thenReturn(EntityState.NEW);
		when(eMock.getSpatial()).thenReturn(geom);
		

		set.add(eMock);

		controller.setRootNode(rn);
		controller.updateEntities(0.5f);

		verify(eMock, times(1)).update(0.5f);
		//verify(phe.getPhysicsSpace(), times(1)).add(geom);
		verify(eMock, times(1)).setState(EntityState.ALIVE);

		verify(rn, times(1)).attachChild(geom);    
	}

	/**
	 * Test if a DEAD entity is removed correctly.
	 */
	@Test
	public void testUpdateEntityDEAD() {
		Entity entity = controller.getGame().getEntities().iterator().next();

		when(entity.getState()).thenReturn(EntityState.DEAD);	

		controller.updateEntities(0.5f);

		verify(entity, times(0)).update(0.5f);        
		verify(rootNode, times(1)).detachChild(entity.getSpatial()); 
		assertFalse(game.getEntities().contains(entity));
	}

	/**
	 * Test if an ALIVE entity is updated correctly.
	 */
	@Test
	public void testUpdateEntityALIVE() {
		Entity entity = controller.getGame().getEntities().iterator().next();
		when(entity.getState()).thenReturn(EntityState.ALIVE);
		controller.updateEntities(0.5f);
		
		verify(entity, times(1)).update(0.5f);
	}

	/**
	 * Test the setters and getters for the level.
	 */
	@Test
	public void testLevel() {
		Level level = new Level(null, null);
		controller.getGame().setLevel(level);
		assertEquals(level, controller.getLevel());
	}

	/**
	 * Test attaching an unset level to the renderer.
	 */
	@Test (expected = IllegalStateException.class)
	public void testAttachLevelNull() {
		controller.getGame().setLevel(null);
		controller.attachLevel();
	}

	/**
	 * Test if attaching a level correctly adds all elements to the renderer.
	 */
	@Test
	public void testAttachLevel() {
		Game g = controller.getGame();
		Spatial pSpatial = g.getPlayer().getSpatial();
		Spatial tSpatial = g.getLevel().getTile(0, 0).getSpatial();
		Light light = g.getLevel().getLights().get(0);

		controller.attachLevel();

		verify(rootNode, times(4)).attachChild(tSpatial);
		verify(rootNode, times(1)).attachChild(pSpatial);
		verify(rootNode, times(1)).addLight(light);
	}

	/**
	 * Test if cleaning up cleans up the entities.
	 */
	@Test
	public void testCleanUpEntities() {
		Entity entity = game.getEntities().iterator().next();
		controller.cleanup();
		verify(entity, times(1)).setState(EntityState.NEW);
	}

	@Override
	public Controller getController() {
		return controller;
	}

	@Override
	public Main getMain() {
		return main;
	}
}