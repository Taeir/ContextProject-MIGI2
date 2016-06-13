package nl.tudelft.contextproject.controller;

import static org.junit.Assert.*;
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
 * Test class for the GameController.
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

		//Global mocks
		rootNode = mock(Node.class);
		inputManager = mock(InputManager.class);
		phe = mock(BulletAppState.class);

		when(phe.getPhysicsSpace()).thenReturn(mock(PhysicsSpace.class));

		//Lights
		Light lightMock = mock(Light.class);
		LightList lightList = new LightList(rootNode);
		lightList.add(lightMock);

		when(rootNode.getLocalLightList()).thenReturn(lightList);

		//Entities
		Entity entityMock = mock(Entity.class);
		when(entityMock.getSpatial()).thenReturn(mock(Spatial.class));
		when(entityMock.getState()).thenReturn(EntityState.ALIVE);

		Set<Entity> entities = ConcurrentHashMap.newKeySet();
		entities.add(entityMock);

		//Player
		VRPlayer playerMock = mock(VRPlayer.class);
		when(playerMock.getSpatial()).thenReturn(mock(Spatial.class));

		//Tiles
		MazeTile tileMock = mock(MazeTile.class);
		when(tileMock.getSpatial()).thenReturn(mock(Spatial.class));

		MazeTile[][] tiles = {{tileMock, tileMock}, {null, tileMock}, {tileMock, null}};

		//Lights
		LinkedList<Light> lights = new LinkedList<>();
		lights.add(lightMock);

		//Create level, controller and game
		level = spy(new Level(tiles, lights, entities));
		controller = spy(new GameController(main, level, 10f));
		game = spy(new Game(level, playerMock, controller, 10f));

		//Set all mocks
		controller.setInputManager(inputManager);
		controller.setRootNode(rootNode);
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
		Entity entityMock = mock(Entity.class);
		Node rootNode = mock(Node.class);
		Geometry geometry = mock(Geometry.class);

		when(entityMock.getState()).thenReturn(EntityState.NEW);
		when(entityMock.getSpatial()).thenReturn(geometry);
		

		set.add(entityMock);

		controller.setRootNode(rootNode);
		controller.updateEntities(0.5f);

		verify(entityMock, times(1)).update(0.5f);
		verify(entityMock, times(1)).setState(EntityState.ALIVE);

		verify(rootNode, times(1)).attachChild(geometry);    
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
		Level level = new Level(null, null, null);
		controller.getGame().setLevel(level);
		assertSame(level, controller.getLevel());
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
		Game game = controller.getGame();
		Spatial playerSpatial = game.getPlayer().getSpatial();
		Spatial tileSpatial = game.getLevel().getTile(0, 0).getSpatial();
		Light light = game.getLevel().getLights().get(0);

		controller.attachLevel();

		verify(rootNode, times(4)).attachChild(tileSpatial);
		verify(rootNode, times(1)).attachChild(playerSpatial);
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