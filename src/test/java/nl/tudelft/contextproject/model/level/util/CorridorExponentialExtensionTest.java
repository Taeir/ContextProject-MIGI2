package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.TestBase;

/**
 * Test class for CorridorExponentialExtension.
 */
public class CorridorExponentialExtensionTest extends TestBase {

	private Random randZeroSeed;
	private Vec2I extendPoint;
	/**
	 * Setup before tests.
	 */
	@Before
	public void setupBeforeTests() {
		randZeroSeed = new Random(0L);
		extendPoint = new Vec2I(1, 1);
	}
	
	/**
	 * Test GetExtendLocationsUsingCorridorType with a HORIZONTAL corridor type.
	 */
	@Test
	public void testGetExtendLocationsUsingCorridorTypeHORIZONTAL() {
		assertEquals(3, CorridorExponentialExtension.getExtendLocationsUsingCorridorType(CorridorType.HORIZONTAL, extendPoint, randZeroSeed).size());
	}
	
	/**
	 * Test GetExtendLocationsUsingCorridorType with a VERTICAL corridor type.
	 */
	@Test
	public void testGetExtendLocationsUsingCorridorTypeVERTICAL() {
		assertEquals(3, CorridorExponentialExtension.getExtendLocationsUsingCorridorType(CorridorType.VERTICAL, extendPoint, randZeroSeed).size());
	}
	
	/**
	 * Test GetExtendLocationsUsingCorridorType with a LOWER_LEFT corridor type.
	 */
	@Test
	public void testGetExtendLocationsUsingCorridorTypeLOWER_LEFT() {
		assertEquals(10, CorridorExponentialExtension.getExtendLocationsUsingCorridorType(CorridorType.LOWER_LEFT, extendPoint, randZeroSeed).size());
	}
	
	/**
	 * Test GetExtendLocationsUsingCorridorType with a LOWER_RIGHT corridor type.
	 */
	@Test
	public void testGetExtendLocationsUsingCorridorTypeLOWER_RIGHT() {
		assertEquals(13, CorridorExponentialExtension.getExtendLocationsUsingCorridorType(CorridorType.LOWER_RIGHT, extendPoint, randZeroSeed).size());
	}
	
	/**
	 * Test GetExtendLocationsUsingCorridorType with a UPPER_LEFT corridor type.
	 */
	@Test
	public void testGetExtendLocationsUsingCorridorTypeUPPER_LEFT() {
		assertEquals(13, CorridorExponentialExtension.getExtendLocationsUsingCorridorType(CorridorType.UPPER_LEFT, extendPoint, randZeroSeed).size());
	}
	
	/**
	 * Test GetExtendLocationsUsingCorridorType with a UPPER_RIGHT corridor type.
	 */
	@Test
	public void testGetExtendLocationsUsingCorridorTypeUPPER_RIGHT() {
		assertEquals(10, CorridorExponentialExtension.getExtendLocationsUsingCorridorType(CorridorType.UPPER_RIGHT, extendPoint, randZeroSeed).size());
	}
	
	/**
	 * Test GetExtendLocationsUsingCorridorType with a NOT_DEFINED corridor type.
	 */
	@Test
	public void testGetExtendLocationsUsingCorridorTypeNOT_DEFINED() {
		assertEquals(0, CorridorExponentialExtension.getExtendLocationsUsingCorridorType(CorridorType.NOT_DEFINED, extendPoint, randZeroSeed).size());
	}

	/**
	 * Test horizontalExtension.
	 */
	@Test
	public void testHorizontalExtension() {
		assertEquals(3, CorridorExponentialExtension.horizontalExtension(extendPoint, randZeroSeed).size());
	}

	/**
	 * Test verticalExtension.
	 */
	@Test
	public void testVerticalExtension() {
		assertEquals(3, CorridorExponentialExtension.verticalExtension(extendPoint, randZeroSeed).size());
	}

	/**
	 * Test lowerLeftExtension.
	 */
	@Test
	public void testLowerLeftExtension() {
		assertEquals(10, CorridorExponentialExtension.lowerLeftExtension(extendPoint, randZeroSeed).size());
	}

	/**
	 * Test lowerRightExtension.
	 */
	@Test
	public void testLowerRightExtension() {
		assertEquals(13, CorridorExponentialExtension.lowerRightExtension(extendPoint, randZeroSeed).size());
	}

	/**
	 * Test upperLeftExtension.
	 */
	@Test
	public void testUpperLeftExtension() {
		assertEquals(13, CorridorExponentialExtension.upperLeftExtension(extendPoint, randZeroSeed).size());
	}

	/**
	 * Test upperRightExtension.
	 */
	@Test
	public void testUpperRightExtension() {
		assertEquals(10, CorridorExponentialExtension.upperRightExtension(extendPoint, randZeroSeed).size());
	}

	/**
	 * Test getExponentialNumber with seeded random.
	 */
	@Test
	public void testGetExponentialNumberSeeded() {
		assertEquals(2, CorridorExponentialExtension.getExponentialNumber(randZeroSeed));
	}
	
	/**
	 * Test getExponentialNumber above or equal to minimum extension.
	 */
	@Test
	public void testGetExponentialNumberAboveMinimum() {
		assertTrue(CorridorExponentialExtension.MINIMUM_EXTENSION <= CorridorExponentialExtension.getExponentialNumber(new Random()));
	}
	
	/**
	 * Test getExponentialNumber below or equal to maximum extension.
	 */
	@Test
	public void testGetExponentialNumberBelow() {
		assertTrue(CorridorExponentialExtension.MAXIMUM_EXTENSION >= CorridorExponentialExtension.getExponentialNumber(new Random()));
	}

}
