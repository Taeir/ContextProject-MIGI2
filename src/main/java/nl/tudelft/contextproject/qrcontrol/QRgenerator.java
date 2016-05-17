package nl.tudelft.contextproject.qrcontrol;

import java.net.InetAddress;
import java.net.UnknownHostException;

import nl.tudelft.contextproject.logging.Log;

/**
 *	QR code generator class. 
 *	Singleton that will generate a QR code and place it in "src/main/assets/QRcode/qrcode.png".
 */
public final class QRgenerator {
	
	//Use eager initialization of the singleton.
	private static final QRgenerator INSTANCE = new QRgenerator();
	
	/**
	 * Private constructor to prevent initialization elsewhere.
	 */
	private QRgenerator() {}
	
	/**
	 * Get the QRgenerator instance.
	 * @return
	 * 			QRgenerator
	 */
	public static QRgenerator getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Generate a QR code in "src/main/assets/QRcode/qrcode.png".
	 * First, get the hostingAddress by using the Java InetAddress class.
	 * Then, create the QRgen class.
	 * 
	 * @param portNumber
	 * 				the port number which hosts the application
	 */
	public static void generateQRcode(String portNumber) {
		try {
			String hostingAddress = InetAddress.getLocalHost().getHostAddress();
			Log.getLog("WebInterface").fine("Creating QRcode with address: " + hostingAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
