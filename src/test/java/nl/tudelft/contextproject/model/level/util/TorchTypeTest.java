package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jme3.math.Vector3f;

import nl.tudelft.contextproject.TestBase;
import nl.tudelft.contextproject.model.entities.environment.Torch;
import nl.tudelft.contextproject.model.level.MazeTile;
import nl.tudelft.contextproject.model.level.TileType;

/**
 * Torch type test class.
 */
public class TorchTypeTest extends TestBase {

	private static Vec2I testPoint;
	private static Vector3f testLocation;
	private MazeTile[][] testMap;
	
	/**
	 * Set up a location for the tile that is being checked in tests for getTorchType.
	 * Set up a location for the Torch that is checked in tests for createTorchOfTorchType.
	 */
	@BeforeClass
	public static void setupBeforeClass() {
		testPoint = new Vec2I(1, 1);
		testLocation = new Vector3f();
	}
	
	/**
	 * Set up a map to test possible locations.
	 */
	@Before
	public void setupBeforeTest() {
		testMap = new MazeTile[3][3];
	}
	
	/**
	 * Test get torch type of a North Wall.
	 */
	@Test
	public void testGetTorchTypeNorth() {
		testMap[0][1] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[1][0] = new MazeTile(0, 0, TileType.WALL);
		testMap[2][1] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[1][2] = new MazeTile(0, 0, TileType.FLOOR);
		assertEquals(TorchType.NORTH_WALL_LIGHT, TorchType.getTorchType(testMap, testPoint));
	}
	
	/**
	 * Test get torch type of a South Wall.
	 */
	@Test
	public void testGetTorchTypeSouth() {
		testMap[0][1] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[1][0] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[2][1] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[1][2] = new MazeTile(0, 0, TileType.WALL);
		assertEquals(TorchType.SOUTH_WALL_LIGHT, TorchType.getTorchType(testMap, testPoint));
	}
	
	/**
	 * Test get torch type of a West Wall.
	 */
	@Test
	public void testGetTorchTypeWest() {
		testMap[0][1] = new MazeTile(0, 0, TileType.WALL);
		testMap[1][0] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[2][1] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[1][2] = new MazeTile(0, 0, TileType.FLOOR);
		assertEquals(TorchType.WEST_WALL_LIGHT, TorchType.getTorchType(testMap, testPoint));
	}
	
	/**
	 * Test get torch type of a East Wall.
	 */
	@Test
	public void testGetTorchTypeEast() {
		testMap[0][1] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[1][0] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[2][1] = new MazeTile(0, 0, TileType.WALL);
		testMap[1][2] = new MazeTile(0, 0, TileType.FLOOR);
		assertEquals(TorchType.EAST_WALL_LIGHT, TorchType.getTorchType(testMap, testPoint));
	}
	
	/**
	 * Test get torch type of a ceiling torch.
	 */
	@Test
	public void testGetTorchTypeCeiling() {
		testMap[0][1] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[1][0] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[2][1] = new MazeTile(0, 0, TileType.FLOOR);
		testMap[1][2] = new MazeTile(0, 0, TileType.FLOOR);
		assertEquals(TorchType.CEILING_LIGHT, TorchType.getTorchType(testMap, testPoint));
	}
	
	/**
	 * Test get Torch of North type, assert the type of torch.
	 */
	@Test 
	public void testCreateTorchOfTorchTypeNorthType() {
		Torch torch = TorchType.createTorchOfTorchType(TorchType.NORTH_WALL_LIGHT, testLocation);
		assertTrue(torch.getTorchType());
	}
	
	/**
	 * Test get Torch of North type, assert the location of the torch.
	 */
	@Test 
	public void testCreateTorchOfTorchTypeNorthLocation() {
		Torch torch = TorchType.createTorchOfTorchType(TorchType.NORTH_WALL_LIGHT, testLocation);
		assertEquals(new Vector3f(-0.05f, 0f, -0.47f), torch.getLocation());
	}
	
	/**
	 * Test get Torch of South type, assert the type of torch.
	 */
	@Test 
	public void testCreateTorchOfTorchTypeSouthType() {
		Torch torch = TorchType.createTorchOfTorchType(TorchType.SOUTH_WALL_LIGHT, testLocation);
		assertTrue(torch.getTorchType());
	}
	
	/**
	 * Test get Torch of South type, assert the location of the torch.
	 */
	@Test 
	public void testCreateTorchOfTorchTypeSouthLocation() {
		Torch torch = TorchType.createTorchOfTorchType(TorchType.SOUTH_WALL_LIGHT, testLocation);
		assertEquals(new Vector3f(-0.05f, 0f, 0.46999997f), torch.getLocation());
	}
	
	/**
	 * Test get Torch of South type, assert the type of torch.
	 */
	@Test 
	public void testCreateTorchOfTorchTypeEastType() {
		Torch torch = TorchType.createTorchOfTorchType(TorchType.EAST_WALL_LIGHT, testLocation);
		assertTrue(torch.getTorchType());
	}
	
	/**
	 * Test get Torch of East type, assert the location of the torch.
	 */
	@Test 
	public void testCreateTorchOfTorchTypeEastLocation() {
		Torch torch = TorchType.createTorchOfTorchType(TorchType.EAST_WALL_LIGHT, testLocation);
		assertEquals(new Vector3f(0.46999997f, 0f, -0.05f), torch.getLocation());
	}
	
	/**
	 * Test get Torch of South type, assert the type of torch.
	 */
	@Test 
	public void testCreateTorchOfTorchTypeWestType() {
		Torch torch = TorchType.createTorchOfTorchType(TorchType.WEST_WALL_LIGHT, testLocation);
		assertTrue(torch.getTorchType());
	}
	
	/**
	 * Test get Torch of East type, assert the location of the torch.
	 */
	@Test 
	public void testCreateTorchOfTorchTypeWestLocation() {
		Torch torch = TorchType.createTorchOfTorchType(TorchType.WEST_WALL_LIGHT, testLocation);
		assertEquals(new Vector3f(-0.47f, 0f, -0.05f), torch.getLocation());
	}
	
	/**
	 * Test get Torch of Ceiling type, assert the type of torch.
	 */
	@Test 
	public void testCreateTorchOfTorchTypeCeilingType() {
		Torch torch = TorchType.createTorchOfTorchType(TorchType.CEILING_LIGHT, testLocation);
		assertFalse(torch.getTorchType());
	}
	
	/**
	 * Test get Torch of Ceiling type, assert the location of the torch.
	 */
	@Test 
	public void testCreateTorchOfTorchTypeCeilingLocation() {
		Torch torch = TorchType.createTorchOfTorchType(TorchType.CEILING_LIGHT, testLocation);
		assertEquals(new Vector3f(-0.05f, 0f, -0.05f), torch.getLocation());
	}
	
	/**
	 * Test exception if wrong tile type is given.
	 * @throws IllegalArgumentException
	 * 			should happen here
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testCreateTorchOfTorchTypeWrongType() throws IllegalArgumentException {
		Torch torch = TorchType.createTorchOfTorchType(TorchType.PLACE_HOLDER_TORCH, testLocation);
		assertEquals(new Vector3f(0f, 0f, 0f), torch.getLocation());
	}

}
