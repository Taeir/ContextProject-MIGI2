package nl.tudelft.contextproject.model.entities;

import org.junit.Before;
import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.test.TestUtil;

/**
 * Test class fot the {@link KillerBunny}.
 */
public class KillerBunnyTest extends MovingEnemyTest {

	private KillerBunny bunny;

	/**
	 * Create a fresh instance for every test.
	 */
	@Before
	public void setUp() {
		TestUtil.mockGame();
		bunny = new KillerBunny(new Vector3f());
	}
	
	@Override
	public MovingEntity getEnemy() {
		return bunny;
	}

}
