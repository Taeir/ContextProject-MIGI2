package nl.tudelft.contextproject;

import nl.tudelft.contextproject.qrgenerator.QRGenerator;

/**
 * Temporary manual test class for QRgenerator.
 *
 */
public class QRMain {
	
	/**
	 * Main method that is called when the program is started.
	 * @param args run-specific arguments.
	 */
	public static void main(String[] args) {
		QRGenerator.getInstance().generateQRcode();
	}
}