package nl.tudelft.contextproject.qrcontrol;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import nl.tudelft.contextproject.logging.Log;

/**
 *	QR code generator class. 
 *	Singleton that will generate a QR code and place it in "src/main/assets/QRcode/qrcode.png".
 */
public final class QRGenerator {

	//Use eager initialization of the singleton.
	private static final QRGenerator INSTANCE = new QRGenerator();

	/**
	 * Private constructor to prevent initialization elsewhere.
	 */
	private QRGenerator() {}

	/**
	 * Get the QRgenerator instance.
	 * @return
	 * 			QRgenerator
	 */
	public static QRGenerator getInstance() {
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
		String hostingAddress = INSTANCE.getIP();
		hostingAddress = "https://" + hostingAddress + ":" + portNumber + "/";
		Log.getLog("WebInterface").info("Creating QRcode with address: " + hostingAddress);
	}

	/**
	 * Get the correct ipv4 address of this computer.
	 * This method simply get all network interfaces of the computer.
	 * Then for each network interface check all the network addresses.
	 * The IP address will be among those, so all address that are local or LAN based
	 * are filtered out.
	 * Please note that you cannot have other network adapters from virtual machines
	 * running as they will interfere with the correct adapter.
	 * @return
	 * 			IP address of this computer.
	 */
	protected String getIP() {
		String  ip = "";
		Enumeration<NetworkInterface> n;
		try {
			n = NetworkInterface.getNetworkInterfaces();
			
			while (n.hasMoreElements()) {
				NetworkInterface e = n.nextElement();
				Enumeration<InetAddress> a = e.getInetAddresses();
				
				while (a.hasMoreElements()) {
					InetAddress addr = a.nextElement();
					String hostAddress = addr.getHostAddress();
					if (!hostAddress.startsWith("127.") 
							&& !hostAddress.contains(":")) {
						ip = addr.getHostAddress();
					}
				}
			}
		} catch (SocketException e1) {
			Log.getLog("WebInterface").severe("No socket", e1);
			e1.printStackTrace();
		}

		return ip;
	}

}
