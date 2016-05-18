package nl.tudelft.contextproject.qrgenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import nl.tudelft.contextproject.Main;

/**
 * QRGenerator test class.
 */
public class QRGeneratorTest {

	private final String testIP = "111.111.111.111";	
	private final String testURL = "https://" + testIP + ":" + Main.PORT_NUMBER + "/";	
	
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
		QRGenerator qrGen2 = QRGenerator.getInstance();
		assertEquals(qrGenerator, qrGen2);
	}
	
	/**
	 * Test the set and get of URL.
	 */
	@Test
	public void testSetGetURL() {
		qrGenerator.setURL(testURL);
		assertEquals(testURL, qrGenerator.getURL());
	}
	
}
