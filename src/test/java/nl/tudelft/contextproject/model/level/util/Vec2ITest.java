package nl.tudelft.contextproject.model.level.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.util.Vec2I;

/**
 * Test class for Vec2I.
 */
public class Vec2ITest {

	private int testX;
	private int testY;
	private Vec2I testVector;
	
	/**
	 * Set up a vec2i for testing.
	 */
	@Before 
	public void setUp() {
		testX = 1;
		testY = 2;
		testVector = new Vec2I(testX, testY); 
	}
	
	/**
	 * Test hashCode.
	 */
	@Test
	public void testHashCode() {
		assertEquals(65538, testVector.hashCode());
	}

	/**
	 * Test constructor.
	 */
	@Test
	public void testVec2I() {
		assertEquals(testX, testVector.x);
		assertEquals(testY, testVector.y);
	}

	/**
	 * Test getter for X.
	 */
	@Test
	public void testGetX() {
		assertEquals(testX, testVector.getX());
	}

	/**
	 * Test setter for X.
	 */
	@Test
	public void testSetX() {
		int setX = 42;
		testVector.setX(setX);
		assertEquals(setX, testVector.getX());
	}

	/**
	 * Test getter for Y.
	 */
	@Test
	public void testGetY() {
		assertEquals(testY, testVector.getY());
	}

	/**
	 * Test setter for Y.
	 */
	@Test
	public void testSetY() {
		int setY = 42;
		testVector.setY(setY);
		assertEquals(setY, testVector.getY());
	}

	/**
	 * Test equals, same vector.
	 */
	@Test
	public void testEqualsSame() {
		assertEquals(testVector, testVector);
	}
	
	/**
	 * Test equals, equal vector.
	 */
	@Test
	public void testEqualsEqual() {
		Vec2I secondVector = new Vec2I(testX, testY);
		assertEquals(testVector, secondVector);
	}
	
	/**
	 * Test equals, not equal vector, x.
	 */
	@Test
	public void testEqualsXDifferent() {
		Vec2I secondVector = new Vec2I(testX + 1, testY);
		assertNotEquals(testVector, secondVector);
	}
	
	/**
	 * Test equals, not equal vector, y.
	 */
	@Test
	public void testEqualsYDifferent() {
		Vec2I secondVector = new Vec2I(testX, testY + 1);
		assertNotEquals(testVector, secondVector);
	}
	
	/**
	 * Test equals, not equal vector, y.
	 */
	@Test
	public void testEqualsOtherObject() {
		assertNotEquals(testVector, 5);
	}
	
	/**
	 * Test equals, null vector, y.
	 */
	@Test
	public void testEqualsNull() {
		assertNotEquals(testVector, null);
	}
	
	/**
	 * Test toString.
	 */
	@Test
	public void testToString() {
		assertEquals("Vec2I [x=1, y=2]", testVector.toString());
	}

}
