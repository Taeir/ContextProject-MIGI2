package nl.tudelft.contextproject;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Temporary class for examining FP control schemes and collisions.
 * This class can be ran independently.
 * Loads a simple town scene. 
 * Copy write rights to jMonkey tutorial. Please use this class to understand collisions in jMonkey.
 * @see <a href="https://wiki.jmonkeyengine.org/doku.php/jme3:advanced:physics">jMonkey physics</a>
 */
public class HelloCollision extends SimpleApplication implements ActionListener {

	private Spatial sceneModel;
	private BulletAppState bulletAppState;
	private RigidBodyControl landscape;
	private CharacterControl player;
	private Vector3f walkDirection = new Vector3f();
	private boolean left, right, up, down;

	//Temporary vectors used on each frame.
	//They here to avoid instanciating new vectors on each frame
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();

	/**
	 * Call instance to start.
	 * @param args arguments.
	 */
	public static void main(String[] args) {
		HelloCollision app = new HelloCollision();
		app.start();
	}

	/**
	 * Start the jMoneky engine method.
	 */
	@Override
	public void simpleInitApp() {
		//Set-up physics
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		//Use a fly camera 
		viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
		flyCam.setMoveSpeed(100);
		setUpKeys();
		setUpLight();

		//Load town scene in and set it in the scene model.
		assetManager.registerLocator("src/main/assets/Scenes/town.zip", ZipLocator.class);
		sceneModel = assetManager.loadModel("main.scene");
		sceneModel.setLocalScale(2f);

		//Create a collision shape of the town. 
		CollisionShape sceneShape =
				CollisionShapeFactory.createMeshShape(sceneModel);
		landscape = new RigidBodyControl(sceneShape, 0);
		sceneModel.addControl(landscape);

		//Set up a collision shape, use this shape to create player.
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
		player = new CharacterControl(capsuleShape, 0.05f);
		
		//Player physics settings
		player.setJumpSpeed(20);
		player.setFallSpeed(30);
		player.setGravity(0);
		player.setPhysicsLocation(new Vector3f(0, 10, 0));

		//Add scene to render the town.
		rootNode.attachChild(sceneModel);
		
		//This adds the player and landscape to the game world
		bulletAppState.getPhysicsSpace().add(landscape);
		bulletAppState.getPhysicsSpace().add(player);

	}

	private void setUpLight() {
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(1.3f));
		rootNode.addLight(al);

		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
		rootNode.addLight(dl);
	}

	/** 
	 *  Temporary key setup.
	 */
	private void setUpKeys() {
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Left");
		inputManager.addListener(this, "Right");
		inputManager.addListener(this, "Up");
		inputManager.addListener(this, "Down");
		inputManager.addListener(this, "Jump");
	}

	/** These are custom actions triggered by key presses.
	 *	 We do not walk yet, we just keep track of the direction the user pressed. 
	 * @param tpf - float value
	 * @param isPressed - if button is pressed.
	 * @param binding - Binding name
	 */
	public void onAction(String binding, boolean isPressed, float tpf) {
		if (binding.equals("Left")) {
			left = isPressed;
		} else if (binding.equals("Right")) {
			right = isPressed;
		} else if (binding.equals("Up")) {
			up = isPressed;
		} else if (binding.equals("Down")) {
			down = isPressed;
		} else if (binding.equals("Jump")) {
			if (isPressed) { 
				player.jump(); 
			}
		}
	}

	/**
	 * This is the main event loop--walking happens here.
	 * We check in which direction the player is walking by interpreting
	 * the camera direction forward (camDir) and to the side (camLeft).
	 * The setWalkDirection() command is what lets a physics-controlled player walk.
	 * We also make sure here that the camera moves with player.
	 */
	@Override
	public void simpleUpdate(float tpf) {
		camDir.set(cam.getDirection()).multLocal(0.6f);
		camLeft.set(cam.getLeft()).multLocal(0.4f);
		walkDirection.set(0, 0, 0);
		if (left) {
			walkDirection.addLocal(camLeft);
		}
		if (right) {
			walkDirection.addLocal(camLeft.negate());
		}
		if (up) {
			walkDirection.addLocal(camDir);
		}
		if (down) {
			walkDirection.addLocal(camDir.negate());
		}
		
		player.setWalkDirection(walkDirection);
		getCamera().setLocation(player.getPhysicsLocation());
	}
}

