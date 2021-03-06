package nl.tudelft.contextproject.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.Main;
import nl.tudelft.contextproject.TestBase;

/**
 * QRGenerator test class.
 */
public class QRGeneratorTest extends TestBase {

	private static final String TEST_URL = "https://111.111.111.111:" + Main.PORT_NUMBER + "/";	
	
	private QRGenerator qrGenerator;
	
	/**
	 * Setup the QRGenerator and a mocked network interface.
	 */
	@Before
	public void init() {
		qrGenerator = QRGenerator.getInstance();
	}
	
	/**
	 * Test correct initialization.
	 */
	@Test
	public void testInitialize() {
		assertNotNull(qrGenerator);
	}
	
	/**
	 * Test uniqueness.
	 */
	@Test
	public void testUniqueness() {
		QRGenerator qrGenerator2 = QRGenerator.getInstance();
		assertEquals(qrGenerator, qrGenerator2);
	}
	
	/**
	 * Test the set and get of URL.
	 */
	@Test
	public void testSetGetURL() {
		qrGenerator.setURL(TEST_URL);
		assertEquals(TEST_URL, qrGenerator.getURL());
	}
	
}
