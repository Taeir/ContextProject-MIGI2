package nl.tudelft.contextproject.qrgenerator;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * QRGenerator test class.
 */
public class QRGeneratorTest {

	private QRGenerator qrGenerator;
	
	/**
	 * Setup the QRGenerator.
	 */
	@Before
	public void init() {
		qrGenerator = QRGenerator.getInstance();
	}
	
	/**
	 * Test uniqueness.
	 */
	@Test
	public void testUniqueness() {
		QRGenerator qrGen2 = QRGenerator.getInstance();
		assertEquals(qrGenerator, qrGen2);
	}

}
