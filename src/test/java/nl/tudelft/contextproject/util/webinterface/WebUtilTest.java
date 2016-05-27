package nl.tudelft.contextproject.util.webinterface;

import nl.tudelft.contextproject.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for the WebUtil class.
 */
public class WebUtilTest extends TestBase {

	/**
	 * Test for decoding actions.
	 */
	@Test
	public void testDecode() {
		assertEquals(WebUtil.decodeAction(0), "placebomb");
		assertEquals(WebUtil.decodeAction(1), "placepitfall");
		assertEquals(WebUtil.decodeAction(2), "placemine");
		assertEquals(WebUtil.decodeAction(3), "spawnenemy");
		assertEquals(WebUtil.decodeAction(4), "dropbait");
		assertEquals(WebUtil.decodeAction(-1), "notanaction");
	}

	/**
	 * Test for checking if an action is valid.
	 */
	@Test
	public void testCheckValidAction() {
		assertTrue(WebUtil.checkValidAction("dropbait", "Elves"));
		assertTrue(WebUtil.checkValidAction("placebomb", "Dwarfs"));
		assertFalse(WebUtil.checkValidAction("wingame", "hax0r"));
	}

	/**
	 * Test for checking if an action is valid as an elf.
	 */
	@Test
	public void testCheckValidElves() {
		assertTrue(WebUtil.checkValidElves("dropbait"));
		assertFalse(WebUtil.checkValidElves("wingame"));
	}

	/**
	 * Test for checking if an action is valid as a dwarf.
	 */
	@Test
	public void testCheckValidDwarfs() {
		assertTrue(WebUtil.checkValidDwarfs("placebomb"));
		assertTrue(WebUtil.checkValidDwarfs("placepitfall"));
		assertTrue(WebUtil.checkValidDwarfs("placemine"));
		assertTrue(WebUtil.checkValidDwarfs("spawnenemy"));
		assertFalse(WebUtil.checkValidDwarfs("wingame"));
	}

}
